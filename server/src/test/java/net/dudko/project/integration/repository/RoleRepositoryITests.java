package net.dudko.project.integration.repository;

import net.dudko.project.domain.entity.Role;
import net.dudko.project.domain.repository.RoleRepository;
import net.dudko.project.integration.AbstractContainerBaseTest;
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
class RoleRepositoryITests extends AbstractContainerBaseTest {

    private final TestEntityManager entityManager;
    private final RoleRepository roleRepository;

    @Autowired
    public RoleRepositoryITests(TestEntityManager entityManager,
                                RoleRepository roleRepository) {
        this.entityManager = entityManager;
        this.roleRepository = roleRepository;
    }

    @Test
    @DisplayName("Unit positive test for find role by name when role exist in DB")
    public void findByRoleName_whenRoleExists_thenReturnRole() {
        Role role = new Role();
        role.setName("ROLE_TEST");
        entityManager.persist(role);
        Role inDB = roleRepository.findByName("ROLE_TEST");
        assertNotNull(inDB);
    }

    @Test
    @DisplayName("Unit negative test for find role by name when role not exist in DB")
    public void findByRoleName_whenRoleNotExists_thenReturnNull() {
        Role inDb = roleRepository.findByName("ROLE_NEGATIVE_TEST");
        assertNull(inDb);
    }

}