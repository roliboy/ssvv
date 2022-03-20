package ssvv.example;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import ssvv.example.domain.Nota;
import ssvv.example.domain.Student;
import ssvv.example.domain.Tema;
import ssvv.example.repository.*;
import ssvv.example.service.Service;
import ssvv.example.validation.NotaValidator;
import ssvv.example.validation.StudentValidator;
import ssvv.example.validation.TemaValidator;
import ssvv.example.validation.ValidationException;

import java.io.File;
import java.io.IOException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class UserServiceTest {
    Service service;

    CrudRepository<String, Student> studentRepository;
    StudentValidator studentValidator;
    File studentRepositoryFile;

    @Mock
    CrudRepository<String, Tema> temaRepository;
    @Mock
    TemaValidator temaValidator;
    @Mock
    CrudRepository<String, Nota> notaRepository;
    @Mock
    NotaValidator notaValidator;


    @BeforeEach
    public void setup() throws IOException {
        studentRepositoryFile = File.createTempFile("student_repository", ".txt");

        studentRepository = new StudentFileRepository(studentRepositoryFile.getAbsolutePath());
        studentValidator = new StudentValidator();

        service = new Service(studentRepository, studentValidator, temaRepository, temaValidator, notaRepository, notaValidator);
    }

    @AfterEach
    public void teardown() {
        studentRepositoryFile.delete();
    }

    @Test
    public void addStudent_withNegativeGroupNumber_throwsException() {
        Student student = new Student("id_one", "Name one", -1, "email1@domain.com");

        assertThrows(ValidationException.class, () -> service.addStudent(student));
    }

    @Test
    public void addStudent_withGroupNumberZero_isAdded() {
        Student student = new Student("id_one", "Name one", 0, "email1@domain.com");

        service.addStudent(student);

        assertThat(service.getAllStudenti(), containsInAnyOrder(student));
    }

    @Test
    public void addStudent_withGroupNumberOne_isAdded() {
        Student student = new Student("id_one", "Name one", 1, "email1@domain.com");

        service.addStudent(student);

        assertThat(service.getAllStudenti(), containsInAnyOrder(student));
    }

    @Test
    public void addStudent_withGroupNumberMaxIntMinusOne_isAdded() {
        Student student = new Student("id_one", "Name one", Integer.MAX_VALUE - 1, "email1@domain.com");

        service.addStudent(student);

        assertThat(service.getAllStudenti(), containsInAnyOrder(student));
    }

    @Test
    public void addStudent_withGroupNumberMaxInt_isAdded() {
        Student student = new Student("id_one", "Name one", Integer.MAX_VALUE, "email1@domain.com");

        service.addStudent(student);

        assertThat(service.getAllStudenti(), containsInAnyOrder(student));
    }

    @Test
    public void addStudent_withGroupNumberMaxIntPlusOne_throwsException() {
        Student student = new Student("id_one", "Name one", Integer.MAX_VALUE + 1, "email1@domain.com");

        assertThrows(ValidationException.class, () -> service.addStudent(student));
    }

    @Test
    public void addStudent_withEmptyId_throwsException() {
        Student student = new Student("", "Name one", 420, "email1@domain.com");

        assertThrows(ValidationException.class, () -> service.addStudent(student));
    }

    @Test
    public void addStudent_withBlankId_throwsException() {
        Student student = new Student("    ", "Name one", 420, "email1@domain.com");

        assertThrows(ValidationException.class, () -> service.addStudent(student));
    }

    @Test
    public void addStudent_withEmptyName_throwsException() {
        Student student = new Student("id_one", "", 420, "email1@domain.com");

        assertThrows(ValidationException.class, () -> service.addStudent(student));
    }

    @Test
    public void addStudent_withEmptyEmail_throwsException() {
        Student student = new Student("id_one", "Name one", 420, "");

        assertThrows(ValidationException.class, () -> service.addStudent(student));
    }
}
