package com.api.task_manager.Config;

import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("Trying to load user " + username);
        if(username.equals("admin")) {
            return User.withUsername("admin").password("{noop}admin")
                    .roles("ADMIN")
                    .build();
        }
        else {
            throw new UsernameNotFoundException("User not found");
        }
    }
}
