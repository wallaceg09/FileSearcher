import java.io.File;
import java.util.Scanner;


public class Main {

	public static void main(String[] args) {
		if(args.length != 2 && args.length != 1)
		{
			System.out.print("Wrong usage. Parameters should be: startingDirectory [searchPattern](optional)");
			return;
		}
		String searchPattern = "";
		if(args.length == 1)
		{
			Scanner scanner = new Scanner(System.in);
			System.out.print("Please enter a search pattern: ");
			searchPattern = scanner.nextLine();
			System.out.println();
		}
		else
		{
			searchPattern = args[1];
		}
		if(searchPattern.equals("") || searchPattern.equals(" "))
		{
			System.out.println("Invalid search pattern.");
		}
		File file = new File(args[0]);
		Searcher searcher = new Searcher(file, searchPattern);
		Thread thread = new Thread(searcher);
		System.out.println("Starting search.");
		long startTime = System.currentTimeMillis();
		thread.start();
		try {
			thread.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		long endTime = System.currentTimeMillis();
		String[] foundFiles = searcher.getMatchingFilenamesAndReset();
		System.out.printf("Search concluded. %d of %d files found \"%s\" in %d ms.\n", foundFiles.length, Searcher.getNumberParsedFilesAndReset(), searchPattern, endTime - startTime);
		
		for(String filename : foundFiles)
		{
			System.out.println(filename);
		}
	}

}
