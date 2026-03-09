package com.back.domain.post.post.controller;

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
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @AllArgsConstructor
    @Getter
    public static class WriteRequestForm {
        @Size(min = 2, max = 10, message = "03-title-제목은 2자 이상 10자 이하로 입력해주세요.")
        @NotBlank(message = "01-title-제목은 필수입니다.")
        private String title;

        @NotBlank(message = "02-content-내용은 필수입니다.")
        @Size(min = 2, max = 100, message = "04-content-내용은 2자 이상 100자 이하로 입력해주세요.")
        private String content;
    }

    @GetMapping("/posts/write")
    @Transactional(readOnly = true)
    public String writeForm(@ModelAttribute("form") WriteRequestForm form) {
        return "write";
    }

    @PostMapping("/posts/write")
    public String write(@Valid @ModelAttribute("form") WriteRequestForm form,
                        BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return "write";
        }

        Post post = postService.write(form.title, form.content);
        return "redirect:/posts/%d".formatted(post.getId()); // GET요청
    }


    @AllArgsConstructor
    @Getter
    public static class ModifyRequestForm {
        @Size(min = 2, max = 10, message = "03-title-제목은 2자 이상 10자 이하로 입력해주세요.")
        @NotBlank(message = "01-title-제목은 필수입니다.")
        private String title;

        @NotBlank(message = "02-content-내용은 필수입니다.")
        @Size(min = 2, max = 100, message = "04-content-내용은 2자 이상 100자 이하로 입력해주세요.")
        private String content;
    }

    @GetMapping("/posts/{id}/modify")
    @Transactional(readOnly = true)
    public String modifyForm(@PathVariable int id, @ModelAttribute("form") ModifyRequestForm form) {

        Post post = postService.findById(id).get();

        form.title = post.getTitle();
        form.content = post.getContent();

        return "modify";
    }

    @PostMapping("/posts/{id}/modify")
    @Transactional
    public String modify(@PathVariable int id,
                         @Valid @ModelAttribute("form") ModifyRequestForm form,
                         BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return "modify";
        }

        Post post = postService.modify(id, form.title, form.content);
        return "redirect:/posts/%d".formatted(post.getId()); // GET요청
    }


    @GetMapping("/posts")
    @Transactional(readOnly = true)
    public String list(Model model) {

        model.addAttribute("posts", postService.findAll());
        return "list";
    }

    @GetMapping("/posts/{id}")
    @Transactional(readOnly = true)
    public String detail(@PathVariable int id, Model model) {
        Post post = postService.findById(id).get();
        model.addAttribute("post", post);

        return "detail";
    }

}