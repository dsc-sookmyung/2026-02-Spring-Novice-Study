package me.sojung.springbootdeveloper;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
// 자바의 main() 메서드와 같은 역할을 한다.
// 여기서 스프링 부트가 시작됨

// 애너테이션 --> 스프링 부트 사용에 필요한 기본 설정
@SpringBootApplication
public class SpringBootDeveloperApplication {
    public static void main(String[] args) {
        // 애플리케이션 실행
        SpringApplication.run(SpringBootDeveloperApplication.class, args);
    }
}
