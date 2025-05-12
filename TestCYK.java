// File: TestCYK.java


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;


public class TestCYK {
    public static void main(String[] args) {
        // Record to hold each test case, CNF rules, input string, expected results
        record Case(List<String> rules, String input, boolean expected) {}
 
 
        var tests = List.of(
      		new Case(readRulesFromFile("Q1_Rules.txt"), "(()())", true),
      		new Case(readRulesFromFile("Q1_Rules.txt"), "(())", true),
			new Case(readRulesFromFile("Q1_Rules.txt"), "((())", false),
      		new Case(readRulesFromFile("Q2_Rules.txt"), "aaabbbc", true),
      		new Case(readRulesFromFile("Q2_Rules.txt"), "aabbbbcccc", true),
			new Case(readRulesFromFile("Q2_Rules.txt"), "aabbbbbcc", false),
      		new Case(readRulesFromFile("Q3_Rules.txt"), "aba#ab#bb", false),
      		new Case(readRulesFromFile("Q3_Rules.txt"), "abba#abba", true),
			new Case(readRulesFromFile("Q3_Rules.txt"), "ab#ab#bbba", false),
      		new Case(readRulesFromFile("Q4_Rules.txt"), "#", true),
      		new Case(readRulesFromFile("Q4_Rules.txt"), "0000", false),
			new Case(readRulesFromFile("Q4_Rules.txt"), "0000##000000", true)
 		); 
 
 
 
        int passed = 0;
        for (var c : tests) {
            CYK.Production_Rules.clear();
            CYK.startingSymbol = null;
            boolean first = true;
            for (var r : c.rules) {
                var parts = r.split("->");
                String L = parts[0].trim();
                String R = parts[1].trim();
                if (first) {
                    CYK.startingSymbol = L;
                    first = false;
                }
                CYK.Production_Rules.computeIfAbsent(L, k -> new HashSet<>()).add(R);
            }
 
 
            CYK.input = c.input;
            boolean result = c.input.isEmpty()
                    ? CYK.Production_Rules.getOrDefault(CYK.startingSymbol, Collections.emptySet()).contains("e")
                    : CYK.CYK_Algorithm();
            boolean ok = result == c.expected;
            System.out.printf("Input %-10s : %s%n", c.input.isEmpty() ? "<empty>" : c.input, ok ? "PASS" : "FAIL");
            if (ok) passed++;
        }
        System.out.printf("Passed %d/%d tests%n", passed, tests.size());
    }


   /**
    * Helper Method to read in all of the rules
    * @param fileName
    * @return
    */
   public static List<String> readRulesFromFile(String fileName) {
    	try {
    	    return Files.readAllLines(Paths.get(fileName)).stream()
    	            .filter(line -> !line.trim().isEmpty())
    	            .collect(Collectors.toList());
    	} catch (IOException e) {
    	    throw new RuntimeException("Error reading rules from file: " + fileName, e);
    	}
	}

}