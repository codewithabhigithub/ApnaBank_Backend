package com.example.filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.cors.reactive.CorsUtils;

import com.example.util.JwtUtil;

import reactor.core.publisher.Mono;
@Component
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config>{

	
	   @Autowired
	    private RouteValidator validator;
	   
	    @Autowired
	    private JwtUtil jwtUtil;	

	    public AuthenticationFilter() {
	        super(Config.class);
	    }
	 public static class Config {

	    }

	@Override
	public GatewayFilter apply(Config config) {
		return ((exchange, chain) -> {
		
			//check jwt
			  if (validator.isSecured.test(exchange.getRequest())) {
	                //header contains token or not
	                if (!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
	                    throw new RuntimeException("missing authorization header");
	                }
	                //if present
	                String authHeader = exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
	                if (authHeader != null && authHeader.startsWith("Bearer ")) {
	                    authHeader = authHeader.substring(7);
	                }
	                try {
//	                    //REST call to AUTH service
//	                    template.getForObject("http://USER-SERVICE//validate?token" + authHeader, String.class);
	                    jwtUtil.validateToken(authHeader);

	                } catch (Exception e) {
	                    System.out.println("invalid access...!");
	                    throw new RuntimeException("un authorized access to application");
	                }
	            }
	            return chain.filter(exchange);
		});
	}
}
