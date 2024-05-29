package service.impl;

import enums.Genre;
import model.Book;
import model.Person;
import service.LibraryService;

import java.util.*;

public class LibraryServiceImpl implements LibraryService {
    @Override
    public void processRequest(List<Book> books, List<Request> requests) {
        Queue<LibraryApp.Request> educationalRequests = new LinkedList<>();
        List<LibraryApp.Request> fictionRequests = new ArrayList<>();

        for (LibraryApp.Request request : requests) {
            Book book = findBookById(request.getBookId(), books);
            if (book != null) {
                if (book.getGenre() == Genre.EDUCATIONAL) {
                    educationalRequests.add(request);
                } else if (book.getGenre() == Genre.FICTION) {
                    fictionRequests.add(request);
                }
            }
        }

        System.out.println("Handling Educational Requests Based on Priority:");
        requestBasedOnPriority(educationalRequests, books);

        System.out.println("\nHandling Fiction Requests Based on FIFO:");
        requestBasedOnFIFO(fictionRequests, books);
    }

    @Override
    public void requestBasedOnPriority(Queue<LibraryApp.Request> requestQueue, List<Book> books) {
        PriorityQueue<LibraryApp.Request> priorityQueue = new PriorityQueue<>(
                (r1, r2) -> compare(r1.getPerson(), getTitleById(r1.getBookId(), books),
                        r2.getPerson(), getTitleById(r2.getBookId(), books))
        );

        Map<Integer, Book> bookMap = new HashMap<>();
        for (Book book : books) {
            if (book.getGenre() == Genre.EDUCATIONAL) {
                bookMap.put(book.getBookId(), book);
            }
        }

        priorityQueue.addAll(requestQueue);

        Set<Person> processedPersons = new HashSet<>();
        while (!priorityQueue.isEmpty()) {
            LibraryApp.Request request = priorityQueue.poll();
            Person person = request.getPerson();
            if (processedPersons.contains(person)) {
                continue;
            }

            Book book = bookMap.get(request.getBookId());
            if (book != null && book.getCopies() > 0) {
                book.borrowBook();
                System.out.println(person.getName() + " borrowed " + book.getTitle());
                processedPersons.add(person);
            }
        }
    }

    @Override
    public void requestBasedOnFIFO(List<LibraryApp.Request> requestList, List<Book> books) {
        Map<Integer, Book> bookMap = new HashMap<>();
        for (Book book : books) {
            if (book.getGenre() == Genre.FICTION) {
                bookMap.put(book.getBookId(), book);
            }
        }

        for (LibraryApp.Request request : requestList) {
            Book book = bookMap.get(request.getBookId());
            if (book != null && book.getCopies() > 0) {
                book.borrowBook();
                System.out.println(request.getPerson().getName() + " borrowed " + book.getTitle());
            }
        }
    }

    @Override
    public int compare(Person p1, String bookTitle1, Person p2, String bookTitle2) {
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
        for (Book book : books) {
            if (book.getBookId() == bookId) {
                return book;
            }
        }
        return null;
    }

    private String getTitleById(int bookId, List<Book> books) {
        Book book = findBookById(bookId, books);
        return (book != null) ? book.getTitle() : null;
    }
}
