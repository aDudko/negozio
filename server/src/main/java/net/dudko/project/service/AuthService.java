package net.dudko.project.service;

import jakarta.servlet.http.HttpServletRequest;
import net.dudko.project.model.dto.LoginDto;
import net.dudko.project.model.dto.RegisterDto;
import net.dudko.project.model.dto.ProfileDto;

public interface AuthService {

    String register(RegisterDto registerDto);

    String login(LoginDto loginDto);

    ProfileDto getProfile(HttpServletRequest request);

}
