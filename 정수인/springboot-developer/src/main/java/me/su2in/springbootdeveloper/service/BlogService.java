package me.su2in.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import me.su2in.dto.AddArticleRequest;
import me.su2in.springbootdeveloper.domain.Article;
import me.su2in.springbootdeveloper.repository.BlogRepository;

@RequiredArgsConstructor
@Service
public class BlogService {

    private final BlogRepository blogRepository;

    // 블로그 글 추가 메서드
    public Article save(AddArticleRequest request) {
        return blogRepository.save(request.toEntity());
    }
}