package com.exchangeBook.ExchangeBook.controller;

import com.exchangeBook.ExchangeBook.dto.PagingDTO;
import com.exchangeBook.ExchangeBook.model.Role;
import com.exchangeBook.ExchangeBook.model.User;
import com.exchangeBook.ExchangeBook.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
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
import java.util.stream.Collectors;

/*
Create By : ANHTUAN
*/
@RestController
public class UserController {
    private UserService userService;
    private Authentication authentication;
    ObjectMapper objectMapper;

    @Autowired
    public UserController(UserService userService, ObjectMapper objectMapper) {
        this.userService = userService;
        this.objectMapper = objectMapper;
    }


    // --------------- FOR ADMIN --------------
    @GetMapping("/admin/{id}")
    public ResponseEntity<?> getAminByID(@PathVariable("id") Long id) throws JsonProcessingException {

        authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = userService.getUserById(id);
        if (!currentUser.getUserName().equals(authentication.getName())) {
            throw new AccessDeniedException("Not authorized");
        }

        if(!userService.existsByID(id)){
            Map<String, String> body = new HashMap<>();
            body.put("error404", "USER DON'T EXISTS");
            String jsonBody = objectMapper.writeValueAsString(body);
            return ResponseEntity.ok().body(jsonBody);
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

    @GetMapping("/admin/edit/{id}")
    public ResponseEntity<?> formEditAd(@PathVariable("id")Long id) throws JsonProcessingException {

        if(!userService.existsByID(id)){
            Map<String, String> body = new HashMap<>();
            body.put("error404", "USER DON'T EXISTS");
            String jsonBody = objectMapper.writeValueAsString(body);
            return ResponseEntity.ok().body(jsonBody);
        }

        authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.getUserById(id);
        if(!authentication.getName().equals(user.getUserName())){
            throw new AccessDeniedException("Not authorized");
        }

        return ResponseEntity.ok().body(userService.getUserById(id));
    }
    @PostMapping("/admin/edit/{id}")
    public ResponseEntity<?> saveEditAd(@PathVariable("id") Long id, @RequestBody User admin) throws JsonProcessingException {

        authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.getUserById(id);
        if(!authentication.getName().equals(user.getUserName())){
            throw new AccessDeniedException("Not authorized");
        }

        if(!userService.existsByID(id)){
            Map<String, String> body = new HashMap<>();
            body.put("error404", "USER DON'T EXISTS");
            String jsonBody = objectMapper.writeValueAsString(body);
            return ResponseEntity.ok().body(jsonBody);
        }

        return ResponseEntity.ok().body(userService.editUser(id,admin));
    }

    @GetMapping("/admin/delete/{id}")
    public ResponseEntity<?> deleteAd(@PathVariable("id")Long id) throws JsonProcessingException {

        authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.getUserById(id);
        if(!authentication.getName().equals(user.getUserName())){
            throw new AccessDeniedException("Not authorized");
        }

        if(!userService.existsByID(id)){
            Map<String, String> body = new HashMap<>();
            body.put("error404", "USER DON'T EXISTS");
            String jsonBody = objectMapper.writeValueAsString(body);
            return ResponseEntity.ok().body(jsonBody);
        }

        return ResponseEntity.ok().body(userService.removeUser(id));
    }

    @GetMapping("/admin/delete-users")
    public ResponseEntity<?> deleteUsers(@RequestBody List<Long> ids) throws JsonProcessingException {

        authentication = SecurityContextHolder.getContext().getAuthentication();
        List<String> roles = authentication.getAuthorities()
                .stream()
                .map(role -> role.getAuthority())
                .collect(Collectors.toList());

        boolean isAdmin = roles.contains("ROLE_ADMIN");
        if(!isAdmin){
            throw new AccessDeniedException("Not authorized");
        }

        if(!userService.existsByIDs(ids)){
            Map<String, String> body = new HashMap<>();
            body.put("error404", "SOME USER DON'T EXISTS");
            String jsonBody = objectMapper.writeValueAsString(body);
            return ResponseEntity.ok().body(jsonBody);
        }

        return ResponseEntity.ok().body(userService.removeUsers(ids));
    }

    //show item and pagination
    @GetMapping(value = "/admin/page")
    @ResponseBody
    public PagingDTO<User> showUser(@RequestBody JsonNode node){
        int size = node.get("size").asInt();
        int page = node.get("page").asInt();

        return userService.paging(page, size);
    }


    // --------------- FOR USER --------------
    @GetMapping("/users/{id}")
    public ResponseEntity<?> getUserByID(@PathVariable("id") Long id) throws JsonProcessingException {

        authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = userService.getUserById(id);
        if (!currentUser.getUserName().equals(authentication.getName())) {
            throw new AccessDeniedException("Not authorized");
        }

        if(!userService.existsByID(id)){
            Map<String, String> body = new HashMap<>();
            body.put("error404", "USER DON'T EXISTS");
            String jsonBody = objectMapper.writeValueAsString(body);
            return ResponseEntity.ok().body(jsonBody);
        }

        return ResponseEntity.ok(currentUser);
    }
    @GetMapping("/users/edit/{id}")
    public ResponseEntity<?> formEditU(@PathVariable("id")Long id) throws JsonProcessingException {

        authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.getUserById(id);
        if(!authentication.getName().equals(user.getUserName())){
            throw new AccessDeniedException("Not authorized");
        }

        if(!userService.existsByID(id)){
            Map<String, String> body = new HashMap<>();
            body.put("error404", "USER DON'T EXISTS");
            String jsonBody = objectMapper.writeValueAsString(body);
            return ResponseEntity.ok().body(jsonBody);
        }

        return ResponseEntity.ok().body(userService.getUserById(id));
    }
    @PostMapping("/users/edit/{id}")
    public ResponseEntity<?> saveEditU(@PathVariable("id") Long id, @RequestBody User admin) throws JsonProcessingException {

        authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.getUserById(id);
        if(!authentication.getName().equals(user.getUserName())){
            throw new AccessDeniedException("Not authorized");
        }

        if(!userService.existsByID(id)){
            Map<String, String> body = new HashMap<>();
            body.put("error404", "USER DON'T EXISTS");
            String jsonBody = objectMapper.writeValueAsString(body);
            return ResponseEntity.ok().body(jsonBody);
        }

        return ResponseEntity.ok().body(userService.editUser(id,admin));
    }

    @GetMapping("/users/delete/{id}")
    public ResponseEntity<?> deleteU(@PathVariable("id")Long id) throws JsonProcessingException {

        authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.getUserById(id);
        if(!authentication.getName().equals(user.getUserName())){
            throw new AccessDeniedException("Not authorized");
        }

        if(!userService.existsByID(id)){
            Map<String, String> body = new HashMap<>();
            body.put("error404", "USER DON'T EXISTS");
            String jsonBody = objectMapper.writeValueAsString(body);
            return ResponseEntity.ok().body(jsonBody);
        }

        return ResponseEntity.ok().body(userService.removeUser(id));
    }


}

