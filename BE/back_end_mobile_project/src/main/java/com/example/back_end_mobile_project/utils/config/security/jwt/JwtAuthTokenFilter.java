package com.example.back_end_mobile_project.utils.config.security.jwt;

import com.example.back_end_mobile_project.utils.config.security.user.MyUserDetailsService;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtAuthTokenFilter extends OncePerRequestFilter {

    private final String STRING_AUTHORIZATION = "Authorization";

    private final String STRING_UNAUTHORIZED = "Unauthorized";

    private final String STRING_BEARER = "Bearer ";

    @Autowired
    MyUserDetailsService service;

    @Autowired
    JwtProvider provider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        try {
            String tokenHeader = request.getHeader(STRING_AUTHORIZATION);
            String username = null;
            String token = null;

            if (tokenHeader != null && tokenHeader.startsWith(STRING_BEARER)) {
                token = tokenHeader.substring(7);
                username = provider.getUsernameFromToken(token);
            }

            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = service.loadUserByUsername(username);
                if (provider.validateToken(token, userDetails)) {
                    UsernamePasswordAuthenticationToken authenticationToken =
                            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                }
            }
        } catch (ExpiredJwtException ex){
            String isRefreshToken = request.getHeader("isRefreshToken");
            String requestURL = request.getRequestURL().toString();
            if (isRefreshToken != null && isRefreshToken.equals("true") && requestURL.contains("refreshtoken")) {
                allowForRefreshToken(ex, request);
            } else{
                ex.printStackTrace();
                request.setAttribute("exception", ex);
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, STRING_UNAUTHORIZED);
            }
        } catch (BadCredentialsException ex) {
            ex.printStackTrace();
            request.setAttribute("exception", ex);
        } catch (IllegalArgumentException e){
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, STRING_UNAUTHORIZED);
        } catch (Exception ex) {
            ex.printStackTrace();
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, STRING_UNAUTHORIZED);
        }

        filterChain.doFilter(request, response);

    }


    private void allowForRefreshToken(ExpiredJwtException ex, HttpServletRequest request) {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                null, null, null);
        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
        request.setAttribute("claims", ex.getClaims());
    }

}
