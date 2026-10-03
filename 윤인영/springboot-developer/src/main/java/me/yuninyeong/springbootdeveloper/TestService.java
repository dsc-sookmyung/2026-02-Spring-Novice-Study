package me.yuninyeong.springbootdeveloper;

import org.springframework.beans.factory.annotation.Autowired;

@Service
public class TestService {

    @Autowired
    MemberRepository memberRepository;

    public List<Member> getAllMembers() {
        return memberRepository.findAll();
    }
}
