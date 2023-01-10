package com.dimas.jumpstart.attendance.jwtsecurity;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import com.dimas.jumpstart.attendance.exception.BadRequestException;
import com.dimas.jumpstart.attendance.service.UsersServiceImpl;

import io.jsonwebtoken.ExpiredJwtException;

public class TokenAuthenticationFilter extends OncePerRequestFilter{
	
	@Autowired
	private TokenProvider tokenProvider;
	
	@Autowired
	private UsersServiceImpl usersServiceImpl;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		try {
			String jwt = getJwt(request);
			if(org.springframework.util.StringUtils.hasText(jwt) && tokenProvider.validateToken(jwt)) {
				int userId = tokenProvider.getUserIdFromToken(jwt);
				
				UserDetails userDetails = usersServiceImpl.loadUserById(userId);
				UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
				authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				SecurityContextHolder.getContext().setAuthentication(authentication);
			}
		} 
		catch (ExpiredJwtException e) {
			String isRefreshToken = request.getHeader("isRefreshToken");
			String requestURL = request.getRequestURL().toString();
			// allow for Refresh Token creation if following conditions are true.
			if (isRefreshToken != null && isRefreshToken.equals("true") && requestURL.contains("refreshtoken")) {
				allowForRefreshToken(e, request);
			} else {
				request.setAttribute("exception", e);
			}
		}
		catch (BadRequestException e) {
			request.setAttribute("exception", e);
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
		filterChain.doFilter(request, response);
		
	}
	
	//Customized getjwt method for bearer token
	//substring(7-first index, last index) B e a r e r - 6 characters
	private String getJwt(HttpServletRequest request) {
		String bearerToken = request.getHeader("Authorization");
		if(org.springframework.util.StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
			return bearerToken.substring(7, bearerToken.length());
		}
		return null;
	}
	
	private void allowForRefreshToken(ExpiredJwtException ex, HttpServletRequest request) {

		// create a UsernamePasswordAuthenticationToken with null values.
		UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
				null, null, null);
		// After setting the Authentication in the context, we specify
		// that the current user is authenticated. So it passes the
		// Spring Security Configurations successfully.
		SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
		// Set the claims so that in controller we will be using it to create
		// new JWT
		request.setAttribute("claims", ex.getClaims());

	}

}
