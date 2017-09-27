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
	
	private static final Instant d1 = Instant.parse("2016-02-17T10:00:00Z");
    private static final Instant d2 = Instant.parse("2016-02-17T11:00:00Z");
    private static final Instant d3 = Instant.parse("2017-02-17T10:00:00Z");
    private static final Instant d4 = Instant.parse("2017-02-17T11:00:00Z");
    
    private static final Tweet tweet1 = new Tweet(
    		1, "alyssa", "is it reasonable to talk about rivest so much?", d1);
    private static final Tweet tweet2 = new Tweet(
    		2, "bbitdiddle", "rivest talk in 30 minutes #hype", d2);
    private static final Tweet tweet3 = new Tweet(
    		3, "SameUser", "Consciousness is a superposition of possibilities .@.!*@& ", d3);
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
    	assertEquals("expected empty list", writtenBy.isEmpty());
    }
    @Test
    public void testWrittenByMultipleTweetsNoResults() {
    	List<Tweet> writtenByMultiple = Filter.writtenBy(
    			Arrays.asList(tweet1, tweet2, tweet3, tweet4, tweet5),
    			"unknown_user"
		);
    	assertEquals("expected empty list", writtenByMultiple.isEmpty());
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
     * partitions:
     * 1)
     */
    
    /*
     * Testing containing()
     * partitions:
     * 1)
     */
    
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
