package grille_cipher;

import java.util.*;

public class GrilleCipherEncryption {
    public static String GrilleEncryption(String text, String mask) {
        char[] maskArray = mask.toCharArray();
        char[] textArray = text.toUpperCase().replaceAll("[^A-Z]", "").toCharArray();
        Stack<Character> cipherTextStack = new Stack<>();
        int underscoreCount = 0;
        int xCount = 0;
        Random rand = new Random();
        int m = 0;

        for (char c : maskArray) {
            if (c != '_' && c != 'X') {
                return "Gabim: Maska lejon te permbajtje te karaktereve _ ose X !";
            }
            if (c == '_') underscoreCount++;
            if (c == 'X') xCount++;
        }
        if (underscoreCount == 0 || xCount == 0) {
            return "Gabim: Maska nuk mund të përmbajë vetëm '_' ose vetëm 'X'.";
        }
        if (textArray.length < 2) {
            return "Gabim: Fjala duhet të përmbajë të paktën 2 shkronja.";
        }

        Map<Character, Integer> requiredCounts = new HashMap<>();
        for (char c : textArray) {
            requiredCounts.put(c, requiredCounts.getOrDefault(c, 0) + 1);
        }

        Map<Character, Integer> seenCounts = new HashMap<>();
        boolean isComplete = false;
        while (!isComplete && m < textArray.length) {
            for (char maskChar : maskArray) {
                char pushedChar;
                boolean isFromTextArray = false;

                if (maskChar == '_' && m < textArray.length) {
                    pushedChar = textArray[m++];
                    isFromTextArray = true;
                } else {
                    pushedChar = (char) ('A' + rand.nextInt(26));
                }
                cipherTextStack.push(pushedChar);

                if (isFromTextArray && requiredCounts.containsKey(pushedChar)) {
                    int newCount = seenCounts.getOrDefault(pushedChar, 0) + 1;
                    seenCounts.put(pushedChar, newCount);
                }

                isComplete = true;
                for (Map.Entry<Character, Integer> entry : requiredCounts.entrySet()) {
                    if (seenCounts.getOrDefault(entry.getKey(), 0) < entry.getValue()) {
                        isComplete = false;
                        break;
                    }
                }

                if (isComplete) {
                    break;
                }
            }
        }

        StringBuilder result = new StringBuilder();
        for (char ch : cipherTextStack) {
            result.append(ch);
        }

        return result.toString();
    }

    public static String generateMask() {
        Random rand = new Random();
        StringBuilder mask;

        while (true) {
            mask = new StringBuilder();
            int countX = 0, countUnderscore = 0;

            for (int i = 0; i < 10; i++) {
                if (rand.nextBoolean()) {
                    mask.append('X');
                    countX++;
                } else {
                    mask.append('_');
                    countUnderscore++;
                }
            }

            if (countX > 0 && countUnderscore > 0) {
                break;
            }
        }

        return mask.toString();
    }
}

//     public static void main(String[] args){
//         Scanner scanner = new Scanner(System.in);

//         System.out.print("Plaintext: ");
//         String plaintext = scanner.nextLine().trim();

//         System.out.print("Key (shtyp Enter per te gjeneruar key): ");
//         String key = scanner.nextLine().trim();

//         if (key.isEmpty()) {
//             key = generateMask();
//             System.out.println("Key i gjeneruar: " + key);
//         }

//         System.out.println("Ciphertext: " + GrilleEncryption(plaintext, key));
//         scanner.close();
//     }
// }
