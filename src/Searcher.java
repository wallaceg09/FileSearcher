import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;


public class Searcher implements Runnable{
	
	private File startingFile;
	private String searchString;
	private static long NUMBER_PARSED_FILES = 0;
	private static ArrayList<String> MATCHING_FILES;
	
	private static Logger LOGGER;
	
	static
	{
		LOGGER = Logger.getLogger(Searcher.class.getName());
		LOGGER.setLevel(Level.SEVERE);

		resetMatchingFilenames();
	}
	
	public Searcher(File startingDirectory, String searchString)
	{
		this.startingFile = startingDirectory;
		this.searchString = searchString.toLowerCase();
	}
	
	public void run()
	{
		if(startingFile.isDirectory())
		{
			for(File file : startingFile.listFiles())
			{
				Searcher searcher = new Searcher(file, searchString);
				Thread searchThread = new Thread(searcher);
				searchThread.start();
				try {
					searchThread.join();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		else if(startingFile.isFile())
		{
			LOGGER.info(String.format("Searching %s.\n", startingFile.getAbsoluteFile()));
			++NUMBER_PARSED_FILES;
			try {
				Scanner scanner = new Scanner(startingFile);
				String line = "";
				while((line = scanner.nextLine()) != null)
				{
					boolean wordFound = searchLine(line);
					if(wordFound)
					{
						LOGGER.info("FOUND");
						MATCHING_FILES.add(startingFile.getAbsolutePath());
						break;
					}
				}
				scanner.close();
			} catch (FileNotFoundException fnfe) {
				//fnfe.printStackTrace();
			} catch (NoSuchElementException nsee)
			{
				//nsee.printStackTrace();
			}
		}
	}
	
	/**
	 * @param line
	 * @return true if the word was found, false otherwise.
	 */
	private boolean searchLine(String line)
	{
		for(String word : line.split("\\s+"))
		{
			if(word.toLowerCase().contains(searchString))
			{
				return true;
			}
		}
		return false;
	}
	
	public static long getNumberParsedFiles()
	{
		return NUMBER_PARSED_FILES;
	}
	
	public static long getNumberParsedFilesAndReset()
	{
		long output = getNumberParsedFiles();
		resetNumberParsedFiles();
		return output;
	}
	
	public static void resetNumberParsedFiles()
	{
		NUMBER_PARSED_FILES = 0;
	}
	
	public static String[] getMatchingFilenames()
	{
		return MATCHING_FILES.toArray(new String[0]);
	}
	
	public static void resetMatchingFilenames()
	{
		MATCHING_FILES = new ArrayList<String>();
	}
	
	public static String[] getMatchingFilenamesAndReset()
	{
		String[] output = getMatchingFilenames();
		resetMatchingFilenames();
		return output;
	}
}
