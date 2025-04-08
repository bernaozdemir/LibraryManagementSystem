import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;

/**
 * LibraryManagementSystem class handles user and item management,
 * processing commands like borrowing, returning, paying penalties,
 * and displaying user/item data.
 */
public class LibraryManagementSystem {
    private List<User> users = new ArrayList<>();
    private List<LibraryItem> items = new ArrayList<>();
    private Map<String, List<LibraryItem>> borrowedItems = new HashMap<>();
    private PrintWriter writer;
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private Set<String> penaltyBlockedUsers = new HashSet<>();

    /**
     * Constructor initializes the system and opens the output file.
     *
     * @param outputFileName Path to the output file
     */
    public LibraryManagementSystem(String outputFileName) {
        try {
            writer = new PrintWriter(new FileWriter(outputFileName, false));
        } catch (IOException e) {
            System.out.println("Error opening output file: " + outputFileName);
        }
    }

    /**
     * Writes a line to the output file.
     *
     * @param line Line to be written
     */
    public void writeLine(String line) {
        if (writer != null) writer.println(line);
    }

    /**
     * Closes the output file stream.
     */
    public void closeOutput() {
        if (writer != null) writer.close();
    }

    /**
     * Loads user data from a file and populates the users list.
     *
     * @param filename Path to the user data file
     */
    public void loadUsersFromFile(String filename) {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                String type = parts[0];
                User user = null;
                switch (type) {
                    case "S":
                        user = new Student(parts[2], parts[1], parts[3], parts[4], parts[5], Integer.parseInt(parts[6]));
                        break;
                    case "A":
                        user = new AcademicStaff(parts[2], parts[1], parts[3], parts[4], parts[5], parts[6]);
                        break;
                    case "G":
                        user = new GuestUser(parts[2], parts[1], parts[3], parts[4]);
                        break;
                }
                users.add(user);
                borrowedItems.put(user.getId(), new ArrayList<>());
            }
        } catch (IOException e) {
            writeLine("Error reading users file: " + filename);
        }
    }

    /**
     * Loads item data from a file and populates the items list.
     *
     * @param filename Path to the item data file
     */
    public void loadItemsFromFile(String filename) {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                String type = parts[0];
                switch (type) {
                    case "B":
                        items.add(new Book(parts[1], parts[2], parts[3], parts[4], parts[5]));
                        break;
                    case "M":
                        items.add(new Magazine(parts[1], parts[2], parts[3], parts[4], parts[5]));
                        break;
                    case "D":
                        int runtime = Integer.parseInt(parts[5].replace(" min", ""));
                        items.add(new DVD(parts[1], parts[2], parts[3], parts[4], runtime, parts[6]));
                        break;
                }
            }
        } catch (IOException e) {
            writeLine("Error reading items file: " + filename);
        }
    }

    /**
     * Processes a single command from the command file.
     *
     * @param line The command line to be executed
     */
    public void processCommand(String line) {
        String[] parts = line.split(",");
        switch (parts[0]) {
            case "borrow":
                borrowItem(parts[1], parts[2], parts[3]);
                break;
            case "return":
                returnItem(parts[1], parts[2]);
                break;
            case "pay":
                payPenalty(parts[1]);
                break;
            case "displayUsers":
                displayUsers();
                break;
            case "displayItems":
                displayItems();
                break;
            default:
                writeLine("Unknown command: " + line);
        }
    }

    /**
     * Handles borrowing of an item by a user.
     */
    public void borrowItem(String userId, String itemId, String dateStr) {
        User user = findUserById(userId);
        LibraryItem item = findItemById(itemId);

        if (user == null || item == null) {
            writeLine("Error: user or item not found!");
            return;
        }

        LocalDate currentDate = LocalDate.parse(dateStr, formatter);
        List<LibraryItem> borrowed = borrowedItems.get(userId);

        int newPenalty = 0;
        for (LibraryItem borrowedItem : borrowed) {
            if (borrowedItem.getBorrowedDate() != null) {
                long days = ChronoUnit.DAYS.between(borrowedItem.getBorrowedDate(), currentDate);
                if (days > user.getOverdueDays()) {
                    newPenalty += 2;
                }
            }
        }

        int totalPenalty = user.getPenalty() + newPenalty;

        if (totalPenalty >= 6 && !user.getHasPaidPenalty()) {
            user.setPenalty(totalPenalty);
            penaltyBlockedUsers.add(userId);
            writeLine(user.getName() + " cannot borrow " + item.getTitle() + ", you must first pay the penalty amount! " + totalPenalty + "$");
            return;
        }

        user.addPenalty(newPenalty);

        if (item.isBorrowed()) {
            writeLine(user.getName() + " cannot borrow " + item.getTitle() + ", it is not available!");
            return;
        }

        if (!user.canBorrow(item, borrowed.size())) {
            writeLine(user.getName() + " cannot borrow " + item.getTitle() + ", since the borrow limit has been reached!");
            return;
        }

        item.setBorrowed(true);
        item.setBorrowedBy(user);
        item.setBorrowedDate(currentDate);
        borrowed.add(item);

        writeLine(user.getName() + " successfully borrowed! " + item.getTitle());
    }

    /**
     * Handles the return of a borrowed item.
     */
    public void returnItem(String userId, String itemId) {
        User user = findUserById(userId);
        LibraryItem item = findItemById(itemId);

        if (user == null || item == null) {
            writeLine("Error: user or item not found!");
            return;
        }

        List<LibraryItem> borrowed = borrowedItems.get(userId);
        if (!borrowed.contains(item)) {
            writeLine("Error: item was not borrowed.");
            return;
        }

        item.setBorrowed(false);
        item.setBorrowedBy(null);
        item.setBorrowedDate(null);
        borrowed.remove(item);
        writeLine(user.getName() + " successfully returned " + item.getTitle());
    }

    /**
     * Clears the penalty for a user.
     */
    public void payPenalty(String userId) {
        User user = findUserById(userId);
        if (user != null) {
            user.setHasPaidPenalty(true);
            user.setPenalty(0);
            penaltyBlockedUsers.remove(userId);
            writeLine(user.getName() + " has paid penalty");
        }
    }

    /**
     * Displays the information of all users.
     */
    public void displayUsers() {
        users.sort(Comparator.comparing(User::getId));
        boolean first = true;
        for (User user : users) {
            if (!first) {
                writeLine("");
            }
            first = false;
            String[] lines = user.getDetailedInfo().split("\n");
            for (String line : lines) {
                if (!line.startsWith("Penalty:") || user.getPenalty() > 0) {
                    writeLine(line);
                }
            }
        }
        writeLine("");
    }

    /**
     * Displays the information of all items.
     */
    public void displayItems() {
        items.sort(Comparator.comparing(LibraryItem::getId));
        for (int i = 0; i < items.size(); i++) {
            String[] lines = items.get(i).getDetailedInfo().split("\n");
            for (String line : lines) {
                writeLine(line);
            }
            if (i < items.size() - 1) {
                writeLine("");
            }
        }
    }

    /**
     * Finds a user by their ID.
     *
     * @param id User ID
     * @return User object if found, null otherwise
     */
    private User findUserById(String id) {
        for (User u : users) {
            if (u.getId().equals(id)) return u;
        }
        return null;
    }

    /**
     * Finds an item by its ID.
     *
     * @param id Item ID
     * @return LibraryItem object if found, null otherwise
     */
    private LibraryItem findItemById(String id) {
        for (LibraryItem i : items) {
            if (i.getId().equals(id)) return i;
        }
        return null;
    }
}