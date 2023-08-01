package com.exchangeBook.ExchangeBook.controller;

import com.exchangeBook.ExchangeBook.model.Role;
import com.exchangeBook.ExchangeBook.model.User;
import com.exchangeBook.ExchangeBook.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
Create By : ANHTUAN
*/
@RestController
public class AuthController {

    @Autowired
    private UserService userService;
    @Autowired
    ObjectMapper objectMapper;
    private Authentication authentication;

    // --------------- FOR ROLE ---------------

    @PostMapping("/roles/save")
    public ResponseEntity<Role> saveRole(@RequestBody Role role) {
        return ResponseEntity.ok(userService.saveRole(role));
    }

    // --------------- FOR ADMIN --------------
    @PostMapping("/admin/save")
    public ResponseEntity<?> saveAdmin(@RequestBody User user) throws JsonProcessingException {

        if (userService.existsByEmail(user.getEmail()) == true || userService.existsByUsername(user.getUserName())){
            Map<String, String> body = new HashMap<>();
            body.put("error-exists", "USERNAME OR EMAIL EXISTS");
            String jsonBody = objectMapper.writeValueAsString(body);
            return ResponseEntity.ok().body(jsonBody);
        }

        return ResponseEntity.ok(userService.saveUserRoleAdmin(user));
    }

    @PostMapping ("/admin/login-auth")
    public ResponseEntity<?> loginAmin(@RequestBody String username, @RequestBody String password) throws JsonProcessingException {

        authentication = SecurityContextHolder.getContext().getAuthentication();
        User loginByUsername = userService.getUserByUsername(username);
        User loginByEmail = userService.getUserByEmail(username);

        if (loginByUsername != null) {
            if(userService.authenticate(loginByUsername.getPassword(),password)){
                Map<String, String> body = new HashMap<>();
                body.put("OK", "LOGIN SUCCESSFULLY");
                String jsonBody = objectMapper.writeValueAsString(body);
                return ResponseEntity.ok().body(jsonBody);
            }else {
                Map<String, String> body = new HashMap<>();
                body.put("FAIL", "LOGIN FAIL");
                String jsonBody = objectMapper.writeValueAsString(body);
                return ResponseEntity.ok().body(jsonBody);
            }
        }
        if (loginByEmail != null) {
            if(userService.authenticate(loginByUsername.getPassword(),password)){
                Map<String, String> body = new HashMap<>();
                body.put("OK", "LOGIN SUCCESSFULLY");
                String jsonBody = objectMapper.writeValueAsString(body);
                return ResponseEntity.ok().body(jsonBody);
            }else {
                Map<String, String> body = new HashMap<>();
                body.put("FAIL", "LOGIN FAIL");
                String jsonBody = objectMapper.writeValueAsString(body);
                return ResponseEntity.ok().body(jsonBody);
            }
        }

        return null;
    }

    // --------------- FOR USER --------------
    @PostMapping("/users/register")
    public ResponseEntity<?> register(@RequestBody User user) throws JsonProcessingException {

        if (userService.existsByEmail(user.getEmail()) == true || userService.existsByUsername(user.getUserName())){
            Map<String, String> body = new HashMap<>();
            body.put("error-exists", "USERNAME OR EMAIL EXISTS");
            String jsonBody = objectMapper.writeValueAsString(body);
            return ResponseEntity.ok().body(jsonBody);
        }
        return ResponseEntity.ok(userService.saveUserRoleUser(user));
    }

    @PostMapping("/users/login-auth")
    public ResponseEntity<?> login(@RequestBody String username, @RequestBody String password) throws JsonProcessingException {

        authentication = SecurityContextHolder.getContext().getAuthentication();
        User loginByUsername = userService.getUserByUsername(username);
        User loginByEmail = userService.getUserByEmail(username);

        if (loginByUsername != null) {
            if(userService.authenticate(loginByUsername.getPassword(),password)){
                Map<String, String> body = new HashMap<>();
                body.put("OK", "LOGIN SUCCESSFULLY");
                String jsonBody = objectMapper.writeValueAsString(body);
                return ResponseEntity.ok().body(jsonBody);
            }else {
                Map<String, String> body = new HashMap<>();
                body.put("FAIL", "LOGIN FAIL");
                String jsonBody = objectMapper.writeValueAsString(body);
                return ResponseEntity.ok().body(jsonBody);
            }
        }
        if (loginByEmail != null) {
            if(userService.authenticate(loginByUsername.getPassword(),password)){
                Map<String, String> body = new HashMap<>();
                body.put("OK", "LOGIN SUCCESSFULLY");
                String jsonBody = objectMapper.writeValueAsString(body);
                return ResponseEntity.ok().body(jsonBody);
            }else {
                Map<String, String> body = new HashMap<>();
                body.put("FAIL", "LOGIN FAIL");
                String jsonBody = objectMapper.writeValueAsString(body);
                return ResponseEntity.ok().body(jsonBody);
            }
        }

        return null;
    }

    // Check user login or not
    @GetMapping("/login-check")
    public ResponseEntity<?> loginCheck() throws JsonProcessingException {
        authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication != null){
            Map<String, String> body = new HashMap<>();
            body.put("OK", "YOU ARE LOGIN");
            String jsonBody = objectMapper.writeValueAsString(body);
            return ResponseEntity.ok().body(jsonBody);
        }else {
            Map<String, String> body = new HashMap<>();
            body.put("FAIL", "YOU ARE NOT LOGGED IN");
            String jsonBody = objectMapper.writeValueAsString(body);
            return ResponseEntity.ok().body(jsonBody);
        }
    }
    // test login by form login myself
    @GetMapping ("/login-test")
    public String formLoginTest(){
        return "login-test";
    }
    @PostMapping ("/login-test")
    public ResponseEntity<? extends Object> loginTest(HttpServletRequest request){
        if(isJsonRequest(request)) {
            // JSON login

            String username = request.getParameter("username");
            String password = request.getParameter("password");

            authentication = SecurityContextHolder.getContext().getAuthentication();
            User loginByUsername = userService.getUserByUsername(username);
            User loginByEmail = userService.getUserByEmail(username);
            try {
                if (loginByUsername != null) {
                    if(userService.authenticate(loginByUsername.getPassword(),password)){
                        Map<String, String> body = new HashMap<>();
                        body.put("OK", "LOGIN SUCCESSFULLY");
                        String jsonBody = objectMapper.writeValueAsString(body);
                        return ResponseEntity.ok().body(jsonBody);
                    }else {
                        Map<String, String> body = new HashMap<>();
                        body.put("FAIL", "LOGIN FAIL");
                        String jsonBody = objectMapper.writeValueAsString(body);
                        return ResponseEntity.ok().body(jsonBody);
                    }
                }
                if (loginByEmail != null) {
                    if(userService.authenticate(loginByUsername.getPassword(),password)){
                        Map<String, String> body = new HashMap<>();
                        body.put("OK", "LOGIN SUCCESSFULLY");
                        String jsonBody = objectMapper.writeValueAsString(body);
                        return ResponseEntity.ok().body(jsonBody);
                    }else {
                        Map<String, String> body = new HashMap<>();
                        body.put("FAIL", "LOGIN FAIL");
                        String jsonBody = objectMapper.writeValueAsString(body);
                        return ResponseEntity.ok().body(jsonBody);
                    }
                }
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("error", "Invalid credentials"));
            }

        } else {
            // Form login
            return ResponseEntity.ok("Redirect to homepage");
        }
        return null;
    }

    // Check if JSON content type
    public boolean isJsonRequest(HttpServletRequest request) {
        String type = request.getContentType();
        return type != null && type.contains("application/json");
    }
}










