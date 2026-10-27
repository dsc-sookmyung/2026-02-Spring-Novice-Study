package me.su2in.springbootdeveloper.repository.repository;

import me.su2in.springbootdeveloper.domain.Article;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BlogRepository extends JpaRepository<Article, Long> {
}
