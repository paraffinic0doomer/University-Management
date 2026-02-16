package com.springproject.universitymanagementsystem.repository;

import com.springproject.universitymanagementsystem.entity.Department;
import com.springproject.universitymanagementsystem.entity.Teacher;
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

/**
 * Repository tests for TeacherRepository
 * Uses @DataJpaTest with H2 in-memory database
 * Tests save(), findById(), findAll(), delete(), and transactional behavior
 * Follows AAA (Arrange-Act-Assert) pattern
 */
@DataJpaTest
@ActiveProfiles("test")
@DisplayName("TeacherRepository Tests")
class TeacherRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private TeacherRepository teacherRepository;

    private Department testDepartment;
    private Teacher testTeacher;

    @BeforeEach
    void setUp() {
        // Arrange - Set up test data
        testDepartment = new Department();
        testDepartment.setName("Computer Science");
        testDepartment.setDescription("CS Department");
        entityManager.persist(testDepartment);

        testTeacher = new Teacher();
        testTeacher.setName("Dr. John Smith");
        testTeacher.setEmail("john.smith@university.edu");
        testTeacher.setPhone("+1234567890");
        testTeacher.setSpecialization("Software Engineering");
        testTeacher.setDepartment(testDepartment);
    }

    // ===== save() Tests =====

    @Nested
    @DisplayName("save() Tests")
    class SaveTests {

        @Test
        @DisplayName("Should save teacher successfully")
        void testSave_Success() {
            // Act
            Teacher savedTeacher = teacherRepository.save(testTeacher);
            entityManager.flush();

            // Assert
            assertThat(savedTeacher).isNotNull();
            assertThat(savedTeacher.getId()).isNotNull();
            assertThat(savedTeacher.getName()).isEqualTo("Dr. John Smith");
            assertThat(savedTeacher.getEmail()).isEqualTo("john.smith@university.edu");
            assertThat(savedTeacher.getPhone()).isEqualTo("+1234567890");
            assertThat(savedTeacher.getSpecialization()).isEqualTo("Software Engineering");
            assertThat(savedTeacher.getDepartment().getName()).isEqualTo("Computer Science");
        }

        @Test
        @DisplayName("Should save teacher with null optional fields")
        void testSave_WithNullOptionalFields() {
            // Arrange
            Teacher teacher = new Teacher();
            teacher.setName("Dr. Minimal");
            teacher.setEmail(null);
            teacher.setPhone(null);
            teacher.setSpecialization(null);

            // Act
            Teacher savedTeacher = teacherRepository.save(teacher);
            entityManager.flush();

            // Assert
            assertThat(savedTeacher).isNotNull();
            assertThat(savedTeacher.getId()).isNotNull();
            assertThat(savedTeacher.getName()).isEqualTo("Dr. Minimal");
            assertThat(savedTeacher.getEmail()).isNull();
            assertThat(savedTeacher.getPhone()).isNull();
            assertThat(savedTeacher.getSpecialization()).isNull();
        }

        @Test
        @DisplayName("Should update existing teacher on save")
        void testSave_Update() {
            // Arrange
            Teacher savedTeacher = entityManager.persistAndFlush(testTeacher);
            Long teacherId = savedTeacher.getId();

            // Act
            savedTeacher.setName("Dr. John Smith Jr.");
            savedTeacher.setSpecialization("Machine Learning");
            Teacher updatedTeacher = teacherRepository.save(savedTeacher);
            entityManager.flush();

            // Assert
            assertThat(updatedTeacher.getId()).isEqualTo(teacherId);
            assertThat(updatedTeacher.getName()).isEqualTo("Dr. John Smith Jr.");
            assertThat(updatedTeacher.getSpecialization()).isEqualTo("Machine Learning");
        }

        @Test
        @DisplayName("Should save teacher without department")
        void testSave_WithoutDepartment() {
            // Arrange
            Teacher teacher = new Teacher();
            teacher.setName("Dr. No Dept");
            teacher.setDepartment(null);

            // Act
            Teacher savedTeacher = teacherRepository.save(teacher);
            entityManager.flush();

            // Assert
            assertThat(savedTeacher).isNotNull();
            assertThat(savedTeacher.getId()).isNotNull();
            assertThat(savedTeacher.getDepartment()).isNull();
        }
    }

    // ===== findById() Tests =====

    @Nested
    @DisplayName("findById() Tests")
    class FindByIdTests {

        @Test
        @DisplayName("Should find teacher by ID successfully")
        void testFindById_Success() {
            // Arrange
            Teacher savedTeacher = entityManager.persistAndFlush(testTeacher);
            Long teacherId = savedTeacher.getId();

            // Act
            Optional<Teacher> foundTeacher = teacherRepository.findById(teacherId);

            // Assert
            assertThat(foundTeacher).isPresent();
            assertThat(foundTeacher.get().getId()).isEqualTo(teacherId);
            assertThat(foundTeacher.get().getName()).isEqualTo("Dr. John Smith");
            assertThat(foundTeacher.get().getEmail()).isEqualTo("john.smith@university.edu");
            assertThat(foundTeacher.get().getSpecialization()).isEqualTo("Software Engineering");
        }

        @Test
        @DisplayName("Should return empty Optional when teacher not found")
        void testFindById_NotFound() {
            // Act
            Optional<Teacher> foundTeacher = teacherRepository.findById(999L);

            // Assert
            assertThat(foundTeacher).isEmpty();
        }

        @Test
        @DisplayName("Should load department relationship eagerly")
        void testFindById_LoadsDepartment() {
            // Arrange
            Teacher savedTeacher = entityManager.persistAndFlush(testTeacher);

            // Act
            Optional<Teacher> foundTeacher = teacherRepository.findById(savedTeacher.getId());

            // Assert
            assertThat(foundTeacher).isPresent();
            assertThat(foundTeacher.get().getDepartment()).isNotNull();
            assertThat(foundTeacher.get().getDepartment().getName()).isEqualTo("Computer Science");
        }
    }

    // ===== findAll() Tests =====

    @Nested
    @DisplayName("findAll() Tests")
    class FindAllTests {

        @Test
        @DisplayName("Should find all teachers")
        void testFindAll_Success() {
            // Arrange
            Teacher teacher2 = new Teacher();
            teacher2.setName("Dr. Jane Doe");
            teacher2.setEmail("jane.doe@university.edu");
            teacher2.setSpecialization("Data Science");
            teacher2.setDepartment(testDepartment);

            Teacher teacher3 = new Teacher();
            teacher3.setName("Prof. Bob");
            teacher3.setEmail("bob@university.edu");
            teacher3.setSpecialization("AI");

            entityManager.persist(testTeacher);
            entityManager.persist(teacher2);
            entityManager.persistAndFlush(teacher3);

            // Act
            List<Teacher> teachers = teacherRepository.findAll();

            // Assert
            assertThat(teachers).hasSize(3);
            assertThat(teachers)
                    .extracting(Teacher::getName)
                    .containsExactlyInAnyOrder("Dr. John Smith", "Dr. Jane Doe", "Prof. Bob");
        }

        @Test
        @DisplayName("Should return empty list when no teachers exist")
        void testFindAll_EmptyList() {
            // Act
            List<Teacher> teachers = teacherRepository.findAll();

            // Assert
            assertThat(teachers).isEmpty();
        }
    }

    // ===== delete() Tests =====

    @Nested
    @DisplayName("delete() Tests")
    class DeleteTests {

        @Test
        @DisplayName("Should delete teacher successfully")
        void testDelete_Success() {
            // Arrange
            Teacher savedTeacher = entityManager.persistAndFlush(testTeacher);
            Long teacherId = savedTeacher.getId();

            // Act
            teacherRepository.deleteById(teacherId);
            entityManager.flush();

            // Assert
            Optional<Teacher> deletedTeacher = teacherRepository.findById(teacherId);
            assertThat(deletedTeacher).isEmpty();
        }

        @Test
        @DisplayName("Should not affect other teachers when deleting one")
        void testDelete_DoesNotAffectOthers() {
            // Arrange
            Teacher teacher2 = new Teacher();
            teacher2.setName("Dr. Jane Doe");
            teacher2.setDepartment(testDepartment);

            Teacher saved1 = entityManager.persist(testTeacher);
            Teacher saved2 = entityManager.persistAndFlush(teacher2);

            // Act
            teacherRepository.deleteById(saved1.getId());
            entityManager.flush();

            // Assert
            List<Teacher> remaining = teacherRepository.findAll();
            assertThat(remaining).hasSize(1);
            assertThat(remaining.get(0).getName()).isEqualTo("Dr. Jane Doe");
        }

        @Test
        @DisplayName("Should reduce count after delete")
        void testDelete_ReducesCount() {
            // Arrange
            entityManager.persist(testTeacher);

            Teacher teacher2 = new Teacher();
            teacher2.setName("Prof. Bob");
            entityManager.persistAndFlush(teacher2);

            long countBefore = teacherRepository.count();

            // Act
            teacherRepository.deleteById(testTeacher.getId());
            entityManager.flush();

            // Assert
            long countAfter = teacherRepository.count();
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
            Teacher savedTeacher = teacherRepository.save(testTeacher);
            entityManager.flush();
            Long id = savedTeacher.getId();

            // Act - Update
            savedTeacher.setEmail("updated@university.edu");
            savedTeacher.setSpecialization("Updated Specialization");
            teacherRepository.save(savedTeacher);
            entityManager.flush();

            // Assert - Read back
            Optional<Teacher> found = teacherRepository.findById(id);
            assertThat(found).isPresent();
            assertThat(found.get().getEmail()).isEqualTo("updated@university.edu");
            assertThat(found.get().getSpecialization()).isEqualTo("Updated Specialization");
        }

        @Test
        @DisplayName("Should count teachers correctly")
        void testCount() {
            // Arrange
            assertThat(teacherRepository.count()).isZero();

            Teacher t1 = new Teacher();
            t1.setName("Teacher 1");
            Teacher t2 = new Teacher();
            t2.setName("Teacher 2");

            entityManager.persist(t1);
            entityManager.persistAndFlush(t2);

            // Act
            long count = teacherRepository.count();

            // Assert
            assertThat(count).isEqualTo(2);
        }

        @Test
        @DisplayName("Should check existence by ID")
        void testExistsById() {
            // Arrange
            Teacher savedTeacher = entityManager.persistAndFlush(testTeacher);

            // Act & Assert
            assertThat(teacherRepository.existsById(savedTeacher.getId())).isTrue();
            assertThat(teacherRepository.existsById(999L)).isFalse();
        }
    }
}
