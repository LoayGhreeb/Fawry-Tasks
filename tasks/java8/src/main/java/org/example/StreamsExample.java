package org.example;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.DoubleConsumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.ToIntFunction;
import java.util.stream.Stream;

public class StreamsExample {

    public static void main(final String[] args) {

        List<Author> authors = Library.getAuthors();
        
        banner("Authors information");
        // SOLVED With functional interfaces declared
        Consumer<Author> authorPrintConsumer = new Consumer<Author>() {
            @Override
            public void accept(Author author) {
                System.out.println(author);
            }
        };
        authors
            .stream()
            .forEach(authorPrintConsumer);

        banner("Authors information - lambda");
        // SOLVED With functional interfaces used directly
        authors
            .stream()
            .forEach(System.out::println);

        banner("Active authors");
        // Solved With functional interfaces declared
        Predicate<Author> activeAuthorsPredicate = new Predicate<Author>() {
            @Override
            public boolean test(Author author) {
                return author.active;
            }
        };
        authors.stream()
               .filter(activeAuthorsPredicate)
               .forEach(authorPrintConsumer);

        banner("Active authors - lambda");
        // Solved With functional interfaces used directly
        authors.stream()
               .filter(author -> author.active)
               .forEach(System.out::println);

        banner("Active books for all authors");
        // Solved With functional interfaces declared
        Predicate<Book> publishedBooksPredicate = new Predicate<Book>() {
            @Override
            public boolean test(Book book) {
                return book.published;
            }
        };
        Consumer<Book> bookPrintConsumer = new Consumer<Book>() {
            @Override
            public void accept(Book book) {
                System.out.println(book);
            }
        };
        Function<Author, Stream<Book>> authorBooks = new Function<Author, Stream<Book>>() {
            @Override
            public Stream<Book> apply(Author author) {
                return author.books.stream();
            }
        };

        authors.stream()
               .flatMap(authorBooks)
               .filter(publishedBooksPredicate)
               .forEach(bookPrintConsumer);

        banner("Active books for all authors - lambda");
        // Solved With functional interfaces used directly
        authors.stream()
               .flatMap(author -> author.books.stream())
               .filter(book -> book.published)
               .forEach(System.out::println);

        banner("Average price for all books in the library");
        // Solved With functional interfaces declared
        ToIntFunction<Book> bookToIntFunction = new ToIntFunction<Book>() {
            @Override
            public int applyAsInt(Book book) {
                return book.price;
            }
        };
        DoubleConsumer averageConsumer = new DoubleConsumer() {
            @Override
            public void accept(double value) {
                System.out.println("Average price: " + value);
            }
        };

        authors.stream()
               .flatMap(authorBooks)
               .mapToInt(bookToIntFunction)
               .average()
               .ifPresent(averageConsumer);

        banner("Average price for all books in the library - lambda");
        // Solved With functional interfaces used directly
        authors.stream()
               .flatMap(author -> author.books.stream())
               .mapToInt(book -> book.price)
               .average()
               .ifPresent(avg -> System.out.println("Average price: " + avg));

        banner("Active authors that have at least one published book");
        // Solved With functional interfaces declared
        Predicate<Author> hasPublishedBookPredicate = new Predicate<Author>() {
            @Override
            public boolean test(Author author) {
                return author.books.stream().anyMatch(book -> book.published);
            }
        };

        authors.stream()
               .filter(activeAuthorsPredicate)
               .filter(hasPublishedBookPredicate)
               .forEach(authorPrintConsumer);

        banner("Active authors that have at least one published book - lambda");
        // Solved With functional interfaces used directly
        authors.stream()
               .filter(activeAuthorsPredicate)
               .filter(author -> author.books.stream()
                                             .anyMatch(book -> book.published))
               .forEach(System.out::println);
    }

    private static void banner(final String m) {
        System.out.println("#### " + m + " ####");
    }
}


class Library {
    public static List<Author> getAuthors() {
        return Arrays.asList(
            new Author("Author A", true, Arrays.asList(
                new Book("A1", 100, true),
                new Book("A2", 200, true),
                new Book("A3", 220, true))),
            new Author("Author B", true, Arrays.asList(
                new Book("B1", 80, true),
                new Book("B2", 80, false),
                new Book("B3", 190, true),
                new Book("B4", 210, true))),
            new Author("Author C", true, Arrays.asList(
                new Book("C1", 110, true),
                new Book("C2", 120, false),
                new Book("C3", 130, true))),
            new Author("Author D", false, Arrays.asList(
                new Book("D1", 200, true),
                new Book("D2", 300, false))),
            new Author("Author X", true, Collections.emptyList()));
    }
}

class Author {
    String name;
    boolean active;
    List<Book> books;

    Author(String name, boolean active, List<Book> books) {
        this.name = name;
        this.active = active;
        this.books = books;
    }

    @Override
    public String toString() {
        return name + "\t| " + (active ? "Active" : "Inactive");
    }
}

class Book {
    String name;
    int price;
    boolean published;

    Book(String name, int price, boolean published) {
        this.name = name;
        this.price = price;
        this.published = published;
    }

    @Override
    public String toString() {
        return name + "\t| " + "\t| $" + price + "\t| " + (published ? "Published" : "Unpublished");
    }
}
