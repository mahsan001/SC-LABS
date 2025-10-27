package twitter;

import java.util.*;

public class StringPermutations {

    // Generate permutations recursively. If includeDuplicates==false, only unique.
    public static List<String> generatePermutations(String str, boolean includeDuplicates) {
        List<String> result = new ArrayList<>();
        if (!includeDuplicates) 
            permuteUnique(str.toCharArray(), 0, result, new HashSet<>());
        else
            permute(str.toCharArray(), 0, result);
        return result;
    }

    private static void permuteUnique(char[] arr, int idx, List<String> res, Set<String> seen) {
        if (idx == arr.length - 1) {
            String p = new String(arr);
            if (seen.add(p)) res.add(p);
            return;
        }
        for (int i = idx; i < arr.length; i++) {
            swap(arr, idx, i);
            permuteUnique(arr, idx + 1, res, seen);
            swap(arr, idx, i);
        }
    }

    private static void permute(char[] arr, int idx, List<String> res) {
        if (idx == arr.length - 1) {
            res.add(new String(arr));
            return;
        }
        for (int i = idx; i < arr.length; i++) {
            swap(arr, idx, i);
            permute(arr, idx + 1, res);
            swap(arr, idx, i);
        }
    }

    private static void swap(char[] arr, int i, int j) {
        char tmp = arr[i];
        arr[i] = arr[j];
        arr[j] = tmp;
    }

    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println("Usage: java twitter.StringPermutations <string> <includeDuplicates:true|false>");
            return;
        }
        String input = args[0];
        boolean includeDuplicates = Boolean.parseBoolean(args[1]);
        List<String> permutations = generatePermutations(input, includeDuplicates);
        for (String p : permutations) System.out.println(p);
        System.out.println("Total: " + permutations.size());
    }
}
