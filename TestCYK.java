// File: TestCYK.java


import java.util.*;


public class TestCYK {
   public static void main(String[] args) {
       // Record to hold each test case, CNF rules, input string, expected results
       record Case(List<String> rules, String input, boolean expected) {}


       var tests = List.of(
               // Question 1: Balanced Parentheses CNF
               new Case(
                       List.of("S0->Z1X2",
                               "S0->Z1Z2",
                               "S0->e",
                               "S->Z1X2",
                               "S->Z1Z2",
                               "Z1->X1S",
                               "Z1->(",
                               "Z2->X2S",
                               "Z2->)",
                               "X1->(",
                               "X2->)"),
                       "(()())", false
               ),
               new Case(
                       List.of("S0->Z1X2",
                               "S0->Z1Z2",
                               "S0->e",
                               "S->Z1X2",
                               "S->Z1Z2",
                               "Z1->X1S",
                               "Z1->(",
                               "Z2->X2S",
                               "Z2->)",
                               "X1->(",
                               "X2->)"),
                       "(())", true
               ),


               // Question 2: a^i b^j c^k CNF
               new Case(
                       List.of("S->AC",
                               "S->A1C1",
                               "S->e",
                               "A->Z1Z2",
                               "C->CX1",
                               "Z1->AX2",
                               "Z2->BX3",
                               "X1->c",
                               "X2->c",
                               "X3->b",
                               "C1->W1W2",
                               "A1->A1Y1",
                               "W1->BY2",
                               "W2->CY3",
                               "Y1->a",
                               "Y2->b",
                               "Y3->c"),
                       "aaabbbc", false
               ),
               new Case(
                       List.of("S->AC",
                               "S->A1C1",
                               "S->e",
                               "A->Z1Z2",
                               "C->CX1",
                               "Z1->AX2",
                               "Z2->BX3",
                               "X1->c",
                               "X2->c",
                               "X3->b",
                               "C1->W1W2",
                               "A1->A1Y1",
                               "W1->BY2",
                               "W2->CY3",
                               "Y1->a",
                               "Y2->b",
                               "Y3->c"),
                       "aaabbbccc", false
               ),


               // Question 3: Palindrome-related CNF
               new Case(
                       List.of("S->X1C",
                               "S->XaC",
                               "S->AB",
                               "S->DH",
                               "S->#",
                               "S->Z1W",
                               "S->Z2Y",
                               "S->a",
                               "S->b",
                               "S->AB1",
                               "S->Z3W",
                               "S->Z4Y",
                               "S->CH",
                               "S->e",
                               "B->Z1W",
                               "B->ZaY",
                               "B->a",
                               "B->b",
                               "B1->Z3W",
                               "B1->Z4Y",
                               "B1->CH",
                               "B1->#",
                               "A->DH",
                               "A->#",
                               "D->WD",
                               "D->YD",
                               "D->DH",
                               "C->HF",
                               "C->#",
                               "F->FW",
                               "F->FY",
                               "F->HF",
                               "X1->AB",
                               "X1->DH",
                               "X1->#",
                               "X1->Z1W",
                               "X1->Z2Y",
                               "X1->a",
                               "X1->b",
                               "X2->AB1",
                               "X2->Z3W",
                               "X2->Z4Y",
                               "X2->CH",
                               "X2->#",
                               "Z1->WB",
                               "Z1->a",
                               "Z2->YB",
                               "Z2->b",
                               "Z3->WB1",
                               "Z4->YB1",
                               "W->a",
                               "Y->b",
                               "H->#"),
                       "aba#aba", false
               ),
               new Case(
                       List.of("S->X1C",
                               "S->XaC",
                               "S->AB",
                               "S->DH",
                               "S->#",
                               "S->Z1W",
                               "S->Z2Y",
                               "S->a",
                               "S->b",
                               "S->AB1",
                               "S->Z3W",
                               "S->Z4Y",
                               "S->CH",
                               "S->e",
                               "B->Z1W",
                               "B->ZaY",
                               "B->a",
                               "B->b",
                               "B1->Z3W",
                               "B1->Z4Y",
                               "B1->CH",
                               "B1->#",
                               "A->DH",
                               "A->#",
                               "D->WD",
                               "D->YD",
                               "D->DH",
                               "C->HF",
                               "C->#",
                               "F->FW",
                               "F->FY",
                               "F->HF",
                               "X1->AB",
                               "X1->DH",
                               "X1->#",
                               "X1->Z1W",
                               "X1->Z2Y",
                               "X1->a",
                               "X1->b",
                               "X2->AB1",
                               "X2->Z3W",
                               "X2->Z4Y",
                               "X2->CH",
                               "X2->#",
                               "Z1->WB",
                               "Z1->a",
                               "Z2->YB",
                               "Z2->b",
                               "Z3->WB1",
                               "Z4->YB1",
                               "W->a",
                               "Y->b",
                               "H->#"),
                       "abba#abba", false
               ),


               // Question 4: Sipser 2.13 CNF
               new Case(
                       List.of("S->TT",
                               "S->X1X2",
                               "S->#",
                               "T->Z1T",
                               "T->TZ1",
                               "T->#",
                               "U->X1X2",
                               "U->#",
                               "X1->Z1U",
                               "X2->Z1Z1",
                               "Z1->0"),
                       "0", true
               ),
               new Case(
                       List.of("S->TT",
                               "S->X1X2",
                               "S->#",
                               "T->Z1T",
                               "T->TZ1",
                               "T->#",
                               "U->X1X2",
                               "U->#",
                               "X1->Z1U",
                               "X2->Z1Z1",
                               "Z1->0"),
                       "0000", true
               )
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
}

