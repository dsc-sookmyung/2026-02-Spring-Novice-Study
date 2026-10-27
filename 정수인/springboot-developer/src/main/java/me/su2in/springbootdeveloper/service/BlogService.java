
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
}
