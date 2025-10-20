package twitter;

import static org.junit.Assert.*;
import java.time.Instant;
import java.util.*;
import org.junit.Test;

public class SocialNetworkTest {

    private static final Instant time1 = Instant.parse("2020-01-01T10:00:00Z");
    private static final Instant time2 = Instant.parse("2020-01-01T11:00:00Z");
    private static final Instant time3 = Instant.parse("2020-01-01T12:00:00Z");

    private static final Tweet tweet1 = new Tweet(1, "alice", "Hello world", time1);
    private static final Tweet tweet2 = new Tweet(2, "bob", "@alice how are you?", time2);
    private static final Tweet tweet3 = new Tweet(3, "charlie", "@bob and @alice are great!", time3);
    private static final Tweet tweet4 = new Tweet(4, "alice", "@bob @charlie hi!", time3);
    private static final Tweet tweet5 = new Tweet(5, "bob", "no mentions here", time3);

    /* ------------------------ TESTS FOR guessFollowsGraph() ------------------------ */

    // 1. Empty list of tweets
    @Test
    public void testEmptyTweetsReturnsEmptyGraph() {
        Map<String, Set<String>> graph = SocialNetwork.guessFollowsGraph(Collections.emptyList());
        assertTrue("Expected empty graph", graph.isEmpty());
    }

    // 2. Tweets without mentions
    @Test
    public void testTweetsWithoutMentions() {
        List<Tweet> tweets = Arrays.asList(tweet1, tweet5);
        Map<String, Set<String>> graph = SocialNetwork.guessFollowsGraph(tweets);
        assertTrue("No mentions should produce empty graph", graph.isEmpty());
    }

    // 3. Single mention
    @Test
    public void testSingleMention() {
        List<Tweet> tweets = Arrays.asList(tweet2);
        Map<String, Set<String>> graph = SocialNetwork.guessFollowsGraph(tweets);
        assertTrue("bob should follow alice", graph.get("bob").contains("alice"));
        assertEquals(1, graph.get("bob").size());
    }

    // 4. Multiple mentions in one tweet
    @Test
    public void testMultipleMentions() {
        List<Tweet> tweets = Arrays.asList(tweet4);
        Map<String, Set<String>> graph = SocialNetwork.guessFollowsGraph(tweets);
        Set<String> follows = graph.get("alice");
        assertTrue(follows.contains("bob"));
        assertTrue(follows.contains("charlie"));
        assertEquals(2, follows.size());
    }

    // 5. Multiple tweets from one user
    @Test
    public void testMultipleTweetsFromOneUser() {
        Tweet t1 = new Tweet(10, "bob", "@alice hi", time1);
        Tweet t2 = new Tweet(11, "bob", "@charlie welcome", time2);
        List<Tweet> tweets = Arrays.asList(t1, t2);
        Map<String, Set<String>> graph = SocialNetwork.guessFollowsGraph(tweets);
        Set<String> follows = graph.get("bob");
        assertTrue(follows.contains("alice"));
        assertTrue(follows.contains("charlie"));
        assertEquals(2, follows.size());
    }

    /* ------------------------ TESTS FOR influencers() ------------------------ */

    // 6. Empty graph
    @Test
    public void testEmptyGraphInfluencers() {
        Map<String, Set<String>> followsGraph = new HashMap<>();
        List<String> influencers = SocialNetwork.influencers(followsGraph);
        assertTrue(influencers.isEmpty());
    }

    // 7. Single user without followers
    @Test
    public void testSingleUserWithoutFollowers() {
        Map<String, Set<String>> followsGraph = new HashMap<>();
        followsGraph.put("alice", new HashSet<>());
        List<String> influencers = SocialNetwork.influencers(followsGraph);
        assertTrue(influencers.contains("alice"));
        assertEquals(1, influencers.size());
    }

    // 8. Single influencer
    @Test
    public void testSingleInfluencer() {
        Map<String, Set<String>> followsGraph = new HashMap<>();
        followsGraph.put("bob", new HashSet<>(Arrays.asList("alice")));
        List<String> influencers = SocialNetwork.influencers(followsGraph);
        assertEquals("alice", influencers.get(0));
    }

    // 9. Multiple influencers with ordering
    @Test
    public void testMultipleInfluencersOrdering() {
        Map<String, Set<String>> followsGraph = new HashMap<>();
        followsGraph.put("bob", new HashSet<>(Arrays.asList("alice", "charlie")));
        followsGraph.put("dave", new HashSet<>(Arrays.asList("alice")));
        followsGraph.put("eve", new HashSet<>(Arrays.asList("charlie")));

        List<String> influencers = SocialNetwork.influencers(followsGraph);
        // alice has 2 followers, charlie has 2, alphabetical tie-breaker
        assertEquals("alice", influencers.get(0));
        assertEquals("charlie", influencers.get(1));
    }

    // 10. Tied influence count
    @Test
    public void testTiedInfluencers() {
        Map<String, Set<String>> followsGraph = new HashMap<>();
        followsGraph.put("bob", new HashSet<>(Arrays.asList("alice")));
        followsGraph.put("charlie", new HashSet<>(Arrays.asList("dave")));
        List<String> influencers = SocialNetwork.influencers(followsGraph);
        assertTrue(influencers.contains("alice"));
        assertTrue(influencers.contains("dave"));
    }
}
