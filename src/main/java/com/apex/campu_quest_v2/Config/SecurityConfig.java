package com.apex.campu_quest_v2.Config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import static org.springframework.http.HttpMethod.DELETE;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.HttpMethod.PUT;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import static com.apex.campu_quest_v2.Enums.Permission.ADMIN_CREATE;
import static com.apex.campu_quest_v2.Enums.Permission.ADMIN_DELETE;
import static com.apex.campu_quest_v2.Enums.Permission.ADMIN_READ;
import static com.apex.campu_quest_v2.Enums.Permission.ADMIN_UPDATE;
import static com.apex.campu_quest_v2.Enums.Role.ADMIN;
import static com.apex.campu_quest_v2.Enums.Role.STAFF;
import static com.apex.campu_quest_v2.Enums.Role.STUDENT;
import static com.apex.campu_quest_v2.Enums.Role.TEACHER;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfig {

    private static final String[] WHITE_LIST_URL = { "/api/v1/auth/**",
            "/v2/api-docs",
            "/v3/api-docs",
            "/v3/api-docs/**",
            "/swagger-resources",
            "/swagger-resources/**",
            "/configuration/ui",
            "/configuration/security",
            "/swagger-ui/**",
            "/webjars/**",
            "/swagger-ui.html",
            "/api/v1/auth**",
            "/api/v1/classes/all" };

    private final AuthenticationProvider authenticationProvider;
    private final JwtAuthFilter jwtAuthFilter;
    private final LogoutHandler logoutHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> {})
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(req -> req
                        .requestMatchers(org.springframework.http.HttpMethod.OPTIONS, "/**").permitAll()
                        .requestMatchers(WHITE_LIST_URL).permitAll()
                        .requestMatchers("/api/v1/admin/**").hasRole(ADMIN.name())
                        .requestMatchers(GET,"/api/v1/users/all").hasRole(ADMIN.name())
                        .requestMatchers(GET,"/api/v1/admin/**").hasAuthority(ADMIN_READ.name())
                        .requestMatchers(POST,"/api/v1/admin/**").hasAuthority(ADMIN_CREATE.name())
                        .requestMatchers(PUT,"/api/v1/admin/**").hasAuthority(ADMIN_UPDATE.name())
                        .requestMatchers(DELETE,"/api/v1/admin/**").hasAuthority(ADMIN_DELETE.name())
                        .requestMatchers("/api/v1/teacher/**").hasAnyRole(ADMIN.name(),TEACHER.name())
                        .requestMatchers(GET,"/api/v1/teacher/**").hasAuthority(ADMIN_READ.name())
                        .requestMatchers(POST,"/api/v1/teacher/**").hasAuthority(ADMIN_CREATE.name())
                        .requestMatchers(PUT,"/api/v1/teacher/**").hasAuthority(ADMIN_UPDATE.name())
                        .requestMatchers(DELETE,"/api/v1/teacher/**").hasAuthority(ADMIN_DELETE.name())
                        .requestMatchers("/api/v1/user/**").hasAnyRole(STAFF.name(), ADMIN.name())
                        .requestMatchers(GET,"/api/v1/user/**").hasAuthority(ADMIN_READ.name())
                        .requestMatchers(POST,"/api/v1/user/**").hasAuthority(ADMIN_CREATE.name())
                        .requestMatchers(PUT,"/api/v1/user/**").hasAuthority(ADMIN_UPDATE.name())
                        .requestMatchers(DELETE,"/api/v1/user/**").hasAuthority(ADMIN_DELETE.name())
                        .requestMatchers("/api/v1/staff/**").hasAnyRole(STAFF.name(), ADMIN.name())
                        .requestMatchers(GET,"/api/v1/staff/**").hasAuthority(ADMIN_READ.name())
                        .requestMatchers(POST,"/api/v1/staff/**").hasAuthority(ADMIN_CREATE.name())
                        .requestMatchers(PUT,"/api/v1/staff/**").hasAuthority(ADMIN_UPDATE.name())
                        .requestMatchers(DELETE,"/api/v1/staff/**").hasAuthority(ADMIN_DELETE.name())
                        .requestMatchers("/api/v1/student/**").hasAnyRole(STUDENT.name(),ADMIN.name())
                        .requestMatchers(GET,"/api/v1/student/**").hasAuthority(ADMIN_READ.name())
                        .requestMatchers(POST,"/api/v1/student/**").hasAuthority(ADMIN_CREATE.name())
                        .requestMatchers(PUT,"/api/v1/student/**").hasAuthority(ADMIN_UPDATE.name())
                        .requestMatchers(DELETE,"/api/v1/student/**").hasAuthority(ADMIN_DELETE.name())
                        .requestMatchers(GET,"/api/v1/tasks/**").hasAnyRole(STAFF.name(),ADMIN.name(),TEACHER.name(),STUDENT.name())
                        .requestMatchers(POST,"/api/v1/tasks/**").hasAnyRole(STAFF.name(),ADMIN.name(),TEACHER.name(),STUDENT.name())
                        .requestMatchers(DELETE,"/api/v1/tasks/**").hasAnyRole(STAFF.name(),ADMIN.name(),TEACHER.name(),STUDENT.name())
                        .requestMatchers(POST, "/api/v1/tasks/global").hasAnyAuthority("global_task:publish")
                        .requestMatchers(POST, "/api/v1/tasks/global/{taskId}/accept").hasAuthority("global_task:accept")
                        .requestMatchers(POST, "/api/v1/tasks/global/{taskId}/withdraw").hasAuthority("global_task:withdraw")
                        .requestMatchers(POST, "/api/v1/tasks/student/{studentTaskId}/submit").hasAuthority("global_task:submit")
                        .requestMatchers(POST, "/api/v1/tasks/student/{studentTaskId}/validate").hasAuthority("global_task:validate")
                        .requestMatchers(POST, "/api/v1/tasks/student/{studentTaskId}/reject").hasAuthority("global_task:validate")
                        .anyRequest()
                        .authenticated())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .logout(logout -> logout.logoutUrl("/api/v1/auth/logout")
                        .addLogoutHandler(logoutHandler)
                        .logoutSuccessHandler(
                                (request, response, authentication) -> SecurityContextHolder.clearContext()));

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("https://CampusQuest.com", "http://localhost:8080", "http://localhost:5173"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("Authorization", "Content-Type", "X-Requested-With", "Accept", "Origin"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

}
