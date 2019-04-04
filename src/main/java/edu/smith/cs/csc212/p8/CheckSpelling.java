package edu.smith.cs.csc212.p8;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class CheckSpelling {
	/**
	 * Read all lines from the UNIX dictionary.
	 * @return a list of words!
	 */
	public static List<String> loadDictionary() {
		long start = System.nanoTime();
		List<String> words;
		try {
			// Read from a file:
			words = Files.readAllLines(new File("src/main/resources/words").toPath());
		} catch (IOException e) {
			throw new RuntimeException("Couldn't find dictionary.", e);
		}
		long end = System.nanoTime();
		double time = (end - start) / 1e9;
		System.out.println("Loaded " + words.size() + " entries in " + time +" seconds.");
		return words;
	}
	
	/**
	 * This method looks for all the words in a dictionary.
	 * @param words - the "queries"
	 * @param dictionary - the data structure.
	 */
	public static void timeLookup(List<String> words, Collection<String> dictionary) {
		long startLookup = System.nanoTime();
		
		int found = 0;
		for (String w : words) {
			if (dictionary.contains(w)) {
				found++;
			}
		}
		
		long endLookup = System.nanoTime();
		double fractionFound = found / (double) words.size();
		double timeSpentPerItem = (endLookup - startLookup) / ((double) words.size());
		int nsPerItem = (int) timeSpentPerItem;
		System.out.println(dictionary.getClass().getSimpleName()+": Lookup of items found="+fractionFound+" time="+nsPerItem+" ns/item");
	}
	
	public static List<String> createMixedDataset(List<String> yesWords, int numSamples, double fractionYes) {
		// Hint to the ArrayList that it will need to grow to numSamples size:
		List<String> output = new ArrayList<>(numSamples);
		// TODO: select numSamples * fractionYes words from yesWords; create the rest as no words.
		
		
		return output;
	}
	
	
	public static void main(String[] args) {
		// --- Load the dictionary.
		List<String> listOfWords = loadDictionary();
		
		// --- How long does it take to inserted the dictionary into the data structure?
		
		
		// --- Create a bunch of data structures for testing:
		long startTime = System.nanoTime();
		TreeSet<String> treeOfWords = new TreeSet<>(listOfWords);
		long endTime = System.nanoTime();
		double totalTime = (endTime - startTime)/(1e9);
		System.out.println("TreeSet Insertion Time: " + totalTime + " seconds.");
		
		startTime = System.nanoTime();
		TreeSet<String> addTree = new TreeSet<>();
		for(String w : listOfWords) {
			addTree.add(w);
		}
		endTime = System.nanoTime();
		totalTime = (endTime - startTime)/(1e9);
		System.out.println("TreeSet Insertion Time (using add): " + totalTime + " seconds.");
		
		startTime = System.nanoTime();
		HashSet<String> hashOfWords = new HashSet<>(listOfWords);
		endTime = System.nanoTime();
		totalTime = (endTime - startTime)/(1e9);
		System.out.println("HashSet Insertion Time: " + totalTime + " seconds.");
		
		startTime = System.nanoTime();
		HashSet<String> addHash = new HashSet<>();
		for(String w : listOfWords) {
			addHash.add(w);
		}
		endTime = System.nanoTime();
		totalTime = (endTime - startTime)/(1e9);
		System.out.println("HashSet Insertion Time (using add): " + totalTime + " seconds.");
		
		startTime = System.nanoTime();
		SortedStringListSet bsl = new SortedStringListSet(listOfWords);
		endTime = System.nanoTime();
		totalTime = (endTime - startTime)/(1e9);
		System.out.println("SortedStringListSet Insertion Time: " + totalTime + " seconds.");
		
		startTime = System.nanoTime();
		CharTrie trie = new CharTrie();
		for (String w : listOfWords) {
			trie.insert(w);
		}
		endTime = System.nanoTime();
		totalTime = (endTime - startTime)/(1e9);
		System.out.println("CharTrie Insertion Time: " + totalTime + " seconds.");
		
		startTime = System.nanoTime();
		LLHash hm100k = new LLHash(100000);
		for (String w : listOfWords) {
			hm100k.add(w);
		}
		endTime = System.nanoTime();
		totalTime = (endTime - startTime)/(1e9);
		System.out.println("LLHash Insertion Time: " + totalTime + " seconds.");
		
		// --- Make sure that every word in the dictionary is in the dictionary:
		timeLookup(listOfWords, treeOfWords);
		timeLookup(listOfWords, hashOfWords);
		timeLookup(listOfWords, bsl);
		timeLookup(listOfWords, trie);
		timeLookup(listOfWords, hm100k);
		
		
		for (int i=0; i<10; i++) {
			// --- Create a dataset of mixed hits and misses with p=i/10.0
			List<String> hitsAndMisses = createMixedDataset(listOfWords, 10_000, i/10.0);
			
			// --- Time the data structures.
			timeLookup(hitsAndMisses, treeOfWords);
			timeLookup(hitsAndMisses, hashOfWords);
			timeLookup(hitsAndMisses, bsl);
			timeLookup(hitsAndMisses, trie);
			timeLookup(hitsAndMisses, hm100k);
		}
			

		
		// --- linear list timing:
		// Looking up in a list is so slow, we need to sample:
		System.out.println("Start of list: ");
		timeLookup(listOfWords.subList(0, 1000), listOfWords);
		System.out.println("End of list: ");
		timeLookup(listOfWords.subList(listOfWords.size()-100, listOfWords.size()), listOfWords);
		
	
		// --- print statistics about the data structures:
		System.out.println("Count-Nodes: "+trie.countNodes());
		System.out.println("Count-Items: "+hm100k.size());

		System.out.println("Count-Collisions[100k]: "+hm100k.countCollisions());
		System.out.println("Count-Used-Buckets[100k]: "+hm100k.countUsedBuckets());
		System.out.println("Load-Factor[100k]: "+hm100k.countUsedBuckets() / 100000.0);

		
		System.out.println("log_2 of listOfWords.size(): "+listOfWords.size());
		
		System.out.println("Done!");
	}
}
