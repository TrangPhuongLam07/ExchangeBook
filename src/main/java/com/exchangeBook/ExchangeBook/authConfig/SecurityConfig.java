package com.exchangeBook.ExchangeBook.authConfig;

import com.exchangeBook.ExchangeBook.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;

import java.util.Map;

/*
Create By : ANHTUAN
*/
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private UserRepository userRepository;
    private Authentication authentication;
    @Autowired
    private CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;

    @Autowired
    ObjectMapper objectMapper;

    //authentication
    @Bean
    public UserDetailsService userDetailsService() {
        // memory users have info email, password, roles to authorize (SecurityContextHolder)
        return new UserDetailService_Security(userRepository);
    }

    @Autowired
    public SecurityConfig(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        authentication = SecurityContextHolder.getContext().getAuthentication();

        // handle url
        http.
                csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(authorize ->
                        authorize
                                .requestMatchers("/roles/save", "/users/register","/users/login").permitAll()
//                                .requestMatchers("/users/delete/{id}").permitAll()
                                .requestMatchers("/users/**").hasAuthority("ROLE_USER")
//                                .requestMatchers("/admin/**").permitAll()
                                .requestMatchers("/admin/**").hasAuthority("ROLE_ADMIN")

                                .anyRequest().authenticated()

                ).formLogin(Customizer.withDefaults()
                );

        // Handel's error 403 and Return a text type json
        http.exceptionHandling(exceptionHandling ->
                exceptionHandling
                        .accessDeniedHandler(accessDeniedHandler())
        );

//         Because is not form Login from FE yet, so I close
        http.formLogin(form -> form

//                .loginPage("/login-test") // your font to login (GET)
                .loginProcessingUrl("/login-test") // you have the method to handle credentials (POST)

                        // I USED FORM DEFAULT TO TEST WHEN LOGIN SUCCESS, IF YOU HAVE FORMS LET USE IT AND CLOSE THIS
                .successHandler(customAuthenticationSuccessHandler)
                .permitAll()
        );

//        http.formLogin(form -> form
//                .loginProcessingUrl("/users/login-auth")
//                .defaultSuccessUrl("/users/{id}")
//                .permitAll()
//        );
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return (request, response, accessDeniedException) -> {
            response.setStatus(HttpStatus.FORBIDDEN.value());
            response.setContentType("application/json");
            response.getWriter().write(objectMapper.writeValueAsString(Map.of("error403", "YOU DON'T AUTHORIZE")));
        };
    }
}
