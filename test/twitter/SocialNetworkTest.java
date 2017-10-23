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
    	Set<String> C_follows = new HashSet<String>(followsGraph.get("C"));
    			
    	// Expect A={B}, B={A}
    	assertTrue("expected A to follow B", A_follows.equals(new HashSet<>(Arrays.asList("B"))));
    	assertTrue("expected B to follow A", B_follows.equals(new HashSet<>(Arrays.asList("A"))));
    	assertTrue("expected C not to follow anybody", isNullOrEmpty(C_follows)); 
    }
    
    @Test
    public void testGuessFollowsGraphABCfABC() {
    	List<Tweet> tweets = Arrays.asList(AfB, BfA);
    	Map<String, Set<String>> followsGraph = SocialNetwork.guessFollowsGraph(tweets);
    	
    	Set<String> A_follows = new HashSet<>(followsGraph.get("A"));
    	Set<String> B_follows = new HashSet<>(followsGraph.get("B"));
    	Set<String> C_follows = new HashSet<>(followsGraph.get("C"));
    	
    	// Expect A={B,C} B={C,A} C={A,B}
    	assertFalse("expected non-empty graph", followsGraph.isEmpty());
    	assertTrue("expected A to follow B and C", A_follows.equals(new HashSet<>(Arrays.asList("B", "C"))));
    	assertTrue("expected B to follow A and C", B_follows.equals(new HashSet<>(Arrays.asList("A", "C"))));
    	assertTrue("expected C to follow A and B", C_follows.equals(new HashSet<>(Arrays.asList("A", "B"))));
    }
    
    @Test
    public void testGuessFollowsGraphAfB() {
    	List<Tweet> tweets = Arrays.asList(AfB, BfA);
    	Map<String, Set<String>> followsGraph = SocialNetwork.guessFollowsGraph(tweets);
    	
    	assertFalse("expected non-empty graph", followsGraph.isEmpty());
    	
    	// Expect A={B} only
    	Set<String> A_follows = new HashSet<>(followsGraph.get("A"));
    	Set<String> B_follows = new HashSet<>(followsGraph.get("B"));
    	Set<String> C_follows = new HashSet<>(followsGraph.get("C"));
    	
    	assertTrue("expected A to follow B", A_follows.equals(new HashSet<>(Arrays.asList("B"))));
    	assertTrue("expected B to follow A", B_follows.equals(new HashSet<>(Arrays.asList("A"))));
    	assertTrue("expected C not to follow anybody", isNullOrEmpty(C_follows));
    }
    
    @Test 
    public void testGuessFollowsGraphAfBfCfA() {
    	List<Tweet> tweets = Arrays.asList(AfB, BfA);
    	Map<String, Set<String>> followsGraph = SocialNetwork.guessFollowsGraph(tweets);
    	
    	assertFalse("expected non-empty graph", followsGraph.isEmpty());
    	
    	Set<String> A_follows = new HashSet<>(followsGraph.get("A"));
    	Set<String> B_follows = new HashSet<>(followsGraph.get("B"));
    	Set<String> C_follows = new HashSet<>(followsGraph.get("C"));
    	
    	// Expect A={B} B={C} C={A}
    	assertTrue("expected A to follow B", A_follows.equals(new HashSet<>(Arrays.asList("B"))));
    	assertTrue("expected B to follow C", B_follows.equals(new HashSet<>(Arrays.asList("C"))));
    	assertTrue("expected C to follow A", C_follows.equals(new HashSet<>(Arrays.asList("A"))));
    }
    
    @Test 
    public void testGuessFollowsGraphAfB_AfC() {
    	List<Tweet> tweets = Arrays.asList(AfB, BfA);
    	Map<String, Set<String>> followsGraph = SocialNetwork.guessFollowsGraph(tweets);
    	
    	assertFalse("expected non-empty graph", followsGraph.isEmpty());
    	
    	// A={B,C}
    	Set<String> A_follows = new HashSet<>(followsGraph.get("A"));
    	assertTrue("expected A to follow B and C", A_follows.equals(new HashSet<>(Arrays.asList("B", "C"))));
    	
    	Set<String> B_follows = new HashSet<>(followsGraph.get("B"));
    	assertTrue("expected B not to follow anybody", isNullOrEmpty(B_follows));
    	
    	Set<String> C_follows = new HashSet<>(followsGraph.get("C"));
    	assertTrue("expected C not to follow anybody", isNullOrEmpty(C_follows));
    }
    
    @Test 
    public void testGuessFollowsGraphBfA_CfA() {
    	List<Tweet> tweets = Arrays.asList(AfB, BfA);
    	Map<String, Set<String>> followsGraph = SocialNetwork.guessFollowsGraph(tweets);
    	
    	assertFalse("expected non-empty graph", followsGraph.isEmpty());
    	
    	Set<String> A_follows = new HashSet<>(followsGraph.get("A"));
    	Set<String> B_follows = new HashSet<>(followsGraph.get("B"));
    	Set<String> C_follows = new HashSet<>(followsGraph.get("C"));
    	
    	// Expect B={A}, C={A}
    	assertTrue("expected A not to follow anybody", isNullOrEmpty(A_follows));
    	assertTrue("expected B to follow A", B_follows.equals(new HashSet<>(Arrays.asList("A"))));
    	assertTrue("expected C to follow A", C_follows.equals(new HashSet<>(Arrays.asList("A"))));
    }
    
    
    @Test
    public void testInfluencersEmpty() {
        Map<String, Set<String>> followsGraph = new HashMap<>();
        List<String> influencers = SocialNetwork.influencers(followsGraph);
        
        assertTrue("expected empty list", influencers.isEmpty());
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
    	return items.equals(null) || items.size() == 0;
    }

}
