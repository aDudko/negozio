package net.dudko.project.controller;

import net.dudko.project.domain.entity.Role;
import net.dudko.project.domain.entity.User;
import net.dudko.project.domain.repository.RoleRepository;
import net.dudko.project.domain.repository.UserRepository;
import net.dudko.project.model.dto.JwtAuthResponse;
import net.dudko.project.model.dto.RegisterDto;
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
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class AuthControllerRegisterTest {

    private static final String BASE_URL = "/auth/register";

    @Autowired
    TestRestTemplate testRestTemplate;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @BeforeEach
    @AfterEach
    public void cleanup() {
        userRepository.deleteAll();
    }

    private RegisterDto createValidRegisterDto() {
        return RegisterDto.builder()
                .name("test-user")
                .email("test@mail.com")
                .phone("+491234567890")
                .address("TestAddress st. 69/7")
                .password("P4ssword")
                .build();
    }

    private <T> ResponseEntity<T> postRegister(Object request, Class<T> response) {
        return testRestTemplate.postForEntity(BASE_URL, request, response);
    }

    @Test
    public void testRegister_whenUserIsValid_thenReturnOk() {
        RegisterDto registerDto = createValidRegisterDto();
        ResponseEntity<Object> response = postRegister(registerDto, Object.class);
        assertEquals(response.getStatusCode(), HttpStatus.OK);
    }

    @Test
    public void testRegister_whenUserIsValid_thenUserSavedToDatabase() {
        RegisterDto registerDto = createValidRegisterDto();
        postRegister(registerDto, Object.class);
        assertEquals((long) userRepository.findAll().size(), 1);
    }

    @Test
    public void testRegister_whenUserIsValid_receiveAccessToken() {
        RegisterDto registerDto = createValidRegisterDto();
        ResponseEntity<JwtAuthResponse> response = postRegister(registerDto, JwtAuthResponse.class);
        assertNotNull(response.getBody().getAccessToken());
    }

    @Test
    public void testRegister_whenUserIsValid_passwordIsHashedInDatabase() {
        RegisterDto registerDto = createValidRegisterDto();
        postRegister(registerDto, Object.class);
        User inDb = userRepository.findAll().get(0);
        assertNotEquals(registerDto.getPassword(), inDb.getPassword());
    }

    // VALIDATION

    // Null-check

    @Test
    public void testRegister_whenUserHasNullName_receiveBadRequest() {
        RegisterDto registerDto = createValidRegisterDto();
        registerDto.setName(null);
        ResponseEntity<Object> response = postRegister(registerDto, Object.class);
        assertEquals(response.getStatusCode(), HttpStatus.BAD_REQUEST);
    }

    @Test
    public void testRegister_whenUserHasNullEmail_receiveBadRequest() {
        RegisterDto registerDto = createValidRegisterDto();
        registerDto.setEmail(null);
        ResponseEntity<Object> response = postRegister(registerDto, Object.class);
        assertEquals(response.getStatusCode(), HttpStatus.BAD_REQUEST);
    }

    @Test
    public void testRegister_whenUserHasNullPhone_receiveBadRequest() {
        RegisterDto registerDto = createValidRegisterDto();
        registerDto.setPhone(null);
        ResponseEntity<Object> response = postRegister(registerDto, Object.class);
        assertEquals(response.getStatusCode(), HttpStatus.BAD_REQUEST);
    }

    @Test
    public void testRegister_whenUserHasNullAddress_receiveBadRequest() {
        RegisterDto registerDto = createValidRegisterDto();
        registerDto.setAddress(null);
        ResponseEntity<Object> response = postRegister(registerDto, Object.class);
        assertEquals(response.getStatusCode(), HttpStatus.BAD_REQUEST);
    }

    @Test
    public void testRegister_whenUserHasNullPassword_receiveBadRequest() {
        RegisterDto registerDto = createValidRegisterDto();
        registerDto.setPassword(null);
        ResponseEntity<Object> response = postRegister(registerDto, Object.class);
        assertEquals(response.getStatusCode(), HttpStatus.BAD_REQUEST);
    }

    // Size-check

    @Test
    public void testRegister_whenUserHasNameWithLessThanRequired_receiveBadRequest() {
        RegisterDto registerDto = createValidRegisterDto();
        registerDto.setName("abc");
        ResponseEntity<Object> response = postRegister(registerDto, Object.class);
        assertEquals(response.getStatusCode(), HttpStatus.BAD_REQUEST);
    }

    @Test
    public void testRegister_whenUserHasPasswordWithLessThanRequire_receiveBadRequest() {
        RegisterDto registerDto = createValidRegisterDto();
        registerDto.setPassword("P4sswd");
        ResponseEntity<Object> response = postRegister(registerDto, Object.class);
        assertEquals(response.getStatusCode(), HttpStatus.BAD_REQUEST);
    }

    @Test
    public void testRegister_whenUserHasNameExceedTheLengthLimit_receiveBadRequest() {
        RegisterDto registerDto = createValidRegisterDto();
        String valueOf256Chars = IntStream.rangeClosed(1, 256).mapToObj(x -> "a").collect(Collectors.joining());
        registerDto.setName(valueOf256Chars);
        ResponseEntity<Object> response = postRegister(registerDto, Object.class);
        assertEquals(response.getStatusCode(), HttpStatus.BAD_REQUEST);
    }

    @Test
    public void testRegister_whenUserHasPasswordExceedTheLengthLimit_receiveBadRequest() {
        RegisterDto registerDto = createValidRegisterDto();
        String valueOf256Chars = IntStream.rangeClosed(1, 256).mapToObj(x -> "a").collect(Collectors.joining());
        registerDto.setPassword(valueOf256Chars + "A1");
        ResponseEntity<Object> response = postRegister(registerDto, Object.class);
        assertEquals(response.getStatusCode(), HttpStatus.BAD_REQUEST);
    }

    // Pattern

    @Test
    public void testRegister_whenUserHasEmailWithoutAtAndDomainName_receiveBadRequest() {
        RegisterDto registerDto = createValidRegisterDto();
        registerDto.setEmail("abc");
        ResponseEntity<Object> response = postRegister(registerDto, Object.class);
        assertEquals(response.getStatusCode(), HttpStatus.BAD_REQUEST);
    }

    @Test
    public void testRegister_whenUserHasEmailWithoutDomainName_receiveBadRequest() {
        RegisterDto registerDto = createValidRegisterDto();
        registerDto.setEmail("abc@");
        ResponseEntity<Object> response = postRegister(registerDto, Object.class);
        assertEquals(response.getStatusCode(), HttpStatus.BAD_REQUEST);
    }

    @Test
    public void testRegister_whenUserHasEmailWithoutCode_receiveBadRequest() {
        RegisterDto registerDto = createValidRegisterDto();
        registerDto.setEmail("abc@abc");
        ResponseEntity<Object> response = postRegister(registerDto, Object.class);
        assertEquals(response.getStatusCode(), HttpStatus.BAD_REQUEST);
    }

    @Test
    public void testRegister_whenUserHasEmailWithoutUserName_receiveBadRequest() {
        RegisterDto registerDto = createValidRegisterDto();
        registerDto.setEmail("@abc");
        ResponseEntity<Object> response = postRegister(registerDto, Object.class);
        assertEquals(response.getStatusCode(), HttpStatus.BAD_REQUEST);
    }

    @Test
    public void testRegister_whenUserHasEmailWithoutUserNameAndCode_receiveBadRequest() {
        RegisterDto registerDto = createValidRegisterDto();
        registerDto.setEmail("@abc.");
        ResponseEntity<Object> response = postRegister(registerDto, Object.class);
        assertEquals(response.getStatusCode(), HttpStatus.BAD_REQUEST);
    }

    @Test
    public void testRegister_whenUserHasEmailWithoutAt_receiveBadRequest() {
        RegisterDto registerDto = createValidRegisterDto();
        registerDto.setEmail("abc.com");
        ResponseEntity<Object> response = postRegister(registerDto, Object.class);
        assertEquals(response.getStatusCode(), HttpStatus.BAD_REQUEST);
    }


    @Test
    public void testRegister_whenUserHasPasswordWithAllLowercase_receiveBadRequest() {
        RegisterDto registerDto = createValidRegisterDto();
        registerDto.setPassword("alllowercase");
        ResponseEntity<Object> response = postRegister(registerDto, Object.class);
        assertEquals(response.getStatusCode(), HttpStatus.BAD_REQUEST);
    }

    @Test
    public void testRegister_whenUserHasPasswordWithAllUppercase_receiveBadRequest() {
        RegisterDto registerDto = createValidRegisterDto();
        registerDto.setPassword("ALLUPPERCASE");
        ResponseEntity<Object> response = postRegister(registerDto, Object.class);
        assertEquals(response.getStatusCode(), HttpStatus.BAD_REQUEST);
    }

    @Test
    public void testRegister_whenUserHasPasswordWithAllNumber_receiveBadRequest() {
        RegisterDto registerDto = createValidRegisterDto();
        registerDto.setPassword("1234567890");
        ResponseEntity<Object> response = postRegister(registerDto, Object.class);
        assertEquals(response.getStatusCode(), HttpStatus.BAD_REQUEST);
    }

    @Test
    public void testRegister_whenAnotherUserHasSameEmail_receiveBadRequest() {
        RegisterDto registerDto = createValidRegisterDto();
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
        registerDto = createValidRegisterDto();
        ResponseEntity<Object> response = postRegister(registerDto, Object.class);
        assertEquals(response.getStatusCode(), HttpStatus.BAD_REQUEST);
    }

    @Test
    public void testRegister_whenUserIsValid_receiveApiError() {
        RegisterDto registerDto = new RegisterDto();
        ResponseEntity<ValidationError> response = postRegister(registerDto, ValidationError.class);
        assertEquals(response.getBody().getUrl(), BASE_URL);
    }

    @Test
    public void testRegister_whenUserIsValid_receiveValidationErrorWithValidationErrors() {
        RegisterDto registerDto = new RegisterDto();
        ResponseEntity<ValidationError> response = postRegister(registerDto, ValidationError.class);
        assertEquals(response.getBody().getValidationErrors().size(), 5);
    }

    // INTERNALIZATION

    @Test
    public void testRegister_whenUserHasNullName_receiveMessageOfNullErrorForName() {
        RegisterDto registerDto = createValidRegisterDto();
        registerDto.setName(null);
        ResponseEntity<ValidationError> response = postRegister(registerDto, ValidationError.class);
        Map<String, String> validationErrors = response.getBody().getValidationErrors();
        assertEquals(validationErrors.get("name"), "Name cannot be empty");
    }

    @Test
    public void testRegister_whenUserHasNullEmail_receiveMessageOfNullErrorForEmail() {
        RegisterDto registerDto = createValidRegisterDto();
        registerDto.setEmail(null);
        ResponseEntity<ValidationError> response = postRegister(registerDto, ValidationError.class);
        Map<String, String> validationErrors = response.getBody().getValidationErrors();
        assertEquals(validationErrors.get("email"), "Email cannot be empty");
    }

    @Test
    public void testRegister_whenUserHasNullPhone_receiveMessageOfNullErrorForPhone() {
        RegisterDto registerDto = createValidRegisterDto();
        registerDto.setPhone(null);
        ResponseEntity<ValidationError> response = postRegister(registerDto, ValidationError.class);
        Map<String, String> validationErrors = response.getBody().getValidationErrors();
        assertEquals(validationErrors.get("phone"), "Phone cannot be empty");
    }

    @Test
    public void testRegister_whenUserHasNullAddress_receiveMessageOfNullErrorForAddress() {
        RegisterDto registerDto = createValidRegisterDto();
        registerDto.setAddress(null);
        ResponseEntity<ValidationError> response = postRegister(registerDto, ValidationError.class);
        Map<String, String> validationErrors = response.getBody().getValidationErrors();
        assertEquals(validationErrors.get("address"), "Address cannot be empty");
    }

    @Test
    public void testRegister_whenUserHasNullPassword_receiveMessageOfNullErrorForPassword() {
        RegisterDto registerDto = createValidRegisterDto();
        registerDto.setPassword(null);
        ResponseEntity<ValidationError> response = postRegister(registerDto, ValidationError.class);
        Map<String, String> validationErrors = response.getBody().getValidationErrors();
        assertEquals(validationErrors.get("password"), "Password cannot be empty");
    }

    @Test
    public void testRegister_whenUserHasInvalidLengthName_receiveMessageOfSizeError() {
        RegisterDto registerDto = createValidRegisterDto();
        registerDto.setName("abc");
        ResponseEntity<ValidationError> response = postRegister(registerDto, ValidationError.class);
        Map<String, String> validationErrors = response.getBody().getValidationErrors();
        assertEquals(validationErrors.get("name"), "It must have minimum 4 and maximum 255 characters");
    }

    @Test
    public void testRegister_whenUserHasInvalidLengthPassword_receiveMessageOfSizeError() {
        RegisterDto registerDto = createValidRegisterDto();
        registerDto.setPassword("P4sswd");
        ResponseEntity<ValidationError> response = postRegister(registerDto, ValidationError.class);
        Map<String, String> validationErrors = response.getBody().getValidationErrors();
        assertEquals(validationErrors.get("password"), "It must have minimum 8 and maximum 255 characters");
    }

    @Test
    public void testRegister_whenUserHasInvalidEmailPattern_receiveMessageOfEmailPatternError() {
        RegisterDto registerDto = createValidRegisterDto();
        registerDto.setEmail("abc@abc");
        ResponseEntity<ValidationError> response = postRegister(registerDto, ValidationError.class);
        Map<String, String> validationErrors = response.getBody().getValidationErrors();
        assertEquals(validationErrors.get("email"), "Email must be correct");
    }

    @Test
    public void testRegister_whenUserHasInvalidPasswordPattern_receiveMessageOfPasswordPatternError() {
        RegisterDto registerDto = createValidRegisterDto();
        registerDto.setPassword("alllowercase");
        ResponseEntity<ValidationError> response = postRegister(registerDto, ValidationError.class);
        Map<String, String> validationErrors = response.getBody().getValidationErrors();
        assertEquals(validationErrors.get("password"), "Password must have at least one uppercase, one lowercase and one number");
    }

    @Test
    public void testRegister_whenAnotherUserHasSameEmail_receiveMessageOfDuplicateEmail() {
        RegisterDto registerDto = createValidRegisterDto();
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
        registerDto = createValidRegisterDto();
        ResponseEntity<ValidationError> response = postRegister(registerDto, ValidationError.class);
        Map<String, String> validationErrors = response.getBody().getValidationErrors();
        assertEquals(validationErrors.get("email"), "This email is already exist");
    }

}