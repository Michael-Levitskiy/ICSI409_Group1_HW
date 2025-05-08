import java.util.*;
import java.util.regex.*;

public class CYK {
    public static String input = null;
    public static String startingSymbol = null;
    public static HashMap<String, Set<String>> Production_Rules = new HashMap<>();

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        rulesInput(scanner);
        stringInput(scanner);
        scanner.close();
    }

    public static void rulesInput(Scanner scanner) {
        System.out.println("Input the grammar in CNF (Chomsky Normal Form)");
        System.out.println("1) Each rule should be entered with the LHS and RHS separated by \" -> \"");
        System.out.println("2) Use e instead ε to represent the empty string");
        System.out.println("3) Enter \"done\" to finalize the grammar");
        System.out.println();

        boolean firstRule = true;
        int ruleNumber = 1;

        while (true) {
            System.out.print("Rule " + ruleNumber + ":");
            String input = scanner.nextLine().trim();

            if (input.equalsIgnoreCase("done")) {
                break;
            }

            String[] parts = input.split(" -> ");
            if (parts.length != 2) {
                System.out.println("Invalid rule format. Use 'LHS -> RHS'.");
                continue;
            }

            String LHS = parts[0].trim();
            String RHS = parts[1].trim();

            if (!isNonTerminal(LHS)) {
                System.out.println("LHS must be a non-terminal: an uppercase letter followed by zero or more digits.");
                continue;
            }

            if (firstRule) {
                startingSymbol = LHS;
                firstRule = false;
            }

            if (!isValidRHS(RHS, LHS)) {
                System.out.println("Invalid RHS.");
                continue;
            }

            if (!Production_Rules.containsKey(LHS)) {
                Production_Rules.put(LHS, new HashSet<>());
            }
            Production_Rules.get(LHS).add(RHS);

            ruleNumber++;
        }
    }

    private static boolean isNonTerminal(String s) {
        return s.matches("[A-Z]\\d*");
    }

    private static boolean isValidRHS(String RHS, String LHS) {
        // Case 1: RHS is the empty string ("e")
        if (RHS.equals("e")) {
            return LHS.equals(startingSymbol); // Valid only if LHS is the start symbol
        }

        // Case 2: RHS is a single terminal (not uppercase)
        if (RHS.length() == 1 && !Character.isUpperCase(RHS.charAt(0))) {
            return true; // Valid terminal
        }

        // Case 3: RHS must be two non-terminals
        Pattern pattern = Pattern.compile("^([A-Z]\\d*)([A-Z]\\d*)$");
        Matcher matcher = pattern.matcher(RHS);
        if (matcher.matches()) {
            String firstNonTerminal = matcher.group(1);
            String secondNonTerminal = matcher.group(2);
            // Valid if neither is the start symbol
            return !firstNonTerminal.equals(startingSymbol) && !secondNonTerminal.equals(startingSymbol);
        }

        // If none of the above cases match, RHS is invalid
        return false;
    }

    public static void stringInput(Scanner scanner) {
        System.out.print("Input string to test: ");
        input = scanner.nextLine();
    }

    /**
     * Method to run the CYK Algorithm
     * @return
     */
    public static boolean CYK_Algorithm(){
        int length = input.length();
        Set<String>[][] CYB_Box = new HashSet[length][length];

        for (int i = 0; i < length; i++){
            String letter = String.valueOf(input.charAt(i));
            CYB_Box[i][i] = getKeys(letter);
        }

        // iterate through all of the boxes in a diagonal pattern
        for (int i = 1; i < length; i++){
            int row = i;
            int col = 0;

            while (row < length && col < length){
                CYB_Box[col][row] = new HashSet<>();

                for (int k = col; k < row; k++) {
                    Set<String> leftSet = CYB_Box[col][k];
                    Set<String> rightSet = CYB_Box[k + 1][row];
                
                    if (leftSet == null || rightSet == null) continue;
                
                    for (String A : leftSet) {
                        for (String B : rightSet) {
                            String combined = A + B;
                            Set<String> possibleNonTerminals = getKeys(combined);
                            CYB_Box[col][row].addAll(possibleNonTerminals);
                        }
                    }
                }
                row++;
                col++;
            }
        }
        return CYB_Box[0][length - 1] != null && CYB_Box[0][length - 1].contains(startingSymbol);
    }

    /**
     * Helper method to get all of the keys for a specific value in an array
     * @param value (String) - to find keys for
     * @return an array of all of the strings
     */
    private static Set<String> getKeys(String value){
        Set<String> keys = new HashSet<>();
        for (Map.Entry<String, Set<String>> entry : Production_Rules.entrySet()) {
            if (entry.getValue().contains(value)) {
                keys.add(entry.getKey());
            }
        }
        return keys;
    }
}