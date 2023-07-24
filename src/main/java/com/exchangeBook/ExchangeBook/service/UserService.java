package com.exchangeBook.ExchangeBook.service;

import com.exchangeBook.ExchangeBook.model.Role;
import com.exchangeBook.ExchangeBook.model.User;
import com.exchangeBook.ExchangeBook.repository.RoleRepository;
import com.exchangeBook.ExchangeBook.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

/*
Create By : ANHTUAN
*/
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private RoleRepository roleRepository;

    // ------------------- ROLE SERVICE ----------------------
    public Role saveRole(Role role) {
        return roleRepository.save(role);
    }

    // ------------------- ADMIN SERVICE ---------------------

    public User saveUserRoleAdmin(User user) {
        String RoleAdmin = "ROLE_ADMIN";
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return addRoleToUser2(user,RoleAdmin);
    }
    public List<User> getAllUser(){
        return userRepository.findAll();
    }

    public  List<User> getAllAdmin(){
        return userRepository.findAllByRoleName("ROLE_ADMIN");
    }
    public  List<User> getAllJustUser(){
        return userRepository.findAllByRoleName("ROLE_USER");
    }
    public boolean existsByEmail(String email){
        return userRepository.existsByEmail(email);
    }
    public boolean existsByUsername(String username){
        return userRepository.existsByUserName(username);
    }

    // ------------------- USER SERVICE ----------------------
    public User saveUser(User user) {
        return userRepository.save(user);
    }

    public User saveUserRoleUser(User user) {
        String RoleUser = "ROLE_USER";
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return addRoleToUser2(user,RoleUser);
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email).get();
    }
    public User getUserByUsername(String username) {
        return userRepository.findByUserName(username).get();
    }


    public Role getRoleByName(String name) {
        return roleRepository.findByName(name);
    }

    public void addRoleToUser(String email, String roleName) {
        User user = userRepository.findByEmail(email).get();
        Role role = roleRepository.findByName(roleName);
        user.getRoles().add(role);
        userRepository.save(user);
    }

    public User addRoleToUser2(User user, String roleName) {
        Role role = roleRepository.findByName(roleName);
        user.getRoles().add(role);
        return userRepository.save(user);
    }

    // compare passwords
    public boolean authenticate(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }

    public User getUserById(Long id){
        return userRepository.findById(id);
    }
}

