package twitter;

import static org.junit.Assert.*;
import org.junit.Test;

import java.io.File;
import java.util.*;

public class RecursiveProgramTest {

    @Test
    public void testPermutationsUnique() {
        List<String> perms = StringPermutations.generatePermutations("aab", false);
        List<String> expected = Arrays.asList("aab", "aba", "baa");
        assertTrue(perms.containsAll(expected));
        assertEquals(3, perms.size());
    }

    @Test
    public void testPermutationsWithDuplicates() {
        List<String> perms = StringPermutations.generatePermutations("aba", true);
        // Should have 6 results: aab, aba, baa, aab, aba, baa (duplicates allowed)
        assertEquals(6, perms.size());
    }

    @Test
    public void testRecursiveFileSearchOnFile() {
        try {
            // Create a temporary file for testing
            File temp = File.createTempFile("tempTestFile", ".txt");
            temp.deleteOnExit();
            Set<String> targets = new HashSet<>(Arrays.asList(temp.getName()));
            Map<String, Integer> foundCounts = new HashMap<>();
            RecursiveFileSearch.searchFiles(temp, targets, true, foundCounts);
            assertEquals(1, foundCounts.getOrDefault(temp.getName(), 0).intValue());
        } catch (Exception e) {
            fail("Exception during file search test: " + e);
        }
    }

    @Test
    public void testRecursiveFileSearchOnNonExistentFile() {
        File fakeFile = new File("definitely_not_a_real_file_123456789.txt");
        Set<String> targets = new HashSet<>(Collections.singleton(fakeFile.getName()));
        Map<String, Integer> foundCounts = new HashMap<>();
        RecursiveFileSearch.searchFiles(fakeFile, targets, true, foundCounts);
        assertEquals(0, foundCounts.getOrDefault(fakeFile.getName(), 0).intValue());
    }
}
