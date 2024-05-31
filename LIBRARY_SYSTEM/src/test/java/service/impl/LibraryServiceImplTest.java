package service.impl;

import enums.Genre;
import model.Book;
import model.User;
import main.LibraryApp.Request;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LibraryServiceImplTest {

    @Test
    void compareTeacherAndStudent() {
        LibraryServiceImpl libraryService = new LibraryServiceImpl();

        // Create a teacher and a student
        User teacher = new User("John"); // Teacher
        User student = new User("Alice", 8); // Student

        // Teacher should have higher priority than student
        int result = libraryService.compare(teacher, "", student, "");
        assertTrue(result < 0);
    }

    @Test
    void compareStudents() {
        LibraryServiceImpl libraryService = new LibraryServiceImpl();

        // Create students with different grade levels
        User student1 = new User("Alice");
        User student2 = new User("Bob", 9);

        // Student with lower grade level should have higher priority
        int result = libraryService.compare(student1, "", student2, "");
        assertTrue(result < 0);
    }

    @Test
    void compareTeachers() {
        LibraryServiceImpl libraryService = new LibraryServiceImpl();

        // Create two teachers
        User teacher1 = new User("John");
        User teacher2 = new User("Jane");

        // Teachers should have equal priority
        int result = libraryService.compare(teacher1, "", teacher2, "");
        assertEquals(0, result);
    }

    @Test
    void processRequest() {
        // Create some books
        List<Book> books = new ArrayList<>();
        books.add(new Book(1234, "Math Basics", "Author A", 1, Genre.EDUCATIONAL));
        books.add(new Book(2345, "Science Facts", "Author B", 1, Genre.EDUCATIONAL));
        books.add(new Book(3456, "Fantastic Fiction", "Author C", 1, Genre.FICTION));

        // Create some people with book IDs they want to borrow
        List<Request> requests = new ArrayList<>();
        requests.add(new Request(new User("Alice", 20), 1234)); // Student
        requests.add(new Request(new User("Bob"), 2345)); // Teacher
        requests.add(new Request(new User("Charlie", 8), 1234)); // Student
        requests.add(new Request(new User("David", 6), 3456)); // Student
        requests.add(new Request(new User("Eve"), 3456)); // Teacher
        requests.add(new Request(new User("Frank", 7), 3456)); // Student

        // Create LibraryServiceImpl instance
        LibraryServiceImpl libraryService = new LibraryServiceImpl();

        // Call processRequest method
        libraryService.processRequest(books, requests);

        // Validate the number of borrowed books for each genre
        assertEquals(2, books.stream().filter(book -> book.getGenre() == Genre.EDUCATIONAL && book.getCopies() == 0).count());
        assertEquals(1, books.stream().filter(book -> book.getGenre() == Genre.FICTION && book.getCopies() == 0).count());
    }
}