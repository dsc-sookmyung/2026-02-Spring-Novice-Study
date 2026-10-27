package me.su2in.springbootdeveloper.controller;

import lombok.RequiredArgsConstructor;
import me.su2in.springbootdeveloper.domain.Article;
import me.su2in.springbootdeveloper.dto.AddArticleRequest;
import me.su2in.springbootdeveloper.service.BlogService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class BlogApiController {

    private final BlogService blogService;

    // 블로그 글 생성 API
    @PostMapping("/api/articles")
    public ResponseEntity<Article> addArticle(
            @RequestBody AddArticleRequest request) {

        Article savedArticle = blogService.save(request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(savedArticle);
    }

    // 블로그 글 전체 조회 API
    @GetMapping("/api/articles")
    public ResponseEntity<List<Article>> findAllArticles() {
        List<Article> articles = blogService.findAll();

        return ResponseEntity.ok()
                .body(articles);
    }
}