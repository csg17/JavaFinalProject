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

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import edu.handong.csee.datas.firstExcel;

import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipFile;

public class ReaderThread implements Runnable{
	private String filePath;
	private BlockingQueue<firstExcel> queue;
	
	public ReaderThread(String filePath, BlockingQueue<firstExcel> queue) {
		this.filePath = filePath;
		this.queue = queue;
	}
	/*
	 * implement this function when call thread
	 * */
	public void run() {
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
		    
		        //ExcelReader myReader = new ExcelReader();
		        ExcelReader();
		        System.out.println("Reading file is end.\n");
		    }
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/*
	 * Read excel files.
	 * */
	public void ExcelReader() {
		System.out.println(filePath);
		
		try( InputStream inp = new FileInputStream(filePath) ) {
			Thread.sleep(1000);
			Workbook wb = WorkbookFactory.create(inp);
			Sheet sheet = wb.getSheetAt(0);
			Iterator<Row> iterator = sheet.iterator(); 
			
			while(iterator.hasNext()) {
				Row currentRow = iterator.next();
				Iterator<Cell> cellIterator = currentRow.iterator();
				
				firstExcel tempRow;
				while(cellIterator.hasNext()) {
					Cell currentCell = cellIterator.next();
					//형변환 시켜주기.
					//tempRow.setTitle(String.valuecurrentCell);
					// tempRow에 1row씩 저장하고 그걸 큐에 넣어주기. 
				}
				//queue.add(tempRow);
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
