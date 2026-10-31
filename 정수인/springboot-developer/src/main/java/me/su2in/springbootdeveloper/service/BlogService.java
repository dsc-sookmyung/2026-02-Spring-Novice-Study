package me.su2in.springbootdeveloper.service;

import lombok.RequiredArgsConstructor;
import me.su2in.springbootdeveloper.domain.Article;
import me.su2in.springbootdeveloper.dto.AddArticleRequest;
import me.su2in.springbootdeveloper.dto.UpdateArticleRequest;
import me.su2in.springbootdeveloper.repository.BlogRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class BlogService {

    private final BlogRepository blogRepository;

    // 새로운 블로그 글을 데이터베이스에 저장
    public Article save(AddArticleRequest request) {
        return blogRepository.save(request.toEntity());
    }

    // 저장된 모든 블로그 글 조회
    public List<Article> findAll() {
        return blogRepository.findAll();
    }

    // ID로 특정 블로그 글 조회 (존재하지 않으면 예외 발생)
    public Article findById(long id) {
        return blogRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("not found : " + id));
    }

    // ID에 해당하는 블로그 글 삭제
    public void delete(long id) {
        Article article = blogRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("not found : " + id));

        blogRepository.delete(article);
    }

    // ID로 게시글을 찾아 제목과 내용을 수정
    @Transactional
    public Article update(long id, UpdateArticleRequest request) {
        Article article = blogRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("not found : " + id));

        article.update(request.getTitle(), request.getContent());

        return article;
    }
}