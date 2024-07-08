package com.example.ecommercespring.Service.auth;

import com.example.ecommercespring.Repository.UserRepository;
import com.example.ecommercespring.dto.SignUpRequest;
import com.example.ecommercespring.dto.UserDto;
import com.example.ecommercespring.entity.User;
import com.example.ecommercespring.enums.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;

    @Autowired
    public AuthServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDto createUser(SignUpRequest signupRequest) {
        User user = new User();
        user.setEmail(signupRequest.getEmail());
        user.setName(signupRequest.getName());
        user.setPassword(new BCryptPasswordEncoder().encode(signupRequest.getPassword()));
        user.setRole(UserRole.CUSTOMER); // Assuming UserRole is an enum with CUSTOMER role defined
        User createdUser = userRepository.save(user);

        UserDto userDto = new UserDto();
        userDto.setUserId(createdUser.getUserId());
        userDto.setName(createdUser.getName());
        userDto.setEmail(createdUser.getEmail());
        userDto.setAddress(createdUser.getAddress());
        userDto.setPhoneNumber(createdUser.getPhoneNumber());

        return userDto;
    }
    public Boolean hasUserWithEmail(String email){
        return userRepository.findFirstBy Email (email).isPresent ( ) ;|
    }
}
