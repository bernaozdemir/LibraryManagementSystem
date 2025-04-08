import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * Entry point of the Library Management System application.
 * Reads data from command line arguments and processes library commands.
 */
public class Main {
    /**
     * Main method that initializes the library system and processes commands.
     *
     * @param args Array of command-line arguments: items file, users file, commands file, and output file.
     */
    public static void main(String[] args) {
        // Check if the correct number of arguments is passed
        if (args.length != 4) {
            System.out.println("Usage: java Main <itemsFile> <usersFile> <commandsFile> <outputFile>");
            return;
        }

        // Assign arguments to respective file variables
        String itemsFile = args[0];
        String usersFile = args[1];
        String commandsFile = args[2];
        String outputFile = args[3];

        // Create an instance of LibraryManagementSystem with output file
        LibraryManagementSystem library = new LibraryManagementSystem(outputFile);

        // Load users and items from input files
        library.loadUsersFromFile(usersFile);
        library.loadItemsFromFile(itemsFile);

        // Process each command from the commands file
        try (BufferedReader br = new BufferedReader(new FileReader(commandsFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                library.processCommand(line);
            }
        } catch (IOException e) {
            library.writeLine("Error reading command file: " + commandsFile);
        }

        // Close the output file
        library.closeOutput();
    }
}
