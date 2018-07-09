/* Copyright (c) 2007-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package twitter;

import java.util.List;
import java.time.Instant;
import java.util.ArrayList;

/**
 * Filter consists of methods that filter a list of tweets for those matching a
 * condition.
 * 
 * DO NOT change the method signatures and specifications of these methods, but
 * you should implement their method bodies, and you may add new public or
 * private methods or classes if you like.
 */
public class Filter {

    /**
     * Find tweets written by a particular user.
     * 
     * @param tweets
     *            a list of tweets with distinct ids, not modified by this method.
     * @param username
     *            Twitter username, required to be a valid Twitter username as
     *            defined by Tweet.getAuthor()'s spec.
     * @return all and only the tweets in the list whose author is username,
     *         in the same order as in the input list.
     */
    public static List<Tweet> writtenBy(List<Tweet> tweets, String username) {
        List<Tweet> ret = new ArrayList<>();
    	for (Tweet t: tweets)
        {
        	if (t.getAuthor() == username)
        		ret.add(t);
        }
    	return ret;
    }

    /**
     * Find tweets that were sent during a particular timespan.
     * 
     * @param tweets
     *            a list of tweets with distinct ids, not modified by this method.
     * @param timespan
     *            timespan
     * @return all and only the tweets in the list that were sent during the timespan,
     *         in the same order as in the input list.
     */
    public static List<Tweet> inTimespan(List<Tweet> tweets, Timespan timespan) {
        Instant tStart = timespan.getStart();
        Instant tEnd = timespan.getEnd();
        List<Tweet> results = new ArrayList<>();
    	for (Tweet t: tweets)
        {
        	Instant timestamp = t.getTimestamp(); 
    		if (!timestamp.isBefore(tStart) && !timestamp.isAfter(tEnd))
    			results.add(t);
        }
    	return results;
    	
    }
    
    /**
     * Find tweets that contain certain words.
     * 
     * @param tweets
     *            a list of tweets with distinct ids, not modified by this method.
     * @param words
     *            a list of words to search for in the tweets. 
     *            A word is a nonempty sequence of nonspace characters.
     * @return all and only the tweets in the list such that the tweet text (when 
     *         represented as a sequence of nonempty words bounded by space characters 
     *         and the ends of the string) includes *at least one* of the words 
     *         found in the words list. Word comparison is not case-sensitive,
     *         so "Obama" is the same as "obama".  The returned tweets are in the
     *         same order as in the input list.
     */
    public static List<Tweet> containing(List<Tweet> tweets, List<String> words) {
        List<Tweet> results = new ArrayList<>();
        for (Tweet t: tweets)
        {
        	String text = t.getText();
        	for (String word: words)
        	{
        		if (findWord(word, text))
        		{
        			results.add(t);
        			break; // only need 1 match
        		}
        	}
        }
        return results;
    }
    
    /**
     * Find a word in a longer string of text
     * NB: case-insensitive.
     * @param word The word to find.
     * @param text The long text string in which to search for occurrences
     * of the word.
     * @return true if one or more occurrences of word was found in text.
     */
    private static boolean findWord(String word, String text) {
    	word = word.toLowerCase();
    	text = text.toLowerCase();
    	// Very lazy (and SLOW) first implementation
    	return
    		// word is exactly text
    		word.equals(text)
    		||
    		// word is at very start of text
    		word.equals(text.substring(0, word.length()) + " ")
    		|| 
    		// word is at very end of text	
    		word.equals(" " + text.substring(text.length()-word.length(), text.length()))
    		||
    		// word is in text separated by spaces
    		text.contains(" " + word + " ");
    }

}
