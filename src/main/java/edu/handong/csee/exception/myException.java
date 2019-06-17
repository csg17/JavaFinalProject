package edu.handong.csee.exception;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class myException extends Exception{
	public myException () {
		super("This string has to be revised. \n");
	}
	public myException(String message) {
		super(message);
	}
	
	public void setFileName(String file) {
		PrintWriter outStream = null;
		String zipName = file.substring(file.lastIndexOf('/')+1, file.lastIndexOf('.'));

		file = file.substring(0, file.lastIndexOf('/')+1) + "error.csv";
		//System.out.println(file);
		File targetFile = new File(file);
		
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

	