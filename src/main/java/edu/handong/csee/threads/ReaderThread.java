package edu.handong.csee.threads;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.concurrent.BlockingQueue;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import edu.handong.csee.exception.myException;
import edu.handong.csee.mygenerics.*;

import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipFile;

public class ReaderThread implements Runnable{
	private String filePath;
	private BlockingQueue<String> queue;
	private String desPath;
	
	public ReaderThread(String filePath, String desPath, BlockingQueue<String> queue) {
		this.filePath = filePath;
		this.queue = queue;
		this.desPath = desPath;
	}
	/*
	 * implement this function when call thread
	 * */
	public void run() {
		System.out.println("\n<<<<<IN READ THREAD>>>>>\n");
		readFileInZip(filePath);
	}
	
	/*
	 * Change the zip file to excel files
	 * */
	public void readFileInZip(String path) {
		ZipFile zipFile;
		try {
			zipFile = new ZipFile(path);
			Enumeration<? extends ZipArchiveEntry> entries = zipFile.getEntries();

		    while(entries.hasMoreElements()){
		    	ZipArchiveEntry entry = entries.nextElement();
		        InputStream stream = zipFile.getInputStream(entry);
		    
		        ExcelReader(stream);
		        queue.put("EOF");
		        System.out.println("Reading file is end.\n");
		    }
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/*
	 * Read excel files.
	 * */
	public void ExcelReader(InputStream is) {
		System.out.println(filePath);
		
		try (InputStream inp = is) {
			
		    Workbook wb = WorkbookFactory.create(inp);
            Sheet datatypeSheet = wb.getSheetAt(0);
            
            Iterator<Row> iterator = datatypeSheet.iterator();

			//ArrayList<String> tempRow = new ArrayList<String>();
			String finalRow = null;
			
			int i=0;
			while(iterator.hasNext()) {
				
				Row currentRow = iterator.next();
				Iterator<Cell> cellIterator = currentRow.iterator();
				
				if(currentRow.getPhysicalNumberOfCells()==4) { continue; }
				//System.out.println("cell: " + currentRow.getPhysicalNumberOfCells() + "\n");
				if(i==0) { 
					finalRow = "파일번호"; 
				}
				else { finalRow = filePath.substring(filePath.lastIndexOf('/')+1,filePath.lastIndexOf('.')); }
				
				ArrayList<String> tempRow = new ArrayList<String>();
				
				while(cellIterator.hasNext()) {
					Cell currentCell = cellIterator.next();
					tempRow.add(String.valueOf(currentCell));
					// tempRow에 1row씩 저장하고 그걸 큐에 넣어주기. */
				}
				// exception : '\n'나오거나, ','나오는 경우 !!  
				// tempRow를 다시 csv에 저장할 수 있게 만들기.  
				for(String tr : tempRow) {
					try {
						if(tr.indexOf('\n')>=0 || tr.indexOf(',')>=0) throw new myException();
					}
					catch(myException e){
						
						if(tr.indexOf('\n')>=0) {
							checkingIndex<String> ch1 = new checkingIndex<String>(tr);
							tr = ch1.deleteEnter();
							//System.out.println(tr);
						}
						if(tr.indexOf(',')>=0) {
							checkingIndex<String> ch2 = new checkingIndex<String>(tr);
							tr = ch2.deleteComma();
						}
						
						//System.out.println(e.getMessage());
						e.setFileName(desPath, filePath);
					}
					finalRow = finalRow + ", " + tr; 
					//System.out.println(finalRow);
				}
				
				queue.put(finalRow);
				i++;
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace(); 
		}
		
	}
}
