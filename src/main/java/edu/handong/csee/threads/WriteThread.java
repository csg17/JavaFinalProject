package edu.handong.csee.threads;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.concurrent.BlockingQueue;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

public class WriteThread implements Runnable {
	private String desPath;
	private BlockingQueue<String> queue;
	
	public WriteThread(String desPath, BlockingQueue<String> queue) {
		this.desPath = desPath;
		this.queue = queue;
	}
	
	public void run() {
	 	PrintWriter outputStream = null;
	 	desPath = desPath.substring(0, desPath.lastIndexOf('.')) + "1" + ".csv";
		File targetFile = new File(desPath);
		System.out.println(desPath);
		String str = null;
		int index;
		System.out.println("IN WriteThread\n");
		
		try {
			
			if("csv".equals(desPath.substring(desPath.lastIndexOf('.')+1)))
				index = 1;
			else 
				index = 0;
				//path가 경로인지 파일 이름인지
			if( !targetFile.exists()) { 
				if(targetFile.getParent() != null ) {
					targetFile.getParentFile().mkdirs();
				}
			}
			
			outputStream = new PrintWriter(targetFile);
			
			switch(index) {
				case 1: // .csv로 저장해야하는 경우
				writeCSVFile(outputStream);
				break;
				
				//엑셀로 저장해야하는 경우.  
				case 0:
					break;
			}
		} catch (Exception e) {
			System.out.println("This is wrong root. Write again please.");
			System.exit(0);
		}
	}
	
	public void writeCSVFile(PrintWriter outputStream) {
		try {
			System.out.println("I will write\n");
			
			while(true){
	            String buffer = queue.take();
	            //Check whether end of file has been reached
	            if(buffer.equals("EOF")){ 
	        	 	desPath = desPath.substring(0, desPath.lastIndexOf('.')) + "2" + ".csv";
	        	 	File targetFile2 = new File(desPath);
	        	 	PrintWriter outputStream2 = new PrintWriter(targetFile2);
	            	writeCSVFile(outputStream2);
	            }
	            System.out.println(buffer);
	            outputStream.println(buffer);
	            outputStream.flush();
	        }   
	
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("<< The file is saved>>");
	}
	public void writeEXCELFile() {
		
	}
}
