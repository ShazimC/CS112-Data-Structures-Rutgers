package apps;

import structures.Queue;
import structures.Stack;

import java.util.*;

public class Friends {

	/**
	 * Finds the shortest chain of people from p1 to p2.
	 * Chain is returned as a sequence of names starting with p1,
	 * and ending with p2. Each pair (n1,n2) of consecutive names in
	 * the returned chain is an edge in the graph.
	 * 
	 * @param g Graph for which shortest chain is to be found.
	 * @param p1 Person with whom the chain originates
	 * @param p2 Person at whom the chain terminates
	 * @return The shortest chain from p1 to p2. Null if there is no
	 *         path from p1 to p2
	 */
	public static ArrayList<String> shortestChain(Graph g, String p1, String p2) {
		
		/** COMPLETE THIS METHOD **/
		
		// FOLLOWING LINE IS A PLACEHOLDER TO MAKE COMPILER HAPPY
		// CHANGE AS REQUIRED FOR YOUR IMPLEMENTATION
		
		
		ArrayList<String> chain = new ArrayList<String>();
		if(p1.equals(p2))
			return chain;
		
		int startIndexInMembers = g.map.get(p1);
		int endIndexInMembers = g.map.get(p2);
		Person start = g.members[startIndexInMembers];
		Person end = g.members[endIndexInMembers];
	
		ArrayList<Person> visited = new ArrayList<Person>();
		Queue<Person> unprocessed = new Queue<Person>();
		unprocessed.enqueue(start);
		
		int[] predecessors = new int[g.members.length];
		for(int i=0; i<predecessors.length; i++) {
			predecessors[i] = -1;
		}
		predecessors[startIndexInMembers] = startIndexInMembers;
		
		Person current = start;
		
		while(!unprocessed.isEmpty()) {
			current = unprocessed.dequeue();
			
			for(Friend i = current.first; i!=null; i = i.next) {
				Person friend = g.members[i.fnum];

				if(visited.contains(friend))
					continue;
				
				if(predecessors[g.map.get(friend.name)] == -1)
					predecessors[g.map.get(friend.name)] = g.map.get(current.name);
				
				if(friend.equals(end)) {
					int curr = g.map.get(friend.name);
					String currName = g.members[curr].name;
					chain.add(currName);
					int pred = predecessors[curr];
				
					while(true) {
						curr = pred;
						currName = g.members[curr].name;
						chain.add(currName);
						pred = predecessors[curr];
						if(currName.equals(start.name))
							break;
					}
					for(int j=chain.size()-2; j>=0; j--)
						chain.add(chain.remove(j));
					return chain;
				}
				unprocessed.enqueue(friend);
			}
			visited.add(current);
		}
		return chain;
	}
	
	/**
	 * Finds all cliques of students in a given school.
	 * 
	 * Returns an array list of array lists - each constituent array list contains
	 * the names of all students in a clique.
	 * 
	 * @param g Graph for which cliques are to be found.
	 * @param school Name of school
	 * @return Array list of clique array lists. Null if there is no student in the
	 *         given school
	 */
	public static ArrayList<ArrayList<String>> cliques(Graph g, String school) {
		
		/** COMPLETE THIS METHOD **/
		
		// FOLLOWING LINE IS A PLACEHOLDER TO MAKE COMPILER HAPPY
		// CHANGE AS REQUIRED FOR YOUR IMPLEMENTATION
		ArrayList<ArrayList<String>> cliques = new ArrayList<ArrayList<String>>();
		
		String start = "";
		for(int i=0; i<g.members.length; i++) {
			if(g.members[i].school!=null && g.members[i].school.equals(school)) {
				start = g.members[i].name;
				break;
			}
		}
		if(start.equals(""))
			return cliques;
		
		ArrayList<String> newClique = new ArrayList<String>();
		newClique.add(start);
	
		ArrayList<String> visited = new ArrayList<String>();
		Stack<String> unprocessed = new Stack<String>();
		visited.add(start);
		unprocessed.push(start);
		
		String current = start;
		while(!unprocessed.isEmpty()) {
			current = unprocessed.pop();
			
			for(Friend i = g.members[g.map.get(current)].first; i!=null; i = i.next) {
				Person friend = g.members[i.fnum];
				if(visited.contains(friend.name))
					continue;
				if(friend!=null) {
					if(friend.school==null || !friend.school.equals(school)) {
						cliques.add(cliquesHelper(g, school, visited, friend));
					}
					else if(friend.school.equals(school)) {
						newClique.add(friend.name);
						visited.add(friend.name);
					}
				}
				if(friend.school!=null && friend.school.equals(school))
					unprocessed.push(friend.name);
			}
			if(!visited.contains(current))
				visited.add(current);
		}
		
		cliques.add(newClique);
		
		for(int i=0; i<cliques.size(); i++) {
			if(cliques.get(i).isEmpty()) {
				if(i==0) {
					cliques.set(i, cliques.get(cliques.size()-1));
					cliques.remove(cliques.size()-1);
					continue;
				}
				cliques.remove(i);
			}
		}
		return cliques;
		
	}
	private static ArrayList<String> cliquesHelper(Graph g, String school, ArrayList<String> visited, Person start){
		
		ArrayList<String> clique = new ArrayList<String>();
		
		Stack<String> unprocessed = new Stack<String>();
		String current = start.name;
		visited.add(current);
		unprocessed.push(current);
		
		while(!unprocessed.isEmpty()) {
			current = unprocessed.pop();
			
			for(Friend i=g.members[g.map.get(current)].first; i!=null; i = i.next) {
				Person friend = g.members[i.fnum];
				if(friend.school == null || !friend.school.equals(school) || visited.contains(friend.name))
					continue;
				if(friend.school.equals(school)) {
					clique.add(friend.name);
					visited.add(friend.name);
				}
				unprocessed.push(friend.name);
			}
			if(!visited.contains(current))
				visited.add(current);
		}
		
		return clique;
	}
	
	/**
	 * Finds and returns all connectors in the graph.
	 * 
	 * @param g Graph for which connectors needs to be found.
	 * @return Names of all connectors. Null if there are no connectors.
	 */
	public static ArrayList<String> connectors(Graph g) {
		
		/** COMPLETE THIS METHOD **/
		
		// FOLLOWING LINE IS A PLACEHOLDER TO MAKE COMPILER HAPPY
		// CHANGE AS REQUIRED FOR YOUR IMPLEMENTATION
		ArrayList<String> connectors = new ArrayList<String>();
		int dfsValue = 0;
		int backValue = 0;
		int start = 0;
		
		ArrayList<Person> visited = new ArrayList<Person>();
		
		HashMap<String, Integer> dfsValues = new HashMap<String, Integer>(500, (float)2.0);
		HashMap<String, Integer> backValues = new HashMap<String, Integer>(500, (float)2.0);
		ArrayList<String> backedAlready = new ArrayList<String>();
		
		while(start<g.members.length){
			if(!visited.contains(g.members[start]))
				connectorsHelper(g, connectors, dfsValue, backValue, visited, start, true, dfsValues, backValues, backedAlready);
			start++;
		}
		return connectors;
	}

		private static void connectorsHelper(Graph g, ArrayList<String> connectors, 
				int dfsValue, int backValue, ArrayList<Person> visited, int start, boolean isStart, HashMap<String, Integer> dfsValues, 
				HashMap<String, Integer> backValues, ArrayList<String> backedAlready){
		
		Person current = g.members[start];		
		visited.add(current);
		dfsValues.put(current.name, dfsValue);
		backValues.put(current.name, backValue);

		Friend friend = current.first;
		
		while(friend != null){
			Person pFriend = g.members[friend.fnum];
	
			dfsValue++;
			backValue++;
			
			if(!visited.contains(pFriend)){
				connectorsHelper(g, connectors, dfsValue, backValue, visited, 
				friend.fnum, false, dfsValues, backValues, backedAlready);
				
				if(dfsValues.get(current.name) > backValues.get(pFriend.name)){
					int minBack = Math.min(backValues.get(current.name), 
					backValues.get(pFriend.name));
					backValues.put(current.name, minBack);
				}
				if(dfsValues.get(current.name) <= backValues.get(pFriend.name)){
					if((backedAlready.contains(current.name) || isStart == false) && !connectors.contains(current.name)) {
						connectors.add(current.name);
					}
				}
				backedAlready.add(current.name);
			}
			else{
				int minBack = Math.min(backValues.get(current.name), dfsValues.get(pFriend.name)); 
				backValues.put(current.name, minBack);
			}friend = friend.next;
		}
	}
	
}

