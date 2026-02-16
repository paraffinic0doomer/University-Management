package com.springproject.universitymanagementsystem.service;

import com.springproject.universitymanagementsystem.entity.Department;
import com.springproject.universitymanagementsystem.entity.Teacher;
import com.springproject.universitymanagementsystem.repository.TeacherRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

/**
 * Unit tests for TeacherService
 * Uses Mockito to mock the repository layer
 * Follows AAA (Arrange-Act-Assert) pattern
 * Covers: success cases, failure cases, edge cases, exception handling
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("TeacherService Unit Tests")
class TeacherServiceTest {

    @Mock
    private TeacherRepository teacherRepository;

    @InjectMocks
    private TeacherService teacherService;

    private Teacher testTeacher;
    private Department testDepartment;

    @BeforeEach
    void setUp() {
        // Arrange - Set up test data
        testDepartment = new Department();
        testDepartment.setId(1L);
        testDepartment.setName("Computer Science");

        testTeacher = new Teacher();
        testTeacher.setId(1L);
        testTeacher.setName("Dr. John Smith");
        testTeacher.setEmail("john.smith@university.edu");
        testTeacher.setPhone("+1234567890");
        testTeacher.setSpecialization("Software Engineering");
        testTeacher.setDepartment(testDepartment);
    }

    // ===== findAll() Tests =====

    @Nested
    @DisplayName("findAll() Tests")
    class FindAllTests {

        @Test
        @DisplayName("Should return all teachers successfully")
        void testFindAll_Success() {
            // Arrange
            Teacher teacher2 = new Teacher();
            teacher2.setId(2L);
            teacher2.setName("Dr. Jane Doe");
            teacher2.setSpecialization("Data Science");

            List<Teacher> expectedTeachers = Arrays.asList(testTeacher, teacher2);
            when(teacherRepository.findAll()).thenReturn(expectedTeachers);

            // Act
            List<Teacher> actualTeachers = teacherService.findAll();

            // Assert
            assertThat(actualTeachers).isNotNull();
            assertThat(actualTeachers).hasSize(2);
            assertThat(actualTeachers).containsExactlyInAnyOrder(testTeacher, teacher2);
            verify(teacherRepository, times(1)).findAll();
        }

        @Test
        @DisplayName("Should return empty list when no teachers exist")
        void testFindAll_EmptyList() {
            // Arrange
            when(teacherRepository.findAll()).thenReturn(Collections.emptyList());

            // Act
            List<Teacher> actualTeachers = teacherService.findAll();

            // Assert
            assertThat(actualTeachers).isNotNull();
            assertThat(actualTeachers).isEmpty();
            verify(teacherRepository, times(1)).findAll();
        }

        @Test
        @DisplayName("Should return single teacher list")
        void testFindAll_SingleTeacher() {
            // Arrange
            when(teacherRepository.findAll()).thenReturn(Collections.singletonList(testTeacher));

            // Act
            List<Teacher> actualTeachers = teacherService.findAll();

            // Assert
            assertThat(actualTeachers).hasSize(1);
            assertThat(actualTeachers.get(0).getName()).isEqualTo("Dr. John Smith");
            verify(teacherRepository, times(1)).findAll();
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
            when(teacherRepository.findById(1L)).thenReturn(Optional.of(testTeacher));

            // Act
            Teacher foundTeacher = teacherService.findById(1L);

            // Assert
            assertThat(foundTeacher).isNotNull();
            assertThat(foundTeacher.getId()).isEqualTo(1L);
            assertThat(foundTeacher.getName()).isEqualTo("Dr. John Smith");
            assertThat(foundTeacher.getEmail()).isEqualTo("john.smith@university.edu");
            assertThat(foundTeacher.getSpecialization()).isEqualTo("Software Engineering");
            verify(teacherRepository, times(1)).findById(1L);
        }

        @Test
        @DisplayName("Should return null when teacher not found by ID")
        void testFindById_NotFound() {
            // Arrange
            when(teacherRepository.findById(999L)).thenReturn(Optional.empty());

            // Act
            Teacher foundTeacher = teacherService.findById(999L);

            // Assert
            assertThat(foundTeacher).isNull();
            verify(teacherRepository, times(1)).findById(999L);
        }

        @Test
        @DisplayName("Should return teacher with department info")
        void testFindById_WithDepartment() {
            // Arrange
            when(teacherRepository.findById(1L)).thenReturn(Optional.of(testTeacher));

            // Act
            Teacher foundTeacher = teacherService.findById(1L);

            // Assert
            assertThat(foundTeacher).isNotNull();
            assertThat(foundTeacher.getDepartment()).isNotNull();
            assertThat(foundTeacher.getDepartment().getName()).isEqualTo("Computer Science");
        }
    }

    // ===== save() Tests =====

    @Nested
    @DisplayName("save() Tests")
    class SaveTests {

        @Test
        @DisplayName("Should save teacher successfully")
        void testSave_Success() {
            // Arrange
            Teacher newTeacher = new Teacher();
            newTeacher.setName("Dr. Alice Brown");
            newTeacher.setEmail("alice.brown@university.edu");
            newTeacher.setSpecialization("Machine Learning");

            Teacher savedTeacher = new Teacher(3L, "Dr. Alice Brown",
                    "alice.brown@university.edu", null, "Machine Learning");
            when(teacherRepository.save(any(Teacher.class))).thenReturn(savedTeacher);

            // Act
            Teacher result = teacherService.save(newTeacher);

            // Assert
            assertThat(result).isNotNull();
            assertThat(result.getId()).isEqualTo(3L);
            assertThat(result.getName()).isEqualTo("Dr. Alice Brown");
            assertThat(result.getSpecialization()).isEqualTo("Machine Learning");
            verify(teacherRepository, times(1)).save(newTeacher);
        }

        @Test
        @DisplayName("Should save teacher with minimal data")
        void testSave_MinimalData() {
            // Arrange
            Teacher newTeacher = new Teacher();
            newTeacher.setName("Dr. Minimal");

            when(teacherRepository.save(any(Teacher.class))).thenReturn(newTeacher);

            // Act
            Teacher result = teacherService.save(newTeacher);

            // Assert
            assertThat(result).isNotNull();
            assertThat(result.getName()).isEqualTo("Dr. Minimal");
            assertThat(result.getEmail()).isNull();
            assertThat(result.getPhone()).isNull();
            verify(teacherRepository, times(1)).save(newTeacher);
        }

        @Test
        @DisplayName("Should save teacher with department")
        void testSave_WithDepartment() {
            // Arrange
            Teacher newTeacher = new Teacher();
            newTeacher.setName("Dr. Dept Teacher");
            newTeacher.setDepartment(testDepartment);

            when(teacherRepository.save(any(Teacher.class))).thenReturn(newTeacher);

            // Act
            Teacher result = teacherService.save(newTeacher);

            // Assert
            assertThat(result).isNotNull();
            assertThat(result.getDepartment()).isNotNull();
            assertThat(result.getDepartment().getName()).isEqualTo("Computer Science");
            verify(teacherRepository, times(1)).save(newTeacher);
        }
    }

    // ===== update() Tests =====

    @Nested
    @DisplayName("update() Tests")
    class UpdateTests {

        @Test
        @DisplayName("Should update teacher successfully - all fields")
        void testUpdate_Success_AllFields() {
            // Arrange
            Department newDept = new Department(2L, "Mathematics", "Math Dept");

            Teacher updateRequest = new Teacher();
            updateRequest.setName("Dr. John Smith Jr.");
            updateRequest.setEmail("john.jr@university.edu");
            updateRequest.setPhone("+9999999999");
            updateRequest.setSpecialization("Machine Learning");
            updateRequest.setDepartment(newDept);

            when(teacherRepository.findById(1L)).thenReturn(Optional.of(testTeacher));
            when(teacherRepository.save(any(Teacher.class))).thenAnswer(inv -> inv.getArgument(0));

            // Act
            Teacher result = teacherService.update(1L, updateRequest);

            // Assert
            assertThat(result).isNotNull();
            assertThat(result.getName()).isEqualTo("Dr. John Smith Jr.");
            assertThat(result.getEmail()).isEqualTo("john.jr@university.edu");
            assertThat(result.getPhone()).isEqualTo("+9999999999");
            assertThat(result.getSpecialization()).isEqualTo("Machine Learning");
            assertThat(result.getDepartment().getName()).isEqualTo("Mathematics");
            verify(teacherRepository, times(1)).findById(1L);
            verify(teacherRepository, times(1)).save(any(Teacher.class));
        }

        @Test
        @DisplayName("Should return null when updating non-existent teacher")
        void testUpdate_NotFound() {
            // Arrange
            Teacher updateRequest = new Teacher();
            updateRequest.setName("Updated Name");

            when(teacherRepository.findById(999L)).thenReturn(Optional.empty());

            // Act
            Teacher result = teacherService.update(999L, updateRequest);

            // Assert
            assertThat(result).isNull();
            verify(teacherRepository, times(1)).findById(999L);
            verify(teacherRepository, never()).save(any(Teacher.class));
        }

        @Test
        @DisplayName("Should preserve ID during update")
        void testUpdate_PreservesId() {
            // Arrange
            Teacher updateRequest = new Teacher();
            updateRequest.setName("Updated");
            updateRequest.setEmail("updated@university.edu");
            updateRequest.setPhone("+0000000000");
            updateRequest.setSpecialization("Updated Spec");
            updateRequest.setDepartment(testDepartment);

            when(teacherRepository.findById(1L)).thenReturn(Optional.of(testTeacher));
            when(teacherRepository.save(any(Teacher.class))).thenAnswer(inv -> inv.getArgument(0));

            // Act
            Teacher result = teacherService.update(1L, updateRequest);

            // Assert
            assertThat(result).isNotNull();
            assertThat(result.getId()).isEqualTo(1L);
        }

        @Test
        @DisplayName("Should update with null optional fields")
        void testUpdate_NullOptionalFields() {
            // Arrange
            Teacher updateRequest = new Teacher();
            updateRequest.setName("Updated Name");
            updateRequest.setEmail(null);
            updateRequest.setPhone(null);
            updateRequest.setSpecialization(null);
            updateRequest.setDepartment(null);

            when(teacherRepository.findById(1L)).thenReturn(Optional.of(testTeacher));
            when(teacherRepository.save(any(Teacher.class))).thenAnswer(inv -> inv.getArgument(0));

            // Act
            Teacher result = teacherService.update(1L, updateRequest);

            // Assert
            assertThat(result).isNotNull();
            assertThat(result.getName()).isEqualTo("Updated Name");
            assertThat(result.getEmail()).isNull();
            assertThat(result.getPhone()).isNull();
            assertThat(result.getSpecialization()).isNull();
            assertThat(result.getDepartment()).isNull();
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
            doNothing().when(teacherRepository).deleteById(1L);

            // Act
            teacherService.delete(1L);

            // Assert
            verify(teacherRepository, times(1)).deleteById(1L);
        }

        @Test
        @DisplayName("Should delegate delete to repository")
        void testDelete_DelegatesToRepository() {
            // Arrange
            doNothing().when(teacherRepository).deleteById(anyLong());

            // Act
            teacherService.delete(5L);

            // Assert
            verify(teacherRepository, times(1)).deleteById(5L);
            verifyNoMoreInteractions(teacherRepository);
        }

        @Test
        @DisplayName("Should propagate exception when deleting non-existent teacher")
        void testDelete_NonExistent_ThrowsException() {
            // Arrange
            doThrow(new org.springframework.dao.EmptyResultDataAccessException(1))
                    .when(teacherRepository).deleteById(999L);

            // Act & Assert
            org.junit.jupiter.api.Assertions.assertThrows(
                    org.springframework.dao.EmptyResultDataAccessException.class,
                    () -> teacherService.delete(999L)
            );
            verify(teacherRepository, times(1)).deleteById(999L);
        }
    }
}
