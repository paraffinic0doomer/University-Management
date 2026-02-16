package com.springproject.universitymanagementsystem.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Entity tests for Teacher
 * Tests getters, setters, constructors, null handling, and relationship mappings
 * Follows AAA (Arrange-Act-Assert) pattern
 */
@DisplayName("Teacher Entity Tests")
class TeacherTest {

    private Teacher teacher;
    private Department department;
    private Course course1;
    private Course course2;
    private User user;

    @BeforeEach
    void setUp() {
        department = new Department();
        department.setId(1L);
        department.setName("Computer Science");
        department.setDescription("CS Department");

        course1 = new Course();
        course1.setId(1L);
        course1.setName("Data Structures");
        course1.setCode("CS201");

        course2 = new Course();
        course2.setId(2L);
        course2.setName("Algorithms");
        course2.setCode("CS301");

        user = new User();
        user.setId(1L);
        user.setUsername("dr.smith");
        user.setRole(User.Role.TEACHER);

        teacher = new Teacher();
        teacher.setId(1L);
        teacher.setName("Dr. John Smith");
        teacher.setEmail("john.smith@university.edu");
        teacher.setPhone("+1234567890");
        teacher.setSpecialization("Software Engineering");
        teacher.setDepartment(department);
        teacher.setCourses(Arrays.asList(course1, course2));
        teacher.setUser(user);
    }

    // ===== Getter/Setter Tests =====

    @Nested
    @DisplayName("Getter and Setter Tests")
    class GetterSetterTests {

        @Test
        @DisplayName("Should set and get ID correctly")
        void testIdGetterSetter() {
            // Arrange
            Teacher newTeacher = new Teacher();

            // Act
            newTeacher.setId(100L);

            // Assert
            assertThat(newTeacher.getId()).isEqualTo(100L);
        }

        @Test
        @DisplayName("Should set and get name correctly")
        void testNameGetterSetter() {
            // Arrange
            Teacher newTeacher = new Teacher();

            // Act
            newTeacher.setName("Dr. Jane Doe");

            // Assert
            assertThat(newTeacher.getName()).isEqualTo("Dr. Jane Doe");
        }

        @Test
        @DisplayName("Should set and get email correctly")
        void testEmailGetterSetter() {
            // Arrange
            Teacher newTeacher = new Teacher();

            // Act
            newTeacher.setEmail("jane.doe@university.edu");

            // Assert
            assertThat(newTeacher.getEmail()).isEqualTo("jane.doe@university.edu");
        }

        @Test
        @DisplayName("Should set and get phone correctly")
        void testPhoneGetterSetter() {
            // Arrange
            Teacher newTeacher = new Teacher();

            // Act
            newTeacher.setPhone("+9876543210");

            // Assert
            assertThat(newTeacher.getPhone()).isEqualTo("+9876543210");
        }

        @Test
        @DisplayName("Should set and get specialization correctly")
        void testSpecializationGetterSetter() {
            // Arrange
            Teacher newTeacher = new Teacher();

            // Act
            newTeacher.setSpecialization("Artificial Intelligence");

            // Assert
            assertThat(newTeacher.getSpecialization()).isEqualTo("Artificial Intelligence");
        }

        @Test
        @DisplayName("Should set and get department correctly")
        void testDepartmentGetterSetter() {
            // Arrange
            Teacher newTeacher = new Teacher();
            Department mathDept = new Department();
            mathDept.setId(2L);
            mathDept.setName("Mathematics");

            // Act
            newTeacher.setDepartment(mathDept);

            // Assert
            assertThat(newTeacher.getDepartment()).isNotNull();
            assertThat(newTeacher.getDepartment().getId()).isEqualTo(2L);
            assertThat(newTeacher.getDepartment().getName()).isEqualTo("Mathematics");
        }

        @Test
        @DisplayName("Should set and get courses correctly")
        void testCoursesGetterSetter() {
            // Arrange
            Teacher newTeacher = new Teacher();
            List<Course> courses = Arrays.asList(course1, course2);

            // Act
            newTeacher.setCourses(courses);

            // Assert
            assertThat(newTeacher.getCourses()).isNotNull();
            assertThat(newTeacher.getCourses()).hasSize(2);
            assertThat(newTeacher.getCourses()).containsExactlyInAnyOrder(course1, course2);
        }

        @Test
        @DisplayName("Should set and get user correctly")
        void testUserGetterSetter() {
            // Arrange
            Teacher newTeacher = new Teacher();
            User newUser = new User();
            newUser.setId(2L);
            newUser.setUsername("dr.doe");
            newUser.setRole(User.Role.TEACHER);

            // Act
            newTeacher.setUser(newUser);

            // Assert
            assertThat(newTeacher.getUser()).isNotNull();
            assertThat(newTeacher.getUser().getId()).isEqualTo(2L);
            assertThat(newTeacher.getUser().getUsername()).isEqualTo("dr.doe");
            assertThat(newTeacher.getUser().getRole()).isEqualTo(User.Role.TEACHER);
        }
    }

    // ===== Constructor Tests =====

    @Nested
    @DisplayName("Constructor Tests")
    class ConstructorTests {

        @Test
        @DisplayName("Should create teacher with no-args constructor")
        void testNoArgsConstructor() {
            // Act
            Teacher newTeacher = new Teacher();

            // Assert
            assertThat(newTeacher).isNotNull();
            assertThat(newTeacher.getId()).isNull();
            assertThat(newTeacher.getName()).isNull();
            assertThat(newTeacher.getEmail()).isNull();
            assertThat(newTeacher.getPhone()).isNull();
            assertThat(newTeacher.getSpecialization()).isNull();
            assertThat(newTeacher.getDepartment()).isNull();
            assertThat(newTeacher.getCourses()).isNull();
            assertThat(newTeacher.getUser()).isNull();
        }

        @Test
        @DisplayName("Should create teacher with parameterized constructor")
        void testParameterizedConstructor() {
            // Act
            Teacher newTeacher = new Teacher(10L, "Dr. Alice Brown",
                    "alice.brown@university.edu", "+5555555555", "Data Science");

            // Assert
            assertThat(newTeacher).isNotNull();
            assertThat(newTeacher.getId()).isEqualTo(10L);
            assertThat(newTeacher.getName()).isEqualTo("Dr. Alice Brown");
            assertThat(newTeacher.getEmail()).isEqualTo("alice.brown@university.edu");
            assertThat(newTeacher.getPhone()).isEqualTo("+5555555555");
            assertThat(newTeacher.getSpecialization()).isEqualTo("Data Science");
        }

        @Test
        @DisplayName("Should create teacher with parameterized constructor - null optional fields")
        void testParameterizedConstructor_NullOptionalFields() {
            // Act
            Teacher newTeacher = new Teacher(11L, "Dr. Bob", null, null, null);

            // Assert
            assertThat(newTeacher).isNotNull();
            assertThat(newTeacher.getId()).isEqualTo(11L);
            assertThat(newTeacher.getName()).isEqualTo("Dr. Bob");
            assertThat(newTeacher.getEmail()).isNull();
            assertThat(newTeacher.getPhone()).isNull();
            assertThat(newTeacher.getSpecialization()).isNull();
        }
    }

    // ===== Null Value Tests =====

    @Nested
    @DisplayName("Null Value Handling Tests")
    class NullValueTests {

        @Test
        @DisplayName("Should handle null email")
        void testNullEmail() {
            // Act
            teacher.setEmail(null);

            // Assert
            assertThat(teacher.getEmail()).isNull();
        }

        @Test
        @DisplayName("Should handle null phone")
        void testNullPhone() {
            // Act
            teacher.setPhone(null);

            // Assert
            assertThat(teacher.getPhone()).isNull();
        }

        @Test
        @DisplayName("Should handle null specialization")
        void testNullSpecialization() {
            // Act
            teacher.setSpecialization(null);

            // Assert
            assertThat(teacher.getSpecialization()).isNull();
        }

        @Test
        @DisplayName("Should handle null department")
        void testNullDepartment() {
            // Act
            teacher.setDepartment(null);

            // Assert
            assertThat(teacher.getDepartment()).isNull();
        }

        @Test
        @DisplayName("Should handle null courses list")
        void testNullCourses() {
            // Act
            teacher.setCourses(null);

            // Assert
            assertThat(teacher.getCourses()).isNull();
        }

        @Test
        @DisplayName("Should handle empty courses list")
        void testEmptyCourses() {
            // Act
            teacher.setCourses(Collections.emptyList());

            // Assert
            assertThat(teacher.getCourses()).isNotNull();
            assertThat(teacher.getCourses()).isEmpty();
        }

        @Test
        @DisplayName("Should handle null user")
        void testNullUser() {
            // Act
            teacher.setUser(null);

            // Assert
            assertThat(teacher.getUser()).isNull();
        }
    }

    // ===== Relationship Tests =====

    @Nested
    @DisplayName("Relationship Mapping Tests")
    class RelationshipTests {

        @Test
        @DisplayName("Should correctly map teacher with department")
        void testTeacherDepartmentRelationship() {
            // Assert
            assertThat(teacher.getDepartment()).isNotNull();
            assertThat(teacher.getDepartment().getName()).isEqualTo("Computer Science");
        }

        @Test
        @DisplayName("Should correctly map teacher with courses")
        void testTeacherCourseRelationship() {
            // Assert
            assertThat(teacher.getCourses()).hasSize(2);
            assertThat(teacher.getCourses())
                    .extracting(Course::getCode)
                    .containsExactlyInAnyOrder("CS201", "CS301");
        }

        @Test
        @DisplayName("Should correctly map teacher with user account")
        void testTeacherUserRelationship() {
            // Assert
            assertThat(teacher.getUser()).isNotNull();
            assertThat(teacher.getUser().getUsername()).isEqualTo("dr.smith");
            assertThat(teacher.getUser().getRole()).isEqualTo(User.Role.TEACHER);
        }

        @Test
        @DisplayName("Should support replacing department")
        void testReplaceDepartment() {
            // Arrange
            Department newDept = new Department(2L, "Physics", "Physics Dept");

            // Act
            teacher.setDepartment(newDept);

            // Assert
            assertThat(teacher.getDepartment().getName()).isEqualTo("Physics");
        }

        @Test
        @DisplayName("Should support replacing courses list")
        void testReplaceCoursesList() {
            // Arrange
            Course newCourse = new Course(3L, "Machine Learning", "CS501", 4);

            // Act
            teacher.setCourses(Collections.singletonList(newCourse));

            // Assert
            assertThat(teacher.getCourses()).hasSize(1);
            assertThat(teacher.getCourses().get(0).getName()).isEqualTo("Machine Learning");
        }
    }

    // ===== Fully Initialized Entity Tests =====

    @Test
    @DisplayName("Should correctly initialize all fields from setUp")
    void testFullyInitializedTeacher() {
        // Assert
        assertThat(teacher.getId()).isEqualTo(1L);
        assertThat(teacher.getName()).isEqualTo("Dr. John Smith");
        assertThat(teacher.getEmail()).isEqualTo("john.smith@university.edu");
        assertThat(teacher.getPhone()).isEqualTo("+1234567890");
        assertThat(teacher.getSpecialization()).isEqualTo("Software Engineering");
        assertThat(teacher.getDepartment()).isNotNull();
        assertThat(teacher.getCourses()).hasSize(2);
        assertThat(teacher.getUser()).isNotNull();
    }

    @Test
    @DisplayName("Should allow overwriting all fields")
    void testOverwriteAllFields() {
        // Act
        teacher.setId(99L);
        teacher.setName("Prof. Updated");
        teacher.setEmail("updated@university.edu");
        teacher.setPhone("+0000000000");
        teacher.setSpecialization("Quantum Computing");
        teacher.setDepartment(null);
        teacher.setCourses(Collections.emptyList());
        teacher.setUser(null);

        // Assert
        assertThat(teacher.getId()).isEqualTo(99L);
        assertThat(teacher.getName()).isEqualTo("Prof. Updated");
        assertThat(teacher.getEmail()).isEqualTo("updated@university.edu");
        assertThat(teacher.getPhone()).isEqualTo("+0000000000");
        assertThat(teacher.getSpecialization()).isEqualTo("Quantum Computing");
        assertThat(teacher.getDepartment()).isNull();
        assertThat(teacher.getCourses()).isEmpty();
        assertThat(teacher.getUser()).isNull();
    }
}
