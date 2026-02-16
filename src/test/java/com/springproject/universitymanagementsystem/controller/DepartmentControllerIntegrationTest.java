package com.springproject.universitymanagementsystem.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springproject.universitymanagementsystem.entity.Department;
import com.springproject.universitymanagementsystem.service.DepartmentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for DepartmentController
 * Uses @SpringBootTest and @AutoConfigureMockMvc
 * Tests HTTP status codes, request/response bodies, validation, and security
 * Follows AAA (Arrange-Act-Assert) pattern
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@DisplayName("DepartmentController Integration Tests")
class DepartmentControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private DepartmentService departmentService;

    private Department testDepartment;

    @BeforeEach
    void setUp() {
        testDepartment = new Department();
        testDepartment.setId(1L);
        testDepartment.setName("Computer Science");
        testDepartment.setDescription("Department of Computer Science and Engineering");
    }

    // ===== GET /api/departments Tests =====

    @Nested
    @DisplayName("GET /api/departments")
    class GetAllDepartmentsTests {

        @Test
        @WithMockUser(roles = "TEACHER")
        @DisplayName("Should return all departments with 200 OK")
        void testGetAllDepartments_AsTeacher_Success() throws Exception {
            // Arrange
            Department dept2 = new Department();
            dept2.setId(2L);
            dept2.setName("Mathematics");
            dept2.setDescription("Math Department");

            List<Department> departments = Arrays.asList(testDepartment, dept2);
            when(departmentService.findAll()).thenReturn(departments);

            // Act & Assert
            mockMvc.perform(get("/api/departments")
                    .contentType(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$", hasSize(2)))
                    .andExpect(jsonPath("$[0].id", is(1)))
                    .andExpect(jsonPath("$[0].name", is("Computer Science")))
                    .andExpect(jsonPath("$[0].description",
                            is("Department of Computer Science and Engineering")))
                    .andExpect(jsonPath("$[1].id", is(2)))
                    .andExpect(jsonPath("$[1].name", is("Mathematics")));
        }

        @Test
        @WithMockUser(roles = "STUDENT")
        @DisplayName("Should return all departments as STUDENT with 200 OK")
        void testGetAllDepartments_AsStudent_Success() throws Exception {
            // Arrange
            when(departmentService.findAll()).thenReturn(Collections.singletonList(testDepartment));

            // Act & Assert
            mockMvc.perform(get("/api/departments"))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(1)))
                    .andExpect(jsonPath("$[0].name", is("Computer Science")));
        }

        @Test
        @WithMockUser(roles = "TEACHER")
        @DisplayName("Should return empty array when no departments exist")
        void testGetAllDepartments_EmptyList() throws Exception {
            // Arrange
            when(departmentService.findAll()).thenReturn(Collections.emptyList());

            // Act & Assert
            mockMvc.perform(get("/api/departments"))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(0)));
        }

        @Test
        @DisplayName("Should return 401 when not authenticated")
        void testGetAllDepartments_Unauthorized() throws Exception {
            // Act & Assert
            mockMvc.perform(get("/api/departments"))
                    .andDo(print())
                    .andExpect(status().isUnauthorized());
        }
    }

    // ===== GET /api/departments/{id} Tests =====

    @Nested
    @DisplayName("GET /api/departments/{id}")
    class GetDepartmentByIdTests {

        @Test
        @WithMockUser(roles = "TEACHER")
        @DisplayName("Should return department by ID with 200 OK")
        void testGetDepartmentById_Success() throws Exception {
            // Arrange
            when(departmentService.findById(1L)).thenReturn(testDepartment);

            // Act & Assert
            mockMvc.perform(get("/api/departments/{id}", 1L))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.id", is(1)))
                    .andExpect(jsonPath("$.name", is("Computer Science")))
                    .andExpect(jsonPath("$.description",
                            is("Department of Computer Science and Engineering")));
        }

        @Test
        @WithMockUser(roles = "STUDENT")
        @DisplayName("Should return department by ID as STUDENT with 200 OK")
        void testGetDepartmentById_AsStudent_Success() throws Exception {
            // Arrange
            when(departmentService.findById(1L)).thenReturn(testDepartment);

            // Act & Assert
            mockMvc.perform(get("/api/departments/{id}", 1L))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.name", is("Computer Science")));
        }

        @Test
        @WithMockUser(roles = "TEACHER")
        @DisplayName("Should return 404 when department not found")
        void testGetDepartmentById_NotFound() throws Exception {
            // Arrange
            when(departmentService.findById(999L)).thenReturn(null);

            // Act & Assert
            mockMvc.perform(get("/api/departments/{id}", 999L))
                    .andDo(print())
                    .andExpect(status().isNotFound());
        }

        @Test
        @DisplayName("Should return 401 when not authenticated")
        void testGetDepartmentById_Unauthorized() throws Exception {
            // Act & Assert
            mockMvc.perform(get("/api/departments/{id}", 1L))
                    .andDo(print())
                    .andExpect(status().isUnauthorized());
        }
    }

    // ===== POST /api/departments Tests =====

    @Nested
    @DisplayName("POST /api/departments")
    class CreateDepartmentTests {

        @Test
        @WithMockUser(roles = "TEACHER")
        @DisplayName("Should create department with TEACHER role")
        void testCreateDepartment_AsTeacher_Success() throws Exception {
            // Arrange
            Department newDept = new Department();
            newDept.setId(3L);
            newDept.setName("Physics");
            newDept.setDescription("Physics Department");

            when(departmentService.save(any(Department.class))).thenReturn(newDept);

            // Act & Assert
            mockMvc.perform(post("/api/departments")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(newDept)))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id", is(3)))
                    .andExpect(jsonPath("$.name", is("Physics")))
                    .andExpect(jsonPath("$.description", is("Physics Department")));
        }

        @Test
        @WithMockUser(roles = "STUDENT")
        @DisplayName("Should return 403 when STUDENT tries to create department")
        void testCreateDepartment_AsStudent_Forbidden() throws Exception {
            // Arrange
            Department newDept = new Department();
            newDept.setName("Unauthorized Dept");

            // Act & Assert
            mockMvc.perform(post("/api/departments")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(newDept)))
                    .andDo(print())
                    .andExpect(status().isForbidden());
        }

        @Test
        @DisplayName("Should return 401 when not authenticated")
        void testCreateDepartment_Unauthorized() throws Exception {
            // Arrange
            Department newDept = new Department();
            newDept.setName("Unauthorized Dept");

            // Act & Assert
            mockMvc.perform(post("/api/departments")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(newDept)))
                    .andDo(print())
                    .andExpect(status().isUnauthorized());
        }

        @Test
        @WithMockUser(roles = "TEACHER")
        @DisplayName("Should create department with null description")
        void testCreateDepartment_NullDescription() throws Exception {
            // Arrange
            Department newDept = new Department();
            newDept.setId(4L);
            newDept.setName("Chemistry");
            newDept.setDescription(null);

            when(departmentService.save(any(Department.class))).thenReturn(newDept);

            // Act & Assert
            mockMvc.perform(post("/api/departments")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(newDept)))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.name", is("Chemistry")))
                    .andExpect(jsonPath("$.description").doesNotExist());
        }
    }

    // ===== PUT /api/departments/{id} Tests =====

    @Nested
    @DisplayName("PUT /api/departments/{id}")
    class UpdateDepartmentTests {

        @Test
        @WithMockUser(roles = "TEACHER")
        @DisplayName("Should update department as TEACHER")
        void testUpdateDepartment_AsTeacher_Success() throws Exception {
            // Arrange
            Department updatedDept = new Department();
            updatedDept.setId(1L);
            updatedDept.setName("Computer Science & Engineering");
            updatedDept.setDescription("Updated CS Department");

            when(departmentService.update(anyLong(), any(Department.class)))
                    .thenReturn(updatedDept);

            // Act & Assert
            mockMvc.perform(put("/api/departments/{id}", 1L)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(updatedDept)))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.name", is("Computer Science & Engineering")))
                    .andExpect(jsonPath("$.description", is("Updated CS Department")));
        }

        @Test
        @WithMockUser(roles = "TEACHER")
        @DisplayName("Should return 404 when updating non-existent department")
        void testUpdateDepartment_NotFound() throws Exception {
            // Arrange
            Department updateRequest = new Department();
            updateRequest.setName("Non-existent");

            when(departmentService.update(anyLong(), any(Department.class))).thenReturn(null);

            // Act & Assert
            mockMvc.perform(put("/api/departments/{id}", 999L)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(updateRequest)))
                    .andDo(print())
                    .andExpect(status().isNotFound());
        }

        @Test
        @WithMockUser(roles = "STUDENT")
        @DisplayName("Should return 403 when STUDENT tries to update department")
        void testUpdateDepartment_AsStudent_Forbidden() throws Exception {
            // Arrange
            Department updateRequest = new Department();
            updateRequest.setName("Updated Name");

            // Act & Assert
            mockMvc.perform(put("/api/departments/{id}", 1L)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(updateRequest)))
                    .andDo(print())
                    .andExpect(status().isForbidden());
        }

        @Test
        @DisplayName("Should return 401 when not authenticated")
        void testUpdateDepartment_Unauthorized() throws Exception {
            // Arrange
            Department updateRequest = new Department();
            updateRequest.setName("Updated Name");

            // Act & Assert
            mockMvc.perform(put("/api/departments/{id}", 1L)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(updateRequest)))
                    .andDo(print())
                    .andExpect(status().isUnauthorized());
        }
    }

    // ===== DELETE /api/departments/{id} Tests =====

    @Nested
    @DisplayName("DELETE /api/departments/{id}")
    class DeleteDepartmentTests {

        @Test
        @WithMockUser(roles = "TEACHER")
        @DisplayName("Should delete department as TEACHER")
        void testDeleteDepartment_AsTeacher_Success() throws Exception {
            // Arrange
            doNothing().when(departmentService).delete(1L);

            // Act & Assert
            mockMvc.perform(delete("/api/departments/{id}", 1L))
                    .andDo(print())
                    .andExpect(status().isNoContent());
        }

        @Test
        @WithMockUser(roles = "STUDENT")
        @DisplayName("Should return 403 when STUDENT tries to delete department")
        void testDeleteDepartment_AsStudent_Forbidden() throws Exception {
            // Act & Assert
            mockMvc.perform(delete("/api/departments/{id}", 1L))
                    .andDo(print())
                    .andExpect(status().isForbidden());
        }

        @Test
        @DisplayName("Should return 401 when not authenticated")
        void testDeleteDepartment_Unauthorized() throws Exception {
            // Act & Assert
            mockMvc.perform(delete("/api/departments/{id}", 1L))
                    .andDo(print())
                    .andExpect(status().isUnauthorized());
        }
    }

    // ===== Response Format Tests =====

    @Nested
    @DisplayName("Response Format Tests")
    class ResponseFormatTests {

        @Test
        @WithMockUser(roles = "TEACHER")
        @DisplayName("Should return JSON content type")
        void testResponseContentType_JSON() throws Exception {
            // Arrange
            when(departmentService.findAll()).thenReturn(Collections.singletonList(testDepartment));

            // Act & Assert
            mockMvc.perform(get("/api/departments")
                    .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON));
        }

        @Test
        @WithMockUser(roles = "TEACHER")
        @DisplayName("Should return correct JSON structure for single department")
        void testResponseStructure_SingleDepartment() throws Exception {
            // Arrange
            when(departmentService.findById(1L)).thenReturn(testDepartment);

            // Act & Assert
            mockMvc.perform(get("/api/departments/{id}", 1L))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").exists())
                    .andExpect(jsonPath("$.name").exists())
                    .andExpect(jsonPath("$.description").exists())
                    .andExpect(jsonPath("$.id").isNumber())
                    .andExpect(jsonPath("$.name").isString())
                    .andExpect(jsonPath("$.description").isString());
        }

        @Test
        @WithMockUser(roles = "TEACHER")
        @DisplayName("Should return correct JSON array for list endpoint")
        void testResponseStructure_DepartmentList() throws Exception {
            // Arrange
            when(departmentService.findAll()).thenReturn(
                    Arrays.asList(testDepartment, new Department(2L, "Math", "Math Dept")));

            // Act & Assert
            mockMvc.perform(get("/api/departments"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$").isArray())
                    .andExpect(jsonPath("$", hasSize(2)));
        }
    }
}
