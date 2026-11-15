package me.yuninyeong.springbootdeveloper.repository;

import me.yuninyeong.springbootdeveloper.domain.Memo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MemoRepository extends JpaRepository<Memo, Long> {
    List<Memo> findAllByOrderByIdDesc();
}
