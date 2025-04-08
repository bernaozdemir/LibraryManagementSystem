
/**
 * Abstract class representing a general user in the library system.
 * Stores basic user information and penalty details.
 */
public abstract class User {
    private String id;
    private String name;
    private String phone;
    private int penalty = 0;
    private boolean hasPaidPenalty = false;

    /**
     * Constructs a new User.
     *
     * @param id    User ID
     * @param name  User name
     * @param phone User phone number
     */
    public User(String id, String name, String phone) {
        this.id = id;
        this.name = name;
        this.phone = phone;
    }

    /** @return User ID */
    public String getId() { return id; }

    /** @return User name */
    public String getName() { return name; }

    /** @return User phone number */
    public String getPhone() { return phone; }

    /** @return Current penalty amount */
    public int getPenalty() { return penalty; }

    /**
     * Adds a penalty amount to the user.
     * @param amount Penalty to add
     */
    public void addPenalty(int amount) { penalty += amount; }

    /**
     * Sets the penalty to a specific amount.
     * @param amount New penalty amount
     */
    public void setPenalty(int amount) { penalty = amount; }

    /** @return Whether the penalty has been paid */
    public boolean getHasPaidPenalty() { return hasPaidPenalty; }

    /**
     * Sets whether the user has paid their penalty.
     * @param value True if paid, false otherwise
     */
    public void setHasPaidPenalty(boolean value) { hasPaidPenalty = value; }

    /**
     * Checks if the user can borrow an item based on their current borrow count.
     *
     * @param item               The item to borrow
     * @param currentBorrowCount Current number of items borrowed
     * @return True if the user can borrow, false otherwise
     */
    public abstract boolean canBorrow(LibraryItem item, int currentBorrowCount);

    /**
     * @return The maximum number of days the user can keep an item without penalty
     */
    public abstract int getOverdueDays();

    /**
     * @return Detailed information about the user
     */
    public abstract String getDetailedInfo();
}

/**
 * Represents a student user in the library system.
 */
class Student extends User {
    private String department;
    private String faculty;
    private int grade;

    /**
     * Constructs a Student user.
     *
     * @param id        Student ID
     * @param name      Student name
     * @param phone     Student phone number
     * @param department Student's department
     * @param faculty   Student's faculty
     * @param grade     Student's grade level
     */
    public Student(String id, String name, String phone, String department, String faculty, int grade) {
        super(id, name, phone);
        this.department = department;
        this.faculty = faculty;
        this.grade = grade;
    }

    @Override
    public boolean canBorrow(LibraryItem item, int currentBorrowCount) {
        return currentBorrowCount < 5;
    }

    @Override
    public int getOverdueDays() {
        return 30;
    }

    @Override
    public String getDetailedInfo() {
        return "------ User Information for " + getId() + " ------\n" +
               "Name: " + getName() + " Phone: " + getPhone() + "\n" +
               "Faculty: " + faculty + " Department: " + department + " Grade: " + grade + "th\n" +
               "Penalty: $" + getPenalty();
    }
}

/**
 * Represents an academic staff user in the library system.
 */
class AcademicStaff extends User {
    private String department;
    private String faculty;
    private String title;

    /**
     * Constructs an AcademicStaff user.
     *
     * @param id        Staff ID
     * @param name      Staff name
     * @param phone     Staff phone number
     * @param department Staff department
     * @param faculty   Staff faculty
     * @param title     Staff academic title
     */
    public AcademicStaff(String id, String name, String phone, String department, String faculty, String title) {
        super(id, name, phone);
        this.department = department;
        this.faculty = faculty;
        this.title = title;
    }

    @Override
    public boolean canBorrow(LibraryItem item, int currentBorrowCount) {
        return currentBorrowCount < 3;
    }

    @Override
    public int getOverdueDays() {
        return 15;
    }

    @Override
    public String getDetailedInfo() {
        return "------ User Information for " + getId() + " ------\n" +
               "Name: " + title + " " + getName() + " Phone: " + getPhone() + "\n" +
               "Faculty: " + faculty + " Department: " + department + "\n" +
               "Penalty: $" + getPenalty();
    }
}

/**
 * Represents a guest user in the library system.
 */
class GuestUser extends User {
    private String occupation;

    /**
     * Constructs a GuestUser.
     *
     * @param id         Guest ID
     * @param name       Guest name
     * @param phone      Guest phone number
     * @param occupation Guest's occupation
     */
    public GuestUser(String id, String name, String phone, String occupation) {
        super(id, name, phone);
        this.occupation = occupation;
    }

    @Override
    public boolean canBorrow(LibraryItem item, int currentBorrowCount) {
        return currentBorrowCount < 1;
    }

    @Override
    public int getOverdueDays() {
        return 7;
    }

    @Override
    public String getDetailedInfo() {
        return "------ User Information for " + getId() + " ------\n" +
               "Name: " + getName() + " Phone: " + getPhone() + "\n" +
               "Occupation: " + occupation + "\n" +
               "Penalty: $" + getPenalty();
    }
}
