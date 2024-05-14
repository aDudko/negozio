package net.dudko.project.controller;

import net.dudko.project.domain.entity.Role;
import net.dudko.project.domain.entity.User;
import net.dudko.project.domain.repository.RoleRepository;
import net.dudko.project.domain.repository.UserRepository;
import net.dudko.project.model.dto.JwtAuthResponse;
import net.dudko.project.model.dto.LoginDto;
import net.dudko.project.model.exception.ValidationError;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class AuthControllerLoginTest {

    private static final String BASE_URL = "/auth/login";

    @Autowired
    TestRestTemplate testRestTemplate;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @BeforeEach
    public void prepare() {
        User user = User.builder()
                .name("test-user")
                .email("test@mail.com")
                .phone("+491234567890")
                .address("TestAddress st. 69/7")
                .password(passwordEncoder.encode("P4ssword"))
                .build();
        Set<Role> roles = new HashSet<>();
        roles.add(roleRepository.findByName("ROLE_USER"));
        user.setRoles(roles);
        userRepository.save(user);
    }

    @AfterEach
    public void cleanup() {
        userRepository.deleteAll();
    }

    private LoginDto createValidLoginDto() {
        return LoginDto.builder()
                .email("test@mail.com")
                .password("P4ssword")
                .build();
    }

    private <T> ResponseEntity<T> postLogin(Object request, Class<T> response) {
        return testRestTemplate.postForEntity(BASE_URL, request, response);
    }

    @Test
    public void testLogin_whenLoginIsValid_thenReturnOk() {
        LoginDto loginDto = createValidLoginDto();
        ResponseEntity<Object> response = postLogin(loginDto, Object.class);
        assertEquals(response.getStatusCode(), HttpStatus.OK);
    }

    @Test
    public void testLogin_whenLoginIsValid_thenReceiveAccessToken() {
        LoginDto loginDto = createValidLoginDto();
        ResponseEntity<JwtAuthResponse> response = postLogin(loginDto, JwtAuthResponse.class);
        assertNotNull(response.getBody().getAccessToken());
    }

    // VALIDATION

    // Null-check

    @Test
    public void testLogin_whenLoginHasNullEmail_receiveBadRequest() {
        LoginDto loginDto = createValidLoginDto();
        loginDto.setEmail(null);
        ResponseEntity<Object> response = postLogin(loginDto, Object.class);
        assertEquals(response.getStatusCode(), HttpStatus.BAD_REQUEST);
    }

    @Test
    public void testLogin_whenLoginHasNullPassword_receiveBadRequest() {
        LoginDto loginDto = createValidLoginDto();
        loginDto.setPassword(null);
        ResponseEntity<Object> response = postLogin(loginDto, Object.class);
        assertEquals(response.getStatusCode(), HttpStatus.BAD_REQUEST);
    }

    // INTERNALIZATION

    @Test
    public void testLogin_whenLoginHasNullEmail_receiveMessageOfNullErrorForEmail() {
        LoginDto loginDto = createValidLoginDto();
        loginDto.setEmail(null);
        ResponseEntity<ValidationError> response = postLogin(loginDto, ValidationError.class);
        Map<String, String> validationErrors = response.getBody().getValidationErrors();
        assertEquals(validationErrors.get("email"), "Email cannot be empty");
    }

    @Test
    public void testLogin_whenLoginHasNullPassword_receiveMessageOfNullErrorForPassword() {
        LoginDto loginDto = createValidLoginDto();
        loginDto.setPassword(null);
        ResponseEntity<ValidationError> response = postLogin(loginDto, ValidationError.class);
        Map<String, String> validationErrors = response.getBody().getValidationErrors();
        assertEquals(validationErrors.get("password"), "Password cannot be empty");
    }

}
