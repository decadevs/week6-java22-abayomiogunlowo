package main;

import enums.Genre;
import model.Book;
import model.User;
import service.LibraryService;
import service.impl.LibraryServiceImpl;

import java.util.ArrayList;
import java.util.List;

public class LibraryApp {
    public static void main(String[] args) {
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

        // Library service
        LibraryService libraryService = new LibraryServiceImpl();

        // Process requests
        libraryService.processRequest(books, requests);
    }

    public static class Request {
        private final User user;
        private final int bookId;

        public Request(User user, int bookId) {
            this.user = user;
            this.bookId = bookId;
        }

        public User getPerson() {
            return user;
        }

        public int getBookId() {
            return bookId;
        }
    }
}
