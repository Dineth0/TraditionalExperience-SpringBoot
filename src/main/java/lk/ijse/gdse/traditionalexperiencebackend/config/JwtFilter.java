package lk.ijse.gdse.traditionalexperiencebackend.config;



import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lk.ijse.gdse.traditionalexperiencebackend.service.UserService;
import lk.ijse.gdse.traditionalexperiencebackend.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;


@Component
public class JwtFilter extends OncePerRequestFilter {
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private UserService userService;
    @Value("${jwt.secret}")
    private String secretKey;


    private String secret = "YourSecretKey"; // JWT Token secret key

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        String authorization = httpServletRequest.getHeader("Authorization");
        String token = null;
        String email = null;


        if (null != authorization && authorization.startsWith("Bearer ")) {

            token = authorization.substring(7);
            email = jwtUtil.getUsernameFromToken(token);
            Claims claims=jwtUtil.getUserRoleCodeFromToken(token);
            httpServletRequest.setAttribute("email", email);
            httpServletRequest.setAttribute("role", claims.get("role"));
        }

        if (null != email && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails
                    = userService.loadUserByUsername(email);

            if (jwtUtil.validateToken(token, userDetails)) {
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken
                        = new UsernamePasswordAuthenticationToken(userDetails,
                        null, userDetails.getAuthorities());

                usernamePasswordAuthenticationToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(httpServletRequest)
                );

                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }

        }
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }

    private Claims getClaimsFromJwtToken(String token) {
        return Jwts.parser().setSigningKey(secretKey.getBytes()).parseClaimsJws(token).getBody();
    }





        public String extractUserId(String token) {
            Claims claims = Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token)
                    .getBody();
            return claims.getSubject();

    }

    public String extractEmailFromToken(String token) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey("2D4A614E645267556B58703273357638792F423F4428472B4B6250655368566DF423F4428472B4B6250655368566D")  // Use your secret key
                    .parseClaimsJws(token)
                    .getBody();


            return claims.getSubject();
        } catch (JwtException e) {
            System.out.println("Error decoding token: " + e.getMessage());
            return null;
        }
    }

}











