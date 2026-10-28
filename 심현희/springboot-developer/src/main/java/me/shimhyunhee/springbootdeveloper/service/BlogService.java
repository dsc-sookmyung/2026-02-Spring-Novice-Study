package me.shimhyunhee.springbootdeveloper.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import me.shimhyunhee.springbootdeveloper.domain.Article;
import me.shimhyunhee.springbootdeveloper.dto.AddArticleRequest;
import me.shimhyunhee.springbootdeveloper.dto.UpdateArticleRequest;
import me.shimhyunhee.springbootdeveloper.repository.BlogRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor // final이 붙거나 @NotNull이 붙은 필드의 생성자 추가
@Service // 빈으로 등록
public class BlogService {

    private final BlogRepository blogRepository;

    // 블로그 글 추가 메서드
    public Article save(AddArticleRequest request) {
        return  blogRepository.save(request.toEntity());
    }

    // 데이터베이스에 저장되어 있는 글을 모두 가져오는 메서드
    // article 테이블에 저장된 모든 테이블 조회
    public List<Article> findAll() {
        return blogRepository.findAll();
    }

    // 데이터베이스에 저장되어 있는 글의 ID를 이용하여 글을 조회하는 메서드
    public Article findById(long id) {
        return blogRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("not found: " + id));
                // 없을 경우 IllegalArgumentException 예외 발생
    }

    // 블로그 글의 ID를 받은 뒤 데이터베이스에서 데이터를 삭제하는 메서드
    public void delete(long id) {
        blogRepository.deleteById(id);
    }

    // 리포지토리를 사용해 글을 수정하는 메서드
    @Transactional // 트랜잭션 메서드 : 매칭한 메서드를 하나의 트랜잭션으로 묶는 역할
    public Article update(long id, UpdateArticleRequest request) {
        Article article = blogRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("not found: " + id));

        article.update(request.getTitle(), request.getContent());

        return article;
    }
}
