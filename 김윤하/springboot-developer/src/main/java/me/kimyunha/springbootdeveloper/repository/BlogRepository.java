package me.kimyunha.springbootdeveloper.repository;

import me.kimyunha.springbootdeveloper.domain.Article;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BlogRepository extends JpaRepository<Article, Long> {
}
