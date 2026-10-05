package org.example.springbootdeveloper;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

//스프링 부트 사용에 필요한 기본 설정을 해주는 에너테이션
@SpringBootApplication
public class SpringBootDeveloperApplication {
    public static void main(String[] args) {
        //애플리케이션 실행
        SpringApplication.run(SpringBootDeveloperApplication.class, args);
    }
}

