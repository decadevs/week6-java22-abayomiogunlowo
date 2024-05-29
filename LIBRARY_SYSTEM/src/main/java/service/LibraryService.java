package service;

import model.Book;
import model.Person;

import java.util.List;
import java.util.Queue;

public interface LibraryService {
    void processRequest(List<Book> books, List<LibraryApp.Request> requests);
    void requestBasedOnPriority(Queue<LibraryApp.Request> requestQueue, List<Book> books);
    void requestBasedOnFIFO(List<LibraryApp.Request> requestList, List<Book> books);
    int compare(Person p1, String bookTitle1, Person p2, String bookTitle2);
}