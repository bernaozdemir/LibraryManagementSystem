import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Abstract class representing a library item such as a book, magazine, or DVD.
 * Includes basic metadata and borrowing-related information.
 */
public abstract class LibraryItem {
    private String id;
    private String title;
    private String type;
    private boolean borrowed = false;
    private LocalDate borrowedDate;
    private User borrowedBy;

    /**
     * Constructs a new LibraryItem.
     *
     * @param id    Unique identifier of the item
     * @param title Title of the item
     * @param type  Type of the item (e.g., normal, reference, limited)
     */
    public LibraryItem(String id, String title, String type) {
        this.id = id;
        this.title = title;
        this.type = type;
    }

    /**
     * @return The unique identifier of the item
     */
    public String getId() { return id; }

    /**
     * @return The title of the item
     */
    public String getTitle() { return title; }

    /**
     * @return The type of the item
     */
    public String getType() { return type; }

    /**
     * @return True if the item is currently borrowed
     */
    public boolean isBorrowed() { return borrowed; }

    /**
     * Sets whether the item is borrowed or not.
     * @param borrowed Borrow status to set
     */
    public void setBorrowed(boolean borrowed) { this.borrowed = borrowed; }

    /**
     * @return The date when the item was borrowed
     */
    public LocalDate getBorrowedDate() { return borrowedDate; }

    /**
     * Sets the date when the item was borrowed.
     * @param date Date to set as borrowed date
     */
    public void setBorrowedDate(LocalDate date) { this.borrowedDate = date; }

    /**
     * @return The user who borrowed the item
     */
    public User getBorrowedBy() { return borrowedBy; }

    /**
     * Sets the user who borrowed the item.
     * @param user The user to associate with the item
     */
    public void setBorrowedBy(User user) { this.borrowedBy = user; }

    /**
     * Abstract method to return detailed information about the item.
     * Must be implemented by subclasses.
     *
     * @return A string representing detailed information about the item
     */
    public abstract String getDetailedInfo();
}

/**
 * Represents a book in the library.
 */
class Book extends LibraryItem {
    private String author;
    private String genre;

    /**
     * Constructs a Book with given parameters.
     *
     * @param id     Book ID
     * @param title  Book title
     * @param author Book author
     * @param genre  Book genre
     * @param type   Book type
     */
    public Book(String id, String title, String author, String genre, String type) {
        super(id, title, type);
        this.author = author;
        this.genre = genre;
    }

    /**
     * @return A string with detailed information about the book
     */
    @Override
    public String getDetailedInfo() {
        StringBuilder sb = new StringBuilder();
        sb.append("------ Item Information for " + getId() + " ------\n");
        sb.append("ID: " + getId() + " Name: " + getTitle() + " ");
        if (isBorrowed()) {
            sb.append("Status: Borrowed Borrowed Date: " +
                      getBorrowedDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
            sb.append(" Borrowed by: " + getBorrowedBy().getName() + "\n");
        } else {
            sb.append("Status: Available\n");
        }
        sb.append("Author: " + author + " Genre: " + genre + "\n");
        return sb.toString();
    }
}

/**
 * Represents a magazine in the library.
 */
class Magazine extends LibraryItem {
    private String publisher;
    private String category;

    /**
     * Constructs a Magazine with given parameters.
     *
     * @param id        Magazine ID
     * @param title     Magazine title
     * @param publisher Magazine publisher
     * @param category  Magazine category
     * @param type      Magazine type
     */
    public Magazine(String id, String title, String publisher, String category, String type) {
        super(id, title, type);
        this.publisher = publisher;
        this.category = category;
    }

    /**
     * @return A string with detailed information about the magazine
     */
    @Override
    public String getDetailedInfo() {
        StringBuilder sb = new StringBuilder();
        sb.append("------ Item Information for " + getId() + " ------\n");
        sb.append("ID: " + getId() + " Name: " + getTitle() + " ");
        if (isBorrowed()) {
            sb.append("Status: Borrowed Borrowed Date: " +
                      getBorrowedDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
            sb.append(" Borrowed by: " + getBorrowedBy().getName() + "\n");
        } else {
            sb.append("Status: Available\n");
        }
        sb.append("Publisher: " + publisher + " Category: " + category + "\n");
        return sb.toString();
    }
}

/**
 * Represents a DVD in the library.
 */
class DVD extends LibraryItem {
    private String director;
    private String category;
    private int runtime;

    /**
     * Constructs a DVD with given parameters.
     *
     * @param id       DVD ID
     * @param title    DVD title
     * @param director DVD director
     * @param category DVD category
     * @param runtime  DVD runtime in minutes
     * @param type     DVD type
     */
    public DVD(String id, String title, String director, String category, int runtime, String type) {
        super(id, title, type);
        this.director = director;
        this.category = category;
        this.runtime = runtime;
    }

    /**
     * @return A string with detailed information about the DVD
     */
    @Override
    public String getDetailedInfo() {
        StringBuilder sb = new StringBuilder();
        sb.append("------ Item Information for " + getId() + " ------\n");
        sb.append("ID: " + getId() + " Name: " + getTitle() + " ");
        if (isBorrowed()) {
            sb.append("Status: Borrowed Borrowed Date: " +
                      getBorrowedDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
            sb.append(" Borrowed by: " + getBorrowedBy().getName() + "\n");
        } else {
            sb.append("Status: Available\n");
        }
        sb.append("Director: " + director + " Category: " + category + " Runtime: " + runtime + " min");
        return sb.toString();
    }
}
