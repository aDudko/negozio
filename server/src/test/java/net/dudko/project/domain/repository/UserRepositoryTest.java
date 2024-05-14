package net.dudko.project.domain.repository;

import net.dudko.project.domain.entity.Role;
import net.dudko.project.domain.entity.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
class UserRepositoryTest {

    @Autowired
    TestEntityManager entityManager;

    @Autowired
    UserRepository userRepository;

    @BeforeEach
    @AfterEach
    public void cleanup() {
        userRepository.deleteAll();
    }

    private User createValidUser() {
        return User.builder()
                .name("test-user")
                .email("test@mail.com")
                .phone("+491234567890")
                .address("TestAddress st. 69/7")
                .password("P4ssword")
                .build();
    }

    @Test
    public void existsByEmail_whenEmailExists_thenReturnTrue() {
        User user = createValidUser();
        entityManager.persist(user);
        assertTrue(userRepository.existsByEmail("test@mail.com"));
    }

    @Test
    public void existsByEmail_whenEmailNotExists_thenReturnFalse() {
        assertFalse(userRepository.existsByEmail("negativee_test@mail.com"));
    }

    @Test
    public void findByEmail_whenEmailExists_thenReturnUser() {
        User user = createValidUser();
        entityManager.persist(user);
        assertTrue(userRepository.findByEmail("test@mail.com").isPresent());
    }

    @Test
    public void findByEmail_whenEmailNotExists_thenReturnUser() {
        assertFalse(userRepository.findByEmail("negativee_test@mail.com").isPresent());
    }

}