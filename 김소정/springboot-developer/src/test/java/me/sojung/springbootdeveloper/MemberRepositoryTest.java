package me.sojung.springbootdeveloper;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class MemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;
    @AfterEach
    public void cleanUp(){
        memberRepository.deleteAll();
    }
    @Test
    void deleteAll(){
        // when
        memberRepository.deleteAll();
        // then
        assertThat(memberRepository.findAll().size()).isZero();
    }
    @Test
    void deleteMemberById(){
        // when
        memberRepository.deleteById(2L);
        // then
        assertThat(memberRepository.findById(2L).isEmpty()).isTrue();
    }
    @Test
    void saveMember() {
        List<Member> members = List.of(new Member(2L, "B"), new Member(3L, "C"));

        memberRepository.saveAll(members);

        assertThat(memberRepository.findAll().size()).isEqualTo(2);
    }

    @Sql("/insert-members.sql")
    @Test
    void update(){
        //given
        Member member = memberRepository.findById(2L).get();
        //when
        member.changeName("BC");
        //then
        assertThat(memberRepository.findById(2L).get().getName()).isEqualTo("BC");
    }
    @Sql("/insert-members.sql")
    @Test
    void getAllMembers() {
        // when
        Member member = memberRepository.findByName("C").get();

        // then
        assertThat(member.getId()).isEqualTo(3L);
    }
}