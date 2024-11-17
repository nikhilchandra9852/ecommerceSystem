package com.details.service.detailsService.interceptors;

// interceptors handling the authentication..


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.catalina.util.SessionConfig;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;


import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;


@Component
public class ProductInterceptor extends OncePerRequestFilter implements HandlerInterceptor {

//    private final Key singingKey;
//
//    public ProductInterceptor(String secretKey) throws NoSuchAlgorithmException, InvalidKeySpecException {
//        /// convert the secret key to bytes and create a hMacKey.
//        // need to use Assymetric Singing
//        byte[] secretKeyBytes = Base64.getDecoder().decode(secretKey);
//        X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(secretKeyBytes);
//        this.singingKey =  KeyFactory.getInstance("RSA").generatePublic(x509EncodedKeySpec);
//    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // Add your pre-processing logic here
        logger.info(request.getHeader("Authorization"));
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getDetails() != null) {
            String userId = (String) authentication.getDetails();
            request.setAttribute("userId",userId);
        }
        return true; // Continue with the next interceptor or request handler
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        // Add your post-processing logic here (optional)
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        // Cleanup or logging after request completion (optional)
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        // get The Author
        String authHeader = request.getHeader("Authorization");
        if(authHeader!=null && authHeader.startsWith("Bearer ")){

            // pick the token
            String token = authHeader.substring(7);

            try{
                Claims claims = JwtUtil.validateToken(token);
                request.setAttribute("claims",claims);

                System.out.println("user id : " + claims.getSubject());

                List<String> roles = claims.get("roles", List.class);
                if(!checkRoles(roles)){
                    throw new Exception();
                }
                List<SimpleGrantedAuthority> authorities = roles.stream()
                        .map(role -> new SimpleGrantedAuthority(role))
                        .collect(Collectors.toList());
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        claims.getSubject(), null, authorities);
                authentication.setDetails(claims.getSubject());
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }catch (Exception e){
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Invalid or expired token");

            }

        }else{
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Missing Authorization header");
        }
        filterChain.doFilter(request,response);


    }

    private static boolean checkRoles(List<String> claims) {
        return List.of("ROLE_Seller", "ROLE_Buyer").containsAll(claims);
    }
}

class JwtUtil{
    private static final String SECRET_KEY= "aHR0cHM6Ly93d3cueW91YmlsdW1hbWVyaWNhLmNvbS9sb2dpbi8=";

    public static Claims validateToken(String token){
        return Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody();
    }
}
