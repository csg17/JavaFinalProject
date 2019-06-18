package edu.handong.csee.exception;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class myException extends Exception{
	public myException () {
		super("This string has to be revised. \n");
	}
	public myException(String message) {
		super(message);
	}
	// users/seulgi/desktop/ff.csv
	
	public void setFileName(String desfile, String inputfile) {
		PrintWriter outStream = null;
		String zipName = inputfile.substring(inputfile.lastIndexOf('/')+1, inputfile.length());
	
		desfile = desfile.substring(0, desfile.lastIndexOf('/')+1) + "error.csv";
		//System.out.println(file);
		File targetFile = new File(desfile);
		
		if( !targetFile.exists()) { 
			if(targetFile.getParent() != null ) {
				targetFile.getParentFile().mkdirs();
			}
		}
		try {
			outStream = new PrintWriter(targetFile);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block 
			e.printStackTrace();
		}
		
		outStream.println(zipName);
		outStream.flush();
	}
}

	