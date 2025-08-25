package hgu.likelion.fish.post.application.service;


import hgu.likelion.fish.ai.AiService;
import hgu.likelion.fish.ai.InferenceResult;
import hgu.likelion.fish.auction.domain.entity.Auction;
import hgu.likelion.fish.auction.domain.repository.AuctionRepository;
import hgu.likelion.fish.commons.entity.AuctionStatus;
import hgu.likelion.fish.commons.entity.DateStatus;
import hgu.likelion.fish.commons.entity.RegisterStatus;
import hgu.likelion.fish.commons.image.entity.Image;
import hgu.likelion.fish.commons.image.repository.S3Repository;
import hgu.likelion.fish.commons.image.service.S3Service;
import hgu.likelion.fish.post.ProductDto;
import hgu.likelion.fish.post.ProductNativeRepository;
import hgu.likelion.fish.post.application.dto.PostDto;
import hgu.likelion.fish.post.domain.entity.Post;
import hgu.likelion.fish.post.domain.repository.PostRepository;
import hgu.likelion.fish.post.presentation.response.*;
import hgu.likelion.fish.user.domain.entity.User;
import hgu.likelion.fish.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {

    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final S3Service s3Service;
    private final ProductNativeRepository productNativeRepository;
    private final S3Repository s3Repository;
    private final AuctionRepository auctionRepository;
    private final AiService aiService;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Value("${cloud.aws.region.static}")
    private String region;

    @Transactional
    public PostDto savePost(PostDto postDto, MultipartFile[] files, String dirName, User user) throws IOException {
        List<Image> images = new ArrayList<>();

        InferenceResult s = null;
        for (MultipartFile file : files) {
            String url = s3Service.uploadFiles(file, "va/");

            try {
                s = aiService.relayToFastApi(file);
                System.out.println(s);

            } catch (Exception ignored) {
            }

            Image image = new Image();
            image.setUrl(url);

            images.add(image);
        }

        assert s != null; // s가 null이면 오류 발생

        Post post = Post.fromDto(postDto, images, user, s);
        for (Image image : images) {
            image.setPost(post);
        }
        postRepository.save(post);

        List<String> urls = new ArrayList<>();
        for(Image image : images) {
            String dtoUrl = generateImageUrl(image.getUrl());
            urls.add(dtoUrl);
        }

        return PostDto.from(post, urls);
    }


    private String generateImageUrl(String storedFileName) {
        return "https://" + bucket + ".s3." + region + ".amazonaws.com/" + storedFileName;
    }

    @Transactional(readOnly = true)
    public List<PostDto> getAllPostWithUser(DateStatus status, String userId) {
        // 1) now는 한 번만
        LocalDateTime now = LocalDateTime.now();

        List<Post> postList;
        User user = userRepository.findUserByUserId(userId);

        if (status == null || status == DateStatus.ALL) {
            // 전체 + 정렬(원하면)
            postList = postRepository.findAllBySellerOrderByRegDateDesc(user);
        } else if (status == DateStatus.RECENT_1WEEK) {
            postList = postRepository.findAllBySellerAndRegDateAfter(user, now.minusDays(7));
        } else if (status == DateStatus.RECENT_1MONTH) {
            postList = postRepository.findAllBySellerAndRegDateAfter(user, now.minusDays(30));
        } else if (status == DateStatus.RECENT_3MONTH) {
            postList = postRepository.findAllBySellerAndRegDateAfter(user, now.minusDays(90));
        } else {
            // 명시적으로 RECENT_6MONTH 같은 enum을 두는 게 좋음
            postList = postRepository.findAllBySellerAndRegDateAfter(user, now.minusDays(180));
        }

        return postList.stream()
                .map(PostDto::toGetResponse)
                .toList();
    }


    @Transactional(readOnly = true)
    public List<PostDto> getAllPost(DateStatus status) {
        // 1) now는 한 번만
        LocalDateTime now = LocalDateTime.now();

        List<Post> postList;

        if (status == null || status == DateStatus.ALL) {
            // 전체 + 정렬(원하면)
            postList = postRepository.findAllByOrderByRegDateDesc();
        } else if (status == DateStatus.RECENT_1WEEK) {
            postList = postRepository.findAllByRegDateAfter(now.minusDays(7));
        } else if (status == DateStatus.RECENT_1MONTH) {
            postList = postRepository.findAllByRegDateAfter(now.minusDays(30));
        } else if (status == DateStatus.RECENT_3MONTH) {
            postList = postRepository.findAllByRegDateAfter(now.minusDays(90));
        } else {
            // 명시적으로 RECENT_6MONTH 같은 enum을 두는 게 좋음
            postList = postRepository.findAllByRegDateAfter(now.minusDays(180));
        }

        return postList.stream()
                .map(PostDto::toGetResponse)
                .toList();
    }
    @Transactional
    public List<PostDto> getSpecificPostsWithUser(DateStatus status, RegisterStatus registerStatus, String userId) {
        User user = userRepository.findUserByUserId(userId);
        List<Post> postList;

        if(status.equals(DateStatus.ALL)) {
            postList = postRepository.findAllByRegistrationStatusAndSellerOrderByRegDateDesc(registerStatus, user);

        } else if(status.equals(DateStatus.RECENT_1WEEK)) {
            var from = LocalDateTime.now().minusDays(7);
            postList = postRepository.findAllByRegDateAfterAndRegistrationStatusAndSeller(from, registerStatus, user);

        } else if(status.equals(DateStatus.RECENT_1MONTH)) {
            var from = LocalDateTime.now().minusDays(30);
            postList = postRepository.findAllByRegDateAfterAndRegistrationStatusAndSeller(from, registerStatus, user);

        } else if(status.equals(DateStatus.RECENT_3MONTH)) {
            var from = LocalDateTime.now().minusDays(90);
            postList = postRepository.findAllByRegDateAfterAndRegistrationStatusAndSeller(from, registerStatus, user);
        } else {
            var from = LocalDateTime.now().minusDays(180);
            postList = postRepository.findAllByRegDateAfterAndRegistrationStatusAndSeller(from, registerStatus, user);
        }

        return postList.stream().map(PostDto::toGetResponse).toList();
    }
    @Transactional
    public List<PostDto> getSpecificPosts(DateStatus status, RegisterStatus registerStatus) {
        List<Post> postList;

        if(status.equals(DateStatus.ALL)) {
            postList = postRepository.findAllByRegistrationStatusOrderByRegDateDesc(registerStatus);

        } else if(status.equals(DateStatus.RECENT_1WEEK)) {
            var from = LocalDateTime.now().minusDays(7);
            postList = postRepository.findAllByRegDateAfterAndRegistrationStatus(from, registerStatus);

        } else if(status.equals(DateStatus.RECENT_1MONTH)) {
            var from = LocalDateTime.now().minusDays(30);
            postList = postRepository.findAllByRegDateAfterAndRegistrationStatus(from, registerStatus);

        } else if(status.equals(DateStatus.RECENT_3MONTH)) {
            var from = LocalDateTime.now().minusDays(90);
            postList = postRepository.findAllByRegDateAfterAndRegistrationStatus(from, registerStatus);
        } else {
            var from = LocalDateTime.now().minusDays(180);
            postList = postRepository.findAllByRegDateAfterAndRegistrationStatus(from, registerStatus);
        }

        return postList.stream().map(PostDto::toGetResponse).toList();
    }

    @Transactional
    public List<PostDto> getAllPostChecks() {

        List<Post> postList;

        postList = postRepository.findAll();

        return postList.stream().map(PostDto::from).toList();
    }

    @Transactional
    public List<PostDto> getSpecificPostChecks(RegisterStatus registerStatus) {

        List<Post> postList;

        postList = postRepository.findAllByRegistrationStatus(registerStatus);

        return postList.stream().map(PostDto::from).toList();
    }









    @Transactional
    public List<PostDto> getAllAuctionPosts() {
        return postRepository.findAllByRegistrationStatus(RegisterStatus.REGISTER_SUCCESS).stream().map(PostDto::toAuctionResponse).toList();
    }
    @Transactional
    public List<PostDto> getAllAuctionPosts(String userId) {
        User user = userRepository.findUserByUserId(userId);
        return postRepository.findAllByRegistrationStatusAndSeller(RegisterStatus.REGISTER_SUCCESS, user).stream().map(PostDto::toAuctionResponse).toList();
    }
    @Transactional
    public List<PostDto> getSpecificAuctionPosts(AuctionStatus status) {
        return postRepository.findAllByRegistrationStatusAndAuctionStatus(RegisterStatus.REGISTER_SUCCESS, status).stream().map(PostDto::toAuctionResponse).toList();
    }
    @Transactional
    public List<PostDto> getSpecificAuctionPosts(AuctionStatus status, String userId) {
        User user = userRepository.findUserByUserId(userId);
        return postRepository.findAllByRegistrationStatusAndAuctionStatusAndSeller(RegisterStatus.REGISTER_SUCCESS, status, user).stream().map(PostDto::toAuctionResponse).toList();
    }














    @Transactional
    public Boolean updateRegisterStatus(Boolean value, Long id, String failedReason) {

        try {
            Post post = postRepository.findById(id).orElse(null);

            if(value) {
                post.setRegistrationStatus(RegisterStatus.REGISTER_SUCCESS);
                post.setTriggerAt(LocalDateTime.now());
                post.setAuctionStatus(AuctionStatus.AUCTION_READY);
                postRepository.save(post);
            } else {
                post.setRegistrationStatus(RegisterStatus.REGISTER_FAILED);
                post.setFailedReason(failedReason);
                postRepository.save(post);
            }

            return true;

        } catch (Exception e) {
            return false;
        }

    }

    @Transactional
    public PostListResponse getPostListNumber(String userId) {
        User user = userRepository.findUserByUserId(userId);

        Long totalCount = postRepository.countBySeller(user);
        Long readyCount = postRepository.countByRegistrationStatusAndSeller(RegisterStatus.REGISTER_READY, user);
        Long approvedCount = postRepository.countByRegistrationStatusAndSeller(RegisterStatus.REGISTER_SUCCESS, user);
        Long rejectedCount = postRepository.countByRegistrationStatusAndSeller(RegisterStatus.REGISTER_FAILED, user);


        return PostListResponse.toResponse(totalCount, readyCount, approvedCount, rejectedCount);
    }

    @Transactional
    public PostAuctionListResponse getPostAuctionListNumber(String userId) {
        User user = userRepository.findUserByUserId(userId);

        Long totalCount = postRepository.countByRegistrationStatusAndSeller(RegisterStatus.REGISTER_SUCCESS, user);
        Long readyCount = postRepository.countByAuctionStatusAndRegistrationStatusAndSeller(AuctionStatus.AUCTION_READY, RegisterStatus.REGISTER_SUCCESS, user);
        Long currentCount = postRepository.countByAuctionStatusAndRegistrationStatusAndSeller(AuctionStatus.AUCTION_CURRENT, RegisterStatus.REGISTER_SUCCESS, user);
        Long finishCount = postRepository.countByAuctionStatusAndRegistrationStatusAndSeller(AuctionStatus.AUCTION_FINISH, RegisterStatus.REGISTER_SUCCESS, user);

        return PostAuctionListResponse.toResponse(totalCount, readyCount, currentCount, finishCount);
    }

    @Transactional
    public PostAuctionListResponse getPostAuctionListNumber() {
        Long totalCount = postRepository.countByRegistrationStatus(RegisterStatus.REGISTER_SUCCESS);
        Long readyCount = postRepository.countByAuctionStatusAndRegistrationStatus(AuctionStatus.AUCTION_READY, RegisterStatus.REGISTER_SUCCESS);
        Long currentCount = postRepository.countByAuctionStatusAndRegistrationStatus(AuctionStatus.AUCTION_CURRENT, RegisterStatus.REGISTER_SUCCESS);
        Long finishCount = postRepository.countByAuctionStatusAndRegistrationStatus(AuctionStatus.AUCTION_FINISH, RegisterStatus.REGISTER_SUCCESS);

        return PostAuctionListResponse.toResponse(totalCount, readyCount, currentCount, finishCount);
    }

    @Transactional
    public PostDto getPost(Long id) {
        Post post = postRepository.findById(id).orElse(null);

        assert post != null;

        List<Image> images = s3Repository.findAllByPost(post);
        List<String> urls = new ArrayList<>();

        for(Image i : images) {
            urls.add(i.getUrl());
        }


        Auction auction = auctionRepository.findTopByPostOrderByPriceDesc(post);

        if(auction == null) {
            return PostDto.toGetOneResponse(post, urls, 0);
        } else {
            return PostDto.toGetOneResponse(post, urls, auction.getPrice());
        }
    }


    @Transactional
    public List<PostSellResponse> getSellList(String userId, DateStatus value) {
        User user = userRepository.findUserByUserId(userId);
        List<Post> postList;

        if(value == DateStatus.ALL) {
            var from = LocalDateTime.now();
            postList = postRepository.findAllBySellerAndTotalPriceNotNullAndRegDateAfter(user, from); // 가격이 0이 아니어야 함

        } else if(value == DateStatus.RECENT_1WEEK) {
            var from = LocalDateTime.now().minusDays(7);

            postList = postRepository.findAllBySellerAndTotalPriceNotNullAndRegDateAfter(user, from); // 가격이 0이 아니어야 함

        } else if(value == DateStatus.RECENT_1MONTH) {
            var from = LocalDateTime.now().minusDays(30);
            postList = postRepository.findAllBySellerAndTotalPriceNotNullAndRegDateAfter(user, from); // 가격이 0이 아니어야 함

        } else {
            var from = LocalDateTime.now().minusDays(90);
            postList = postRepository.findAllBySellerAndTotalPriceNotNullAndRegDateAfter(user, from); // 가격이 0이 아니어야 함
        }

        return postList.stream().map(PostSellResponse::toResponse).toList();
    }

    @Transactional
    public List<PostSellResponse> getBuyList(String userId, DateStatus value) {
        User user = userRepository.findUserByUserId(userId);
        List<Post> postList;

        if(value == DateStatus.ALL) {
            var from = LocalDateTime.now();
            postList = postRepository.findAllByBuyerAndTotalPriceNotNullAndRegDateAfter(user, from); // 가격이 0이 아니어야 함

        } else if(value == DateStatus.RECENT_1WEEK) {
            var from = LocalDateTime.now().minusDays(7);

            postList = postRepository.findAllByBuyerAndTotalPriceNotNullAndRegDateAfter(user, from); // 가격이 0이 아니어야 함

        } else if(value == DateStatus.RECENT_1MONTH) {
            var from = LocalDateTime.now().minusDays(30);
            postList = postRepository.findAllByBuyerAndTotalPriceNotNullAndRegDateAfter(user, from); // 가격이 0이 아니어야 함

        } else {
            var from = LocalDateTime.now().minusDays(90);
            postList = postRepository.findAllByBuyerAndTotalPriceNotNullAndRegDateAfter(user, from); // 가격이 0이 아니어야 함
        }

        return postList.stream().map(PostSellResponse::toResponse).toList();
    }

    @Transactional
    public List<ProductDto> getRecentList(String name, Long status) {
        if(status == 0) {
            name = "(활)" + name;
            return productNativeRepository.findLatest2ByName(name);
        } else if(status == 1) {
            name = "(선)" + name;
            return productNativeRepository.findLatest2ByName(name);
        } else {
            name = "(냉)" + name;
            return productNativeRepository.findLatest2ByName(name);
        }
    }

    @Transactional
    public List<PostReceiveResponse> getReceiveList(String userId) {
        User user = userRepository.findUserByUserId(userId);
        List<Post> postList = postRepository.findAllByBuyerAndTotalPriceNotNull(user);

        return postList.stream().map(PostReceiveResponse::toResponse).toList();
    }

    @Transactional
    public List<PostReceiveResponse> getReceiveAdmin() {
        List<Post> postList = postRepository.findAllByTotalPriceNotNull();

        return postList.stream().map(PostReceiveResponse::toResponse).toList();
    }




    @Transactional
    public PostAuctionCountResponse getPostReceiveUserList(String userId) {
        User user = userRepository.findUserByUserId(userId);

        Long totalCount = postRepository.countByTotalPriceNotNullAndBuyer(user);
        Long waitCount = postRepository.countByTotalPriceNotNullAndIsReceivedFalseAndBuyer(user);
        Long finishCount = postRepository.countByTotalPriceNotNullAndIsReceivedTrueAndBuyer(user);

        return PostAuctionCountResponse.toResponse(totalCount, waitCount, finishCount);
    }

    @Transactional
    public PostAuctionCountResponse getPostReceiveAdminList() {
        Long totalCount = postRepository.countByTotalPriceNotNull();
        Long waitCount = postRepository.countByTotalPriceNotNullAndIsReceivedFalse();
        Long finishCount = postRepository.countByTotalPriceNotNullAndIsReceivedTrue();

        return PostAuctionCountResponse.toResponse(totalCount, waitCount, finishCount);
    }



    @Transactional
    public PostDetailCheckResponse getDetailResponse(Long id) {
        Post post = postRepository.findById(id).orElse(null);

        assert post != null;

        List<String> urls = new ArrayList<>();

        List<Image> images = s3Repository.findAllByPost(post);

        for(Image i : images) {
            urls.add(i.getUrl());
        }

        return PostDetailCheckResponse.toResponse(post, urls);
    }

    @Transactional
    public Boolean updateReceiveStatus(Long id) {
        Post post = postRepository.findById(id).orElse(null);

        try {
            post.setIsReceived(true);
            return true;
        } catch (Exception e) {
            return false;
        }
    }


}
