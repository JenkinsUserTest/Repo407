package com.project.feign;

import com.project.dto.UserDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class FallBackService implements UserClient{
    @Override
    public ResponseEntity<UserDTO> getUserById(Long userId) {
        System.out.println("--fall back method--");
;        return new ResponseEntity<>(null, HttpStatus.OK);
    }
}
