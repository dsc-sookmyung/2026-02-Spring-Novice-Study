package me.shimhyunhee.springbootdeveloper;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest // 테스트를 위한 설정 제공
class MemberRepositoryTest {
    @Autowired
    MemberRepository memberRepository;

    @AfterEach
    public void cleanup() {
        memberRepository.deleteAll();
    }

    @Sql("/insert-members.sql") // @Sql : 테스트를 실행하기 전 SQL 스크립트 실행 가능
    @Test
    void getAllMembers() {
        // when
        List<Member> members = memberRepository.findAll();

        //then
        assertThat(members.size()).isEqualTo(3);

    }

    // SELECT * FROM member WHERE id = 2;와 동일한 JPA의 조회 메서드
    @Sql("/insert-members.sql")
    @Test
    void getMemberById() {
        // when
        Member member = memberRepository.findById(2L).get();

        // then
        assertThat(member.getName()).isEqualTo("B");
    }

    // SELECT * FROM member WHERE name = 'C'와 동일한 JPA의 쿼리 메서드
    @Sql("/insert-members.sql")
    @Test
    void getMemberByName() {
        // when
        Member member = memberRepository.findByName("C").get();

        // then
        assertThat(member.getId()).isEqualTo(3);
    }

    //INSERT INTO member (id, name)
    //VALUES (1, 'A');와 동일한 JPA의 추가 메서드
    // 이미 추가된 데이터가 있으면 안 되므로 @Sql 애너테이션 사용하지 않음
    @Test
    void saveMember() {
        // given : 새로운 A 멤버 객체 준비
        Member member = new Member(null, "A");

        //when : 실제로 저장
        memberRepository.save(member);

        //then : 1번 아이디에 해당하는 멤버의 이름 가져오기
        assertThat(memberRepository.findById(1L).get().getName()).isEqualTo("A");
    }

    // 여러 엔티티를 한번에 저장하는 메서드
    @Test
    void saveMembers() {
        // given
        List<Member> members = List.of(new Member(null, "B"), // 추가할 멤버 객체들을 리스트로 만듦
        new Member(null, "C"));

        //when
        memberRepository.saveAll(members);

        //then
        assertThat(memberRepository.findAll().size()).isEqualTo(2);
    }

    // DELETE FROM member WHERE id = '2';와 동일한 JPA의 삭제 메서드
    @Sql("/insert-members.sql") // 해당 스크립트로 3명의 멤버 추가
    @Test
    void deleteMemberById() { // 아이디로 레코드 삭제하기
        //when
        memberRepository.deleteById(2L); // 2번 멤버 삭제

        //then
        assertThat(memberRepository.findById(2L).isEmpty()).isTrue(); // 2번 아이디를 가진 레코드가 있는지 조회
    }

    // DELETE FROM member
    @Sql("/insert-members.sql")
    @Test
    void deleteAll() {
        //when
        memberRepository.deleteAll(); // 모든 멤버 삭제

        //then
        assertThat(memberRepository.findAll().size()).isZero(); // 멤버들 조회 크기가 0인지 검증
    }

    // UPDATE member
    // SET name = 'BC'
    // WHERE id = 2;와 동일한 JPA의 수정 메서드
    @Sql("/insert-members.sql")
    @Test
    void update() {
        // given
        Member member = memberRepository.findById(2L).get();

        // when
        member.changeName("BC");

        // then
        assertThat(memberRepository.findById(2L).get().getName()).isEqualTo("BC");
    }
}

