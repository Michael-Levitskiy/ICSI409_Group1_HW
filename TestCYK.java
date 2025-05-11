// File: TestCYK.java

import java.util.*;

public class TestCYK {
    public static void main(String[] args) {
        // Record to hold each test case, CNF rules, input string, expected results
        record Case(List<String> rules, String input, boolean expected) {}

        var tests = List.of(
                // 1) Balanced parentheses CNF (friend's rules)
                new Case(
                        List.of("S0->Z1X2",
                                "S0->e",
                                "Z1->X1S0",
                                "Z1->(",
                                "X1->(",
                                "X2->)"),
                        "(())", true
                ),
                new Case(
                        List.of("S0->Z1X2",
                                "S0->e",
                                "Z1->X1S0",
                                "Z1->(",
                                "X1->(",
                                "X2->)"),
                        "(()", false
                ),


                // 2) {a^i b^j c^k | i=j or j=k} CNF \

                new Case(
                        List.of(
                                "S->AC",
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
                                "Y3->c"
                        ),
                        "aaabbbc", false
                ),

                new Case(
                        List.of(
                                "S->AC",
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
                                "Y3->c"
                        ),
                        "aaabbbccc", false
                ),

                // 3) Palindrome-list CNF (Sipser §2.6d)
                new Case(
                        List.of(
                                "S0->X2S",
                                "S0->X5S",
                                "S0->AZ",
                                "S0->BZ",
                                "S0->a",
                                "S0->b",
                                "S0->e",
                                "S->X2S",
                                "S->X5S",
                                "S->AZ",
                                "S->BZ",
                                "S->a",
                                "S->b",
                                "Z->AZ",
                                "Z->BZ",
                                "Z->a",
                                "Z->b",
                                "Y->a",
                                "Y->b",
                                "Y->X3A",
                                "Y->X4B",
                                "X1->YH",
                                "X1->#",
                                "X2->ZH",
                                "X2->#",
                                "X3->AY",
                                "X3->a",
                                "X4->BY",
                                "X4->b",
                                "X5->X1X2",
                                "A->a",
                                "B->b",
                                "H->#"
                        ),
                        "ab#ba", true
                ),
                new Case(
                        List.of(
                                "S0->X2S",
                                "S0->X5S",
                                "S0->AZ",
                                "S0->BZ",
                                "S0->a",
                                "S0->b",
                                "S0->e",
                                "S->X2S",
                                "S->X5S",
                                "S->AZ",
                                "S->BZ",
                                "S->a",
                                "S->b",
                                "Z->AZ",
                                "Z->BZ",
                                "Z->a",
                                "Z->b",
                                "Y->a",
                                "Y->b",
                                "Y->X3A",
                                "Y->X4B",
                                "X1->YH",
                                "X1->#",
                                "X2->ZH",
                                "X2->#",
                                "X3->AY",
                                "X3->a",
                                "X4->BY",
                                "X4->b",
                                "X5->X1X2",
                                "A->a",
                                "B->b",
                                "H->#"
                        ),
                        "ab#ab", false
                ),
                new Case(
                        List.of(
                                "S0->X2S",
                                "S0->X5S",
                                "S0->AZ",
                                "S0->BZ",
                                "S0->a",
                                "S0->b",
                                "S0->e",
                                "S->X2S",
                                "S->X5S",
                                "S->AZ",
                                "S->BZ",
                                "S->a",
                                "S->b",
                                "Z->AZ",
                                "Z->BZ",
                                "Z->a",
                                "Z->b",
                                "Y->a",
                                "Y->b",
                                "Y->X3A",
                                "Y->X4B",
                                "X1->YH",
                                "X1->#",
                                "X2->ZH",
                                "X2->#",
                                "X3->AY",
                                "X3->a",
                                "X4->BY",
                                "X4->b",
                                "X5->X1X2",
                                "A->a",
                                "B->b",
                                "H->#"
                        ),
                        "aba#aba", false
                ),



        // 4) Sipser 2.13 CNF
        new Case(
                List.of(
                        "S->TT",
                        "S->X1X2",
                        "S->#",
                        "T->Z1T",
                        "T->TZ1",
                        "T->#",
                        "U->X1X2",
                        "U->#",
                        "X1->Z1U",
                        "X2->Z1Z1",
                        "Z1->0"
                ),
                "0", true
        ),

                new Case(
                        List.of(
                                "S->TT",
                                "S->X1X2",
                                "S->#",
                                "T->Z1T",
                                "T->TZ1",
                                "T->#",
                                "U->X1X2",
                                "U->#",
                                "X1->Z1U",
                                "X2->Z1Z1",
                                "Z1->0"
                        ),
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
                if (first) { CYK.startingSymbol = L; first = false; }
                CYK.Production_Rules.computeIfAbsent(L, k -> new HashSet<>()).add(R);
            }
            CYK.input = c.input;
            boolean result = c.input.isEmpty()
                    ? CYK.Production_Rules.getOrDefault(CYK.startingSymbol, Collections.emptySet()).contains("e")
                    : CYK.CYK_Algorithm();
            boolean ok = result == c.expected;
            System.out.printf("Input %-10s : %s%n", c.input.isEmpty()?"<empty>":c.input, ok?"PASS":"FAIL");
            if (ok) passed++;
        }
        System.out.printf("Passed %d/%d tests%n", passed, tests.size());
    }
}