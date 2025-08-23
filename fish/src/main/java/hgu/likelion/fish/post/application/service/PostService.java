package hgu.likelion.fish.post.application.service;

import hgu.likelion.fish.commons.entity.DateStatus;
import hgu.likelion.fish.commons.entity.RegisterStatus;
import hgu.likelion.fish.commons.image.entity.Image;
import hgu.likelion.fish.commons.image.service.S3Service;
import hgu.likelion.fish.post.application.dto.PostDto;
import hgu.likelion.fish.post.domain.entity.Post;
import hgu.likelion.fish.post.domain.repository.PostRepository;
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
    private S3Service s3Service;

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
        postRepository.save(post);

        List<String> urls = new ArrayList<>();
        for(Image image : images) {
            String dtoUrl = generateImageUrl(image.getUrl());
            urls.add(dtoUrl);
        }

        return PostDto.from(post, urls);
    }

//    public List<PostDto> getPostCheck(String status) {
//        List<Post> posts;
//        posts = postRepository.findAllByRegistrationStatus(status);
//        List<PostDto> postDtoList = new ArrayList<>();
//
//        for (Post post : posts) {
//            User user = post.getSeller();
//            PostDto postDto = PostDto.from(post, user);
//            postDtoList.add(postDto);
//        }
//
//        return postDtoList;
//    }
//
//    public List<PostDto> getAllPostCheck() {
//        List<Post> posts;
//        posts = postRepository.findAll();
//        List<PostDto> postDtoList = new ArrayList<>();
//
//        for (Post post : posts) {
//            User user = post.getSeller();
//            PostDto postDto = PostDto.from(post, user);
//            postDtoList.add(postDto);
//        }
//
//        return postDtoList;
//    }

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



}
