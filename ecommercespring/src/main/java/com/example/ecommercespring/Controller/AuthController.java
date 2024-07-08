package com.example.ecommercespring.Controller;

import com.example.ecommercespring.Repository.UserRepository;
import com.example.ecommercespring.Service.auth.AuthService;
import com.example.ecommercespring.Utils.JwtUtil;
import com.example.ecommercespring.dto.AuthenticationRequest;
import com.example.ecommercespring.dto.SignUpRequest;
import com.example.ecommercespring.dto.UserDto;
import com.example.ecommercespring.entity.User;
import lombok.RequiredArgsConstructor;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private static final String HEADER_STRING = "Authorization";
    private static final String TOKEN_PREFIX = "Bearer ";

    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final AuthService authService; // Corrected variable name

    public AuthController(AuthenticationManager authenticationManager, UserDetailsService userDetailsService, UserRepository userRepository, JwtUtil jwtUtil, AuthService authService) {
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
        this.authService = authService; // Corrected assignment
    }

    @PostMapping("/authenticate")
    public void createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest,
                                          HttpServletResponse response) throws IOException, JSONException {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(),
                            authenticationRequest.getPassword())
            );
        } catch (BadCredentialsException e) {
            throw new RuntimeException("Mauvaises informations d'identification", e);
        }

        final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
        Optional<User> optionalUser = userRepository.findFirstByEmail(userDetails.getUsername());

        if (optionalUser.isPresent()) {
            String jwt = jwtUtil.generateToken(userDetails.getUsername());
            response.getWriter().write(
                    new JSONObject()
                            .put("userId", optionalUser.get().getUserId())
                            .put("role", optionalUser.get().getRole())
                            .toString()
            );
            response.addHeader(HEADER_STRING, TOKEN_PREFIX + jwt);
        } else {
            throw new RuntimeException("Utilisateur non trouvé dans la base de données");
        }
    }

    @PostMapping("/sign-up")
    public ResponseEntity<?> signupUser(@RequestBody SignUpRequest signupRequest) {
        if (authService.hasUserWithEmail(signupRequest.getEmail())) { // Corrected method call
            return new ResponseEntity<>("User already exists", HttpStatus.NOT_ACCEPTABLE);
        }

        UserDto userDto = authService.createUser(signupRequest); // Corrected method call
        return new ResponseEntity<>(userDto, HttpStatus.OK);
    }
}
