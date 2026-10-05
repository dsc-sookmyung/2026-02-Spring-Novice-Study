package me.shimhyunhee.springbootdeveloper;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity // 1. 엔터티로 지정 : Member객체를 JPA가 관리하는 엔티티(Member클래스와 실제 DB 테이블 매핑)
@NoArgsConstructor(access = AccessLevel.PROTECTED) // 2. 기본 생성자
@AllArgsConstructor

public class Member {
    @Id // 3. id 필드를 테이블의 기본키로 지정 (Long 타입)
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 기본키의 생성 방식을 결정
    // 4. 기본키를 자동으로 1씩 증가
    @Column(name = "id", updatable = false)
    private Long id; // DB 테이블의 'id' 컬럼과 매칭

    @Column(name = "name", nullable = false) // 5. name이라는 not null 컬럼과 매핑
    private String name; // DB 테이블의 'name' 컬럼과 매칭

    // 해당 메서드가 @Transactional 애너테이션이 포함된 메서드에서 호출된 경우
    // JPA는 변경 감지 기능을 통해 엔티티의 필드값이 변경될 때 그 변경 사항을 DB에 자동으로 반영
    public void changeName(String name) {
        this.name = name;
    }
}
