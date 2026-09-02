import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

class Book {
    String isbn;
    String title;
    String author;
    int year;

    Book(String isbn, String title, String author, int year) {
        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.year = year;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Book)) return false;
        Book book = (Book) o;
        return isbn.equals(book.isbn);
    }

    @Override
    public int hashCode() {
        return isbn.hashCode();
    }

    @Override
    public String toString() {
        return "ISBN: " + isbn + " | Title: " + title + " | Author: " + author + " | Year: " + year;
    }
}

public class BookManager {
    private HashSet<Book> books = new HashSet<>();

    public void addBook(Book book) {
        books.add(book);
    }

    public void removeBook(String isbn) {
        Iterator<Book> it = books.iterator();
        while (it.hasNext()) {
            if (it.next().isbn.equals(isbn)) {
                it.remove();
                break;
            }
        }
    }

    public void displaySortedByYear() {
        List<Book> sortedList = new ArrayList<>(books);
        Collections.sort(sortedList, new Comparator<Book>() {
            @Override
            public int compare(Book b1, Book b2) {
                return Integer.compare(b2.year, b1.year);
            }
        });

        Iterator<Book> it = sortedList.iterator();
        while (it.hasNext()) {
            System.out.println(it.next());
        }
    }

    public void findBooksByAuthor(String author) {
        Iterator<Book> it = books.iterator();
        while (it.hasNext()) {
            Book b = it.next();
            if (b.author.equalsIgnoreCase(author)) {
                System.out.println(b);
            }
        }
    }

    public static void main(String[] args) {
        BookManager manager = new BookManager();
        manager.addBook(new Book("123", "Java Basics", "John Doe", 2021));
        manager.addBook(new Book("456", "Advanced Java", "Jane Smith", 2023));
        manager.addBook(new Book("123", "Duplicate Book", "John Doe", 2022));
        manager.addBook(new Book("789", "Design Patterns", "John Doe", 2019));

        System.out.println("All Books Sorted by Year (Newest First):");
        manager.displaySortedByYear();

        System.out.println("\nBooks by John Doe:");
        manager.findBooksByAuthor("John Doe");

        System.out.println("\nRemoving Book with ISBN 456...");
        manager.removeBook("456");

        System.out.println("\nRemaining Books:");
        manager.displaySortedByYear();
    }
}
