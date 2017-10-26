/* Copyright (c) 2007-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package twitter;

import static org.junit.Assert.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.HashSet;

import org.junit.Test;

public class SocialNetworkTest {

    /*
     * TODO: your testing strategies for these methods should go here.
     * See the ic03-testing exercise for examples of what a testing strategy comment looks like.
     * Make sure you have partitions.
     */
	Instant i = Instant.parse("2017-02-17T15:00:00Z");
	Tweet AfB = new Tweet(1, "A", "@B", i);
	Tweet AfC = new Tweet(2, "A", "@C", i);
	Tweet BfA = new Tweet(3, "B", "@A", i);
	Tweet BfC = new Tweet(4, "B", "@C", i);
	Tweet CfA = new Tweet(5, "C", "@A", i);
	Tweet CfB = new Tweet(6, "C", "@B", i);
	Tweet A = new Tweet(7, "A", "void", i);
	Tweet B = new Tweet(8, "B", "void", i);
	Tweet C = new Tweet(9, "C", "void", i);
    
    @Test(expected=AssertionError.class)
    public void testAssertionsEnabled() {
        assert false; // make sure assertions are enabled with VM argument: -ea
    }
    
    /*
     * Tests for guessFollows
     * NB the weak postcondition merely requires that C not contain 
     */
    
    @Test
    public void testGuessFollowsGraphEmpty() {
        Map<String, Set<String>> followsGraph = 
        		SocialNetwork.guessFollowsGraph(new ArrayList<>());
        assertTrue("expected empty graph", followsGraph.isEmpty());
    }
    
    @Test
    public void testGuessFollowsGraphUnconnectedTweets() {
    	List<Tweet> tweets = Arrays.asList(A, B, C);
    	Map<String, Set<String>> followsGraph = SocialNetwork.guessFollowsGraph(tweets);
    	assertTrue("expected empty graph", followsGraph.isEmpty());
    }
    
    @Test
    public void testGuessFollowsGraphAfBfA() {
    	List<Tweet> tweets = Arrays.asList(AfB, BfA);
    	Map<String, Set<String>> followsGraph = SocialNetwork.guessFollowsGraph(tweets);
    	
    	assertFalse("expected non-empty graph", followsGraph.isEmpty());
    	
    	Set<String> A_follows = new HashSet<String>(followsGraph.get("A"));
    	Set<String> B_follows = new HashSet<String>(followsGraph.get("B"));
    			
    	// Expect A={B}, B={A}
    	assertTrue("expected A to follow B", setEq(A_follows, new HashSet<>(Arrays.asList("B"))));
    	assertTrue("expected B to follow A", setEq(B_follows, new HashSet<>(Arrays.asList("A"))));
    	assertTrue("expected C not to follow anybody", isNullOrEmpty(followsGraph.get("C"))); 
    }
    
    @Test
    public void testGuessFollowsGraphABCfABC() {
    	List<Tweet> tweets = Arrays.asList(AfB, AfC, BfA, BfC, CfA, CfB);
    	Map<String, Set<String>> followsGraph = SocialNetwork.guessFollowsGraph(tweets);
    	
    	Set<String> A_follows = new HashSet<>(followsGraph.get("A"));
    	Set<String> B_follows = new HashSet<>(followsGraph.get("B"));
    	Set<String> C_follows = new HashSet<>(followsGraph.get("C"));
    	
    	// Expect A={B,C} B={C,A} C={A,B}
    	assertFalse("expected non-empty graph", followsGraph.isEmpty());
    	assertTrue("expected A to follow B and C", setEq(A_follows, new HashSet<>(Arrays.asList("B", "C"))));
    	assertTrue("expected B to follow A and C", setEq(B_follows, new HashSet<>(Arrays.asList("A", "C"))));
    	assertTrue("expected C to follow A and B", setEq(C_follows, new HashSet<>(Arrays.asList("A", "B"))));
    }
    
    @Test
    public void testGuessFollowsGraphAfB() {
    	List<Tweet> tweets = Arrays.asList(AfB);
    	Map<String, Set<String>> followsGraph = SocialNetwork.guessFollowsGraph(tweets);
    	
    	assertFalse("expected non-empty graph", followsGraph.isEmpty());
    	
    	// Expect A={B} only
    	Set<String> A_follows = new HashSet<>(followsGraph.get("A"));
    	
    	assertTrue("expected A to follow B", setEq(A_follows, new HashSet<>(Arrays.asList("B"))));
    	assertTrue("expected B not to follow anybody", isNullOrEmpty(followsGraph.get("B")));
    	assertTrue("expected C not to follow anybody", isNullOrEmpty(followsGraph.get("C")));
    }
    
    @Test 
    public void testGuessFollowsGraphAfBfCfA() {
    	List<Tweet> tweets = Arrays.asList(AfB, BfC, CfA);
    	Map<String, Set<String>> followsGraph = SocialNetwork.guessFollowsGraph(tweets);
    	
    	assertFalse("expected non-empty graph", followsGraph.isEmpty());
    	
    	Set<String> A_follows = new HashSet<>(followsGraph.get("A"));
    	Set<String> B_follows = new HashSet<>(followsGraph.get("B"));
    	Set<String> C_follows = new HashSet<>(followsGraph.get("C"));
    	
    	// Expect A={B} B={C} C={A}
    	assertTrue("expected A to follow B", setEq(A_follows, new HashSet<>(Arrays.asList("B"))));
    	assertTrue("expected B to follow C", setEq(B_follows, new HashSet<>(Arrays.asList("C"))));
    	assertTrue("expected C to follow A", setEq(C_follows, new HashSet<>(Arrays.asList("A"))));
    }
    
    @Test 
    public void testGuessFollowsGraphAfB_AfC() {
    	List<Tweet> tweets = Arrays.asList(AfB, AfC);
    	Map<String, Set<String>> followsGraph = SocialNetwork.guessFollowsGraph(tweets);
    	
    	assertFalse("expected non-empty graph", followsGraph.isEmpty());
    	
    	// A={B,C}
    	Set<String> A_follows = new HashSet<>(followsGraph.get("A"));
    	assertTrue("expected A to follow B and C", setEq(A_follows, new HashSet<>(Arrays.asList("B", "C"))));
    	assertTrue("expected B not to follow anybody", isNullOrEmpty(followsGraph.get("B")));
    	assertTrue("expected C not to follow anybody", isNullOrEmpty(followsGraph.get("C")));
    }
    
    @Test 
    public void testGuessFollowsGraphBfA_CfA() {
    	List<Tweet> tweets = Arrays.asList(BfA, CfA);
    	Map<String, Set<String>> followsGraph = SocialNetwork.guessFollowsGraph(tweets);
    	
    	assertFalse("expected non-empty graph", followsGraph.isEmpty());
    	
    	Set<String> B_follows = new HashSet<>(followsGraph.get("B"));
    	Set<String> C_follows = new HashSet<>(followsGraph.get("C"));
    	
    	// Expect B={A}, C={A}
    	assertTrue("expected A not to follow anybody", isNullOrEmpty(followsGraph.get("A")));
    	assertTrue("expected B to follow A", setEq(B_follows, new HashSet<>(Arrays.asList("A"))));
    	assertTrue("expected C to follow A", setEq(C_follows, new HashSet<>(Arrays.asList("A"))));
    }
    
    
    @Test
    public void testInfluencersEmpty() {
        Map<String, Set<String>> followsGraph = new HashMap<>();
        List<String> influencers = SocialNetwork.influencers(followsGraph);
        
        assertTrue("expected empty list", influencers.isEmpty());
    }
    
    @Test
    public void testInfluencersEmptySet() {
        Map<String, Set<String>> followsGraph = new HashMap<>();
        followsGraph.put("A", new HashSet<>());
        List<String> influencers = SocialNetwork.influencers(followsGraph);
        
        assertEquals("expected list of size 1", influencers.size(), 1);
        assertTrue("expected A to be in list", influencers.contains("A"));
    }
    
    @Test
    public void testInfluencersAfB() {
        Map<String, Set<String>> followsGraph = new HashMap<>();
        followsGraph.put("A", new HashSet<>(Arrays.asList("B")));
        List<String> influencers = SocialNetwork.influencers(followsGraph);
        
        // Expect [A, B] or [B, A]
        assertEquals("expected list of size 2", influencers.size(), 2);
        assertTrue("expected A to be in list", influencers.contains("A"));
        assertTrue("expected B to be in list", influencers.contains("B"));
    }
    
    @Test
    public void testInfluencersAfBfA() {
    	Map<String, Set<String>> followsGraph = new HashMap<>();
        followsGraph.put("A", new HashSet<>(Arrays.asList("B")));
        followsGraph.put("B", new HashSet<>(Arrays.asList("A")));
        List<String> influencers = SocialNetwork.influencers(followsGraph);
        
        assertEquals("expected list of size 2", influencers.size(), 2);
        assertTrue("expected A to be in list", influencers.contains("A"));
        assertTrue("expected B to be in list", influencers.contains("B"));
    }
    
    @Test
    public void testInfluencersAfBC() {
    	Map<String, Set<String>> followsGraph = new HashMap<>();
        followsGraph.put("A", new HashSet<>(Arrays.asList("B", "C")));
        List<String> influencers = SocialNetwork.influencers(followsGraph);
        
        // Expect [B,C,A] or [A,C,B]
        assertEquals("expected list of size 3", influencers.size(), 3);
        assertTrue("expected B to be in list", influencers.contains("B"));
        assertTrue("expected C to be in list", influencers.contains("C"));
        assertEquals("expected A to be last in list", influencers.get(2), "A");
    }
    
    @Test
    public void testInfluencersAfB_CfB() {
    	Map<String, Set<String>> followsGraph = new HashMap<>();
        followsGraph.put("A", new HashSet<>(Arrays.asList("B")));
        followsGraph.put("C", new HashSet<>(Arrays.asList("B")));
        List<String> influencers = SocialNetwork.influencers(followsGraph);
        
        // Expect [B,C,A] or [A,C,B]
        assertEquals("expected list of size 3", influencers.size(), 3);
        assertEquals("expected B to be first in list", influencers.get(0), "B");
        assertTrue("expected A to be in list", influencers.contains("A"));
        assertTrue("expected C to be in list", influencers.contains("C"));
    }
    
    @Test 
    public void testInfluencersLots() {
    	Map<String, Set<String>> followsGraph = new HashMap<>();
    	followsGraph.put("B", new HashSet<>(Arrays.asList("A", "C", "D")));
    	followsGraph.put("C", new HashSet<>(Arrays.asList("A")));
    	followsGraph.put("D", new HashSet<>(Arrays.asList("A","B","C")));
    	
    	List<String> influencers = SocialNetwork.influencers(followsGraph);
    	
    	// Expect [A,C,B,D] or [A,C,D,B]
    	assertEquals("expected list of size 4", influencers.size(), 4);
    	assertEquals("expected A to be first in list", influencers.get(0), "A");
    	assertTrue("expected B to be in list", influencers.contains("B"));
    	assertTrue("expected C to be in list", influencers.contains("C"));
    }

    /*
     * Warning: all the tests you write here must be runnable against any
     * SocialNetwork class that follows the spec. It will be run against several
     * staff implementations of SocialNetwork, which will be done by overwriting
     * (temporarily) your version of SocialNetwork with the staff's version.
     * DO NOT strengthen the spec of SocialNetwork or its methods.
     * 
     * In particular, your test cases must not call helper methods of your own
     * that you have put in SocialNetwork, because that means you're testing a
     * stronger spec than SocialNetwork says. If you need such helper methods,
     * define them in a different class. If you only need them in this test
     * class, then keep them in this test class.
     */
    
    /**
     * Helper method which asserts a set is null or empty
     * @param items A set of any type
     */
    private <T> boolean isNullOrEmpty(Set<T> items) {
    	return items == null || items.size() == 0;
    }
    
    private <T> boolean setEq(final Set<T> setA, final Set<T> setB) {
    	// Use copies 
    	Set<T> a = new HashSet<T>(setA);
    	Set<T> b = new HashSet<T>(setB);
    	for(T t: a)
    	{
    		if (!b.contains(t))
    			return false;
    		b.remove(t);
    	}
    	// Popped everything from B as we found it, so
    	// if the sets were equal it should now be empty
    	return b.isEmpty();
    }

}
