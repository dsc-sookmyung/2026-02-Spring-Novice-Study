package me.su2in.springbootdeveloper;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    // 이름으로 멤버 조회 쿼리 메서드
    Optional<Member> findByName(String name);
}