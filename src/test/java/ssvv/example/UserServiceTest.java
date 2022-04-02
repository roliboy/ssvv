package ssvv.example;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
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
import static org.hamcrest.Matchers.hasItems;
import static org.junit.Assert.assertThrows;

public class UserServiceTest {
    Service service;

    CrudRepository<String, Student> studentRepository;
    StudentValidator studentValidator;
    File studentRepositoryFile;

    CrudRepository<String, Tema> temaRepository;
    TemaValidator temaValidator;
    File temaRepositoryFile;

    @Mock
    CrudRepository<String, Nota> notaRepository;
    @Mock
    NotaValidator notaValidator;

    @Before
    public void setup() throws IOException {
        studentRepositoryFile = File.createTempFile("student_repository", ".txt");
        temaRepositoryFile = File.createTempFile("tema_repository", ".txt");

        studentRepository = new StudentFileRepository(studentRepositoryFile.getAbsolutePath());
        studentValidator = new StudentValidator();

        temaRepository = new TemaFileRepository(temaRepositoryFile.getAbsolutePath());
        temaValidator = new TemaValidator();

        service = new Service(studentRepository, studentValidator, temaRepository, temaValidator, notaRepository, notaValidator);
    }

    @After
    public void teardown() {
        studentRepositoryFile.delete();
        temaRepositoryFile.delete();
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



    @Test
    public void addAssignment_withEmptyId_throwsValidationException() {
        Tema assignment = new Tema("", "description", 3, 2);

        assertThrows(ValidationException.class, () -> service.addTema(assignment));
    }

    @Test
    public void addAssignment_withDeliveryWeekAfterDeadline_throwsValidationException() {
        Tema assignment = new Tema("1", "description", 1, 8);

        assertThrows(ValidationException.class, () -> service.addTema(assignment));
    }

    @Test
    public void addAssignment_withEmptyDescription_throwsValidationException() {
        Tema assignment = new Tema("1", "", 3, 2);

        assertThrows(ValidationException.class, () -> service.addTema(assignment));
    }

    @Test
    public void addAssignment_withNegativeDeadline_throwsValidationException() {
        Tema assignment = new Tema("1", "description", -1, 2);

        assertThrows(ValidationException.class, () -> service.addTema(assignment));
    }

    @Test
    public void addAssignment_withNegativeDeliveryWeek_throwsValidationException() {
        Tema assignment = new Tema("1", "description", 1, -1);

        assertThrows(ValidationException.class, () -> service.addTema(assignment));
    }

    @Test
    public void addAssignment_withDuplicateId_existingAssignmentIsUsed() {
        Tema firstAssignment = new Tema("1", "description 1", 3, 2);
        Tema secondAssignment = new Tema("1", "description 2", 4, 3);

        service.addTema(firstAssignment);
        service.addTema(secondAssignment);

        assertThat(service.getAllTeme(), hasItems(firstAssignment));
    }
}
