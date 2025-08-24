package com.api.task_manager.Controller;

import com.api.task_manager.Config.CustomUserDetailsService;
import com.api.task_manager.Config.JwtUtil;
import com.api.task_manager.DTO.AuthRequest;
import com.api.task_manager.DTO.RegisterRequest;
import com.api.task_manager.Model.UserAcc;
import com.api.task_manager.Repository.UserRepo;
import io.jsonwebtoken.Claims;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import lombok.Data;
import java.util.*;

@RestController
@RequestMapping("/auth")
@Data
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final CustomUserDetailsService userDetailsService;
    private final BCryptPasswordEncoder encoder;
    private final UserRepo repo;

    @PostMapping("/login")
    public ResponseEntity<?> login (@RequestBody AuthRequest authRequest) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
        UserDetails userDetails = userDetailsService.loadUserByUsername(authRequest.getUsername());
        String token = jwtUtil.generateToken(userDetails);
        return ResponseEntity.ok(Collections.singletonMap("token", token));
    }

    @PostMapping("/register")
    public ResponseEntity<?> register (@RequestBody RegisterRequest reg) {
        if(repo.findByUsername(reg.getUsername()).isPresent()) {
            return ResponseEntity.badRequest().body("Username already exists");
        }
        UserAcc user = UserAcc.builder()
                .username(reg.getUsername())
                .password(encoder.encode(reg.getPassword()))
                .role("ROLE_" + (reg.getRole()==null? "USER" : reg.getRole().toUpperCase()))
                        .build();
        repo.save(user);
        return ResponseEntity.ok("User registered");
    }

    @PostMapping("/token-info")
    public ResponseEntity<?> getInfo(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.substring(7); //Removes "Bearer "
        Claims claims = jwtUtil.extractAll(token);
        Date iat = claims.getIssuedAt();
        Date exp = claims.getExpiration();
        String user = claims.getSubject();
        Map<String, Object> response = new HashMap<>();
        response.put("User: ", user);
        response.put("issued at: ", iat);
        response.put("expires at: ", exp);
        return ResponseEntity.ok(response);
    }
}
