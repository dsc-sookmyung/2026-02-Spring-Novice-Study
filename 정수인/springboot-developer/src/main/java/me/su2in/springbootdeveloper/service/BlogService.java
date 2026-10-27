package me.su2in.springbootdeveloper.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import me.su2in.springbootdeveloper.dto.AddArticleRequest;
import me.su2in.springbootdeveloper.domain.Article;
import me.su2in.springbootdeveloper.repository.BlogRepository;
import java.util.List;

@RequiredArgsConstructor
@Service
public class BlogService {

    private final BlogRepository blogRepository;

    // 블로그 글 추가 메서드
    public Article save(AddArticleRequest request) {
        return blogRepository.save(request.toEntity());
    }

    public List<Article> findAll() {
        return blogRepository.findAll();
    }

    // 블로그 글 하나 조회 메서드 (192쪽)
    public Article findById(long id) {
        return blogRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("not found : " + id));
    }

    // 블로그 글 삭제 메서드 (194쪽)
    public void delete(long id) {
        Article article = blogRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("not found : " + id));

        blogRepository.delete(article);
    }
}