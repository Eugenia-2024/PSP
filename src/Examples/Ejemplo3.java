package Examples;

import java.io.*;

public class Ejemplo3 {
	public static void main(String[] args) throws IOException, InterruptedException { 
		String comando="dir"; //comando a executar i params
		ProcessBuilder pb = new ProcessBuilder("cmd.exe", "/c", "dir");
		Process p;
		// 0 will mean Ok exec, 1 will mean Error
		int exitVal;

		
		
		if (args.length<1) {
			System.out.println("You need to enter the filename.");
			System.exit(1);
		}// parameter control

		try{ // creating an exit file
			FileOutputStream fos=new FileOutputStream(args[0]); 
			PrintWriter pw=new PrintWriter (fos);

			//execute command
		    p = pb.start(); //

			// created Buffer
			InputStream is=p.getInputStream(); 
			BufferedReader br=new BufferedReader(new InputStreamReader (is));

			String line;
			exitVal = p.waitFor();
			//I read a line and insert it into the file
			while ((line=br.readLine()) !=null){
				System.out.println("Give a filename for exit..."); 
				System.out.println("I insert in "+args [0]+">"+line ); 
				pw.println(line); // adding line to the file
			}//fi 

			br.close();
			pw.close();
			
			 
			System.out.println("Exit Value "+exitVal); 

		}catch (Exception e) {
			e.printStackTrace(); 
		}

	}//fi main 
}//fi Exemple3