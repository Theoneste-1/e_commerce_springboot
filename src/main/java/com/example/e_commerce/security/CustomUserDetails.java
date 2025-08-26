package com.example.e_commerce.security;

import com.example.e_commerce.models.User;
import com.example.e_commerce.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class CustomUserDetails implements  UserDetails {

    private static final long serialVersionUID = 1L;
    private User user;
    private Collection<? extends GrantedAuthority> authorities;

    public CustomUserDetails(User user) {
        this.user = user;
        this.authorities = this.user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toList());

    }


    public String getId() {
        return user.getId();
    }

    public String getEmail() {
        return user.getEmail();
    }

    public User getUser() {
        return user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public String getPassword() {
        return "";
    }

    @Override
    public String getUsername() {
        return "";
    }


    @Override
    public boolean isAccountNonExpired() {
        return true; // Implement if needed
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // Implement if needed
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // Implement if needed
    }

    @Override
    public boolean isEnabled() {
        return user.getEnabled();
    }

    @Service
    public static class CustomUserDetailsService implements UserDetailsService {

        @Autowired
        private UserRepository userRepository;

        @Override
        @Transactional()
        public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {

           try{
               User user = userRepository.findByUsernameOrEmail(usernameOrEmail)
                       .orElseThrow(() -> new UsernameNotFoundException(
                               "User with username or email " + usernameOrEmail
                       ));

               return new CustomUserDetails(user);
           }catch (Exception e){
               throw new UsernameNotFoundException(e.getMessage());
           }

        }


        @Transactional()
        public UserDetails loadUserById(String userId) {
         try {
             User user = userRepository.findByIdWithRoles(userId)
                     .orElseThrow(()-> new UsernameNotFoundException(
                             "User with id "+ userId +" Not found"
                     ));

             return new CustomUserDetails(user);
         }catch (Exception e){
             throw new UsernameNotFoundException(e.getMessage());
         }
        }
    }
}
