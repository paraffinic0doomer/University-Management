package com.springproject.universitymanagementsystem.repository;

import com.springproject.universitymanagementsystem.entity.Department;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Repository tests for DepartmentRepository
 * Uses @DataJpaTest with H2 in-memory database
 * Tests save(), findById(), findAll(), delete(), and transactional behavior
 * Follows AAA (Arrange-Act-Assert) pattern
 */
@DataJpaTest
@ActiveProfiles("test")
@DisplayName("DepartmentRepository Tests")
class DepartmentRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private DepartmentRepository departmentRepository;

    private Department testDepartment;

    @BeforeEach
    void setUp() {
        // Arrange - Set up test data
        testDepartment = new Department();
        testDepartment.setName("Computer Science");
        testDepartment.setDescription("Department of Computer Science and Engineering");
    }

    // ===== save() Tests =====

    @Nested
    @DisplayName("save() Tests")
    class SaveTests {

        @Test
        @DisplayName("Should save department successfully")
        void testSave_Success() {
            // Act
            Department savedDept = departmentRepository.save(testDepartment);
            entityManager.flush();

            // Assert
            assertThat(savedDept).isNotNull();
            assertThat(savedDept.getId()).isNotNull();
            assertThat(savedDept.getName()).isEqualTo("Computer Science");
            assertThat(savedDept.getDescription()).isEqualTo("Department of Computer Science and Engineering");
        }

        @Test
        @DisplayName("Should save department with null description")
        void testSave_NullDescription() {
            // Arrange
            Department dept = new Department();
            dept.setName("Mathematics");
            dept.setDescription(null);

            // Act
            Department savedDept = departmentRepository.save(dept);
            entityManager.flush();

            // Assert
            assertThat(savedDept).isNotNull();
            assertThat(savedDept.getId()).isNotNull();
            assertThat(savedDept.getName()).isEqualTo("Mathematics");
            assertThat(savedDept.getDescription()).isNull();
        }

        @Test
        @DisplayName("Should update existing department on save")
        void testSave_Update() {
            // Arrange
            Department savedDept = entityManager.persistAndFlush(testDepartment);
            Long deptId = savedDept.getId();

            // Act
            savedDept.setName("Computer Science & Engineering");
            savedDept.setDescription("Updated description");
            Department updatedDept = departmentRepository.save(savedDept);
            entityManager.flush();

            // Assert
            assertThat(updatedDept.getId()).isEqualTo(deptId);
            assertThat(updatedDept.getName()).isEqualTo("Computer Science & Engineering");
            assertThat(updatedDept.getDescription()).isEqualTo("Updated description");
        }

        @Test
        @DisplayName("Should generate auto-incremented ID on save")
        void testSave_AutoIncrementId() {
            // Arrange
            Department dept1 = new Department();
            dept1.setName("Physics");
            Department dept2 = new Department();
            dept2.setName("Chemistry");

            // Act
            Department saved1 = departmentRepository.save(dept1);
            Department saved2 = departmentRepository.save(dept2);
            entityManager.flush();

            // Assert
            assertThat(saved1.getId()).isNotNull();
            assertThat(saved2.getId()).isNotNull();
            assertThat(saved2.getId()).isGreaterThan(saved1.getId());
        }
    }

    // ===== findById() Tests =====

    @Nested
    @DisplayName("findById() Tests")
    class FindByIdTests {

        @Test
        @DisplayName("Should find department by ID successfully")
        void testFindById_Success() {
            // Arrange
            Department savedDept = entityManager.persistAndFlush(testDepartment);
            Long deptId = savedDept.getId();

            // Act
            Optional<Department> foundDept = departmentRepository.findById(deptId);

            // Assert
            assertThat(foundDept).isPresent();
            assertThat(foundDept.get().getId()).isEqualTo(deptId);
            assertThat(foundDept.get().getName()).isEqualTo("Computer Science");
            assertThat(foundDept.get().getDescription())
                    .isEqualTo("Department of Computer Science and Engineering");
        }

        @Test
        @DisplayName("Should return empty Optional when department not found by ID")
        void testFindById_NotFound() {
            // Act
            Optional<Department> foundDept = departmentRepository.findById(999L);

            // Assert
            assertThat(foundDept).isEmpty();
        }

        @Test
        @DisplayName("Should return correct department among multiple")
        void testFindById_CorrectAmongMultiple() {
            // Arrange
            Department dept1 = new Department();
            dept1.setName("Physics");
            entityManager.persist(dept1);

            Department savedTarget = entityManager.persistAndFlush(testDepartment);

            Department dept3 = new Department();
            dept3.setName("Chemistry");
            entityManager.persistAndFlush(dept3);

            // Act
            Optional<Department> foundDept = departmentRepository.findById(savedTarget.getId());

            // Assert
            assertThat(foundDept).isPresent();
            assertThat(foundDept.get().getName()).isEqualTo("Computer Science");
        }
    }

    // ===== findAll() Tests =====

    @Nested
    @DisplayName("findAll() Tests")
    class FindAllTests {

        @Test
        @DisplayName("Should find all departments")
        void testFindAll_Success() {
            // Arrange
            Department dept2 = new Department();
            dept2.setName("Mathematics");
            dept2.setDescription("Math Department");

            Department dept3 = new Department();
            dept3.setName("Physics");
            dept3.setDescription("Physics Department");

            entityManager.persist(testDepartment);
            entityManager.persist(dept2);
            entityManager.persistAndFlush(dept3);

            // Act
            List<Department> departments = departmentRepository.findAll();

            // Assert
            assertThat(departments).hasSize(3);
            assertThat(departments)
                    .extracting(Department::getName)
                    .containsExactlyInAnyOrder("Computer Science", "Mathematics", "Physics");
        }

        @Test
        @DisplayName("Should return empty list when no departments exist")
        void testFindAll_EmptyList() {
            // Act
            List<Department> departments = departmentRepository.findAll();

            // Assert
            assertThat(departments).isEmpty();
        }

        @Test
        @DisplayName("Should return single department")
        void testFindAll_SingleDepartment() {
            // Arrange
            entityManager.persistAndFlush(testDepartment);

            // Act
            List<Department> departments = departmentRepository.findAll();

            // Assert
            assertThat(departments).hasSize(1);
            assertThat(departments.get(0).getName()).isEqualTo("Computer Science");
        }
    }

    // ===== delete() Tests =====

    @Nested
    @DisplayName("delete() Tests")
    class DeleteTests {

        @Test
        @DisplayName("Should delete department successfully")
        void testDelete_Success() {
            // Arrange
            Department savedDept = entityManager.persistAndFlush(testDepartment);
            Long deptId = savedDept.getId();

            // Act
            departmentRepository.deleteById(deptId);
            entityManager.flush();

            // Assert
            Optional<Department> deletedDept = departmentRepository.findById(deptId);
            assertThat(deletedDept).isEmpty();
        }

        @Test
        @DisplayName("Should not affect other departments when deleting one")
        void testDelete_DoesNotAffectOthers() {
            // Arrange
            Department dept2 = new Department();
            dept2.setName("Mathematics");

            Department savedDept1 = entityManager.persist(testDepartment);
            Department savedDept2 = entityManager.persistAndFlush(dept2);

            // Act
            departmentRepository.deleteById(savedDept1.getId());
            entityManager.flush();

            // Assert
            List<Department> remaining = departmentRepository.findAll();
            assertThat(remaining).hasSize(1);
            assertThat(remaining.get(0).getName()).isEqualTo("Mathematics");
        }

        @Test
        @DisplayName("Should reduce count after delete")
        void testDelete_ReducesCount() {
            // Arrange
            entityManager.persist(testDepartment);
            Department dept2 = new Department();
            dept2.setName("Physics");
            entityManager.persistAndFlush(dept2);

            long countBefore = departmentRepository.count();

            // Act
            departmentRepository.deleteById(testDepartment.getId());
            entityManager.flush();

            // Assert
            long countAfter = departmentRepository.count();
            assertThat(countAfter).isEqualTo(countBefore - 1);
        }
    }

    // ===== Transactional Behavior Tests =====

    @Nested
    @DisplayName("Transactional Behavior Tests")
    class TransactionalTests {

        @Test
        @DisplayName("Should maintain data consistency across operations")
        void testTransactionalConsistency() {
            // Arrange & Act - Save
            Department savedDept = departmentRepository.save(testDepartment);
            entityManager.flush();
            Long id = savedDept.getId();

            // Act - Update
            savedDept.setDescription("Updated via transaction");
            departmentRepository.save(savedDept);
            entityManager.flush();

            // Assert - Read back
            Optional<Department> found = departmentRepository.findById(id);
            assertThat(found).isPresent();
            assertThat(found.get().getDescription()).isEqualTo("Updated via transaction");
        }

        @Test
        @DisplayName("Should count departments correctly")
        void testCount() {
            // Arrange
            assertThat(departmentRepository.count()).isZero();

            Department dept1 = new Department();
            dept1.setName("Dept 1");
            Department dept2 = new Department();
            dept2.setName("Dept 2");
            Department dept3 = new Department();
            dept3.setName("Dept 3");

            entityManager.persist(dept1);
            entityManager.persist(dept2);
            entityManager.persistAndFlush(dept3);

            // Act
            long count = departmentRepository.count();

            // Assert
            assertThat(count).isEqualTo(3);
        }

        @Test
        @DisplayName("Should check existence by ID")
        void testExistsById() {
            // Arrange
            Department savedDept = entityManager.persistAndFlush(testDepartment);

            // Act & Assert
            assertThat(departmentRepository.existsById(savedDept.getId())).isTrue();
            assertThat(departmentRepository.existsById(999L)).isFalse();
        }
    }
}
