import java.util.*;
import java.util.regex.*;
import java.io.*;

public class CYK {
    public static String input = null;
    public static String startingSymbol = null;
    public static HashMap<String, Set<String>> Production_Rules = new HashMap<>();

    /**
     * Main method to execute the CYK algorithm.
     * Prompts for a grammar file and input string, processes the grammar,
     * and determines if the string belongs to the language.
     * @param args Command-line arguments (not used)
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Prompt user for the filename
        System.out.println("Please provide a file containing the grammar in CNF.");
        System.out.println("Each rule should be on a separate line in the form 'LHS -> RHS'.");
        System.out.println("Use 'e' for epsilon, only for the starting symbol.");
        System.out.print("Enter the filename: ");
        String filename = scanner.nextLine();

        // Read the grammar from the provided file
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            rulesInput(reader);
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + filename);
            return;
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
            return;
        }

        // Console input for the string
        stringInput(scanner);
        scanner.close();

        if (CYK_Algorithm()) {
            System.out.println("\nThe string '" + input + "' IS in the language!");
        } else {
            System.out.println("\nThe string '" + input + "' is NOT in the language!");
        }
    }

    /**
     * Reads and processes grammar rules from a file.
     * Each rule is expected in the format 'LHS -> RHS' and is validated for CNF.
     * Populates the Production_Rules map with valid rules.
     * @param reader BufferedReader for reading the grammar file
     * @throws IOException If an error occurs while reading the file
     */
    public static void rulesInput(BufferedReader reader) throws IOException {
        boolean firstRule = true;
        String line;
        while ((line = reader.readLine()) != null) {
            line = line.trim();
            if (line.isEmpty()) continue; // Skip empty lines

            // Split on "->" with optional whitespace around it
            String[] parts = line.split("\\s*->\\s*");
            if (parts.length != 2) {
                System.out.println("Invalid rule format: " + line);
                continue;
            }

            String LHS = parts[0].trim();
            String RHS = parts[1].trim();

            // Check LHS
            if (!isNonTerminal(LHS)) {
                System.out.println("LHS must be a non-terminal: " + LHS);
                continue;
            }

            // Set the starting symbol
            if (firstRule) {
                startingSymbol = LHS;
                firstRule = false;
            }

            // Check RHS
            if (!isValidRHS(RHS, LHS)) {
                System.out.println("Invalid RHS: " + RHS);
                continue;
            }

            // Add the rule to production rules
            if (!Production_Rules.containsKey(LHS)) {
                Production_Rules.put(LHS, new HashSet<>());
            }
            Production_Rules.get(LHS).add(RHS);
        }
    }

    /**
     * Checks if a string represents a valid non-terminal.
     * A non-terminal is an uppercase letter followed by optional digits.
     * @param s String to check
     * @return true if the string is a valid non-terminal, false otherwise
     */
    private static boolean isNonTerminal(String s) {
        return s.matches("[A-Z]\\d*");
    }

    /**
     * Validates the right-hand side (RHS) of a grammar rule for CNF.
     * RHS must be a terminal, two non-terminals, or epsilon (for the starting symbol).
     * @param RHS Right-hand side of the rule
     * @param LHS Left-hand side of the rule
     * @return true if the RHS is valid, false otherwise
     */
    private static boolean isValidRHS(String RHS, String LHS) {
        // Case 1: RHS is the empty string ("e")
        if (RHS.equals("e")) {
            return LHS.equals(startingSymbol);
        }

        // Case 2: RHS is a single terminal (not uppercase)
        if (RHS.length() == 1 && !Character.isUpperCase(RHS.charAt(0))) {
            return true;
        }

        // Case 3: RHS must be two non-terminals
        Pattern pattern = Pattern.compile("^([A-Z]\\d*)([A-Z]\\d*)$");
        Matcher matcher = pattern.matcher(RHS);
        if (matcher.matches()) {
            String firstNonTerminal = matcher.group(1);
            String secondNonTerminal = matcher.group(2);
            return !firstNonTerminal.equals(startingSymbol) && !secondNonTerminal.equals(startingSymbol);
        }
        return false;
    }

    /**
     * Prompts the user to input a string to test against the grammar.
     * Stores the input string in the global 'input' variable.
     * @param scanner Scanner for reading console input
     */
    public static void stringInput(Scanner scanner) {
        System.out.print("\nInput string to test: ");
        input = scanner.nextLine();
    }

    /**
     * Method to run the CYK Algorithm
     * @return true if the string is in the language, false otherwise
     */
    public static boolean CYK_Algorithm() {
        // Handle empty string case
        if (input.isEmpty()) {
            return Production_Rules.getOrDefault(startingSymbol, new HashSet<>()).contains("e");
        }

        int length = input.length();
        Set<String>[][] CYK_Box = new HashSet[length][length];  // initialize the table

        // fill the diagonal of the table (all non-terminals that produce the given terminal)
        for (int i = 0; i < length; i++) {
            String letter = String.valueOf(input.charAt(i));
            CYK_Box[i][i] = getKeys(letter);
        }

        // fill the triangle with the correct non-terminal productions
        for (int i = 1; i < length; i++) {
            int row = i;
            int col = 0;

            // move diagonally across the table
            while (row < length && col < length) {
                CYK_Box[col][row] = new HashSet<>();

                // split the input at different positions
                for (int k = col; k < row; k++) {
                    Set<String> leftSet = CYK_Box[col][k];
                    Set<String> rightSet = CYK_Box[k + 1][row];
                    if (leftSet == null || rightSet == null) continue;
                    
                    // loops for every non-terminal pair combination
                    for (String A : leftSet) {
                        for (String B : rightSet) {
                            String combined = A + B;
                            Set<String> possibleNonTerminals = getKeys(combined);
                            CYK_Box[col][row].addAll(possibleNonTerminals);
                        }
                    }
                }
                row++;
                col++;
            }
        }
        return CYK_Box[0][length - 1] != null && CYK_Box[0][length - 1].contains(startingSymbol);
    }

    /**
     * Helper method to get all of the keys for a specific value in an array
     * @param value (String) - to find keys for
     * @return an array of all of the strings
     */
    private static Set<String> getKeys(String value) {
        Set<String> keys = new HashSet<>();
        for (Map.Entry<String, Set<String>> entry : Production_Rules.entrySet()) {
            if (entry.getValue().contains(value)) {
                keys.add(entry.getKey());
            }
        }
        return keys;
    }
}