package net.dudko.project.integration.controller;

import net.dudko.project.TestUtil;
import net.dudko.project.domain.entity.Role;
import net.dudko.project.domain.entity.User;
import net.dudko.project.domain.repository.RoleRepository;
import net.dudko.project.domain.repository.UserRepository;
import net.dudko.project.integration.AbstractContainerBaseTest;
import net.dudko.project.model.dto.JwtAuthResponse;
import net.dudko.project.model.dto.RegisterDto;
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
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class AuthControllerRegisterITests extends AbstractContainerBaseTest {

    private static final String BASE_URL = "/auth/register";

    private final TestRestTemplate testRestTemplate;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    private RegisterDto registerDto;

    @Autowired
    public AuthControllerRegisterITests(TestRestTemplate testRestTemplate,
                                        PasswordEncoder passwordEncoder,
                                        UserRepository userRepository,
                                        RoleRepository roleRepository) {
        this.testRestTemplate = testRestTemplate;
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @BeforeEach
    public void setup() {
        registerDto = TestUtil.getValidRegisterDto();
    }

    @AfterEach
    public void cleanup() {
        userRepository.deleteAll();
    }

    private <T> ResponseEntity<T> postRegister(Object request, Class<T> response) {
        return testRestTemplate.postForEntity(BASE_URL, request, response);
    }

    @Test
    @DisplayName("Integration test for check status code when register is successfully")
    public void testRegister_whenUserIsValid_thenReturnOk() {
        ResponseEntity<Object> response = postRegister(registerDto, Object.class);
        assertEquals(response.getStatusCode(), HttpStatus.OK);
    }

    @Test
    @DisplayName("Integration test for check saved to DB when register is successfully")
    public void testRegister_whenUserIsValid_thenUserSavedToDatabase() {
        postRegister(registerDto, Object.class);
        assertEquals((long) userRepository.findAll().size(), 1);
    }

    @Test
    @DisplayName("Integration test for get access token when register is successfully")
    public void testRegister_whenUserIsValid_receiveAccessToken() {
        ResponseEntity<JwtAuthResponse> response = postRegister(registerDto, JwtAuthResponse.class);
        assertNotNull(response.getBody().getAccessToken());
    }

    @Test
    @DisplayName("Integration test for check hashed password in DB when register is successfully")
    public void testRegister_whenUserIsValid_passwordIsHashedInDatabase() {
        postRegister(registerDto, Object.class);
        User inDb = userRepository.findAll().get(0);
        assertNotEquals(registerDto.getPassword(), inDb.getPassword());
    }

    // VALIDATION

    // Null-check

    @Test
    @DisplayName("Integration test for check status code when register not successfully, because name is empty")
    public void testRegister_whenUserHasNullName_receiveBadRequest() {
        registerDto.setName(null);
        ResponseEntity<Object> response = postRegister(registerDto, Object.class);
        assertEquals(response.getStatusCode(), HttpStatus.BAD_REQUEST);
    }

    @Test
    @DisplayName("Integration test for check status code when register not successfully, because email is empty")
    public void testRegister_whenUserHasNullEmail_receiveBadRequest() {
        registerDto.setEmail(null);
        ResponseEntity<Object> response = postRegister(registerDto, Object.class);
        assertEquals(response.getStatusCode(), HttpStatus.BAD_REQUEST);
    }

    @Test
    @DisplayName("Integration test for check status code when register not successfully, because phone is empty")
    public void testRegister_whenUserHasNullPhone_receiveBadRequest() {
        registerDto.setPhone(null);
        ResponseEntity<Object> response = postRegister(registerDto, Object.class);
        assertEquals(response.getStatusCode(), HttpStatus.BAD_REQUEST);
    }

    @Test
    @DisplayName("Integration test for check status code when register not successfully, because address is empty")
    public void testRegister_whenUserHasNullAddress_receiveBadRequest() {
        registerDto.setAddress(null);
        ResponseEntity<Object> response = postRegister(registerDto, Object.class);
        assertEquals(response.getStatusCode(), HttpStatus.BAD_REQUEST);
    }

    @Test
    @DisplayName("Integration test for check status code when register not successfully, because password is empty")
    public void testRegister_whenUserHasNullPassword_receiveBadRequest() {
        registerDto.setPassword(null);
        ResponseEntity<Object> response = postRegister(registerDto, Object.class);
        assertEquals(response.getStatusCode(), HttpStatus.BAD_REQUEST);
    }

    // Size-check

    @Test
    @DisplayName("Integration test for check status code when register not successfully, because name is short")
    public void testRegister_whenUserHasNameWithLessThanRequired_receiveBadRequest() {
        registerDto.setName("abc");
        ResponseEntity<Object> response = postRegister(registerDto, Object.class);
        assertEquals(response.getStatusCode(), HttpStatus.BAD_REQUEST);
    }

    @Test
    @DisplayName("Integration test for check status code when register not successfully, because password is short")
    public void testRegister_whenUserHasPasswordWithLessThanRequire_receiveBadRequest() {
        registerDto.setPassword("P4sswd");
        ResponseEntity<Object> response = postRegister(registerDto, Object.class);
        assertEquals(response.getStatusCode(), HttpStatus.BAD_REQUEST);
    }

    @Test
    @DisplayName("Integration test for check status code when register not successfully, because name is long")
    public void testRegister_whenUserHasNameExceedTheLengthLimit_receiveBadRequest() {
        String valueOf256Chars = IntStream.rangeClosed(1, 256).mapToObj(x -> "a").collect(Collectors.joining());
        registerDto.setName(valueOf256Chars);
        ResponseEntity<Object> response = postRegister(registerDto, Object.class);
        assertEquals(response.getStatusCode(), HttpStatus.BAD_REQUEST);
    }

    @Test
    @DisplayName("Integration test for check status code when register not successfully, because password is long")
    public void testRegister_whenUserHasPasswordExceedTheLengthLimit_receiveBadRequest() {
        String valueOf256Chars = IntStream.rangeClosed(1, 256).mapToObj(x -> "a").collect(Collectors.joining());
        registerDto.setPassword(valueOf256Chars + "A1");
        ResponseEntity<Object> response = postRegister(registerDto, Object.class);
        assertEquals(response.getStatusCode(), HttpStatus.BAD_REQUEST);
    }

    // Pattern

    @Test
    @DisplayName("Integration test for check status code when register not successfully, because email without at and domain name")
    public void testRegister_whenUserHasEmailWithoutAtAndDomainName_receiveBadRequest() {
        registerDto.setEmail("abc");
        ResponseEntity<Object> response = postRegister(registerDto, Object.class);
        assertEquals(response.getStatusCode(), HttpStatus.BAD_REQUEST);
    }

    @Test
    @DisplayName("Integration test for check status code when register not successfully, because email without domain name")
    public void testRegister_whenUserHasEmailWithoutDomainName_receiveBadRequest() {
        registerDto.setEmail("abc@");
        ResponseEntity<Object> response = postRegister(registerDto, Object.class);
        assertEquals(response.getStatusCode(), HttpStatus.BAD_REQUEST);
    }

    @Test
    @DisplayName("Integration test for check status code when register not successfully, because email without code")
    public void testRegister_whenUserHasEmailWithoutCode_receiveBadRequest() {
        registerDto.setEmail("abc@abc");
        ResponseEntity<Object> response = postRegister(registerDto, Object.class);
        assertEquals(response.getStatusCode(), HttpStatus.BAD_REQUEST);
    }

    @Test
    @DisplayName("Integration test for check status code when register not successfully, because email without user name")
    public void testRegister_whenUserHasEmailWithoutUserName_receiveBadRequest() {
        registerDto.setEmail("@abc");
        ResponseEntity<Object> response = postRegister(registerDto, Object.class);
        assertEquals(response.getStatusCode(), HttpStatus.BAD_REQUEST);
    }

    @Test
    @DisplayName("Integration test for check status code when register not successfully, because email without user name and code")
    public void testRegister_whenUserHasEmailWithoutUserNameAndCode_receiveBadRequest() {
        registerDto.setEmail("@abc.");
        ResponseEntity<Object> response = postRegister(registerDto, Object.class);
        assertEquals(response.getStatusCode(), HttpStatus.BAD_REQUEST);
    }

    @Test
    @DisplayName("Integration test for check status code when register not successfully, because email without at")
    public void testRegister_whenUserHasEmailWithoutAt_receiveBadRequest() {
        registerDto.setEmail("abc.com");
        ResponseEntity<Object> response = postRegister(registerDto, Object.class);
        assertEquals(response.getStatusCode(), HttpStatus.BAD_REQUEST);
    }


    @Test
    @DisplayName("Integration test for check status code when register not successfully, because password with all lowercase")
    public void testRegister_whenUserHasPasswordWithAllLowercase_receiveBadRequest() {
        registerDto.setPassword("alllowercase");
        ResponseEntity<Object> response = postRegister(registerDto, Object.class);
        assertEquals(response.getStatusCode(), HttpStatus.BAD_REQUEST);
    }

    @Test
    @DisplayName("Integration test for check status code when register not successfully, because password with all uppercase")
    public void testRegister_whenUserHasPasswordWithAllUppercase_receiveBadRequest() {
        registerDto.setPassword("ALLUPPERCASE");
        ResponseEntity<Object> response = postRegister(registerDto, Object.class);
        assertEquals(response.getStatusCode(), HttpStatus.BAD_REQUEST);
    }

    @Test
    @DisplayName("Integration test for check status code when register not successfully, because password with all number")
    public void testRegister_whenUserHasPasswordWithAllNumber_receiveBadRequest() {
        registerDto.setPassword("1234567890");
        ResponseEntity<Object> response = postRegister(registerDto, Object.class);
        assertEquals(response.getStatusCode(), HttpStatus.BAD_REQUEST);
    }

    @Test
    @DisplayName("Integration test for check status code when register not successfully, because another user has same email")
    public void testRegister_whenAnotherUserHasSameEmail_receiveBadRequest() {
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
        registerDto = TestUtil.getValidRegisterDto();
        ResponseEntity<Object> response = postRegister(registerDto, Object.class);
        assertEquals(response.getStatusCode(), HttpStatus.BAD_REQUEST);
    }

    @Test
    @DisplayName("Integration test for check url when register")
    public void testRegister_whenUserIsValid_receiveApiError() {
        RegisterDto registerDto = new RegisterDto();
        ResponseEntity<ValidationError> response = postRegister(registerDto, ValidationError.class);
        assertEquals(response.getBody().getUrl(), BASE_URL);
    }

    @Test
    @DisplayName("Integration test for check count errors messages when register is not successfully")
    public void testRegister_whenUserIsValid_receiveValidationErrorWithValidationErrors() {
        RegisterDto registerDto = new RegisterDto();
        ResponseEntity<ValidationError> response = postRegister(registerDto, ValidationError.class);
        assertEquals(response.getBody().getValidationErrors().size(), 5);
    }

    // INTERNALIZATION

    @Test
    @DisplayName("Integration test for check error message when register not successfully, because name is empty")
    public void testRegister_whenUserHasNullName_receiveMessageOfNullErrorForName() {
        registerDto.setName(null);
        ResponseEntity<ValidationError> response = postRegister(registerDto, ValidationError.class);
        Map<String, String> validationErrors = response.getBody().getValidationErrors();
        assertEquals(validationErrors.get("name"), "Name cannot be empty");
    }

    @Test
    @DisplayName("Integration test for check error message when register not successfully, because email is empty")
    public void testRegister_whenUserHasNullEmail_receiveMessageOfNullErrorForEmail() {
        registerDto.setEmail(null);
        ResponseEntity<ValidationError> response = postRegister(registerDto, ValidationError.class);
        Map<String, String> validationErrors = response.getBody().getValidationErrors();
        assertEquals(validationErrors.get("email"), "Email cannot be empty");
    }

    @Test
    @DisplayName("Integration test for check error message when register not successfully, because phone is empty")
    public void testRegister_whenUserHasNullPhone_receiveMessageOfNullErrorForPhone() {
        registerDto.setPhone(null);
        ResponseEntity<ValidationError> response = postRegister(registerDto, ValidationError.class);
        Map<String, String> validationErrors = response.getBody().getValidationErrors();
        assertEquals(validationErrors.get("phone"), "Phone cannot be empty");
    }

    @Test
    @DisplayName("Integration test for check error message when register not successfully, because address is empty")
    public void testRegister_whenUserHasNullAddress_receiveMessageOfNullErrorForAddress() {
        registerDto.setAddress(null);
        ResponseEntity<ValidationError> response = postRegister(registerDto, ValidationError.class);
        Map<String, String> validationErrors = response.getBody().getValidationErrors();
        assertEquals(validationErrors.get("address"), "Address cannot be empty");
    }

    @Test
    @DisplayName("Integration test for check error message when register not successfully, because password is empty")
    public void testRegister_whenUserHasNullPassword_receiveMessageOfNullErrorForPassword() {
        registerDto.setPassword(null);
        ResponseEntity<ValidationError> response = postRegister(registerDto, ValidationError.class);
        Map<String, String> validationErrors = response.getBody().getValidationErrors();
        assertEquals(validationErrors.get("password"), "Password cannot be empty");
    }

    @Test
    @DisplayName("Integration test for check error message when register not successfully, because name is short")
    public void testRegister_whenUserHasInvalidLengthName_receiveMessageOfSizeError() {
        registerDto.setName("abc");
        ResponseEntity<ValidationError> response = postRegister(registerDto, ValidationError.class);
        Map<String, String> validationErrors = response.getBody().getValidationErrors();
        assertEquals(validationErrors.get("name"), "It must have minimum 4 and maximum 255 characters");
    }

    @Test
    @DisplayName("Integration test for check error message when register not successfully, because password is short")
    public void testRegister_whenUserHasInvalidLengthPassword_receiveMessageOfSizeError() {
        registerDto.setPassword("P4sswd");
        ResponseEntity<ValidationError> response = postRegister(registerDto, ValidationError.class);
        Map<String, String> validationErrors = response.getBody().getValidationErrors();
        assertEquals(validationErrors.get("password"), "It must have minimum 8 and maximum 255 characters");
    }

    @Test
    @DisplayName("Integration test for check error message when register not successfully, because email is incorrect")
    public void testRegister_whenUserHasInvalidEmailPattern_receiveMessageOfEmailPatternError() {
        registerDto.setEmail("abc@abc");
        ResponseEntity<ValidationError> response = postRegister(registerDto, ValidationError.class);
        Map<String, String> validationErrors = response.getBody().getValidationErrors();
        assertEquals(validationErrors.get("email"), "Email must be correct");
    }

    @Test
    @DisplayName("Integration test for check error message when register not successfully, because password is incorrect")
    public void testRegister_whenUserHasInvalidPasswordPattern_receiveMessageOfPasswordPatternError() {
        registerDto.setPassword("alllowercase");
        ResponseEntity<ValidationError> response = postRegister(registerDto, ValidationError.class);
        Map<String, String> validationErrors = response.getBody().getValidationErrors();
        assertEquals(validationErrors.get("password"), "Password must have at least one uppercase, one lowercase and one number");
    }

    @Test
    @DisplayName("Integration test for check error message when register not successfully, because another user has same email")
    public void testRegister_whenAnotherUserHasSameEmail_receiveMessageOfDuplicateEmail() {
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
        registerDto = TestUtil.getValidRegisterDto();
        ResponseEntity<ValidationError> response = postRegister(registerDto, ValidationError.class);
        Map<String, String> validationErrors = response.getBody().getValidationErrors();
        assertEquals(validationErrors.get("email"), "This email is already exist");
    }

}