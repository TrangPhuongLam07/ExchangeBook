package com.exchangeBook.ExchangeBook.config;

import com.exchangeBook.ExchangeBook.model.User;
import com.exchangeBook.ExchangeBook.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/*
Create By : ANHTUAN
*/
public class UserDetailService_Security implements UserDetailsService {

    private UserRepository userRepository;

    @Autowired
    public UserDetailService_Security(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUserName(username).get();
        if (user == null) {
            throw new UsernameNotFoundException(username);
        }
        return new UserDetail_Impl(user);
    }
}
