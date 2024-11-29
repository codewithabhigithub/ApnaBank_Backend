package com.src.jwt;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtFilter extends OncePerRequestFilter {
    
	
	@Autowired
	private JwtUtil jwtUtil;
	@Autowired
	private MyUserService service;
	
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		 String header=request.getHeader("Authorization");
	     String token=null;
	     String username=null;
		if(header!=null&&header.startsWith("Bearer ")) {
			token=header.substring(7);
			username=jwtUtil.extractUsername(token);
			request.setAttribute("username", username);
			
			
		}
		if(username!=null&&SecurityContextHolder.getContext().getAuthentication()==null) {
			UserDetails user=service.loadUserByUsername(username);
			
			if(jwtUtil.validateToken(token,user)) {
				request.setAttribute("username",username);
				UsernamePasswordAuthenticationToken authToken=new UsernamePasswordAuthenticationToken(user,null,user.getAuthorities());
			authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails((request)));
			SecurityContextHolder.getContext().setAuthentication(authToken);
			}
		}
		filterChain.doFilter(request, response);
	}

}