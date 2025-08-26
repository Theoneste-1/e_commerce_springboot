package com.example.e_commerce.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    public static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    @Autowired
    private  JWTUtil jwtUtil;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

      try{
          String jwt = parseToken(request);
          if(jwt != null && jwtUtil.validateJwtToken(jwt))
          {
              if(jwtUtil.isRefreshToken(jwt)) {
                  logger.warn("Attempted to use refresh token for authentication");
              }else {
                  String userId = jwtUtil.getUserIdFromJwtToken(jwt);
                  UserDetails userDetails = customUserDetailsService.loadUserByUsername(userId);

                  UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                  SecurityContextHolder.getContext().setAuthentication(authentication);

                  authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

              }
          }

      }catch (Exception e){
         logger.error("Exception occurred while attempting to process JWT token ,  {}", e.getMessage());
      }
        filterChain.doFilter(request, response);
    }

    public String parseToken(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");
        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
            return  headerAuth.substring(7);
        }
        return null;
    }
}
