package apps;
import java.io.*;
import java.util.*;
public class Driver {

	public static void main(String[] args) throws FileNotFoundException {
		// TODO Auto-generated method stub
		String test = "test2.txt";
		Scanner sc = new Scanner(new File(test));
		
		Graph g = new Graph(sc);
		Friends f = new Friends();
		
		System.out.println(f.shortestChain(g, "A", "D"));
		System.out.println(f.cliques(g, "rutgers"));
		System.out.println(f.connectors(g));
		sc.close();
	}

}
