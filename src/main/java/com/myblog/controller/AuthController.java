package com.myblog.controller;

import com.myblog.entity.User;
import com.myblog.payload.JWTAuthResponse;
import com.myblog.payload.LoginDto;
import com.myblog.payload.SignInDto;
import com.myblog.repository.UserRepository;
import com.myblog.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepo;
    @Autowired
    private BCryptPasswordEncoder getEncodedPassword;

    @Autowired
    private AuthenticationManager authenticationManager;



    @PostMapping("/signup")
    public ResponseEntity<?> signInUser(@RequestBody SignInDto dto) {

        if (userRepo.existsByEmail(dto.getEmail())) {
            return new ResponseEntity<>("This email " + dto.getEmail() + " already exists!", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        if (userRepo.existsByUsername((dto.getUsername()))){
            return new ResponseEntity<>("This username " + dto.getUsername() + " already exists", HttpStatus.INTERNAL_SERVER_ERROR);

        }

        User user=new User();
        user.setName(dto.getName());
        user.setUsername(dto.getUsername());
        user.setPassword(getEncodedPassword.encode(dto.getPassword()));
        user.setEmail(dto.getEmail());

        userRepo.save(user);

        return new ResponseEntity<>("congratulations! you're successfully registered",HttpStatus.CREATED);
    }

    @Autowired
    private JwtTokenProvider tokenProvider;

    @PostMapping("/signin")
    public ResponseEntity<JWTAuthResponse> authenticateUser(@RequestBody LoginDto loginDto){
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginDto.getUsernameOrEmail(), loginDto.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        // get token form tokenProvider
        String token = tokenProvider.generateToken(authentication);

        return ResponseEntity.ok(new JWTAuthResponse(token));
    }

}