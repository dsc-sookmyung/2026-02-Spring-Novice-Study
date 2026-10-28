package me.shimhyunhee.springbootdeveloper.repository;

import me.shimhyunhee.springbootdeveloper.domain.Article;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BlogRepository extends JpaRepository<Article, Long> {
}
