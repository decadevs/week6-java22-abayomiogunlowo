package service;

import model.Book;
import model.User;
import main.LibraryApp.Request; // Correct import

import java.util.List;

public interface LibraryService {
    void processRequest(List<Book> books, List<Request> requests);
    int compare(User p1, String bookTitle1, User p2, String bookTitle2);
}