import java.io.File;
import java.io.FileNotFoundException;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;

public class WordIndex {
    private static final int LINES_PER_PAGE = 45;

    public static void main(String[] args) throws FileNotFoundException {
        if (args.length != 1) {
            throw new IllegalArgumentException("Only one argument expected for the input file path!");
        }

        File inputFile = new File(args[0]);

        Scanner scanner = new Scanner(inputFile);

        processScanner(scanner).entrySet().stream()
                .sorted(Comparator.comparing(Map.Entry::getKey))
                .forEach(WordIndex::printEntry);
    }

    private static Map<String, Set<Integer>> processScanner(Scanner scanner) {
        Map<String, Set<Integer>> wordToPages = new HashMap<>();
        int lineIndex = 0;
        while (scanner.hasNextLine()) {
            lineIndex++;

            String line = scanner.nextLine();

            String[] words = line.split(" ");

            for (String word : words) {
                if (!word.isEmpty()) {
                    Set<Integer> pages = wordToPages.getOrDefault(word, new HashSet<>());
                    pages.add(lineIndexToPage(lineIndex));

                    wordToPages.put(word, pages);
                }
            }
        }

        return wordToPages;
    }

    static int lineIndexToPage(int lineIndex) {
        return lineIndex / (LINES_PER_PAGE + 1) + 1;
    }

    static <T> String joinCollection(Collection<T> collection) {
        Collection<String> stringCollection = collection.stream().map(Objects::toString).collect(Collectors.toList());
        return String.join(", ", stringCollection);
    }

    private static void printEntry(Map.Entry<String, Set<Integer>> wordOccurrences) {
        String string = String.format("%s %s", wordOccurrences.getKey(), joinCollection(wordOccurrences.getValue()));
        System.out.println(string);
    }
}
