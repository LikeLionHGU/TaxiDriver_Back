package hgu.likelion.fish.post.application.service;

import hgu.likelion.fish.commons.entity.AuctionStatus;
import hgu.likelion.fish.commons.entity.DateStatus;
import hgu.likelion.fish.commons.entity.RegisterStatus;
import hgu.likelion.fish.commons.image.entity.Image;
import hgu.likelion.fish.commons.image.service.S3Service;
import hgu.likelion.fish.post.application.dto.PostDto;
import hgu.likelion.fish.post.domain.entity.Post;
import hgu.likelion.fish.post.domain.repository.PostRepository;
import hgu.likelion.fish.post.presentation.response.PostAuctionListResponse;
import hgu.likelion.fish.post.presentation.response.PostListResponse;
import hgu.likelion.fish.post.presentation.response.PostSellResponse;
import hgu.likelion.fish.user.application.service.UserService;
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

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Value("${cloud.aws.region.static}")
    private String region;

    @Transactional
    public PostDto savePost(PostDto postDto, MultipartFile[] files, String dirName, User user) throws IOException {
        List<Image> images = new ArrayList<>();
        for (MultipartFile file : files) {
            String url = s3Service.uploadFiles(file, "va/");
            Image image = new Image();
            image.setUrl(url);

            images.add(image);
        }

        Post post = Post.fromDto(postDto, images, user);
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

    @Transactional
    public List<PostDto> getAllPost(DateStatus status) {

        List<Post> postList;

        if(status.equals(DateStatus.ALL)) {
            var from = LocalDateTime.now();
            postList = postRepository.findAllByRegDateAfter(from);

        } else if(status.equals(DateStatus.RECENT_1WEEK)) {
            var from = LocalDateTime.now().minusDays(7);
            postList = postRepository.findAllByRegDateAfter(from);

        } else if(status.equals(DateStatus.RECENT_1MONTH)) {
            var from = LocalDateTime.now().minusDays(30);
            postList = postRepository.findAllByRegDateAfter(from);

        } else if(status.equals(DateStatus.RECENT_3MONTH)) {
            var from = LocalDateTime.now().minusDays(90);
            postList = postRepository.findAllByRegDateAfter(from);
        } else {
            var from = LocalDateTime.now().minusDays(180);
            postList = postRepository.findAllByRegDateAfter(from);
        }

        return postList.stream().map(PostDto::toGetResponse).toList();
    }

    @Transactional
    public List<PostDto> getSpecificPosts(DateStatus status, RegisterStatus registerStatus) {
        List<Post> postList;

        if(status.equals(DateStatus.ALL)) {
            var from = LocalDateTime.now();
            postList = postRepository.findAllByRegDateAfterAndRegistrationStatus(from, registerStatus);

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
    public List<PostDto> getAllPostChecks(DateStatus status) {

        List<Post> postList;

        if(status.equals(DateStatus.ALL)) {
            var from = LocalDateTime.now();
            postList = postRepository.findAllByRegDateAfter(from);
        } else if(status.equals(DateStatus.RECENT_1WEEK)) {
            var from = LocalDateTime.now().minusDays(7);
            postList = postRepository.findAllByRegDateAfter(from);
        } else if(status.equals(DateStatus.RECENT_1MONTH)) {
            var from = LocalDateTime.now().minusDays(30);
            postList = postRepository.findAllByRegDateAfter(from);
        } else if(status.equals(DateStatus.RECENT_3MONTH)) {
            var from = LocalDateTime.now().minusDays(90);
            postList = postRepository.findAllByRegDateAfter(from);
        } else {
            var from = LocalDateTime.now().minusDays(180);
            postList = postRepository.findAllByRegDateAfter(from);
        }

        return postList.stream().map(PostDto::from).toList();
    }

    @Transactional
    public List<PostDto> getSpecificPostChecks(DateStatus status, RegisterStatus registerStatus) {

        List<Post> postList;

        if(status.equals(DateStatus.ALL)) {
            var from = LocalDateTime.now();
            postList = postRepository.findAllByRegDateAfterAndRegistrationStatus(from, registerStatus);
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

        return postList.stream().map(PostDto::from).toList();
    }

    @Transactional
    public List<PostDto> getAllAuctionPosts() {
        return postRepository.findAllByRegistrationStatus(RegisterStatus.REGISTER_SUCCESS).stream().map(PostDto::toAuctionResponse).toList();
    }
    @Transactional
    public List<PostDto> getSpecificAuctionPosts(AuctionStatus status) {
        return postRepository.findAllByRegistrationStatusAndAuctionStatus(RegisterStatus.REGISTER_SUCCESS, status).stream().map(PostDto::toAuctionResponse).toList();
    }

    @Transactional
    public Boolean updateRegisterStatus(Boolean value, Long id) {

        try {
            Post post = postRepository.findById(id).orElse(null);

            if(value) {
                post.setRegistrationStatus(RegisterStatus.REGISTER_SUCCESS);
                post.setTriggerAt(LocalDateTime.now());
                postRepository.save(post);
            } else {
                post.setRegistrationStatus(RegisterStatus.REGISTER_FAILED);
                postRepository.save(post);
            }

            return true;

        } catch (Exception e) {
            return false;
        }

    }

    @Transactional
    public PostListResponse getPostListNumber() {
        Long totalCount = postRepository.count();
        Long readyCount = postRepository.countByRegistrationStatus(RegisterStatus.REGISTER_READY);
        Long approvedCount = postRepository.countByRegistrationStatus(RegisterStatus.REGISTER_SUCCESS);
        Long rejectedCount = postRepository.countByRegistrationStatus(RegisterStatus.REGISTER_FAILED);


        return PostListResponse.toResponse(totalCount, readyCount, approvedCount, rejectedCount);
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
        return PostDto.toGetOneResponse(post);
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


}
