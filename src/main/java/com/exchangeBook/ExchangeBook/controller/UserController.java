package com.exchangeBook.ExchangeBook.controller;

import com.exchangeBook.ExchangeBook.dto.PagingDTO;
import com.exchangeBook.ExchangeBook.model.User;
import com.exchangeBook.ExchangeBook.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/*
Create By : ANHTUAN
*/
@RestController
public class UserController {
    private UserService userService;
    private Authentication authentication;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    // --------------- FOR ADMIN --------------
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

    @GetMapping("/admin/edit/{id}")
    public ResponseEntity<?> formEditAd(@PathVariable("id")Long id){
        return ResponseEntity.ok().body(userService.getUserById(id));
    }
    @PostMapping("admin/edit/{id}")
    public ResponseEntity<?> saveEditAd(@PathVariable("id") Long id, @RequestBody User admin){
        return ResponseEntity.ok().body(userService.editUser(id,admin));
    }

    @GetMapping("/admin/delete/{id}")
    public ResponseEntity<?> deleteAd(@PathVariable("id")Long id){
        return ResponseEntity.ok().body(userService.removeUser(id));
    }

    //show item and pagination
    @GetMapping(value = "/admin/page")
    @ResponseBody
    public PagingDTO<User> showUser(@RequestBody JsonNode node){
        int size = node.get("size").asInt();
        int page = node.get("page").asInt();

        return userService.paging(page, size);
    }





}

