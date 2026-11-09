
package me.su2in.springbootdeveloper.controller;

import lombok.RequiredArgsConstructor;
import me.su2in.springbootdeveloper.dto.AddUserRequest;
import me.su2in.springbootdeveloper.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class UserApiController {

    private final UserService userService;

    @PostMapping("/user")
    public ResponseEntity<Long> signup(@RequestBody AddUserRequest request) {
        Long userId = userService.save(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(userId);
    }
}
