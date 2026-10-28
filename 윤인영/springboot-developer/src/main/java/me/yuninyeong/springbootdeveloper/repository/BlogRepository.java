package me.yuninyeong.springbootdeveloper.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import me.yuninyeong.springbootdeveloper.domain.Article;

public interface BlogRepository extends JpaRepository<Article, Long> {
    
}
