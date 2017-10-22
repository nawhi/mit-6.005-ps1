/* Copyright (c) 2007-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package twitter;

import static org.junit.Assert.*;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class FilterTest {
	
	private static final Instant d0 = Instant.parse("2016-01-17T10:00:00Z");
	private static final Instant d1 = Instant.parse("2016-02-17T10:00:00Z");
    private static final Instant d2 = Instant.parse("2016-02-17T11:00:00Z");
    private static final Instant d2a = Instant.parse("2016-02-17T12:00:00Z");
    private static final Instant d3 = Instant.parse("2017-02-17T10:00:00Z");
    private static final Instant d3a = Instant.parse("2017-02-17T10:30:00Z");
    private static final Instant d4 = Instant.parse("2017-02-17T11:00:00Z");
    private static final Instant d5 = Instant.parse("2017-02-17T15:00:00Z");
    
    private static final Tweet tweet1 = new Tweet(
    		1, "alyssa", "is it reasonable to talk about rivest so much?", d1);
    private static final Tweet tweet2 = new Tweet(
    		2, "bbitdiddle", "rivest talk in 30 minutes #hype", d2);
    private static final Tweet tweet3 = new Tweet(
    		3, "SameUser", "Consciousness is a superposition of possibilities .@.!*@& ", d3);
    private static final Tweet tweet3a = new Tweet(3, "SameUser", "Consciousness", d3);
    private static final Tweet tweet3b = new Tweet(
    		3, "SameUser", "Consciousness is a SUPERPOSITION of possibilities", d3);
    private static final Tweet tweet4 = new Tweet(
    		4, "SameUser", "Despite the constant negative press covfefe", d4);
    private static final Tweet tweet5 = new Tweet(
    		5, "SameUser", "Because of the infrequent positive press efefvoc", d4);
    private static final Tweet tweet6 = new Tweet(
    		6, "SameUser", "Another tweet to get the test suite working", d4);
    
    @Test(expected=AssertionError.class)
    public void testAssertionsEnabled() {
        assert false; // make sure assertions are enabled with VM argument: -ea
    }

	/*
	 * Testing writtenBy() 
     * Partition 1: no results
     */
    @Test
    public void testWrittenByOneTweetNoResults() {
    	List<Tweet> writtenBy = Filter.writtenBy(
    			Arrays.asList(tweet1), 
    			"unknown_user"
		);
    	assertTrue("expected empty list", writtenBy.isEmpty());
    }
    @Test
    public void testWrittenByMultipleTweetsNoResults() {
    	List<Tweet> writtenByMultiple = Filter.writtenBy(
    			Arrays.asList(tweet1, tweet2, tweet3, tweet4, tweet5),
    			"unknown_user"
		);
    	assertTrue("expected empty list", writtenByMultiple.isEmpty());
    }
    
    /*
     * Partition 2: one result
     */
    @Test
    public void testWrittenByOneTweetOneResult() {
        List<Tweet> writtenBy = Filter.writtenBy(
        		Arrays.asList(tweet1), 
        		"alyssa"
		);
        assertEquals("expected singleton list", 1, writtenBy.size());
        assertTrue("expected list to contain tweet", writtenBy.contains(tweet1));
    }
    @Test
    public void testWrittenByMultipleTweetsOneResult() {
        List<Tweet> writtenBy = Filter.writtenBy(
        		Arrays.asList(tweet1, tweet2, tweet3, tweet4, tweet5), 
        		"alyssa"
		);
        assertEquals("expected singleton list", 1, writtenBy.size());
        assertTrue("expected list to contain tweet", writtenBy.contains(tweet1));
    }
    
    /*
     * Partition 3: 0 < results size < input size 
     */
    @Test
    public void testWrittenByMultipleTweetsMultipleResults() {
    	List<Tweet> writtenBy = Filter.writtenBy(
    			Arrays.asList(tweet1, tweet4, tweet3, tweet2),
    			"SameUser"
		);
    	assertFalse("expected non-empty list", writtenBy.isEmpty());
    	assertTrue("expected list to contain tweets", 
    			writtenBy.containsAll(Arrays.asList(tweet4, tweet3)));
    	assertEquals("expected same order", 0, writtenBy.indexOf(tweet4));
    }
    
    /*
     * Partition 4: results size = input size
     */
    @Test
    public void testWrittenByMultipleTweetsAllResults() {
    	List<Tweet> writtenBy = Filter.writtenBy(
    			Arrays.asList(tweet5, tweet4, tweet3, tweet6),
    			"SameUser"
		);
    	assertFalse("expected non-empty list", writtenBy.isEmpty());
    	assertTrue("expected list to contain tweets", 
    			writtenBy.containsAll(Arrays.asList(tweet5, tweet4, tweet3, tweet6)));
    	assertEquals("expected same order", 0, writtenBy.indexOf(tweet5));
    	assertEquals("expected same order", 1, writtenBy.indexOf(tweet4));
    	assertEquals("expected same order", 2, writtenBy.indexOf(tweet3));
    }
    
    /*
     * Testing inTimespan()
     * Partition 1: 1 tweet, no results
     */
    @Test 
    public void testInTimespanOneTweetNoResult() {
    	List<Tweet> inTimespan = Filter.inTimespan(
    			Arrays.asList(tweet1),
    			new Timespan(d2, d3)
		);
    	assertTrue("expected empty list", inTimespan.isEmpty());
    }
    
    /*
     * Partition 2: 1 tweet, 1 result
     */
    @Test
    public void testInTimespanOneTweetOneResult() {
    	List<Tweet> inTimespan = Filter.inTimespan(
    			Arrays.asList(tweet2),
    			new Timespan(d1, d2a)
		);
    	assertEquals("expected singleton list", 1, inTimespan.size());
    	assertEquals("expected list to contain 1 tweet", tweet2, inTimespan.get(0));
    }
    
    /*
     * Partition 3: Many tweets, no results
     */
    @Test
    public void testInTimespanManyTweetsNoResult() {
    	List<Tweet> inTimespan = Filter.inTimespan(
    			Arrays.asList(tweet3, tweet4, tweet5, tweet6),
    			new Timespan(d1, d2)
		);
    	assertTrue("expected empty list", inTimespan.isEmpty());
    }
    
    /*
     * Partition 4: Many tweets, some results
     */
    @Test
    public void testInTimespanManyTweetsSomeResults() {
    	List<Tweet> inTimespan = Filter.inTimespan(
    			Arrays.asList(tweet3, tweet2, tweet4, tweet5, tweet6),
    			new Timespan(d1, d3a)
		);
    	assertEquals("expected list of size 2", 2, inTimespan.size());
    	assertTrue("expected list to contain 2 tweets", 
    			inTimespan.containsAll(Arrays.asList(tweet3, tweet2)));
    	assertEquals("expected same order", 0, inTimespan.indexOf(tweet3));
    }
    
    /*
     * Partition 5: Many tweets, all results
     */
    @Test 
    public void testInTimespanManyTweetsAllResults() {
    	List<Tweet> expected = Arrays.asList(
    			tweet3, tweet2, tweet5, tweet4, tweet6, tweet1
		); 
    	List<Tweet> inTimespan = Filter.inTimespan(
    			expected,     			
    			new Timespan(d0, d5)
		);
    	assertEquals("expected list of size 6", 6, inTimespan.size());
    	assertTrue(
    			"expected list to contain tweets", 
    			inTimespan.containsAll(expected)
		);
    	for (int i=0; i<expected.size(); ++i)
    	{
    		assertEquals(
    				"expected same order", 
    				i, 
    				inTimespan.indexOf(expected.get(i))
			);
    	}
    }
    
    /*
     * Partition 6: Timespan of 0 and tweet at same instant
     */
    @Test
    public void testInTimespanOneTweetAtTimespanInstant() {
    	List<Tweet> inTimespan = Filter.inTimespan(
    			Arrays.asList(tweet1),
    			new Timespan(d1, d1)
		);
    	assertEquals("expected singleton list", 1, inTimespan.size());
    	assertEquals("expected list to contain 1 tweet", tweet1, inTimespan.get(0));
    }
    /*
     * Partition 7: Tweet at min boundary of timespan
     */
    @Test
    public void testInTimespanOneTweetAtTimespanMin() {
    	List<Tweet> inTimespan = Filter.inTimespan(
    			Arrays.asList(tweet1),
    			new Timespan(d1, d2)
		);
    	assertEquals("expected singleton list", 1, inTimespan.size());
    	assertEquals("expected list to contain 1 tweet", tweet1, inTimespan.get(0));
    }
    
    /*
     * Partition 8: tweet at max boundary of timespan
     */
    @Test
    public void testInTimespanOneTweetAtTimespanMax() {
    	List<Tweet> inTimespan = Filter.inTimespan(
    			Arrays.asList(tweet2),
    			new Timespan(d1, d2)
		);
    	assertEquals("expected singleton list", 1, inTimespan.size());
    	assertEquals("expected list to contain 1 tweet", tweet2, inTimespan.get(0));
    }
    /*
     * Testing containing()
     */
    @Test
    public void testContainingOneWordNoMatches() {
    	List<Tweet> containing = Filter.containing(
    			Arrays.asList(tweet1, tweet2),
    			Arrays.asList("foo")
		);
    	assertTrue("expected empty list", containing.isEmpty());
    }
    
    @Test 
    public void testContainingManyWordsNoMatches() {
    	List<Tweet> containing = Filter.containing(
    			Arrays.asList(tweet1, tweet2),
    			Arrays.asList("foo", "bar", "baz", "qux")
		);
    	assertTrue("expected empty list", containing.isEmpty());
    }
    
    @Test 
    public void testContaining1WordPartialMatch() {
    	List<Tweet> containing = Filter.containing(
    			Arrays.asList(tweet1),
    			Arrays.asList("rives")
		);
    	assertTrue("expected empty list", containing.isEmpty());
    	
    }
    
    @Test
    public void testContaining1WordFulltextMatch() {
    	List<Tweet> containing = Filter.containing(
    			Arrays.asList(tweet3a),
    			Arrays.asList("Consciousness")
		);
    	assertFalse("expected non-empty list", containing.isEmpty());
        assertTrue("expected list to contain tweets", containing.containsAll(Arrays.asList(tweet3a)));
    	
    }
    
    @Test
    public void testContaining2Words2MatchesDifferentTweets() {
    	List<Tweet> containing = Filter.containing(
    			Arrays.asList(tweet5, tweet4),
    			Arrays.asList("negative", "positive")
		);
    	assertFalse("expected non-empty list", containing.isEmpty());
        assertTrue("expected list to contain tweets", 
        		containing.containsAll(Arrays.asList(tweet5, tweet4)));
        assertEquals("expected same order as input tweet list", 0, containing.indexOf(tweet5));
    	
    }

    @Test
    public void testContaining2IdenticalWords2MatchesDiffCase() {
    	List<Tweet> containing = Filter.containing(
    			Arrays.asList(tweet3, tweet3b),
    			Arrays.asList("superposition")
		);
    	assertFalse("expected non-empty list", containing.isEmpty());
        assertTrue("expected list to contain tweets", 
        		containing.containsAll(Arrays.asList(tweet3, tweet3b)));
        assertEquals("expected same order as input tweet list", 0, containing.indexOf(tweet3));
    }
    
    @Test
    public void testContaining() {
        List<Tweet> containing = Filter.containing(Arrays.asList(tweet1, tweet2), Arrays.asList("talk"));
        
        assertFalse("expected non-empty list", containing.isEmpty());
        assertTrue("expected list to contain tweets", containing.containsAll(Arrays.asList(tweet1, tweet2)));
        assertEquals("expected same order", 0, containing.indexOf(tweet1));
    }

    /*
     * Warning: all the tests you write here must be runnable against any Filter
     * class that follows the spec. It will be run against several staff
     * implementations of Filter, which will be done by overwriting
     * (temporarily) your version of Filter with the staff's version.
     * DO NOT strengthen the spec of Filter or its methods.
     * 
     * In particular, your test cases must not call helper methods of your own
     * that you have put in Filter, because that means you're testing a stronger
     * spec than Filter says. If you need such helper methods, define them in a
     * different class. If you only need them in this test class, then keep them
     * in this test class.
     */

}
