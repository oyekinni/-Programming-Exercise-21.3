import java.io.File;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class CountKeywords {

    public static void main(String[] args) throws Exception {

        if (args.length != 1) {
            System.out.println("Usage: java CountKeywords <JavaSourceFile>");
            return;
        }

        File file = new File(args[0]);

        if (!file.exists()) {
            System.out.println("File not found.");
            return;
        }

        // Java keywords
        String[] keywordArray = {
            "abstract","assert","boolean","break","byte","case","catch","char",
            "class","const","continue","default","do","double","else","enum",
            "extends","final","finally","float","for","goto","if","implements",
            "import","instanceof","int","interface","long","native","new",
            "package","private","protected","public","return","short","static",
            "strictfp","super","switch","synchronized","this","throw","throws",
            "transient","try","void","volatile","while"
        };

        Set<String> keywords = new HashSet<>();
        for (String k : keywordArray) {
            keywords.add(k);
        }

        int count = 0;

        boolean inBlockComment = false;
        boolean inString = false;

        Scanner input = new Scanner(file);

        while (input.hasNextLine()) {
            String line = input.nextLine();

            // handle line comments
            if (!inBlockComment && !inString) {
                int index = line.indexOf("//");
                if (index != -1) {
                    line = line.substring(0, index);
                }
            }

            for (int i = 0; i < line.length(); i++) {
                char ch = line.charAt(i);

                // handle block comments
                if (!inString && i < line.length() - 1) {
                    if (!inBlockComment && ch == '/' && line.charAt(i + 1) == '*') {
                        inBlockComment = true;
                        i++;
                        continue;
                    }
                    if (inBlockComment && ch == '*' && line.charAt(i + 1) == '/') {
                        inBlockComment = false;
                        i++;
                        continue;
                    }
                }

                if (inBlockComment) continue;

                // handle strings
                if (ch == '"') {
                    inString = !inString;
                    continue;
                }

                if (inString) continue;
            }

            // split remaining valid code into words
            String[] tokens = line.split("[^a-zA-Z]+");

            for (String token : tokens) {
                if (keywords.contains(token)) {
                    count++;
                }
            }
        }

        input.close();

        System.out.println("Total number of keywords: " + count);
    }
}
