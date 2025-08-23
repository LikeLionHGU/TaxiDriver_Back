package hgu.likelion.fish.post.application.service;

import hgu.likelion.fish.commons.image.entity.Image;
import hgu.likelion.fish.commons.image.service.S3Service;
import hgu.likelion.fish.post.application.dto.PostDto;
import hgu.likelion.fish.post.domain.entity.Post;
import hgu.likelion.fish.post.domain.repository.PostRepository;
import hgu.likelion.fish.user.domain.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {

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

    private String generateImageUrl(String storedFileName) {
        return "https://" + bucket + ".s3." + region + ".amazonaws.com/" + storedFileName;
    }

    @Transactional
    public List<PostDto> getAllPost() {
        List<Post> postList = postRepository.findAll();

        return postList.stream().map(PostDto::toGetResponse).toList();
    }



}
