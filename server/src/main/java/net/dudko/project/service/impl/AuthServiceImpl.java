package net.dudko.project.service.impl;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import net.dudko.project.domain.entity.Role;
import net.dudko.project.domain.entity.User;
import net.dudko.project.domain.mapper.UserMapper;
import net.dudko.project.domain.repository.RoleRepository;
import net.dudko.project.domain.repository.UserRepository;
import net.dudko.project.model.dto.LoginDto;
import net.dudko.project.model.dto.ProfileDto;
import net.dudko.project.model.dto.RegisterDto;
import net.dudko.project.model.exception.NegozioAPIException;
import net.dudko.project.security.JwtAuthenticationFilter;
import net.dudko.project.security.JwtTokenProvider;
import net.dudko.project.service.AuthService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor
@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    private final JwtTokenProvider jwtTokenProvider;

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Override
    public String register(RegisterDto registerDto) {
        if (userRepository.existsByEmail(registerDto.getEmail())) {
            throw new NegozioAPIException("Email already exists!");
        }
        User user = User.builder()
                .name(registerDto.getName())
                .email(registerDto.getEmail())
                .phone(registerDto.getPhone())
                .address(registerDto.getAddress())
                .password(passwordEncoder.encode(registerDto.getPassword()))
                .build();
        Set<Role> roles = new HashSet<>();
        roles.add(roleRepository.findByName("ROLE_USER"));
        user.setRoles(roles);
        userRepository.save(user);
        return getToken(registerDto.getEmail(), registerDto.getPassword());
    }

    @Override
    public String login(LoginDto loginDto) {
        return getToken(loginDto.getEmail(), loginDto.getPassword());
    }

    @Override
    public ProfileDto getProfile(HttpServletRequest request) {
        String token = jwtAuthenticationFilter.getTokenFromRequest(request);
        if (jwtTokenProvider.validateToken(token)) {
            String email = jwtTokenProvider.getUsernameFromToken(token);
            User user = userRepository.findByEmail(email).orElseThrow(() -> new NegozioAPIException("Profile not found"));
            return UserMapper.mapToProfileDto(user);
        }
        return null;
    }

    private String getToken(String login, String password) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                login,
                password
        ));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return jwtTokenProvider.generateToken(authentication);
    }

}
