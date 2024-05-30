package service.impl;

import enums.Genre;
import model.Book;
import model.User;
import service.LibraryService;
import main.LibraryApp.Request; // Correct import

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class LibraryServiceImpl implements LibraryService {
    @Override
    public void processRequest(List<Book> books, List<Request> requests) {
        Map<Genre, List<Request>> requestsByGenre = requests.stream()
                .collect(Collectors.groupingBy(request -> findBookById(request.getBookId(), books).getGenre()));

        List<Request> educationalRequests = requestsByGenre.getOrDefault(Genre.EDUCATIONAL, Collections.emptyList());
        List<Request> fictionRequests = requestsByGenre.getOrDefault(Genre.FICTION, Collections.emptyList());

        System.out.println("Handling Educational Requests Based on Priority:");
        requestBasedOnPriority(educationalRequests, books);

        System.out.println("\nHandling Fiction Requests Based on FIFO:");
        requestBasedOnFIFO(fictionRequests, books);
    }

    private void requestBasedOnPriority(List<Request> requestList, List<Book> books) {
        Map<Integer, Book> educationalBooks = books.stream()
                .filter(book -> book.getGenre() == Genre.EDUCATIONAL)
                .collect(Collectors.toMap(Book::getBookId, Function.identity()));

        requestList.stream()
                .sorted((r1, r2) -> compare(r1.getPerson(), getTitleById(r1.getBookId(), books),
                        r2.getPerson(), getTitleById(r2.getBookId(), books)))
                .forEach(request -> {
                    Book book = educationalBooks.get(request.getBookId());
                    if (book != null && book.getCopies() > 0) {
                        book.borrowBook();
                        System.out.println(request.getPerson().getName() + " borrowed " + book.getTitle());
                    }
                });
    }

    private void requestBasedOnFIFO(List<Request> requestList, List<Book> books) {
        Map<Integer, Book> fictionBooks = books.stream()
                .filter(book -> book.getGenre() == Genre.FICTION)
                .collect(Collectors.toMap(Book::getBookId, Function.identity()));

        requestList.forEach(request -> {
            Book book = fictionBooks.get(request.getBookId());
            if (book != null && book.getCopies() > 0) {
                book.borrowBook();
                System.out.println(request.getPerson().getName() + " borrowed " + book.getTitle());
            }
        });
    }

    @Override
    public int compare(User p1, String bookTitle1, User p2, String bookTitle2) {
        // Check if one is a teacher and the other is not
        if (p1.isTeacher() && !p2.isTeacher()) {
            return -1;
        } else if (!p1.isTeacher() && p2.isTeacher()) {
            return 1;
        }

        // If both are students, compare by grade level
        if (!p1.isTeacher() && !p2.isTeacher()) {
            return Integer.compare(p2.getGradeLevel(), p1.getGradeLevel());
        }

        // If both are teachers, they are considered equal in priority
        return 0;
    }

    private Book findBookById(int bookId, List<Book> books) {
        return books.stream()
                .filter(book -> book.getBookId() == bookId)
                .findFirst()
                .orElse(null);
    }

    private String getTitleById(int bookId, List<Book> books) {
        return books.stream()
                .filter(book -> book.getBookId() == bookId)
                .map(Book::getTitle)
                .findFirst()
                .orElse(null);
    }
}