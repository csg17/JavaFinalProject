package edu.handong.csee.threads;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class WriteThread implements Runnable {
	private String desPath;
	private BlockingQueue<String> queue;
	private int flag;
	private int flag2; 
	
	public WriteThread(String desPath, BlockingQueue<String> queue) {
		this.desPath = desPath;
		this.queue = queue;
		flag = 1;
		flag2 = 1;
	}
	
	public void run() {
	 	PrintWriter outputStream = null;
		File targetFile = new File(desPath);
		//System.out.println(desPath);
		
		String str = null;
		int index;
		System.out.println("<<<<<IN WRITE THREAD>>>>>\n");
		
		try {
			if("csv".equals(desPath.substring(desPath.lastIndexOf('.')+1)))
				{
				desPath = desPath.substring(0, desPath.lastIndexOf('.')) + "1" + ".csv";
				index = 1;
				}
			else 
				{
				desPath = desPath.substring(0, desPath.lastIndexOf('.')) + "1" + ".xlsx";
				index = 0;
				}
				//path가 경로인지 파일 이름인지
			if( !targetFile.exists()) { 
				if(targetFile.getParent() != null ) {
					targetFile.getParentFile().mkdirs();
				}
			}
			
			switch(index) {
				case 1: //.csv로 저장해야하는 경우
					outputStream = new PrintWriter(targetFile);
					writeCSVFile(outputStream);
					break;
				
				//엑셀로 저장해야하는 경우.  
				case 0:
					XSSFWorkbook workbook= new XSSFWorkbook();
				    XSSFSheet sheet = workbook.createSheet("JavaFinalProject");
				    ArrayList<String> buffer = new ArrayList<String>();
				    
				    FileOutputStream outputStreamE = new FileOutputStream(new File(desPath));
					writeEXCELFile(outputStreamE, workbook, sheet, buffer);
					break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void writeCSVFile(PrintWriter outputStream) {
		//int flag=1; 필드로 빼주기.
		
		try {
			System.out.println("[I will write]\n");
			
			while(true){
	            String buffer = queue.take();
	            //Check whether end of file has been reached
	            if(buffer.equals("EOF")){ 
	            	if(flag == 0) break;
	            	
	        	 	desPath = desPath.substring(0, desPath.indexOf('1')) + "2" + ".csv";
	        	 	File targetFile2 = new File(desPath);
	        	 	PrintWriter outputStream2 = new PrintWriter(targetFile2);
	        	 	flag--;
	            	writeCSVFile(outputStream2);
	            }
	            
	            else {
		            outputStream.println(buffer);
		            outputStream.flush();
	            }
	        }   
	
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			//e.getMessage();
		}
		System.out.println("<< The file is saved>>");
	}
	
	public void writeEXCELFile(FileOutputStream outputStreamE, XSSFWorkbook workbook, XSSFSheet sheet, ArrayList<String> buffer) throws FileNotFoundException {
		int rowNum=0;

		  while(true) {
			  try {
		
				String tempQ = queue.take();
				if(tempQ.equals("EOF")) {
					//break;
					//System.out.println("들어왓당" +flag2);
					if(flag2 == 0) break;
					
					XSSFWorkbook workbook2= new XSSFWorkbook();
				    XSSFSheet sheet2 = workbook2.createSheet("JavaFinalProject2");

				    desPath = desPath.substring(0, desPath.indexOf('1')) + "2" + ".xlsx";

				    FileOutputStream outputStreamE2 = new FileOutputStream(new File(desPath));
				    ArrayList<String> buffer2 = new ArrayList<String>();
				    flag2--;
					writeEXCELFile(outputStreamE2, workbook2, sheet2, buffer2);
					break;
				}
				else { buffer.add(tempQ); }
				
			  } catch (Exception e) {
				  e.printStackTrace();
			  }
		  }
		  
		  for (String data : buffer) {
			  Row row = sheet.createRow(rowNum++);
			  int colNum=0;
			  
			  for( String d : data.split(",")) {
				  Cell cell = row.createCell(colNum++);
				  cell.setCellValue((String) d);
			  }
		  }
	
			try {
				workbook.write(outputStreamE);
				workbook.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		  
			System.out.println("<< The file is saved>>");
	}
}