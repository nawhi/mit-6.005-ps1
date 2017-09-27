/* Copyright (c) 2007-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package twitter;

import static org.junit.Assert.*;

import java.time.Instant;
import java.util.Arrays;
import java.util.Set;

import org.junit.Test;

public class ExtractTest {

    private static final Instant d1 = Instant.parse("2016-02-17T10:00:00Z");
    private static final Instant d2 = Instant.parse("2016-02-17T11:00:00Z");
    private static final Instant d3 = Instant.parse("2017-02-17T10:00:00Z");
    private static final Instant d4 = Instant.parse("2017-02-17T11:00:00Z");
    
    private static final Tweet tweet1 = new Tweet(
    		1, "alyssa", "is it reasonable to talk about rivest so much?", d1);
    private static final Tweet tweet2 = new Tweet(
    		2, "bbitdiddle", "rivest talk in 30 minutes #hype", d2);
    private static final Tweet tweet3 = new Tweet(
    		3, "DeepakChopra", "Consciousness is a superposition of possibilities", d3);
    private static final Tweet tweet4 = new Tweet(
    		4, "realDonaldTrump", "Despite the constant negative press covfefe", d4);
    private static final Tweet tweet5 = new Tweet(
    		5, "fakeRonaldDump", "Because of the infrequent positive press efefvoc", d4);
    
    @Test(expected=AssertionError.class)
    public void testAssertionsEnabled() {
        assert false; // make sure assertions are enabled with VM argument: -ea
    }
    
    /*
     * Partition 1: one tweet
     */
    @Test
    public void testGetTimespanOneTweet() {
    	Timespan timespan = Extract.getTimespan(Arrays.asList(tweet1));
    	assertEquals("expected start", d1, timespan.getStart());
    	assertEquals("expected end", d1, timespan.getEnd());
    }
    
    /*
     * Partition 2: two tweets with different times 
     */
    @Test
    public void testGetTimespanTwoTweets() {
        Timespan timespan = Extract.getTimespan(Arrays.asList(tweet1, tweet2));
        assertEquals("expected start", d1, timespan.getStart());
        assertEquals("expected end", d2, timespan.getEnd());
    }
    
    /*
     * Partition 3: two tweets at the same time
     */
    @Test
    public void testGetTimespanSimultaneousTweets() {
    	Timespan timespan = Extract.getTimespan(Arrays.asList(tweet4, tweet5));
    	assertEquals("expected start", d4, timespan.getStart());
    	assertEquals("expected end", d4, timespan.getEnd());
    }
    
    /*
     * Partition 4: three tweets with different times
     */
    @Test
    public void testGetTimespanThreeTweets() {
    	Timespan timespan = Extract.getTimespan(Arrays.asList(tweet1, tweet3, tweet5));
    	assertEquals("expected start", d1, timespan.getStart());
    	assertEquals("expected end", d3, timespan.getEnd());
    }
    
    /*
     * Partition 5: three tweets, two of which were at the same time
     */
    @Test 
    public void testGetTimespanThreeTweetsIncSimultaneous() {
    	Timespan timespan = Extract.getTimespan(Arrays.asList(tweet3, tweet4, tweet5));
    	assertEquals("expected start", d3, timespan.getStart());
    	assertEquals("expected end", d4, timespan.getEnd());
    }
    
    /*
     * getMentionedUsers partitions:
     * 1. tweets with no mentioned users
     * 2. @ followed by non-username char
     * 3. @ preceded by username char
     * 4. two mentions of the same username with different capitalisations
     * 5. >1 different usernames mentioned
     */
    @Test
    public void testGetMentionedUsersNoMention() {
        Set<String> mentionedUsers = Extract.getMentionedUsers(Arrays.asList(tweet1));
        
        assertTrue("expected empty set", mentionedUsers.isEmpty());
    }

    /*
     * Warning: all the tests you write here must be runnable against any
     * Extract class that follows the spec. It will be run against several staff
     * implementations of Extract, which will be done by overwriting
     * (temporarily) your version of Extract with the staff's version.
     * DO NOT strengthen the spec of Extract or its methods.
     * 
     * In particular, your test cases must not call helper methods of your own
     * that you have put in Extract, because that means you're testing a
     * stronger spec than Extract says. If you need such helper methods, define
     * them in a different class. If you only need them in this test class, then
     * keep them in this test class.
     */

}
