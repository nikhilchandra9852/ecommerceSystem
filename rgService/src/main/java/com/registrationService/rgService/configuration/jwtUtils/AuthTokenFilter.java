package com.registrationService.rgService.configuration.jwtUtils;


import ch.qos.logback.core.util.StringUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Filter to validate the JWT token and set user authentication in the security context.
 */

public class AuthTokenFilter extends OncePerRequestFilter {

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private JwtImpl jwtImpl;

    /**
     * Filter method to process the JWT token and set authentication.
     *
     * @param request The HTTP request.
     * @param response The HTTP response.
     * @param filterChain The filter chain for further processing.
     * @throws ServletException If a servlet-related exception occurs.
     * @throws IOException If an input or output exception occurs.
     */

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        try{
            // parse and  validate the jwt token
            String jwt = parseJwt(request);
            if(jwt!=null && jwtImpl.validateJwtToken(jwt)){
                String email = jwtImpl.getEmailfromJwtToken(jwt);
                UserDetails userDetails = userDetailsService.loadUserByUsername(email);

                // Create an authentication token with the user details
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails,null,userDetails.getAuthorities());

                //Set the additional Details from the request
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));


                //set the authentication in the securityContext

                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        }catch (Exception e){
            logger.error("Cannot set user authentication: {}", e);
        }
        filterChain.doFilter(request,response);

    }

    /**
     * Parse the JWT token from the Authorization header.
     *
     * @param request The HTTP request.
     * @return The JWT token if found, or null if not found.
     */
    private String parseJwt(HttpServletRequest request) {

        String authHeader  = request.getHeader("Authorization");

        if(StringUtils.hasText(authHeader) && authHeader.startsWith("Bearer ")){
            return authHeader.substring(7);
        }
        return null;
    }
}
