package com.exchangeBook.ExchangeBook.controller;

import com.exchangeBook.ExchangeBook.model.Role;
import com.exchangeBook.ExchangeBook.model.User;
import com.exchangeBook.ExchangeBook.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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

    @GetMapping("/admin/{id}")
    public ResponseEntity<?> getAllUser(@PathVariable("id") Long id) throws JsonProcessingException {
            authentication = SecurityContextHolder.getContext().getAuthentication();
            User currentUser = userService.getUserById(id);

            if (!currentUser.getUserName().equals(authentication.getName())) {
                throw new AccessDeniedException("Not authorized");
            }
            return ResponseEntity.ok(currentUser);
    }

    @GetMapping("/admin/user-all")
    public ResponseEntity<List<User>> getAllUser() {
        return ResponseEntity.ok().body(userService.getAllUser());
    }

    @GetMapping("/admin/role-admin-all")
    public ResponseEntity<List<User>> getAllAdmin() {
        return ResponseEntity.ok().body(userService.getAllAdmin());
    }
    @GetMapping("/admin/role-user-all")
    public ResponseEntity<List<User>> getAllJustUser() {
        return ResponseEntity.ok().body(userService.getAllJustUser());
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

    @GetMapping("/users/{id}")
    public ResponseEntity<?> getUserById(@PathVariable("id") Long id)  {

        authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = userService.getUserById(id);

        if (!currentUser.getUserName().equals(authentication.getName())) {

            throw new AccessDeniedException("Not authorized"); // it will be caught by the AccessDeniedHandler  configured in the SecurityFilterChain
//            throw new AuthenticationCredentialsNotFoundException("401 Unauthorized");
        }
        return ResponseEntity.ok(currentUser);
    }
}