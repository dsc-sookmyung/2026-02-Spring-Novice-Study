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
}
