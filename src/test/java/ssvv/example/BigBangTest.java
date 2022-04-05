package ssvv.example;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import ssvv.example.domain.Nota;
import ssvv.example.domain.Student;
import ssvv.example.domain.Tema;
import ssvv.example.repository.CrudRepository;
import ssvv.example.repository.NotaFileRepository;
import ssvv.example.repository.StudentFileRepository;
import ssvv.example.repository.TemaFileRepository;
import ssvv.example.service.Service;
import ssvv.example.validation.NotaValidator;
import ssvv.example.validation.StudentValidator;
import ssvv.example.validation.TemaValidator;
import ssvv.example.validation.ValidationException;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.Assert.assertThrows;

public class BigBangTest {
    Service service;

    CrudRepository<String, Student> studentRepository;
    StudentValidator studentValidator;
    File studentRepositoryFile;

    CrudRepository<String, Tema> temaRepository;
    TemaValidator temaValidator;
    File temaRepositoryFile;

    CrudRepository<String, Nota> notaRepository;
    NotaValidator notaValidator;
    File notaRepositoryFile;

    Tema tema = new Tema("1", "tema faina", 14, 2);
    Student student = new Student("1", "chiar el", 935, "chiar.el@stud.ubbcluj.ro");
    Nota nota = new Nota("1", "1", "1", 10, LocalDate.now());

    @Before
    public void setup() throws IOException {
        studentRepositoryFile = File.createTempFile("student_repository", ".txt");
        temaRepositoryFile = File.createTempFile("tema_repository", ".txt");
        notaRepositoryFile = File.createTempFile("nota_repository", ".txt");

        studentRepository = new StudentFileRepository(studentRepositoryFile.getAbsolutePath());
        studentValidator = new StudentValidator();

        temaRepository = new TemaFileRepository(temaRepositoryFile.getAbsolutePath());
        temaValidator = new TemaValidator();

        notaRepository = new NotaFileRepository(notaRepositoryFile.getAbsolutePath());
        notaValidator = new NotaValidator(studentRepository, temaRepository);

        service = new Service(studentRepository, studentValidator, temaRepository, temaValidator, notaRepository, notaValidator);

        service.addStudent(student);
        service.addTema(tema);
    }

    @After
    public void teardown() {
        studentRepositoryFile.delete();
        temaRepositoryFile.delete();
        notaRepositoryFile.delete();
    }

    @Test
    public void addGrade() {
        service.addNota(nota, "foarte fain");

        assertThat(service.getAllNote(), containsInAnyOrder(nota));
    }

    @Test
    public void addAssignment() {
        service.addTema(tema);

        assertThat(service.getAllTeme(), containsInAnyOrder(tema));
    }

    @Test
    public void addStudent() {
        service.addStudent(student);

        assertThat(service.getAllStudenti(), containsInAnyOrder(student));
    }

    @Test
    public void bang() {
        service.addStudent(student);
        service.addTema(tema);
        service.addNota(nota, "feedback");

        assertThat(service.getAllNote(), containsInAnyOrder(nota));
    }
}
