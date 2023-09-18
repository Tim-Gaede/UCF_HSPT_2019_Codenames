
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

@SuppressWarnings("unchecked")
public class codenames {
	
	//this approach will use the bruteforce + greedy strategy
	//since at most 8 companies will begin with the same letter,
	//and since the first letter for each company is fixed to be that beginning letter,
	//we can split up the input into 26 different "buckets" of companies, for every beginning letter
	//each bucket will have at most 8 companies
	//for each bucket, we can bruteforce each permutation of companies
	//for each permutation, greedily assign the first company the first valid subsequence that hasn't
	//been taken yet... this way we can always get an optimal answer in at least one permutation, or
	//guarantee that one doesn't exist...
	
	
	//class for each company
	class Company {
		String name; //the name for the company initially
		String ans; //our answer for this company
		boolean[][] subseq; //each subsequence that this company name consists of
		Company(String s) { 
			//since we only care about the last two characters
			//we can precompute all the subsequences that this company
			//will have for the last 2 characters (also needs to be in all uppercase)
			name = s.replaceAll(" ", "").toUpperCase();
			//precompute the subsequences
			subseq = new boolean[26][26];
			for(int i = 1; i < name.length(); ++i) {
				int c1 = name.charAt(i) - 'A';
				for(int j = i + 1; j < name.length(); ++j) {
					int c2 = name.charAt(j) - 'A';
					subseq[c1][c2] = true;
				}
			}
		}
	}
	
	//for a given permutation array "order", check if this permutation gives a
	//valid assignment of codenames for the "list" of companies
	boolean check(int[] order, ArrayList<Company> list) {
		boolean[][] taken = new boolean[26][26]; //see which 2 letter combinations have been taken so far
		for(int i : order) { //go through the order array for the ordered permutation
			Company c = list.get(i); //get the current company we're looking at
			boolean found = false; //check if this company has been matched yet
			
			//now go through all possible subsequences that this company can have
			for(int c1 = 0; !found && c1 < 26; ++c1) {
				for(int c2 = 0; !found && c2 < 26; ++c2) {
					if(!c.subseq[c1][c2]) continue; //if this subsequence doesn't exist, then continue
					if(taken[c1][c2]) continue; //if this subsequence exists in the company, but has been taken, continue
					//otherwise, greedily assign this subsequence to this company
					c.ans = "" + c.name.charAt(0) + (char) (c1 + 'A') + (char) (c2 + 'A');
					taken[c1][c2] = true; //mark that this subsequence is now taken
					found = true; //we found an assignment for this company
				}
			}
			
			//if an assignment cannot be made, it's impossible to satisfy the list with this permutation
			if(!found) return false;
		}
		
		//since all things have been assigned, we can return true
		return true;
	}
	
	//function to generate permutations
	boolean go(int ind, boolean[] used, int[] perm, ArrayList<Company> list) {
		if(ind == used.length) return check(perm, list); //we have found a permutation, check if it's valid
		
		//assign the next number that hasn't been taken yet for the current index "ind"
		for(int i = 0; i < used.length; ++i) {
			if(used[i]) continue; //has been used, continue
			//mark used and assign the number to the current index
			used[i] = true;
			perm[ind] = i;
			//recursively call to keep building the permutation
			boolean ans = go(ind + 1, used, perm, list);
			//if this assignment results in a success, no need to go further, just return true here
			if(ans) return true;
			//otherwise, unassign this number from the used and perm arrays
			perm[ind] = -1;
			used[i] = false;
		}
		
		//no assignemnt was found for this state, so return false
		return false;
	}
	
	//function to check if this bucket of companies can have a valid assignemnt
	boolean go(ArrayList<Company> list) {
		//setup for running permutations
		int n = list.size();
		int[] perm = new int[n]; Arrays.fill(perm, -1);
		boolean[] used = new boolean[n];
		//generate permutations!
		return go(0, used, perm, list);
	}
	
	//solve function
	public void solve(Scanner in, PrintWriter out) {
		int t = Integer.parseInt(in.nextLine()); //number of test cases
		for(int tt = 1; tt <= t; ++tt) { //go through each test case
			out.printf("Event #%d:%n", tt); //header for each test case
			int n = Integer.parseInt(in.nextLine()); //number of companies
			Company[] comps = new Company[n]; //save each company in an array for index access
			ArrayList<Company>[] buckets = new ArrayList[26]; //also save each company according to the bucket they belong to (by starting letter)
			for(int i = 0; i < 26; ++i) buckets[i] = new ArrayList<>(); //initialize each bucket
			for(int i = 0; i < n; ++i) { //read in each company name
				comps[i] = new Company(in.nextLine()); //initialize the company and put it in the array
				buckets[comps[i].name.charAt(0) - 'A'].add(comps[i]); //also put it in the corresponding bucket
			}
			boolean possible = true; //see if it's possible to assign valid, unique codenames for all companies
			for(int i = 0; i < 26; ++i) { //for each bucket
				possible &= go(buckets[i]); //see if it can produce a valid assignemnt of codenames
			}
			if(!possible) out.println("Not Possible"); //if any bucket is invalid, the entire thing is invalid
			else {
				for(int i = 0; i < n; ++i) { //otherwise print out all the answers for companies
					out.println(comps[i].ans);
				}
			}
			out.println(); //blank line after test case
		}
	}
	
	public static void main(String[] args) {
		Scanner in = new Scanner(System.in);
		PrintWriter out = new PrintWriter(System.out);
		new codenames().solve(in, out);
		in.close();
		out.close();
	}
}
