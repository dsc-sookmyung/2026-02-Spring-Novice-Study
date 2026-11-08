package me.yeonah.springbootdeveloper.controller;

import lombok.RequiredArgsConstructor;
import me.yeonah.springbootdeveloper.domain.Article;
import me.yeonah.springbootdeveloper.dto.ArticleListViewResponse;
import me.yeonah.springbootdeveloper.dto.ArticleViewResponse;
import me.yeonah.springbootdeveloper.service.BlogService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@RequiredArgsConstructor
@Controller
public class BlogViewController {
    private final BlogService blogService;
    //블로그 글 전체조회 뷰 컨트롤러 메서드
    @GetMapping("/articles")
    public String getArticles(Model model) {
        List<ArticleListViewResponse> articles = blogService.findAll().stream()
                .map(ArticleListViewResponse::new)
                .toList();

        model.addAttribute("articles", articles); //1. 블로그 글 리스트 저장

        return "articleList"; //2. articleList.html 조회
    }
    //블로그 글 상세조회 뷰 컨트롤러 메서드
    @GetMapping("/articles/{id}")
    public String getArticle(@PathVariable Long id, Model model) {
        Article article = blogService.findById(id);
        model.addAttribute("article", new ArticleViewResponse(article));
        return "article";
    }

    //수정 화면 보여주기위한 컨트롤러 메서드
    @GetMapping("/new-article")
    //id 키를 가진 쿼리 파라미터의 값을 id 변수에 매핑 (id 없을 수도 있음)
    public String newArticle(@RequestParam(required = false) Long id, Model model) {
        if (id == null) { //id 없으면 글 생성
            model.addAttribute("article", new ArticleViewResponse()); //빈 객체
        } else { //id가 있으면 글 수정
            Article article = blogService.findById(id); //id에 따라 엔티티 조회
            model.addAttribute("article", new ArticleViewResponse(article));
        }

        return "newArticle";
    }
}
