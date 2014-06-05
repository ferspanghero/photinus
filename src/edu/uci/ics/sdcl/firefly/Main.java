import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;


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
					int methodFound = currentLine.indexOf("method name");
					System.out.println(currentLine);
					
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
