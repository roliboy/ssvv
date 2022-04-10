package ssvv.example;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
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

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;

public class TopDownIntegrationTest {
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
    }

    @After
    public void teardown() {
        studentRepositoryFile.delete();
        temaRepositoryFile.delete();
        notaRepositoryFile.delete();
    }

    @Test
    public void addStudent_withValidProperties_isAdded() {
        var student = new Student("1", "chiar el", 935, "chiar.el@stud.ubbcluj.ro");
        service.addStudent(student);
        assertThat(service.getAllStudenti(), containsInAnyOrder(student));
    }

    @Test
    public void addAssignment_withValidProperties_isAdded() {
        var student = new Student("1", "chiar el", 935, "chiar.el@stud.ubbcluj.ro");
        service.addStudent(student);
        assertThat(service.getAllStudenti(), containsInAnyOrder(student));

        var tema = new Tema("1", "tema faina", 14, 2);
        service.addTema(tema);
        assertThat(service.getAllTeme(), containsInAnyOrder(tema));
    }

    @Test
    public void addGrade_withValidProperties_isAdded() {
        var student = new Student("1", "chiar el", 935, "chiar.el@stud.ubbcluj.ro");
        service.addStudent(student);
        assertThat(service.getAllStudenti(), containsInAnyOrder(student));

        var tema = new Tema("1", "tema faina", 14, 2);
        service.addTema(tema);
        assertThat(service.getAllTeme(), containsInAnyOrder(tema));

        var nota = new Nota("1", "1", "1", 10, LocalDate.now());
        service.addNota(nota, "feedback");
        assertThat(service.getAllNote(), containsInAnyOrder(nota));
    }
}
