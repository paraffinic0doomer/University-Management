package com.springproject.universitymanagementsystem.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springproject.universitymanagementsystem.entity.Department;
import com.springproject.universitymanagementsystem.entity.Teacher;
import com.springproject.universitymanagementsystem.service.TeacherService;
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
 * Integration tests for TeacherController
 * Uses @SpringBootTest and @AutoConfigureMockMvc
 * Tests HTTP status codes, request/response bodies, validation, and security
 * Follows AAA (Arrange-Act-Assert) pattern
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@DisplayName("TeacherController Integration Tests")
class TeacherControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private TeacherService teacherService;

    private Teacher testTeacher;
    private Department testDepartment;

    @BeforeEach
    void setUp() {
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

    // ===== GET /api/teachers Tests =====

    @Nested
    @DisplayName("GET /api/teachers")
    class GetAllTeachersTests {

        @Test
        @WithMockUser(roles = "TEACHER")
        @DisplayName("Should return all teachers with 200 OK")
        void testGetAllTeachers_AsTeacher_Success() throws Exception {
            // Arrange
            Teacher teacher2 = new Teacher();
            teacher2.setId(2L);
            teacher2.setName("Dr. Jane Doe");
            teacher2.setEmail("jane.doe@university.edu");
            teacher2.setSpecialization("Data Science");

            List<Teacher> teachers = Arrays.asList(testTeacher, teacher2);
            when(teacherService.findAll()).thenReturn(teachers);

            // Act & Assert
            mockMvc.perform(get("/api/teachers")
                    .contentType(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$", hasSize(2)))
                    .andExpect(jsonPath("$[0].id", is(1)))
                    .andExpect(jsonPath("$[0].name", is("Dr. John Smith")))
                    .andExpect(jsonPath("$[0].email", is("john.smith@university.edu")))
                    .andExpect(jsonPath("$[0].specialization", is("Software Engineering")))
                    .andExpect(jsonPath("$[1].id", is(2)))
                    .andExpect(jsonPath("$[1].name", is("Dr. Jane Doe")));
        }

        @Test
        @WithMockUser(roles = "STUDENT")
        @DisplayName("Should return all teachers as STUDENT with 200 OK")
        void testGetAllTeachers_AsStudent_Success() throws Exception {
            // Arrange
            when(teacherService.findAll()).thenReturn(Collections.singletonList(testTeacher));

            // Act & Assert
            mockMvc.perform(get("/api/teachers"))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(1)))
                    .andExpect(jsonPath("$[0].name", is("Dr. John Smith")));
        }

        @Test
        @WithMockUser(roles = "TEACHER")
        @DisplayName("Should return empty array when no teachers exist")
        void testGetAllTeachers_EmptyList() throws Exception {
            // Arrange
            when(teacherService.findAll()).thenReturn(Collections.emptyList());

            // Act & Assert
            mockMvc.perform(get("/api/teachers"))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(0)));
        }

        @Test
        @DisplayName("Should return 401 when not authenticated")
        void testGetAllTeachers_Unauthorized() throws Exception {
            // Act & Assert
            mockMvc.perform(get("/api/teachers"))
                    .andDo(print())
                    .andExpect(status().isUnauthorized());
        }
    }

    // ===== GET /api/teachers/{id} Tests =====

    @Nested
    @DisplayName("GET /api/teachers/{id}")
    class GetTeacherByIdTests {

        @Test
        @WithMockUser(roles = "TEACHER")
        @DisplayName("Should return teacher by ID with 200 OK")
        void testGetTeacherById_Success() throws Exception {
            // Arrange
            when(teacherService.findById(1L)).thenReturn(testTeacher);

            // Act & Assert
            mockMvc.perform(get("/api/teachers/{id}", 1L))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.id", is(1)))
                    .andExpect(jsonPath("$.name", is("Dr. John Smith")))
                    .andExpect(jsonPath("$.email", is("john.smith@university.edu")))
                    .andExpect(jsonPath("$.phone", is("+1234567890")))
                    .andExpect(jsonPath("$.specialization", is("Software Engineering")));
        }

        @Test
        @WithMockUser(roles = "STUDENT")
        @DisplayName("Should return teacher by ID as STUDENT with 200 OK")
        void testGetTeacherById_AsStudent_Success() throws Exception {
            // Arrange
            when(teacherService.findById(1L)).thenReturn(testTeacher);

            // Act & Assert
            mockMvc.perform(get("/api/teachers/{id}", 1L))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.name", is("Dr. John Smith")));
        }

        @Test
        @WithMockUser(roles = "TEACHER")
        @DisplayName("Should return 404 when teacher not found")
        void testGetTeacherById_NotFound() throws Exception {
            // Arrange
            when(teacherService.findById(999L)).thenReturn(null);

            // Act & Assert
            mockMvc.perform(get("/api/teachers/{id}", 999L))
                    .andDo(print())
                    .andExpect(status().isNotFound());
        }

        @Test
        @DisplayName("Should return 401 when not authenticated")
        void testGetTeacherById_Unauthorized() throws Exception {
            // Act & Assert
            mockMvc.perform(get("/api/teachers/{id}", 1L))
                    .andDo(print())
                    .andExpect(status().isUnauthorized());
        }
    }

    // ===== POST /api/teachers Tests =====

    @Nested
    @DisplayName("POST /api/teachers")
    class CreateTeacherTests {

        @Test
        @WithMockUser(roles = "TEACHER")
        @DisplayName("Should create teacher with TEACHER role")
        void testCreateTeacher_AsTeacher_Success() throws Exception {
            // Arrange
            Teacher newTeacher = new Teacher();
            newTeacher.setId(3L);
            newTeacher.setName("Dr. Alice Brown");
            newTeacher.setEmail("alice.brown@university.edu");
            newTeacher.setSpecialization("Machine Learning");

            when(teacherService.save(any(Teacher.class))).thenReturn(newTeacher);

            // Act & Assert
            mockMvc.perform(post("/api/teachers")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(newTeacher)))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id", is(3)))
                    .andExpect(jsonPath("$.name", is("Dr. Alice Brown")))
                    .andExpect(jsonPath("$.email", is("alice.brown@university.edu")))
                    .andExpect(jsonPath("$.specialization", is("Machine Learning")));
        }

        @Test
        @WithMockUser(roles = "STUDENT")
        @DisplayName("Should return 403 when STUDENT tries to create teacher")
        void testCreateTeacher_AsStudent_Forbidden() throws Exception {
            // Arrange
            Teacher newTeacher = new Teacher();
            newTeacher.setName("Unauthorized Teacher");

            // Act & Assert
            mockMvc.perform(post("/api/teachers")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(newTeacher)))
                    .andDo(print())
                    .andExpect(status().isForbidden());
        }

        @Test
        @DisplayName("Should return 401 when not authenticated")
        void testCreateTeacher_Unauthorized() throws Exception {
            // Arrange
            Teacher newTeacher = new Teacher();
            newTeacher.setName("Unauthorized Teacher");

            // Act & Assert
            mockMvc.perform(post("/api/teachers")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(newTeacher)))
                    .andDo(print())
                    .andExpect(status().isUnauthorized());
        }

        @Test
        @WithMockUser(roles = "TEACHER")
        @DisplayName("Should create teacher with minimal data")
        void testCreateTeacher_MinimalData() throws Exception {
            // Arrange
            Teacher newTeacher = new Teacher();
            newTeacher.setId(5L);
            newTeacher.setName("Dr. Minimal");

            when(teacherService.save(any(Teacher.class))).thenReturn(newTeacher);

            // Act & Assert
            mockMvc.perform(post("/api/teachers")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(newTeacher)))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.name", is("Dr. Minimal")))
                    .andExpect(jsonPath("$.email").doesNotExist());
        }
    }

    // ===== PUT /api/teachers/{id} Tests =====

    @Nested
    @DisplayName("PUT /api/teachers/{id}")
    class UpdateTeacherTests {

        @Test
        @WithMockUser(roles = "TEACHER")
        @DisplayName("Should update teacher as TEACHER")
        void testUpdateTeacher_AsTeacher_Success() throws Exception {
            // Arrange
            Teacher updatedTeacher = new Teacher();
            updatedTeacher.setId(1L);
            updatedTeacher.setName("Dr. John Smith Jr.");
            updatedTeacher.setEmail("john.jr@university.edu");
            updatedTeacher.setSpecialization("Machine Learning");

            when(teacherService.update(anyLong(), any(Teacher.class)))
                    .thenReturn(updatedTeacher);

            // Act & Assert
            mockMvc.perform(put("/api/teachers/{id}", 1L)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(updatedTeacher)))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.name", is("Dr. John Smith Jr.")))
                    .andExpect(jsonPath("$.email", is("john.jr@university.edu")))
                    .andExpect(jsonPath("$.specialization", is("Machine Learning")));
        }

        @Test
        @WithMockUser(roles = "TEACHER")
        @DisplayName("Should return 404 when updating non-existent teacher")
        void testUpdateTeacher_NotFound() throws Exception {
            // Arrange
            Teacher updateRequest = new Teacher();
            updateRequest.setName("Non-existent");

            when(teacherService.update(anyLong(), any(Teacher.class))).thenReturn(null);

            // Act & Assert
            mockMvc.perform(put("/api/teachers/{id}", 999L)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(updateRequest)))
                    .andDo(print())
                    .andExpect(status().isNotFound());
        }

        @Test
        @WithMockUser(roles = "STUDENT")
        @DisplayName("Should return 403 when STUDENT tries to update teacher")
        void testUpdateTeacher_AsStudent_Forbidden() throws Exception {
            // Arrange
            Teacher updateRequest = new Teacher();
            updateRequest.setName("Updated Name");

            // Act & Assert
            mockMvc.perform(put("/api/teachers/{id}", 1L)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(updateRequest)))
                    .andDo(print())
                    .andExpect(status().isForbidden());
        }

        @Test
        @DisplayName("Should return 401 when not authenticated")
        void testUpdateTeacher_Unauthorized() throws Exception {
            // Arrange
            Teacher updateRequest = new Teacher();
            updateRequest.setName("Updated Name");

            // Act & Assert
            mockMvc.perform(put("/api/teachers/{id}", 1L)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(updateRequest)))
                    .andDo(print())
                    .andExpect(status().isUnauthorized());
        }
    }

    // ===== DELETE /api/teachers/{id} Tests =====

    @Nested
    @DisplayName("DELETE /api/teachers/{id}")
    class DeleteTeacherTests {

        @Test
        @WithMockUser(roles = "TEACHER")
        @DisplayName("Should delete teacher as TEACHER")
        void testDeleteTeacher_AsTeacher_Success() throws Exception {
            // Arrange
            doNothing().when(teacherService).delete(1L);

            // Act & Assert
            mockMvc.perform(delete("/api/teachers/{id}", 1L))
                    .andDo(print())
                    .andExpect(status().isNoContent());
        }

        @Test
        @WithMockUser(roles = "STUDENT")
        @DisplayName("Should return 403 when STUDENT tries to delete teacher")
        void testDeleteTeacher_AsStudent_Forbidden() throws Exception {
            // Act & Assert
            mockMvc.perform(delete("/api/teachers/{id}", 1L))
                    .andDo(print())
                    .andExpect(status().isForbidden());
        }

        @Test
        @DisplayName("Should return 401 when not authenticated")
        void testDeleteTeacher_Unauthorized() throws Exception {
            // Act & Assert
            mockMvc.perform(delete("/api/teachers/{id}", 1L))
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
            when(teacherService.findAll()).thenReturn(Collections.singletonList(testTeacher));

            // Act & Assert
            mockMvc.perform(get("/api/teachers")
                    .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON));
        }

        @Test
        @WithMockUser(roles = "TEACHER")
        @DisplayName("Should return correct JSON structure for single teacher")
        void testResponseStructure_SingleTeacher() throws Exception {
            // Arrange
            when(teacherService.findById(1L)).thenReturn(testTeacher);

            // Act & Assert
            mockMvc.perform(get("/api/teachers/{id}", 1L))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").exists())
                    .andExpect(jsonPath("$.name").exists())
                    .andExpect(jsonPath("$.email").exists())
                    .andExpect(jsonPath("$.phone").exists())
                    .andExpect(jsonPath("$.specialization").exists())
                    .andExpect(jsonPath("$.id").isNumber())
                    .andExpect(jsonPath("$.name").isString());
        }
    }
}
