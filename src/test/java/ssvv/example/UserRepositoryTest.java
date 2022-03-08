package ssvv.example;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ssvv.example.domain.Student;
import ssvv.example.repository.StudentFileRepository;

import java.io.File;
import java.io.IOException;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.Assert.assertThat;

public class UserRepositoryTest {
    File file;

    @Before
    public void setup() throws IOException {
         file = File.createTempFile("student_repository", ".txt");
    }

    @After
    public void teardown() {
        file.delete();
    }

    @Test
    public void add_two_students__empty_file_repository__students_are_added() {
        StudentFileRepository repository = new StudentFileRepository(file.getAbsolutePath());
        Student studentOne = new Student("id_one", "Name one", 420, "email1@domain.com");
        Student studentTwo = new Student("id_two", "Name two", 420, "email2@domain.com");

        repository.save(studentOne);
        repository.save(studentTwo);

        assertThat(repository.findAll(), containsInAnyOrder(studentOne, studentTwo));
    }
}
