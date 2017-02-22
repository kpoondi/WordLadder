
/* WORD LADDER Main.java
 * EE422C Project 3 submission by
 * Replace <...> with your actual data.
 * <Kausthub Poondi>
 * <kp26753>
 * <16215>
 * <Haley Alexander>
 * <ha5722 >
 * <16215>
 * Slip days used: <0>
 * Git URL: https://github.com/hal00alex/WordLadder
 * Spring 2017
 */


package assignment3;
import java.util.*;
import java.io.*;

public class Main {
	
	/**
	 * Declare all static constants needed for the execution of the BFS and DFS functions. 
	 */
	static String[] alpha = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "N", "M", "O", 
			"P", "Q", "R", "S", "T","U", "V", "W", "X", "Y", "Z"}; 
	
	static Stack<String> stack = new Stack<String>(); 
	static Set<String> DFSvisited = new HashSet<String>();
	static Set<String> tmpvisited = new HashSet<String>(); 
	static ArrayList<String> Dne = new ArrayList<String>();
	
	public static boolean found = false;	 
	static String end_global; 
	static String start_global; 
	static boolean quit = false;
	
	static HashMap<String, String> DFSstore = new HashMap<String, String>(); 
	static Set<String> dictDFS = new HashSet<String>(); 
	
	public static void main(String[] args) throws Exception {
		
		//input scanner 
		Scanner kb;	
		//output .txt
		PrintStream ps;	
		
		// If arguments are specified, read/write from/to files instead of Std IO.
		if (args.length != 0) {
			kb = new Scanner(new File(args[0]));
			ps = new PrintStream(new File(args[1]));
			System.setOut(ps);			// redirect output to ps
		} else {
			kb = new Scanner(System.in);// default from Stdin
			ps = System.out;			// default to Stdout
		}
		
		//now our turn
		initialize();
		//parse(kb); 
		
		// TODO methods to read in words, output ladder
		//getWordLadderBFS(start_global, end_global);
		printLadder(getWordLadderDFS("aldol", "drawl"));
		

	}
	
	/**
	 * Method to initialize all static constants. Empty because all necessary initialization
	 * was done in a separate function parse for ease of readability. 
	 */
	public static void initialize() {
	 		
	}
	
	/**
	 * @param keyboard Scanner connected to System.in
	 * @return ArrayList of 2 Strings containing start word and end word. 
	 * If command is /quit, return empty ArrayList. 
	 */
	public static ArrayList<String> parse(Scanner keyboard) { 
		String oneLine = keyboard.nextLine(); 
		
		/*
		 * Checks the words entered by the user and determines if the program
		 * should terminate or continue and find the word ladder. 
		 */
		if (oneLine.contains("/quit")) {
			return null; 
		}
		else {
			
			/* Finds the start and word from the user input. */
			String[] words = oneLine.split(" "); 
			String start = words[0]; 
			String end = words [1]; 

			/* Converts the start and end words to upperCase. */
			start_global = start.toUpperCase(); 
			end_global = end.toUpperCase();
			dictDFS = makeDictionary();
			
			/* Creates an arrayList that will be printed in the event that
			 * there is no word ladder for DFS. */
			Dne = new ArrayList<String>(); 
			Dne.add(start_global);
			Dne.add(end_global); 
									
		}
		return Dne;
	}

	/**
	 * Creates the ladder from a DFS search using the DFS Store
	 * HashMap created during implementation of DFS
	 * @return
	 */
	public static ArrayList<String> PrintDFS(String start_global, String end_global){
				
		String tempPrint = end_global;  
		ArrayList<String> toPrint = new ArrayList<String>();
		
		toPrint.add(end_global); 
		
		/* Traverses the parent-> child HashMap to create the ArrayList to be printed.
		 * Contains the path from start to end. */
		while (tempPrint != null && start_global != null && 
				!tempPrint.equals(start_global)) {
			tempPrint = DFSstore.get(tempPrint); 
			toPrint.add(tempPrint); 
		}
		return toPrint; 
	}
	
	/**
	 * Implements a recursive depth first search that will take the parameter
	 * "start" and see if the word end can be found in a depth first traversal
	 * @param start the word to start from
	 * @param end the word to be found
	 * @param dict the dictionary being passed in 
	 * @return an ArrayList with the word ladder if there is a path or
	 * 		   null if there is not a path between start and end 
	 */
	public static ArrayList<String> getWordLadderDFS(String start_param, String end_param) {
		ArrayList<String> output = new ArrayList<String>();
		String start = start_param.toUpperCase();
		String end = end_param.toUpperCase();
		dictDFS = makeDictionary();
		
		try {
			output = getWordLadderDFS2(start, end);
		} catch (StackOverflowError e) {
			output = getWordLadderDFS2(start, end);
		}
		return output;
	}
	
	/** HELPER
	 * Implements a recursive depth first search that will take the parameter
	 * "start" and see if the word end can be found in a depth first traversal
	 * @param start the word to start from
	 * @param end the word to be found
	 * @param dict the dictionary being passed in 
	 * @return an ArrayList with the word ladder if there is a path or
	 * 		   null if there is not a path between start and end 
	 */
	public static ArrayList<String> getWordLadderDFS2(String start, String end) {
		
		/* Called when the user enters quit or when the word has been found. */
			
		Set<String> dict = dictDFS; 
		
		/* Checks to see if the start is the same as the end, meaning the
		 * word ladder is complete.
		 */
		if (start.equalsIgnoreCase(end)) {
			found = true;
		}
		
		/* Adds node to a visited ArrayList. */
		DFSvisited.add(start); 

		
		/* Iterates through every letter of the alphabet and tries out many combinations 
		 * in an attempt to find a word in the dictionary.
		 * 
		 */
		for (int k = 0; k < start.length(); k++) {
			
			/* Adds different letters in different places of start to find a valid word. */
			for (char c = 'A'; c <= 'Z' && !found; c++) {
				
				/* Makes the new word. */
				String checkW = start.substring(0, k) + c + start.substring(k + 1);
				
				/* If the word doesn't exist already and other conditions, calls recursive 
				 * DFS function to find a valid ladder. 
				 */
				if(!DFSvisited.contains(checkW) && dict.contains(checkW)) {
					DFSstore.put(checkW, start);
					getWordLadderDFS2(checkW, end);
				}
			}
		}

		if (found) return PrintDFS(start, end);

		ArrayList<String> Dne = new ArrayList<String>(); 
		Dne.add(start); 
		Dne.add(end); 
		return Dne; 	
		
	}

	
	/**
	 * Implements a breadth first search that will take the parameter
	 * "start" and see if the word end can be found in a breadth first traversal
	 * @param start the word to start from
	 * @param end the word to go to
	 * @return
	 */
    public static ArrayList<String> getWordLadderBFS(String start_param, String end_param) {
    	String start = start_param.toUpperCase();
    	String end = end_param.toUpperCase();
		Set<String> dict = makeDictionary();

		Queue<String> queue = new LinkedList<String>(); 
		Set<String> visited = new HashSet(); 
		HashMap<String, String> store = new HashMap<String, String>(); 
			
		queue.add(start); 
		
		/*
		 * Continues a BFS while the queue is not empty. If the queue is empty,
		 * then there is no word ladder. 
		 */
		while(!queue.isEmpty()){
			String rung = queue.remove(); 
			
			visited.add(rung); 
			
			/* Iterates through every letter of the alphabet and tries out many combinations 
			 * in an attempt to find a word in the dictionary.
			 */
			for (int i = 0 ; i < start.length(); i++){
				
				String checkW = ""; 
				String beg = rung.substring(0,i); 
				String last = rung.substring(i+1, start.length()); 
				
				/* Places every letter into a certain space of the checked word
				 * in order to find a word in the dictionary.
				 */
				for (int a = 0; a < alpha.length; a ++){
					checkW = beg + alpha[a] + last; 
					
					/* If the word is found, create an ArrayList ladder and return. */
					if (checkW.equals(end)){
						store.put(checkW, rung);
						String tempPrint = end;  
						ArrayList<String> toPrint = new ArrayList<String>(); 
						toPrint.add(end);
						
						/* Iterates through parent->child HashMap to get the 
						 * parent of each word in the ladder
						 */
						while (!tempPrint.equals(start.toUpperCase())){
							tempPrint = store.get(tempPrint); 
							toPrint.add(tempPrint); 
						}
						
						printLadder(toPrint);
						return toPrint; 
					}
					
					/* If there is not a match, then places the word in the queue (if it meets
					 * certain conditions) and continues with a BFS
					 */
					if (dict.contains(checkW) && !checkW.equals(rung) && !queue.contains(checkW) 
							&& !visited.contains(checkW)) {			
						queue.add(checkW); 
						store.put(checkW, rung); 
						visited.add(checkW); 

					}
				}
			}
		}
		
		/* ArrayList to be printed when there is no match. */
		ArrayList<String> Dne = new ArrayList<String>(); 
		Dne.add(start); 
		Dne.add(end); 
		//printLadder(Dne);
		return Dne; 
	}
    
    
    
    /**
     * Creates the dictionary
     * @return a HashSet that has the dictionary
     * GIVEN 
     */
	public static Set<String>  makeDictionary () {
		Set<String> words = new HashSet<String>();
		Scanner infile = null;
		try {
			infile = new Scanner (new File("five_letter_words.txt"));
		} catch (FileNotFoundException e) {
			System.out.println("Dictionary File not Found!");
			e.printStackTrace();
			System.exit(1);
		}
		while (infile.hasNext()) {
			words.add(infile.next().toUpperCase());
		}
		return words;
	}
	
	/**
	 * Prints the word ladder by traversing the passed in ArrayList
	 * @param ladder the ArrayList that contains the word ladder
	 */
	public static void printLadder(ArrayList<String> ladder) {
		
		if (ladder == null || ladder.size() == 2) {
			System.out.println("no word ladder can be found between " + ladder.get(0).toLowerCase() + " and " + ladder.get(1).toLowerCase());
			return; 
		}
		
		System.out.println("a " + (ladder.size() - 2) + " rung word ladder exists between " + ladder.get(0).toLowerCase() + " and " + ladder.get(ladder.size() - 1).toLowerCase());
		
		for (int i = ladder.size() - 1; i > -1; i--) {
			System.out.println(ladder.get(i).toLowerCase());
		}
	}
}
