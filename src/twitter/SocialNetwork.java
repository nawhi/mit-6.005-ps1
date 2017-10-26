/* Copyright (c) 2007-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package twitter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

class Influencer {
	public String name;
	public int followers;
	public Influencer(String name) {
		this.name = name;
		this.followers = 0;
	}
	public Influencer(String name, int followers) {
		this.name = name;
		this.followers = followers;
	}
};
class ReverseSortByFollowers implements Comparator<Influencer> {
	public int compare(Influencer a, Influencer b) {
		return -1 * ((Integer)a.followers).compareTo(b.followers);
	}
}

/**
 * SocialNetwork provides methods that operate on a social network.
 * 
 * A social network is represented by a Map<String, Set<String>> where map[A] is
 * the set of people that person A follows on Twitter, and all people are
 * represented by their Twitter usernames. Users can't follow themselves. If A
 * doesn't follow anybody, then map[A] may be the empty set, or A may not even exist
 * as a key in the map; this is true even if A is followed by other people in the network.
 * Twitter usernames are not case sensitive, so "ernie" is the same as "ERNie".
 * A username should appear at most once as a key in the map or in any given
 * map[A] set.
 * 
 * DO NOT change the method signatures and specifications of these methods, but
 * you should implement their method bodies, and you may add new public or
 * private methods or classes if you like.
 */
public class SocialNetwork {

    /**
     * Guess who might follow whom, from evidence found in tweets.
     * 
     * @param tweets
     *            a list of tweets providing the evidence, not modified by this
     *            method.
     * @return a social network (as defined above) in which Ernie follows Bert
     *         if and only if there is evidence for it in the given list of
     *         tweets.
     *         One kind of evidence that Ernie follows Bert is if Ernie
     *         @-mentions Bert in a tweet. This must be implemented. Other kinds
     *         of evidence may be used at the implementor's discretion.
     *         All the Twitter usernames in the returned social network must be
     *         either authors or @-mentions in the list of tweets.
     */
    public static Map<String, Set<String>> guessFollowsGraph(List<Tweet> tweets) {
        Map<String, Set<String>> results = new HashMap<>();
        for (Tweet t: tweets)
        {
        	String author = t.getAuthor();
        	Set<String> mentionedUsers = Extract.getMentionedUsers(t);
        	if (mentionedUsers.isEmpty())
        		continue;
        	Set<String> currentFollows = results.get(author);
        	if (currentFollows == null)
        		results.put(author, mentionedUsers);
        	else
        		currentFollows.addAll(mentionedUsers);
        }
        return results;
    }
    
    /**
     * Find the people in a social network who have the greatest influence, in
     * the sense that they have the most followers.
     * 
     * @param followsGraph
     *            a social network (as defined above)
     * @return a list of all distinct Twitter usernames in followsGraph, in
     *         descending order of follower count.
     */
    public static List<String> influencers(Map<String, Set<String>> followsGraph) {
        Map<String, Influencer> influencers = new HashMap<>();
        
        for (String followerName: followsGraph.keySet())
        {
        	influencers.put(followerName, new Influencer(followerName));
        	for (String followee: followsGraph.get(followerName))
        	{
        		// Record that followee has a new follower
        		if (influencers.get(followee) != null)
        			influencers.get(followee).followers++;
        		else
        			influencers.put(followee, new Influencer(followee, 1));
    		}
        }
        
        List<Influencer> results = new ArrayList<Influencer>();
        for (Influencer i : influencers.values())
        	results.add(i);
        
        Collections.sort(results, new ReverseSortByFollowers());
        List<String> ret = new ArrayList<>();
        for (Influencer i: results)
        	ret.add(i.name);
        return ret;
    }

}
