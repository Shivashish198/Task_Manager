package com.api.task_manager.Controller;

import com.api.task_manager.Config.CustomUserDetailsService;
import com.api.task_manager.Config.JwtUtil;
import com.api.task_manager.DTO.AuthRequest;
import io.jsonwebtoken.Claims;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import lombok.Data;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
@Data
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final CustomUserDetailsService userDetailsService;

    @PostMapping("/login")
    public ResponseEntity<?> login (@RequestBody AuthRequest authRequest) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
        UserDetails userDetails = userDetailsService.loadUserByUsername(authRequest.getUsername());
        String token = jwtUtil.generateToken(userDetails.getUsername());
        return ResponseEntity.ok(Collections.singletonMap("token", token));
    }

    @PostMapping("/token-info")
    public ResponseEntity<?> getInfo(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.substring(7); //Removes "Bearer "
        Claims claims = jwtUtil.extractAll(token);
        Date iat = claims.getIssuedAt();
        Date exp = claims.getExpiration();
        Map<String, Object> response = new HashMap<>();
        response.put("issued at: ", iat);
        response.put("expires at: ", exp);
        return ResponseEntity.ok(response);
    }
}
