package me.shimhyunhee.springbootdeveloper.controller;


import lombok.RequiredArgsConstructor;
import me.shimhyunhee.springbootdeveloper.domain.Article;
import me.shimhyunhee.springbootdeveloper.dto.ArticleListViewResponse;
import me.shimhyunhee.springbootdeveloper.dto.ArticleViewResponse;
import me.shimhyunhee.springbootdeveloper.service.BlogService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@RequiredArgsConstructor
@Controller
public class BlogViewController {

    private final BlogService blogService;

    // 블로그 글 목록 뷰
    @GetMapping("/articles")
    public String getArticles(Model model) {
        List<ArticleListViewResponse> articles = blogService.findAll().stream()
                .map(ArticleListViewResponse::new)
                .toList();
        model.addAttribute("articles", articles); // 1. 블로그 글 리스트 저장

        return "articleList"; // 2. articleList.html 라는 뷰 조회
    }

    // 블로그 글 뷰
    @GetMapping("articles/{id}")
    public String getArticle(@PathVariable Long id, Model model) {
        Article article = blogService.findById(id);
        model.addAttribute("article", new ArticleViewResponse(article));

        return "article";
    }

    // 블로그 글 수정/생성 뷰
    @GetMapping("/new-article")
    // 1. id 키를 가진 쿼리 파라미터의 값을 id 변수에 매핑(id는 없을 수도 있음)
    public String newArticle(@RequestParam(required = false) Long id, Model model) {
        if (id == null) { // 2. id가 없을 경우 생성
            model.addAttribute("article", new ArticleViewResponse());
        } else { // 3. id가 없을 경우 수정
            Article article = blogService.findById(id);
            model.addAttribute("article", new ArticleViewResponse(article));
        }

        return "newArticle";
    }
}
