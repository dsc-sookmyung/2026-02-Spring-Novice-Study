package me.shimhyunhee.springbootdeveloper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TestService {

    @Autowired
    MemberRepository memberRepository; // MemberRepository 빈 주입

    public List<Member> getAllMembers() {
        return memberRepository.findAll(); // findAll 메서드 호출하여 멤버 테이블에 저장된 멤버 목록 얻기
    }
}
