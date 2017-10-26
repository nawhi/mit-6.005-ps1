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
    
    
    // Testing getTimespan()
    private static final Tweet tweet1 = new Tweet(
    		1, "alyssa", "is it reasonable to talk about rivest so much?", d1);
    private static final Tweet tweet2 = new Tweet(
    		2, "bbitdiddle", "rivest talk in 30 minutes #hype", d2);
    private static final Tweet tweet3 = new Tweet(
    		3, "DeepakChopra", "Consciousness is a superposition of possibilities .@.!*@& ", d3);
    private static final Tweet tweet4 = new Tweet(
    		4, "realDonaldTrump", "Despite the constant negative press covfefe", d4);
    private static final Tweet tweet5 = new Tweet(
    		5, "fakeRonaldDump", "Because of the infrequent positive press efefvoc", d4);
    
    // Testing getMentionedUsers()
    private static final Tweet tweet6 = new Tweet(
    		6, "bbitdiddle", "@alyssa mention at start of tweet", d1);
    private static final Tweet tweet7 = new Tweet(
    		7, "bbitdiddle", "mention @alyssa during tweet", d1);
    private static final Tweet tweet8 = new Tweet(
    		8, "bbitdiddle", "mention !@alyssa with non-tweet char beforehand", d1);
    private static final Tweet tweet9 = new Tweet(
    		9, "bbitdiddle", "not a mention a@b not a mention", d1);
    private static final Tweet tweet10 = new Tweet(
    		10, "realDonaldTrump", "@covfefe @covfefe @covfefe @covfefe @covfefe - only 1 mention", d1);
    private static final Tweet tweet11 = new Tweet(
    		11, "realDonaldTrump", "@Covfefe @covFEFE @CovFeFe @covFefe @COVFEFE - only 1 mention", d1);
    private static final Tweet tweet12 = new Tweet(
    		12, "bbitdiddle", "@mention1 @mention2 @mention3 @mention4 @mention5", d1);
    
    @Test(expected=AssertionError.class)
    public void testAssertionsEnabled() {
        assert false; // make sure assertions are enabled with VM argument: -ea
    }
    
    /*
     * getTimespan tests
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
    	assertEquals("expected end", d4, timespan.getEnd());
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
     * getMentionedUsers tests
     * Partition 1: tweets with no mentioned users
     */
    @Test
    public void testGetMentionedUsersNoMention() {
        Set<String> mentionedUsers = Extract.getMentionedUsers(Arrays.asList(
        		tweet1, tweet2, tweet4, tweet5));
        assertTrue("expected empty set", mentionedUsers.isEmpty());
    }
    
    /*
     * Partition 2: invalid mentions
     */
    @Test
    public void testGetMentionedUsersInvalidMention() {
        Set<String> mentionedUsers = Extract.getMentionedUsers(Arrays.asList(tweet3));
        assertTrue("expected empty set", mentionedUsers.isEmpty());
    }
    
    @Test
    public void testGetMentionedUsersInvalidMention2() {
    	Set<String> mentionedUsers = Extract.getMentionedUsers(Arrays.asList(tweet9));
    	assertTrue("expected empty set", mentionedUsers.isEmpty());
    }
    
    /*
     * Partition 3: tweet with mention at the start
     */
    @Test
    public void testGetMentionedUsersMentionAtStart() {
        Set<String> mentionedUsers = Extract.getMentionedUsers(Arrays.asList(tweet6));
        assertEquals(1, mentionedUsers.size());
        assertTrue("expected set to contain 'alyssa'", mentionedUsers.contains("alyssa"));
    }
    
    /*
     * Partition 4: tweet with mention in the middle
     */
    @Test
    public void testGetMentionedUsersMentionInMiddle() {
        Set<String> mentionedUsers = Extract.getMentionedUsers(Arrays.asList(tweet7));
        assertEquals(1, mentionedUsers.size());
        assertTrue("expected set to contain 'alyssa'", mentionedUsers.contains("alyssa"));
    }
    
    /*
     * Partition 5: tweet with non-tweet char before mention
     */
    @Test
    public void testGetMentionedUsersMentionWithPrecedingChars() {
        Set<String> mentionedUsers = Extract.getMentionedUsers(Arrays.asList(tweet8));
        assertEquals(1, mentionedUsers.size());
        assertTrue("expected set to contain 'alyssa'", mentionedUsers.contains("alyssa"));
    }
    
    /*
     * Partition 6: tweets with duplicate mentions of same username
     */
    @Test 
    public void testGetMentionedUsersDuplicateMentions() {
        Set<String> mentionedUsers = Extract.getMentionedUsers(Arrays.asList(tweet10));
        assertEquals(1, mentionedUsers.size());
        assertTrue("expected set to contain 'covfefe'", mentionedUsers.contains("covfefe"));
    }
    
    @Test
    public void testGetMentionedUsersDuplicateMentions2() {
        Set<String> mentionedUsers = Extract.getMentionedUsers(Arrays.asList(tweet11));
        assertEquals(1, mentionedUsers.size());
        
        // covfefe could be in any of the tweet's case combinations 
        boolean containsCovfefe = false;
        for (String user: mentionedUsers)
        {
        	containsCovfefe = user.toLowerCase().equals("covfefe");
        	if (containsCovfefe)
        		break;
        }
        assertTrue("expected set to contain 'covfefe'", containsCovfefe);
    }
    
    /*
     * Partition 7: tweets with multiple mentions
     */
    @Test 
    public void testGetMentionedUsersMultipleMentions() {
    	Set<String> mentionedUsers = Extract.getMentionedUsers(Arrays.asList(tweet12));
    	assertEquals(5, mentionedUsers.size());
    	for (String s : Arrays.asList("mention1", "mention2", "mention3", "mention4", "mention5"))
    		assertTrue("expected set to contain " + s, mentionedUsers.contains(s));
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
