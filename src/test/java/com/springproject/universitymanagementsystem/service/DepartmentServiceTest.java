package com.springproject.universitymanagementsystem.service;

import com.springproject.universitymanagementsystem.entity.Department;
import com.springproject.universitymanagementsystem.repository.DepartmentRepository;
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
 * Unit tests for DepartmentService
 * Uses Mockito to mock the repository layer
 * Follows AAA (Arrange-Act-Assert) pattern
 * Covers: success cases, failure cases, edge cases, exception handling
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("DepartmentService Unit Tests")
class DepartmentServiceTest {

    @Mock
    private DepartmentRepository departmentRepository;

    @InjectMocks
    private DepartmentService departmentService;

    private Department testDepartment;

    @BeforeEach
    void setUp() {
        // Arrange - Set up test data
        testDepartment = new Department();
        testDepartment.setId(1L);
        testDepartment.setName("Computer Science");
        testDepartment.setDescription("Department of Computer Science and Engineering");
    }

    // ===== findAll() Tests =====

    @Nested
    @DisplayName("findAll() Tests")
    class FindAllTests {

        @Test
        @DisplayName("Should return all departments successfully")
        void testFindAll_Success() {
            // Arrange
            Department dept2 = new Department();
            dept2.setId(2L);
            dept2.setName("Mathematics");
            dept2.setDescription("Math Department");

            List<Department> expectedDepartments = Arrays.asList(testDepartment, dept2);
            when(departmentRepository.findAll()).thenReturn(expectedDepartments);

            // Act
            List<Department> actualDepartments = departmentService.findAll();

            // Assert
            assertThat(actualDepartments).isNotNull();
            assertThat(actualDepartments).hasSize(2);
            assertThat(actualDepartments).containsExactlyInAnyOrder(testDepartment, dept2);
            verify(departmentRepository, times(1)).findAll();
        }

        @Test
        @DisplayName("Should return empty list when no departments exist")
        void testFindAll_EmptyList() {
            // Arrange
            when(departmentRepository.findAll()).thenReturn(Collections.emptyList());

            // Act
            List<Department> actualDepartments = departmentService.findAll();

            // Assert
            assertThat(actualDepartments).isNotNull();
            assertThat(actualDepartments).isEmpty();
            verify(departmentRepository, times(1)).findAll();
        }

        @Test
        @DisplayName("Should return single department list")
        void testFindAll_SingleDepartment() {
            // Arrange
            when(departmentRepository.findAll()).thenReturn(Collections.singletonList(testDepartment));

            // Act
            List<Department> actualDepartments = departmentService.findAll();

            // Assert
            assertThat(actualDepartments).hasSize(1);
            assertThat(actualDepartments.get(0).getName()).isEqualTo("Computer Science");
            verify(departmentRepository, times(1)).findAll();
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
            when(departmentRepository.findById(1L)).thenReturn(Optional.of(testDepartment));

            // Act
            Department foundDept = departmentService.findById(1L);

            // Assert
            assertThat(foundDept).isNotNull();
            assertThat(foundDept.getId()).isEqualTo(1L);
            assertThat(foundDept.getName()).isEqualTo("Computer Science");
            assertThat(foundDept.getDescription())
                    .isEqualTo("Department of Computer Science and Engineering");
            verify(departmentRepository, times(1)).findById(1L);
        }

        @Test
        @DisplayName("Should return null when department not found by ID")
        void testFindById_NotFound() {
            // Arrange
            when(departmentRepository.findById(999L)).thenReturn(Optional.empty());

            // Act
            Department foundDept = departmentService.findById(999L);

            // Assert
            assertThat(foundDept).isNull();
            verify(departmentRepository, times(1)).findById(999L);
        }

        @Test
        @DisplayName("Should handle findById with different IDs correctly")
        void testFindById_DifferentIds() {
            // Arrange
            Department dept2 = new Department(2L, "Mathematics", "Math Dept");
            when(departmentRepository.findById(1L)).thenReturn(Optional.of(testDepartment));
            when(departmentRepository.findById(2L)).thenReturn(Optional.of(dept2));

            // Act
            Department found1 = departmentService.findById(1L);
            Department found2 = departmentService.findById(2L);

            // Assert
            assertThat(found1.getName()).isEqualTo("Computer Science");
            assertThat(found2.getName()).isEqualTo("Mathematics");
        }
    }

    // ===== save() Tests =====

    @Nested
    @DisplayName("save() Tests")
    class SaveTests {

        @Test
        @DisplayName("Should save department successfully")
        void testSave_Success() {
            // Arrange
            Department newDept = new Department();
            newDept.setName("Physics");
            newDept.setDescription("Physics Department");

            Department savedDept = new Department(3L, "Physics", "Physics Department");
            when(departmentRepository.save(any(Department.class))).thenReturn(savedDept);

            // Act
            Department result = departmentService.save(newDept);

            // Assert
            assertThat(result).isNotNull();
            assertThat(result.getId()).isEqualTo(3L);
            assertThat(result.getName()).isEqualTo("Physics");
            assertThat(result.getDescription()).isEqualTo("Physics Department");
            verify(departmentRepository, times(1)).save(newDept);
        }

        @Test
        @DisplayName("Should save department with null description")
        void testSave_NullDescription() {
            // Arrange
            Department newDept = new Department();
            newDept.setName("Chemistry");
            newDept.setDescription(null);

            when(departmentRepository.save(any(Department.class))).thenReturn(newDept);

            // Act
            Department result = departmentService.save(newDept);

            // Assert
            assertThat(result).isNotNull();
            assertThat(result.getName()).isEqualTo("Chemistry");
            assertThat(result.getDescription()).isNull();
            verify(departmentRepository, times(1)).save(newDept);
        }

        @Test
        @DisplayName("Should delegate save to repository")
        void testSave_DelegatesToRepository() {
            // Arrange
            when(departmentRepository.save(any(Department.class))).thenReturn(testDepartment);

            // Act
            departmentService.save(testDepartment);

            // Assert
            verify(departmentRepository, times(1)).save(testDepartment);
            verifyNoMoreInteractions(departmentRepository);
        }
    }

    // ===== update() Tests =====

    @Nested
    @DisplayName("update() Tests")
    class UpdateTests {

        @Test
        @DisplayName("Should update department successfully")
        void testUpdate_Success() {
            // Arrange
            Department updateRequest = new Department();
            updateRequest.setName("Computer Science & Engineering");
            updateRequest.setDescription("Updated CS Department");

            Department updatedDept = new Department();
            updatedDept.setId(1L);
            updatedDept.setName("Computer Science & Engineering");
            updatedDept.setDescription("Updated CS Department");

            when(departmentRepository.findById(1L)).thenReturn(Optional.of(testDepartment));
            when(departmentRepository.save(any(Department.class))).thenReturn(updatedDept);

            // Act
            Department result = departmentService.update(1L, updateRequest);

            // Assert
            assertThat(result).isNotNull();
            assertThat(result.getName()).isEqualTo("Computer Science & Engineering");
            assertThat(result.getDescription()).isEqualTo("Updated CS Department");
            verify(departmentRepository, times(1)).findById(1L);
            verify(departmentRepository, times(1)).save(any(Department.class));
        }

        @Test
        @DisplayName("Should return null when updating non-existent department")
        void testUpdate_NotFound() {
            // Arrange
            Department updateRequest = new Department();
            updateRequest.setName("Updated Name");

            when(departmentRepository.findById(999L)).thenReturn(Optional.empty());

            // Act
            Department result = departmentService.update(999L, updateRequest);

            // Assert
            assertThat(result).isNull();
            verify(departmentRepository, times(1)).findById(999L);
            verify(departmentRepository, never()).save(any(Department.class));
        }

        @Test
        @DisplayName("Should update only name when description is null")
        void testUpdate_PartialUpdate_NullDescription() {
            // Arrange
            Department updateRequest = new Department();
            updateRequest.setName("New Name");
            updateRequest.setDescription(null);

            when(departmentRepository.findById(1L)).thenReturn(Optional.of(testDepartment));
            when(departmentRepository.save(any(Department.class))).thenAnswer(inv -> inv.getArgument(0));

            // Act
            Department result = departmentService.update(1L, updateRequest);

            // Assert
            assertThat(result).isNotNull();
            assertThat(result.getName()).isEqualTo("New Name");
            assertThat(result.getDescription()).isNull();
        }

        @Test
        @DisplayName("Should preserve ID during update")
        void testUpdate_PreservesId() {
            // Arrange
            Department updateRequest = new Department();
            updateRequest.setName("Updated");
            updateRequest.setDescription("Updated Desc");

            when(departmentRepository.findById(1L)).thenReturn(Optional.of(testDepartment));
            when(departmentRepository.save(any(Department.class))).thenAnswer(inv -> inv.getArgument(0));

            // Act
            Department result = departmentService.update(1L, updateRequest);

            // Assert
            assertThat(result).isNotNull();
            assertThat(result.getId()).isEqualTo(1L);
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
            doNothing().when(departmentRepository).deleteById(1L);

            // Act
            departmentService.delete(1L);

            // Assert
            verify(departmentRepository, times(1)).deleteById(1L);
        }

        @Test
        @DisplayName("Should delegate delete to repository")
        void testDelete_DelegatesToRepository() {
            // Arrange
            doNothing().when(departmentRepository).deleteById(anyLong());

            // Act
            departmentService.delete(5L);

            // Assert
            verify(departmentRepository, times(1)).deleteById(5L);
            verifyNoMoreInteractions(departmentRepository);
        }

        @Test
        @DisplayName("Should propagate exception when deleting non-existent department")
        void testDelete_NonExistent_ThrowsException() {
            // Arrange
            doThrow(new org.springframework.dao.EmptyResultDataAccessException(1))
                    .when(departmentRepository).deleteById(999L);

            // Act & Assert
            org.junit.jupiter.api.Assertions.assertThrows(
                    org.springframework.dao.EmptyResultDataAccessException.class,
                    () -> departmentService.delete(999L)
            );
            verify(departmentRepository, times(1)).deleteById(999L);
        }
    }
}
