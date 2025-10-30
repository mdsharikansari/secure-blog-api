package com.sharik.secure_blog_api.controller;

import com.sharik.secure_blog_api.model.Post;
import com.sharik.secure_blog_api.model.User;
import com.sharik.secure_blog_api.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostRepository postRepository;

    // PUBLIC Endpoint
    @GetMapping
    public ResponseEntity<List<Post>> getAllPosts() {
        List<Post> posts = postRepository.findAll();
        return new ResponseEntity<>(posts, HttpStatus.OK);
    }

    // PRIVATE Endpoint
    @PostMapping
    public ResponseEntity<Post> createPost(
            @RequestBody Post post,
            @AuthenticationPrincipal User user
    ) {
        // Set the logged-in user as author of the post
        post.setUser(user);

        // Save the post to database
        Post savedPost = postRepository.save(post);

        return new ResponseEntity<>(savedPost, HttpStatus.CREATED);
    }
}
