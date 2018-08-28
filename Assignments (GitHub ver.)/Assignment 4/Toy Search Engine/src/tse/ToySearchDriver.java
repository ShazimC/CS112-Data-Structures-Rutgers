package tse;
import java.io.*;
import java.util.*;
public class ToySearchDriver {

	public static void main(String[] args) throws FileNotFoundException {
		// TODO Auto-generated method stub
		Scanner sc = new Scanner(new File("docs.txt"));
		sc.close();
		
		String docs = "docs.txt";
		String noiseWords = "noisewords.txt";
			
		ToySearchEngine tse = new ToySearchEngine();
		tse.buildIndex(docs, noiseWords);
		
		/*ArrayList<Occurrence> tests = new ArrayList<Occurrence>();
		tests.add(new Occurrence("AliceCh1.txt", 12));
		tests.add(new Occurrence("AliceCh1.txt", 13));
		tests.add(new Occurrence("AliceCh1.txt", 7));

		tests.add(new Occurrence("AliceCh1.txt", 5));
		tests.add(new Occurrence("AliceCh1.txt", 3));
		tests.add(new Occurrence("AliceCh1.txt", 2));
		
		tests.add(new Occurrence("WowCh1.txt", 6));
		for(int i = 0; i<tests.size(); i++)
			System.out.println(tests.get(i));
	
		System.out.println(tse.insertLastOccurrence(tests));
		System.out.println();
		for(int i = 0; i<tests.size(); i++)
			System.out.println(tests.get(i));
		System.out.println();*/
		
		ArrayList<String> result = tse.top5search("deep", "world");
		System.out.println(result);
	}

}
