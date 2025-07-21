package com.example.cosmetics.config;

import com.example.cosmetics.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, AuthenticationSuccessHandler successHandler) throws Exception {
        http
                .authorizeHttpRequests(authorizeRequests ->
                        authorizeRequests
                                .requestMatchers("/", "/shop", "/shop-detail", "/register", "/login", "/css/**", "/js/**", "/img/**").permitAll()
                                .requestMatchers("/admin/**").hasRole("Admin")
                                .requestMatchers("/customer/**").hasAnyRole("Admin", "Customer")
                                .requestMatchers("/profile", "/profile/edit", "/cart/**", "/checkout", "/checkout/**").authenticated()
                                .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .usernameParameter("email")
                        .successHandler(successHandler)
                        .failureUrl("/login?error=true")
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/")
                        .invalidateHttpSession(true)
                        .permitAll());
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance(); // Cần thay bằng BCryptPasswordEncoder trong thực tế
    }

    @Bean
    public UserDetailsService userDetailsService(UserService userService) {
        return email -> {
            com.example.cosmetics.entity.User user = userService.findByEmail(email);
            if (user == null) {
                throw new UsernameNotFoundException("Không tìm thấy email: " + email);
            }
            if (user.getStatus() == 0) {
                throw new UsernameNotFoundException("Tài khoản đã bị khóa: " + email);
            }
            System.out.println("Stored password: " + user.getPassword());
            return org.springframework.security.core.userdetails.User
                    .withUsername(user.getEmail())
                    .password(user.getPassword())
                    .roles(user.getRole().getRoleName())
                    .disabled(user.getStatus() == 0) // Đảm bảo tài khoản không hoạt động bị vô hiệu hóa
                    .build();
        };
    }

    @Bean
    public AuthenticationSuccessHandler authenticationSuccessHandler() {
        return (request, response, authentication) -> {
            if (authentication.getAuthorities().stream()
                    .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_Admin"))) {
                response.sendRedirect(request.getContextPath() + "/admin");
            } else {
                response.sendRedirect(request.getContextPath() + "/");
            }
        };
    }
}