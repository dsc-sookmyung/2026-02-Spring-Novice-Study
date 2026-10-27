package me.yeonah.springbootdeveloper.service;

import lombok.RequiredArgsConstructor;
import me.yeonah.springbootdeveloper.domain.Article;
import me.yeonah.springbootdeveloper.dto.AddArticleRequest;
import me.yeonah.springbootdeveloper.repository.BlogRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@RequiredArgsConstructor // final이 붙거나 @NotNull이 붙은 필드의 생성자 추가
@Service //빈으로 등록
public class BlogService {

    private final BlogRepository blogRepository;

    //블로그 글 추가 메서드
    public Article save(AddArticleRequest request){
        return blogRepository.save(request.toEntity());
    }

    //모든 글 조회 메서드
    public List<Article> findAll() {
        return blogRepository.findAll();
    }

    //글 상세 조회 메서드
    public Article findById(long id){
        return blogRepository.findById(id)
                .orElseThrow(()-> new IllegalArgumentException("not found: "+id));
    }
}
