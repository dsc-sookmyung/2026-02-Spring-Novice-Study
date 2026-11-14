package me.yeonah.springbootdeveloper.service;

import lombok.RequiredArgsConstructor;
import me.yeonah.springbootdeveloper.domain.User;
import me.yeonah.springbootdeveloper.dto.AddUserRequest;
import me.yeonah.springbootdeveloper.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;

    public Long save(AddUserRequest dto) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        return userRepository.save(User.builder()
                .email(dto.getEmail())
                .password(encoder.encode(dto.getPassword())) //패스워드 암호화
                .build()).getId();
    }
    //유저 ID로 유저 검색해서 전달하는 메서드
    public User findById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Unexpected user"));
    }
    //이메일을 입력받아 users 테이블에서 유저를 찾고 없으면 예외 발생
    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Unexpected user"));
    }
}
