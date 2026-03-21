package com.example.demo.service;
import com.example.demo.entity.Role;
import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepository,
                       EmailService emailService,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.emailService = emailService;
        this.passwordEncoder = passwordEncoder;
    }

    public User register(User user) {

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(Role.EMPLOYEE);
        user.setEnabled(false);

        String code = generateCode();
        user.setVerificationCode(code);

        User saved = userRepository.save(user);

        emailService.sendVerificationEmail(saved.getEmail(), code);

        return saved;
    }

    public void verify(String email, String code) {

        User user = userRepository.findByEmail(email)
                .orElseThrow();

        if (!user.getVerificationCode().equals(code)) {
            throw new RuntimeException("Invalid code");
        }

        user.setEnabled(true);
        user.setVerificationCode(null);

        userRepository.save(user);
    }

    private String generateCode() {
        return String.valueOf(100000 + new Random().nextInt(900000));
    }
}