// ===================== Book.java =====================
public class Book {
    private String id;
    private String title;
    private String author;
    private boolean isAvailable = true;

    public Book(String id, String title, String author) {
        this.id = id;
        this.title = title;
        this.author = author;
    }

    public String getId() { return id; }
    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public boolean isAvailable() { return isAvailable; }

    public void setAvailable(boolean available) {
        this.isAvailable = available;
    }

    @Override
    public String toString() {
        return title + " — " + author + (isAvailable ? " (Available)" : " (Not Available)");
    }
}

// ===================== User.java =====================
public abstract class User {
    protected String name;

    public User(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}

// ===================== Reader.java =====================
import java.util.ArrayList;
import java.util.List;

public class Reader extends User {

    private List<Book> borrowedBooks = new ArrayList<>();

    public Reader(String name) {
        super(name);
    }

    public void viewCatalog(Library library) {
        library.showCatalog();
    }

    public void searchBook(Library library, String title) {
        library.searchBook(title);
    }

    public void reserveBook(Library library, String bookId) {
        library.reserveBook(this, bookId);
    }

    public void addBorrowedBook(Book book) {
        borrowedBooks.add(book);
    }
}

// ===================== Librarian.java =====================
public class Librarian extends User {

    public Librarian(String name) {
        super(name);
    }

    public void issueBook(Library library, String bookId, Reader reader) {
        library.issueBook(bookId, reader);
    }

    public void returnBook(Library library, String bookId, Reader reader) {
        library.returnBook(bookId, reader);
    }

    public void addBook(Library library, Book book) {
        library.addBook(book);
    }

    public void updateBookInfo(Library library, String bookId, String newTitle) {
        library.updateBook(bookId, newTitle);
    }
}

// ===================== Administrator.java =====================
public class Administrator extends User {

    public Administrator(String name) {
        super(name);
    }

    public void manageUsers() {
        System.out.println("Пайдаланушылар басқарылды.");
    }

    public void assignRoles() {
        System.out.println("Рөлдер тағайындалды.");
    }

    public void changeSystemSettings() {
        System.out.println("Жүйе параметрлері жаңартылды.");
    }
}

// ===================== Library.java =====================
import java.util.ArrayList;
import java.util.List;

public class Library {

    private List<Book> books = new ArrayList<>();

    public void showCatalog() {
        System.out.println("=== Каталог ===");
        for (Book book : books) {
            System.out.println(book);
        }
    }

    public void searchBook(String title) {
        System.out.println("Іздеу нәтижесі:");
        for (Book book : books) {
            if (book.getTitle().toLowerCase().contains(title.toLowerCase())) {
                System.out.println(book);
            }
        }
    }

    public void reserveBook(Reader reader, String bookId) {
        for (Book book : books) {
            if (book.getId().equals(bookId) && book.isAvailable()) {
                book.setAvailable(false);
                System.out.println(reader.getName() + " кітапты брондады: " + book.getTitle());
                return;
            }
        }
        System.out.println("Кітап қолжетімсіз.");
    }

    public void issueBook(String bookId, Reader reader) {
        for (Book book : books) {
            if (book.getId().equals(bookId) && !book.isAvailable()) {
                System.out.println("Кітап берілді: " + reader.getName());
                reader.addBorrowedBook(book);
                return;
            }
        }
        System.out.println("Кітапты беру мүмкін емес.");
    }

    public void returnBook(String bookId, Reader reader) {
        for (Book book : books) {
            if (book.getId().equals(bookId)) {
                book.setAvailable(true);
                System.out.println("Кітап қайтарылды: " + book.getTitle());
                return;
            }
        }
    }

    public void addBook(Book book) {
        books.add(book);
        System.out.println("Кітап қосылды: " + book.getTitle());
    }

    public void updateBook(String bookId, String newTitle) {
        for (Book book : books) {
            if (book.getId().equals(bookId)) {
                System.out.println("Кітап атауы жаңартылды.");
                return;
            }
        }
    }
}

// ===================== Main.java =====================
public class Main {
    public static void main(String[] args) {

        Library library = new Library();

        Librarian librarian = new Librarian("Айжан");
        Reader reader = new Reader("Ұлбо");
        Administrator admin = new Administrator("Админ");

        librarian.addBook(library, new Book("1", "Java Basics", "John"));
        librarian.addBook(library, new Book("2", "Algorithms", "CLRS"));

        reader.viewCatalog(library);
        reader.searchBook(library, "Java");

        reader.reserveBook(library, "1");
        librarian.issueBook(library, "1", reader);

        librarian.returnBook(library, "1", reader);
        admin.manageUsers();
    }
}
