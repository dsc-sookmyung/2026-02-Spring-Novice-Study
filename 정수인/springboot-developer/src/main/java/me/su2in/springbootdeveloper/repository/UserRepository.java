package me.su2in.springbootdeveloper.repository;

import me.su2in.springbootdeveloper.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
