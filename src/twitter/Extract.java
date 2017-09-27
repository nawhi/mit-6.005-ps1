/* Copyright (c) 2007-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package twitter;

import java.util.List;
import java.util.ArrayList;
import java.time.Instant;
import java.util.Set;

/**
 * Extract consists of methods that extract information from a list of tweets.
 * 
 * DO NOT change the method signatures and specifications of these methods, but
 * you should implement their method bodies, and you may add new public or
 * private methods or classes if you like.
 */
public class Extract {

    /**
     * Get the time period spanned by tweets.
     * 
     * @param tweets
     *            list of tweets with distinct ids, not modified by this method.
     * @return a minimum-length time interval that contains the timestamp of
     *         every tweet in the list.
     */
	public static Timespan getTimespan(List<Tweet> tweets) {
        Instant min = tweets.get(0).getTimestamp();
        Instant max = tweets.get(0).getTimestamp();
        return getTimespanRec(tweets, min, max);
    }
    
	/**
	 * Recursive utility method for finding the time period
	 * spanned by tweets.
	 * 
	 * @param tweets 
	 * 			list of tweets with distinct ids, not modified
	 * @param min
	 * 			Instant object which keeps track of the minimum time
	 * @param max
	 * 			Instant object which keeps track of the maximum time
	 * @return a minimum-length time interval that contains the timestamp of
	 * 		every tweet in the list
	 */
    private static Timespan getTimespanRec(final List<Tweet> tweets, Instant min, Instant max) {
    	if (tweets.isEmpty())
    		return new Timespan(min, max);
    	Tweet t = tweets.get(0);
    	if (min.compareTo(t.getTimestamp()) >= 0)
    		min = t.getTimestamp();
    	if (max.compareTo(t.getTimestamp()) <= 0)
    		max = t.getTimestamp();
    	return getTimespanRec(tweets.subList(1, tweets.size()), min, max);
    			
    }

    /**
     * Get usernames mentioned in a list of tweets.
     * 
     * @param tweets
     *            list of tweets with distinct ids, not modified by this method.
     * @return the set of usernames who are mentioned in the text of the tweets.
     *         A username-mention is "@" followed by a Twitter username (as
     *         defined by Tweet.getAuthor()'s spec).
     *         The username-mention cannot be immediately preceded or followed by any
     *         character valid in a Twitter username.
     *         For this reason, an email address like bitdiddle@mit.edu does NOT 
     *         contain a mention of the username mit.
     *         Twitter usernames are case-insensitive, and the returned set may
     *         include a username at most once.
     */
    public static Set<String> getMentionedUsers(List<Tweet> tweets) {
        throw new RuntimeException("not implemented");
    }

}
