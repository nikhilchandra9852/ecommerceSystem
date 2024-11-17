package com.registrationService.rgService.configuration;


import com.registrationService.rgService.configuration.jwtUtils.AuthEntryPointJwt;
import com.registrationService.rgService.configuration.jwtUtils.AuthTokenFilter;
import com.registrationService.rgService.configuration.jwtUtils.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableMethodSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig {

    @Autowired
    UserDetailsServiceImpl userDetailsService; // injects the user details service

    @Autowired
    private AuthEntryPointJwt authEntryPointJwt; // it will injects the entry point service for unauthorized requests



    @Bean
    public AuthTokenFilter getAuthTokenFilter(){
        return new AuthTokenFilter();
    }

    /**
     * Creates a bean for the DAO authentication provider.
     *
     * @return DaoAuthenticationProvider instance
     */
    @Bean
    public DaoAuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();


        daoAuthenticationProvider.setUserDetailsService(userDetailsService);
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());

        return daoAuthenticationProvider;
    }

    /**
     * Creates a bean for the authentication manager.
     *
     * @param authenticationConfiguration Authentication configuration
     * @return AuthenticationManager instance
     * @throws Exception if there is an error getting the authentication manager
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    /**
     * Creates a bean for the password encoder.
     *
     * @return PasswordEncoder instance
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // Returns a new instance of BCryptPasswordEncoder
    }

    /**
     * Configures the security filter chain for HTTP requests.
     *
     * @param http HttpSecurity configuration
     * @return SecurityFilterChain instance
     * @throws Exception if there is an error configuring the security filter chain
     */

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{

        // configure the csrf

        http.csrf(AbstractHttpConfigurer::disable)
                .exceptionHandling(exception->
                        exception.authenticationEntryPoint(authEntryPointJwt))
                //set sessiioon part
                .sessionManagement(session->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth ->
                        auth.requestMatchers("/api/v1/auth/**").permitAll()
                                .anyRequest().authenticated());
        http.authenticationProvider(authenticationProvider());
        http.addFilterBefore(getAuthTokenFilter(),
                UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

}
