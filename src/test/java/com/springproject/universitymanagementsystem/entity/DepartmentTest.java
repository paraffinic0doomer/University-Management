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
 * Entity tests for Department
 * Tests getters, setters, constructors, null handling, and relationship mappings
 * Follows AAA (Arrange-Act-Assert) pattern
 */
@DisplayName("Department Entity Tests")
class DepartmentTest {

    private Department department;
    private Student student1;
    private Student student2;
    private Teacher teacher;
    private Course course;

    @BeforeEach
    void setUp() {
        student1 = new Student();
        student1.setId(1L);
        student1.setRollNumber("CS2024001");
        student1.setName("John Doe");

        student2 = new Student();
        student2.setId(2L);
        student2.setRollNumber("CS2024002");
        student2.setName("Jane Smith");

        teacher = new Teacher();
        teacher.setId(1L);
        teacher.setName("Dr. Smith");
        teacher.setEmail("dr.smith@university.edu");

        course = new Course();
        course.setId(1L);
        course.setName("Data Structures");
        course.setCode("CS201");

        department = new Department();
        department.setId(1L);
        department.setName("Computer Science");
        department.setDescription("Department of Computer Science and Engineering");
        department.setStudents(Arrays.asList(student1, student2));
        department.setTeachers(Collections.singletonList(teacher));
        department.setCourses(Collections.singletonList(course));
    }

    // ===== Getter/Setter Tests =====

    @Nested
    @DisplayName("Getter and Setter Tests")
    class GetterSetterTests {

        @Test
        @DisplayName("Should set and get ID correctly")
        void testIdGetterSetter() {
            // Arrange
            Department newDept = new Department();

            // Act
            newDept.setId(100L);

            // Assert
            assertThat(newDept.getId()).isEqualTo(100L);
        }

        @Test
        @DisplayName("Should set and get name correctly")
        void testNameGetterSetter() {
            // Arrange
            Department newDept = new Department();

            // Act
            newDept.setName("Mathematics");

            // Assert
            assertThat(newDept.getName()).isEqualTo("Mathematics");
        }

        @Test
        @DisplayName("Should set and get description correctly")
        void testDescriptionGetterSetter() {
            // Arrange
            Department newDept = new Department();

            // Act
            newDept.setDescription("Department of Mathematics and Statistics");

            // Assert
            assertThat(newDept.getDescription()).isEqualTo("Department of Mathematics and Statistics");
        }

        @Test
        @DisplayName("Should set and get students correctly")
        void testStudentsGetterSetter() {
            // Arrange
            Department newDept = new Department();
            List<Student> students = Arrays.asList(student1, student2);

            // Act
            newDept.setStudents(students);

            // Assert
            assertThat(newDept.getStudents()).isNotNull();
            assertThat(newDept.getStudents()).hasSize(2);
            assertThat(newDept.getStudents()).containsExactlyInAnyOrder(student1, student2);
        }

        @Test
        @DisplayName("Should set and get teachers correctly")
        void testTeachersGetterSetter() {
            // Arrange
            Department newDept = new Department();
            List<Teacher> teachers = Collections.singletonList(teacher);

            // Act
            newDept.setTeachers(teachers);

            // Assert
            assertThat(newDept.getTeachers()).isNotNull();
            assertThat(newDept.getTeachers()).hasSize(1);
            assertThat(newDept.getTeachers().get(0).getName()).isEqualTo("Dr. Smith");
        }

        @Test
        @DisplayName("Should set and get courses correctly")
        void testCoursesGetterSetter() {
            // Arrange
            Department newDept = new Department();
            List<Course> courses = Collections.singletonList(course);

            // Act
            newDept.setCourses(courses);

            // Assert
            assertThat(newDept.getCourses()).isNotNull();
            assertThat(newDept.getCourses()).hasSize(1);
            assertThat(newDept.getCourses().get(0).getName()).isEqualTo("Data Structures");
        }
    }

    // ===== Constructor Tests =====

    @Nested
    @DisplayName("Constructor Tests")
    class ConstructorTests {

        @Test
        @DisplayName("Should create department with no-args constructor")
        void testNoArgsConstructor() {
            // Act
            Department newDept = new Department();

            // Assert
            assertThat(newDept).isNotNull();
            assertThat(newDept.getId()).isNull();
            assertThat(newDept.getName()).isNull();
            assertThat(newDept.getDescription()).isNull();
            assertThat(newDept.getStudents()).isNull();
            assertThat(newDept.getTeachers()).isNull();
            assertThat(newDept.getCourses()).isNull();
        }

        @Test
        @DisplayName("Should create department with parameterized constructor")
        void testParameterizedConstructor() {
            // Act
            Department newDept = new Department(10L, "Physics", "Department of Physics");

            // Assert
            assertThat(newDept).isNotNull();
            assertThat(newDept.getId()).isEqualTo(10L);
            assertThat(newDept.getName()).isEqualTo("Physics");
            assertThat(newDept.getDescription()).isEqualTo("Department of Physics");
        }

        @Test
        @DisplayName("Should create department with parameterized constructor - null description")
        void testParameterizedConstructor_NullDescription() {
            // Act
            Department newDept = new Department(11L, "Chemistry", null);

            // Assert
            assertThat(newDept).isNotNull();
            assertThat(newDept.getId()).isEqualTo(11L);
            assertThat(newDept.getName()).isEqualTo("Chemistry");
            assertThat(newDept.getDescription()).isNull();
        }
    }

    // ===== Null Value Tests =====

    @Nested
    @DisplayName("Null Value Handling Tests")
    class NullValueTests {

        @Test
        @DisplayName("Should handle null description")
        void testNullDescription() {
            // Act
            department.setDescription(null);

            // Assert
            assertThat(department.getDescription()).isNull();
        }

        @Test
        @DisplayName("Should handle null students list")
        void testNullStudents() {
            // Act
            department.setStudents(null);

            // Assert
            assertThat(department.getStudents()).isNull();
        }

        @Test
        @DisplayName("Should handle null teachers list")
        void testNullTeachers() {
            // Act
            department.setTeachers(null);

            // Assert
            assertThat(department.getTeachers()).isNull();
        }

        @Test
        @DisplayName("Should handle null courses list")
        void testNullCourses() {
            // Act
            department.setCourses(null);

            // Assert
            assertThat(department.getCourses()).isNull();
        }

        @Test
        @DisplayName("Should handle empty students list")
        void testEmptyStudents() {
            // Act
            department.setStudents(Collections.emptyList());

            // Assert
            assertThat(department.getStudents()).isNotNull();
            assertThat(department.getStudents()).isEmpty();
        }

        @Test
        @DisplayName("Should handle empty teachers list")
        void testEmptyTeachers() {
            // Act
            department.setTeachers(Collections.emptyList());

            // Assert
            assertThat(department.getTeachers()).isNotNull();
            assertThat(department.getTeachers()).isEmpty();
        }

        @Test
        @DisplayName("Should handle empty courses list")
        void testEmptyCourses() {
            // Act
            department.setCourses(Collections.emptyList());

            // Assert
            assertThat(department.getCourses()).isNotNull();
            assertThat(department.getCourses()).isEmpty();
        }
    }

    // ===== Relationship Tests =====

    @Nested
    @DisplayName("Relationship Mapping Tests")
    class RelationshipTests {

        @Test
        @DisplayName("Should correctly map department with students")
        void testDepartmentStudentRelationship() {
            // Assert
            assertThat(department.getStudents()).hasSize(2);
            assertThat(department.getStudents())
                    .extracting(Student::getRollNumber)
                    .containsExactlyInAnyOrder("CS2024001", "CS2024002");
        }

        @Test
        @DisplayName("Should correctly map department with teachers")
        void testDepartmentTeacherRelationship() {
            // Assert
            assertThat(department.getTeachers()).hasSize(1);
            assertThat(department.getTeachers().get(0).getName()).isEqualTo("Dr. Smith");
        }

        @Test
        @DisplayName("Should correctly map department with courses")
        void testDepartmentCourseRelationship() {
            // Assert
            assertThat(department.getCourses()).hasSize(1);
            assertThat(department.getCourses().get(0).getCode()).isEqualTo("CS201");
        }

        @Test
        @DisplayName("Should support replacing entire students list")
        void testReplaceStudentsList() {
            // Arrange
            Student newStudent = new Student();
            newStudent.setId(3L);
            newStudent.setRollNumber("CS2024003");
            newStudent.setName("Alice Johnson");

            // Act
            department.setStudents(Collections.singletonList(newStudent));

            // Assert
            assertThat(department.getStudents()).hasSize(1);
            assertThat(department.getStudents().get(0).getName()).isEqualTo("Alice Johnson");
        }
    }

    // ===== Fully Initialized Entity Tests =====

    @Test
    @DisplayName("Should correctly initialize all fields from setUp")
    void testFullyInitializedDepartment() {
        // Assert
        assertThat(department.getId()).isEqualTo(1L);
        assertThat(department.getName()).isEqualTo("Computer Science");
        assertThat(department.getDescription()).isEqualTo("Department of Computer Science and Engineering");
        assertThat(department.getStudents()).hasSize(2);
        assertThat(department.getTeachers()).hasSize(1);
        assertThat(department.getCourses()).hasSize(1);
    }

    @Test
    @DisplayName("Should allow overwriting all fields")
    void testOverwriteAllFields() {
        // Act
        department.setId(99L);
        department.setName("Electrical Engineering");
        department.setDescription("EE Department");
        department.setStudents(Collections.emptyList());
        department.setTeachers(Collections.emptyList());
        department.setCourses(Collections.emptyList());

        // Assert
        assertThat(department.getId()).isEqualTo(99L);
        assertThat(department.getName()).isEqualTo("Electrical Engineering");
        assertThat(department.getDescription()).isEqualTo("EE Department");
        assertThat(department.getStudents()).isEmpty();
        assertThat(department.getTeachers()).isEmpty();
        assertThat(department.getCourses()).isEmpty();
    }
}
