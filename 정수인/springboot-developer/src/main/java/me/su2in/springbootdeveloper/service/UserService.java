package me.su2in.springbootdeveloper.service;

import lombok.RequiredArgsConstructor;
import me.su2in.springbootdeveloper.domain.User;
import me.su2in.springbootdeveloper.repository.UserRepository;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;

    public User findById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Unexpected user"));
    }
}