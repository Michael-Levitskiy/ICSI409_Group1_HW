import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class CYK{
    public static String input;
    public static HashMap<String, Set<String>> Production_Rules;
    public static String startingSymbol;

    /**
     * 
     * @param args
     */
    public static void main(String[] args) {
        
    }


    /**
     * Get the input string to check
     */
    public static void stringInput(){

    }

    /**
     * Get the Production rules for the CNF
     */
    public static void rulesInput(){

    }

    /**
     * 
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