package me.su2in.springbootdeveloper.service;

import lombok.RequiredArgsConstructor;
import me.su2in.springbootdeveloper.domain.Article;
import me.su2in.springbootdeveloper.dto.AddArticleRequest;
import me.su2in.springbootdeveloper.dto.UpdateArticleRequest;
import me.su2in.springbootdeveloper.repository.BlogRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class BlogService {

    private final BlogRepository blogRepository;

    // 새로운 블로그 글을 데이터베이스에 저장
    public Article save(AddArticleRequest request) {
        String email = getCurrentUserEmail();

        return blogRepository.save(request.toEntity(email));
    }

    // 저장된 모든 블로그 글 조회
    public List<Article> findAll() {
        return blogRepository.findAll();
    }

    // ID로 특정 블로그 글 조회
    public Article findById(long id) {
        return blogRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("not found : " + id));
    }

    // ID에 해당하는 블로그 글 삭제
    public void delete(long id) {
        Article article = blogRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("not found : " + id));

        authorizeArticleAuthor(article);
        blogRepository.delete(article);
    }

    // ID로 게시글을 찾아 제목과 내용을 수정
    @Transactional
    public Article update(long id, UpdateArticleRequest request) {
        Article article = blogRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("not found : " + id));

        authorizeArticleAuthor(article);
        article.update(request.getTitle(), request.getContent());

        return article;
    }

    // 현재 로그인한 사용자의 이메일 조회
    private String getCurrentUserEmail() {
        Authentication authentication = SecurityContextHolder.getContext()
                .getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()
                || "anonymousUser".equals(authentication.getPrincipal())) {
            throw new IllegalStateException("로그인이 필요합니다.");
        }

        Object principal = authentication.getPrincipal();

        if (principal instanceof OAuth2User) {
            String email = ((OAuth2User) principal).getAttribute("email");

            if (email == null) {
                throw new IllegalStateException("사용자 이메일을 찾을 수 없습니다.");
            }

            return email;
        }

        return authentication.getName();
    }

    // 게시글 작성자 확인
    private void authorizeArticleAuthor(Article article) {
        String email = getCurrentUserEmail();

        if (!email.equals(article.getAuthor())) {
            throw new IllegalArgumentException("not authorized");
        }
    }
}
