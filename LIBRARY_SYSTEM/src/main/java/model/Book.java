package model;

import enums.Genre;

public class Book {
    private int bookId;
    private String title;
    private String author;
    private int copies;
    private Genre genre;

    public Book(int bookId,String title, String author, int copies, Genre genre) {
        this.bookId = bookId;
        this.title = title;
        this.author = author;
        this.copies = copies;
        this.genre = genre;
    }

    public int getBookId() {
        return bookId;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public int getCopies() {
        return copies;
    }

    public Genre getGenre() {
        return genre;
    }

    public void borrowBook() {
        if (copies > 0) {
            copies--;
        } else {
            throw new IllegalStateException("No copies available");
        }
    }

    public void returnBook() {
        copies++;
    }
}