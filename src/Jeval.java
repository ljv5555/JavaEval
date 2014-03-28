import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * 2014-03-28
 * <pre style="border:2px solid blue;">
 * This class evaluates Java commands passed in 
 * via command line arguments
 * 
 * Example: 
 * java Jeval "System.out.println(123);"
 * 
 * Returns:
 * 123
 * </pre>
 * 
 * License: GPL - see http://www.gnu.org/licenses/gpl.txt
 * 
 * @author ljv5555
 * @see https://github.com/ljv5555/JavaEval
 * 
 * 
 *
 */
public class Jeval {

	public static void main(String[] args) throws Exception 
	{
//		LinkedList<String> cmda = new LinkedList<String>();
		String allCommands = join(args,"\n ");
		allCommands = "public class ProcClass{ public static void main(String[] args){ "+allCommands+" } } ";
		File f = new File("ProcClass.java");
		File f2 = new File("ProcClass.class");
		FileWriter fw = new FileWriter(f.getAbsolutePath(),false);
		fw.write(allCommands);
		fw.close();
		//System.out.println("compiling "+f.getAbsolutePath()+"...");
		LinkedList<String> compileResults = new LinkedList<String>();
		LinkedList<String> runResults = new LinkedList<String>();
		
		compileResults = runCommand(new String[]{"javac",f.getAbsolutePath()});
		//Thread.sleep(20);
		System.out.print(join(compileResults,"\n").trim());
		//System.out.println("running "+f.getAbsolutePath().replaceAll("\\.java", ".class")+"...");
		runResults = runCommand(new String[]{"java","ProcClass"});
		//Thread.sleep(20);
		System.out.println(join(runResults,"\n"));
		f.delete();
		f2.delete();
		
	}

	public static String join(String[] a,String j)
	{
		StringBuilder rtn = new StringBuilder();
		boolean isFirst=true;
		for(String s : a)
		{
			if(isFirst){isFirst=false;}
			else{rtn.append(j);}
			rtn.append(s);
		}
		return rtn.toString();
	}
	public static String join(List<String> a,String j)
	{
		return join(a.toArray(new String[0]),j);
	}
	public static LinkedList<String> runCommand(String[] cmda) throws Exception
	{
		return runCommand(Arrays.asList(cmda));
	}
	public static LinkedList<String> runCommand(List<String> cmda) throws Exception
	{
		
		LinkedList<String> rtn = new LinkedList<String>();
		ProcessBuilder pb = new ProcessBuilder(cmda);
		pb.redirectErrorStream(true);
		Process p = pb.start();
		BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
		p.waitFor();
		while(true)
		{
			String l = br.readLine();
			if(l==null){break;}
			rtn.add(l);
		}
		return rtn;
	}
}
