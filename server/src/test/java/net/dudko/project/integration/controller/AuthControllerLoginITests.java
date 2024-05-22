package net.dudko.project.integration.controller;

import net.dudko.project.TestUtil;
import net.dudko.project.domain.entity.Role;
import net.dudko.project.domain.entity.User;
import net.dudko.project.domain.repository.RoleRepository;
import net.dudko.project.domain.repository.UserRepository;
import net.dudko.project.model.dto.JwtAuthResponse;
import net.dudko.project.model.dto.LoginDto;
import net.dudko.project.model.exception.ValidationError;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class AuthControllerLoginITests {

    private static final String BASE_URL = "/auth/login";

    private final TestRestTemplate testRestTemplate;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    private LoginDto loginDto;

    @Autowired
    public AuthControllerLoginITests(TestRestTemplate testRestTemplate,
                                     UserRepository userRepository,
                                     RoleRepository roleRepository,
                                     PasswordEncoder passwordEncoder) {
        this.testRestTemplate = testRestTemplate;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @BeforeEach
    public void setup() {
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
        loginDto = TestUtil.getValidLoginDto();
    }

    @AfterEach
    public void cleanup() {
        userRepository.deleteAll();
    }

    private <T> ResponseEntity<T> postLogin(Object request, Class<T> response) {
        return testRestTemplate.postForEntity(BASE_URL, request, response);
    }

    @Test
    @DisplayName("Integration test for check status code when login successfully")
    public void testLogin_whenLoginIsValid_thenReturnOk() {
        ResponseEntity<Object> response = postLogin(loginDto, Object.class);
        assertEquals(response.getStatusCode(), HttpStatus.OK);
    }

    @Test
    @DisplayName("Integration test for get access token when login successfully")
    public void testLogin_whenLoginIsValid_thenReceiveAccessToken() {
        ResponseEntity<JwtAuthResponse> response = postLogin(loginDto, JwtAuthResponse.class);
        assertNotNull(response.getBody().getAccessToken());
    }

    // VALIDATION

    // Null-check

    @Test
    @DisplayName("Integration test for check status code when login not successfully, because email is empty")
    public void testLogin_whenLoginHasNullEmail_receiveBadRequest() {
        loginDto.setEmail(null);
        ResponseEntity<Object> response = postLogin(loginDto, Object.class);
        assertEquals(response.getStatusCode(), HttpStatus.BAD_REQUEST);
    }

    @Test
    @DisplayName("Integration test for check status code when login not successfully, because password is empty")
    public void testLogin_whenLoginHasNullPassword_receiveBadRequest() {
        loginDto.setPassword(null);
        ResponseEntity<Object> response = postLogin(loginDto, Object.class);
        assertEquals(response.getStatusCode(), HttpStatus.BAD_REQUEST);
    }

    // INTERNALIZATION

    @Test
    @DisplayName("Integration test for check error message when login not successfully, because email is empty")
    public void testLogin_whenLoginHasNullEmail_receiveMessageOfNullErrorForEmail() {
        loginDto.setEmail(null);
        ResponseEntity<ValidationError> response = postLogin(loginDto, ValidationError.class);
        Map<String, String> validationErrors = response.getBody().getValidationErrors();
        assertEquals(validationErrors.get("email"), "Email cannot be empty");
    }

    @Test
    @DisplayName("Integration test for check error message when login not successfully, because password is empty")
    public void testLogin_whenLoginHasNullPassword_receiveMessageOfNullErrorForPassword() {
        loginDto.setPassword(null);
        ResponseEntity<ValidationError> response = postLogin(loginDto, ValidationError.class);
        Map<String, String> validationErrors = response.getBody().getValidationErrors();
        assertEquals(validationErrors.get("password"), "Password cannot be empty");
    }

}
