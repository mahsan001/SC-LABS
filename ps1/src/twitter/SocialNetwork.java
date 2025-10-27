package twitter;

import java.util.*;

/**
 * SocialNetwork provides methods that operate on a social network.
 */
public class SocialNetwork {

    /**
     * Guess who might follow whom, from evidence found in tweets.
     *
     * @param tweets
     *            a list of tweets providing the evidence, not modified by this
     *            method.
     * @return a social network in which user A follows user B if and only if
     *         there is evidence for it in the given list of tweets.
     *         Evidence: A mentions B using @username in a tweet.
     *         All usernames are case-insensitive (stored in lowercase).
     */
    public static Map<String, Set<String>> guessFollowsGraph(List<Tweet> tweets) {
        Map<String, Set<String>> followsGraph = new HashMap<>();

        for (Tweet tweet : tweets) {
            String author = tweet.getAuthor().toLowerCase();
            Set<String> mentions = Extract.getMentionedUsers(Collections.singletonList(tweet));

            if (!followsGraph.containsKey(author)) {
                followsGraph.put(author, new HashSet<>());
            }

            for (String mentioned : mentions) {
                if (!mentioned.equals(author)) { // users cannot follow themselves
                    followsGraph.get(author).add(mentioned);
                }
            }
        }

        return followsGraph;
    }

    /**
     * Find the people in a social network who have the greatest influence.
     *
     * @param followsGraph
     *            a social network map of follower → set of followed users.
     * @return list of all usernames, in descending order of follower count.
     */
    public static List<String> influencers(Map<String, Set<String>> followsGraph) {
        Map<String, Integer> followerCounts = new HashMap<>();

        // count how many people follow each user
        for (String follower : followsGraph.keySet()) {
            for (String followed : followsGraph.get(follower)) {
                followed = followed.toLowerCase();
                followerCounts.put(followed, followerCounts.getOrDefault(followed, 0) + 1);
            }
            // ensure follower appears even if they have zero followers
            followerCounts.putIfAbsent(follower.toLowerCase(), 0);
        }

        // sort users by follower count descending
        List<String> influencers = new ArrayList<>(followerCounts.keySet());
        influencers.sort((u1, u2) -> {
            int cmp = followerCounts.get(u2) - followerCounts.get(u1);
            if (cmp == 0) return u1.compareTo(u2); // tie-break alphabetically
            return cmp;
        });

        return influencers;
    }
}
