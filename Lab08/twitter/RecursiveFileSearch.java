package twitter;

import java.io.File;
import java.util.*;

public class RecursiveFileSearch {

    /**
     * Recursively searches files from root. (Supports multiple targets!)
     */
    public static void searchFiles(File root, Set<String> targets, boolean caseSensitive, Map<String, Integer> foundCounts) {
        if (root == null || !root.exists()) return;
        if (root.isFile()) {
            for (String target : targets) {
                if (caseSensitive) {
                    if (root.getName().equals(target)) {
                        foundCounts.put(target, foundCounts.getOrDefault(target, 0) + 1);
                        System.out.println("Found: " + root.getAbsolutePath());
                    }
                } else {
                    if (root.getName().equalsIgnoreCase(target)) {
                        foundCounts.put(target, foundCounts.getOrDefault(target, 0) + 1);
                        System.out.println("Found: " + root.getAbsolutePath());
                    }
                }
            }
        } else if (root.isDirectory()) {
            File[] children = root.listFiles();
            if (children != null) {
                for (File child : children) {
                    searchFiles(child, targets, caseSensitive, foundCounts);
                }
            }
        }
    }

    /**
     * Example main method to run search from command line.
     * Usage: java twitter.RecursiveFileSearch <dirPath> <true|false> <target1> [target2 ...]
     */
    public static void main(String[] args) {
        if (args.length < 3) {
            System.out.println("Usage: java twitter.RecursiveFileSearch <dirPath> <caseSensitive:true|false> <filename1> [filename2 ...]");
            return;
        }
        File root = new File(args[0]);
        boolean caseSensitive = Boolean.parseBoolean(args[1]);
        Set<String> targets = new HashSet<>();
        for (int i = 2; i < args.length; i++) targets.add(args[i]);
        Map<String, Integer> foundCounts = new HashMap<>();
        searchFiles(root, targets, caseSensitive, foundCounts);

        System.out.println("Summary:");
        for (String target : targets) {
            System.out.printf("File '%s' found %d times%n", target, foundCounts.getOrDefault(target, 0));
        }
    }
}

