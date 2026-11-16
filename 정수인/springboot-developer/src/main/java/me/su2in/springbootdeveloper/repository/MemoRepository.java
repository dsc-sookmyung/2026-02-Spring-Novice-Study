
package me.su2in.springbootdeveloper.repository;

import me.su2in.springbootdeveloper.domain.Memo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MemoRepository extends JpaRepository<Memo, Long> {

    List<Memo> findAllByOrderByIdDesc();
}
