package com.back.domain.post.comment.controller;

import com.back.domain.post.post.entity.Post;
import com.back.domain.post.post.service.PostService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class CommentController {

    private final PostService postService;

    @AllArgsConstructor
    @Getter
    public static class WriteRequestForm {
        @NotBlank(message = "01-content-댓글 내용은 필수입니다.")
        @Size(min = 2, max = 100, message = "02-content-댓글 내용은 2자 이상 100자 이하로 입력해주세요.")
        private String content;
    }

    @PostMapping("/posts/{postId}/comments/write")
    @Transactional
    public String writeComment(@PathVariable int postId,
                               @Valid WriteRequestForm form) {


        Post post = postService.findById(postId).get();
        post.addComment(form.content);
        return "redirect:/posts/%d".formatted(post.getId());
    }
}