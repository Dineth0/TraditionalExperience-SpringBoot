package lk.ijse.gdse.traditionalexperiencebackend.config;

import lk.ijse.gdse.traditionalexperiencebackend.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    @Autowired
    private UserServiceImpl userService;
    @Autowired
    private JwtFilter jwtFilter;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService).passwordEncoder(passwordEncoder());
    }



    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
            throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    protected SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .cors(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                /*.cors()
                .and()*/
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/uploads/**",
                                "/css/**",
                                "/js/**",
                                "/images/**",
                                "/api/v1/user/register",
                                "/api/v1/auth/authenticate",
                                "/api/v1/auth/forgot-password",
                                "/api/v1/auth/reset-password",
                                "/api/v1/auth/verify-otp",
                                "/api/v1/item/getAllItems",
                                "/api/v1/item/getItem/**",
                                "/api/v1/instructor/getAllInstructors",
                                "/api/v1/workshop/getAllWorkshops",
                                "/api/v1/workshop/getWorkshopByItem/**",
                                "/api/v1/workshop/getWorkshopById/**",
                                "/api/v1/workshop/getParticipantCount/**",
                                "/api/v1/workshopRegistration/registerWorkshop/",
                                "/api/v1/workshopRegistration/checkAvailability/**",
                                "api/v1/instructor/getInstructorById/**"

                        ).permitAll()
                        .requestMatchers("/api/v1/item/addItem",
                                "/api/v1/instructor/addInstructor",
                                "/api/v1/workshop/addWorkshop",
                                "/api/v1/item/updateItem",
                                "/api/v1/instructor/updateInstructor",
                                "/api/v1/workshop/updateWorkshop")
                        .hasAuthority("ADMIN")
                        .anyRequest().authenticated()

                )


                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }



}
