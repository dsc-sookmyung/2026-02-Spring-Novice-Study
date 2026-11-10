package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.domain.Article;
import org.example.dto.AddArticleRequest;
import org.example.dto.UpdateArticleRequest;
import org.example.repository.BlogRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class BlogService {

    private final BlogRepository blogRepository;

    // 블로그 글 추가 메서드
    public Article save(AddArticleRequest request, String userName) {
        return blogRepository.save(request.toEntity(userName));
    }
    public List<Article> findAll(){
        return blogRepository.findAll();
    }
    public Article findById(long id) {
        return blogRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("not found: " + id));
    }
    public void delete(Long id) {
        Article article = blogRepository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException("not found: " + id));

        authorizeArticleAuthor(article);

        blogRepository.delete(article);
    }

    @Transactional
    public Article update(Long id, UpdateArticleRequest request) {
        Article article = blogRepository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException("not found: " + id));

        authorizeArticleAuthor(article);

        article.update(request.getTitle(), request.getContent());

        return article;
    }
    private static void authorizeArticleAuthor(Article article) {
        String userName = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        if (!article.getAuthor().equals(userName)) {
            throw new IllegalArgumentException("not authorized");
        }
    }

}
