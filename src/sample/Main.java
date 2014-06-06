import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;


public class Main
{

	public static void main(String[] args)
	{
		File folderPath = null;
		File paths[];
		
		if (args.length == 0)
		{
			System.out.println("The path of the folder is missing.");
			System.exit(0);
		}

		try
		{
			folderPath = new File(args[0]);		// setting the File to the path
			paths = folderPath.listFiles();		// listing all the files on that path
			
			for (File pathFile:paths)			// for each file...
			{
				/* CHECK IF THE FILE IS .JAVA */
				BufferedReader br = new BufferedReader(new FileReader(pathFile));
				String currentLine;
				while( (currentLine = br.readLine()) != null )
				{
					/* searching method key words and non-method key words*/
					int visibilityFound[] = {currentLine.indexOf("public"), currentLine.indexOf("protected"),
							currentLine.indexOf("private")};
					int braceFound = currentLine.indexOf("(");
					int curlyBraceFound = currentLine.indexOf("{");
					int dotComaFound = currentLine.indexOf(";");
					int classFound = currentLine.indexOf("class");
					int visibilityFoundResult = visibilityFound[0]*visibilityFound[1]*visibilityFound[2];
					/* identifying methods */
					if ( (visibilityFoundResult>0)&&( ((curlyBraceFound>-1)&&(classFound==-1)) || 
							(braceFound>-1)&&(dotComaFound==-1) ) ) 
					{
						/* parsing into words */
						System.out.println(currentLine);
						String newLine = new String();
						newLine = currentLine.replaceAll("\\(", " ");
						newLine = newLine.replaceAll("\\)", " ");
						newLine = newLine.replaceAll(",", " ");
						newLine = newLine.replaceAll("\\{", "");
						String words[] = newLine.split(" ");
						List<String> listOfWords = new ArrayList<String>();
						for (String word : words)
						{
							if(word != null && word.length() > 0) 
							{
								/* new valid word found, adding to the list */
								word = word.replaceAll("\\s+","");
								listOfWords.add(word);
								System.out.println(word);
							}	
						}
						System.out.println("----------------------");
						Thread.sleep(2000);
					}	
				}
				br.close();
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		
		
		

	}

}
