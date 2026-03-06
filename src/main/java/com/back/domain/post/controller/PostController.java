package com.back.domain.post.controller;

import com.back.domain.post.entity.Post;
import com.back.domain.post.service.PostService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @GetMapping("/posts/write-form")
    @ResponseBody
    public String writeForm() {

        return getWriteForm("", "", "", "");
    }

    @AllArgsConstructor
    public static class WriteRequestForm {
        @Size(min=2, max=10, message = "3-제목은 2자 이상 10자 이하로 입력해주세요.")
        @NotBlank(message = "1-제목은 필수입니다.")
        private String title;

        @NotBlank(message = "2-내용은 필수입니다.")
        @Size(min=2, max=100, message = "4-내용은 2자 이상 100자 이하로 입력해주세요.")
        private String content;
    }

    @PostMapping("/posts/write")
    @ResponseBody
    public String write(@Valid WriteRequestForm form, BindingResult bindingResult) {

        if(bindingResult.hasErrors()) {

            String errorMessages = bindingResult.getFieldErrors()
                    .stream()
                    .map(FieldError::getDefaultMessage)
                    .sorted()
                    .collect(Collectors.joining("<br>"));

            return getWriteForm(errorMessages, form.title, form.content, "title");
        }

        Post post = postService.write(form.title, form.content);

        return "%d번 글이 작성되었습니다.".formatted(post.getId());
    }

    private String getWriteForm(String errorMessage, String title, String content, String errorFieldName) {
        return """
                <div style="color:red">%s</div>
                <form method="post" action="/posts/write">
                  <input type="text" name="title" value="%s" autoFocus>
                  <br>
                  <textarea name="content">%s</textarea>
                  <br>
                  <input type="submit" value="작성">
                </form>
                
                <script>
                    const errorFieldName = "%s";
                
                    if(errorFieldName.length > 0) {
                        const form = document.querySelector("form");
                        form[errorFieldName].focus();
                    }
                </script>
                """.formatted(errorMessage, title, content, errorFieldName);
    }

}