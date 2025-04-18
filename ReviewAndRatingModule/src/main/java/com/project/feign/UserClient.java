package com.project.feign;

import com.project.dto.UserDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "USER-SERVICE",fallback = FallBackService.class)
public interface UserClient {

	@GetMapping("dbs/user/admin/get-user/{userId}")
	ResponseEntity<UserDTO> getUserById(@PathVariable Long userId);
}
