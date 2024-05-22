package net.dudko.project.integration.domain.repository;

import net.dudko.project.TestUtil;
import net.dudko.project.domain.entity.User;
import net.dudko.project.domain.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
class UserRepositoryITests {

    private final TestEntityManager entityManager;
    private final UserRepository userRepository;

    @Autowired
    public UserRepositoryITests(TestEntityManager entityManager,
                                UserRepository userRepository) {
        this.entityManager = entityManager;
        this.userRepository = userRepository;
    }

    @BeforeEach
    @AfterEach
    public void cleanup() {
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("Unit test for check exist user in DB by email")
    public void existsByEmail_whenEmailExists_thenReturnTrue() {
        User user = TestUtil.getValidUser();
        entityManager.persist(user);
        assertTrue(userRepository.existsByEmail("test@mail.com"));
    }

    @Test
    @DisplayName("Unit test for check not exist user in DB by email")
    public void existsByEmail_whenEmailNotExists_thenReturnFalse() {
        assertFalse(userRepository.existsByEmail("negativee_test@mail.com"));
    }

    @Test
    @DisplayName("Unit test for find exist user in DB by email")
    public void findByEmail_whenEmailExists_thenReturnUser() {
        User user = TestUtil.getValidUser();
        entityManager.persist(user);
        assertTrue(userRepository.findByEmail("test@mail.com").isPresent());
    }

    @Test
    @DisplayName("Unit test for find not exist user in DB by email")
    public void findByEmail_whenEmailNotExists_thenReturnUser() {
        assertFalse(userRepository.findByEmail("negativee_test@mail.com").isPresent());
    }

}