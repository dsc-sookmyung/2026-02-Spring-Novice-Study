package me.su2in.springbootdeveloper.controller;

import lombok.RequiredArgsConstructor;
import me.su2in.springbootdeveloper.domain.Article;
import me.su2in.springbootdeveloper.dto.AddArticleRequest;
import me.su2in.springbootdeveloper.dto.ArticleResponse;
import me.su2in.springbootdeveloper.dto.UpdateArticleRequest;
import me.su2in.springbootdeveloper.service.BlogService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class BlogApiController {

    private final BlogService blogService;

    // 새로운 블로그 글 생성
    @PostMapping("/api/articles")
    public ResponseEntity<Article> addArticle(
            @RequestBody AddArticleRequest request) {

        Article savedArticle = blogService.save(request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(savedArticle);
    }

    // 모든 블로그 글 조회
    @GetMapping("/api/articles")
    public ResponseEntity<List<Article>> findAllArticles() {
        List<Article> articles = blogService.findAll();

        return ResponseEntity.ok()
                .body(articles);
    }

    // ID에 해당하는 블로그 글 조회
    @GetMapping("/api/articles/{id}")
    public ResponseEntity<ArticleResponse> findArticle(@PathVariable long id) {
        Article article = blogService.findById(id);

        return ResponseEntity.ok()
                .body(new ArticleResponse(article));
    }

    // ID에 해당하는 블로그 글 삭제
    @DeleteMapping("/api/articles/{id}")
    public ResponseEntity<Void> deleteArticle(@PathVariable long id) {
        blogService.delete(id);

        return ResponseEntity.ok()
                .build();
    }

    // ID에 해당하는 블로그 글의 제목과 내용 수정
    @PutMapping("/api/articles/{id}")
    public ResponseEntity<Article> updateArticle(
            @PathVariable long id,
            @RequestBody UpdateArticleRequest request) {

        Article updatedArticle = blogService.update(id, request);

        return ResponseEntity.ok()
                .body(updatedArticle);
    }
}