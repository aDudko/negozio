package net.dudko.project.domain.repository;

import net.dudko.project.domain.entity.Role;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
class RoleRepositoryTest {

    @Autowired
    TestEntityManager entityManager;

    @Autowired
    RoleRepository roleRepository;

    @Test
    public void findByRoleName_whenRoleExists_thenReturnRole() {
        Role role = new Role();
        role.setName("ROLE_TEST");
        entityManager.persist(role);
        Role inDB = roleRepository.findByName("ROLE_TEST");
        assertNotNull(inDB);
    }

    @Test
    public void findByRoleName_whenRoleNotExists_thenReturnNull() {
        Role inDb = roleRepository.findByName("ROLE_NEGATIVE_TEST");
        assertNull(inDb);
    }

}