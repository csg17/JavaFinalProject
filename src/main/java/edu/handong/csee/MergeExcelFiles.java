package edu.handong.csee;

import edu.handong.csee.datas.firstExcel;
import edu.handong.csee.threads.*;
import edu.handong.csee.threads.ReaderThread;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;

public class MergeExcelFiles {
	// the components for options 
	private String inputPath;
	private String desPath;
	private boolean help;
	
	/*
	 * In this main method, instantiate MergeExcelFile class and run it.
	 * */
	public static void main(String[] args) {
		MergeExcelFiles mergeFile = new MergeExcelFiles();
		mergeFile.run(args);
	}
	
	/*
	 * In run method, we will implements reader thread and write thread.
	 * */
	public void run(String[] args) {
		
		Options options = createOption();
		if ( parseOption(options, args) ) {
			if (help) {
				printHelp(options);
				System.out.println( "<<This is printed since you want>>");
			}
			BlockingQueue<String> queue = new ArrayBlockingQueue<String>(1024);
			ReaderThread reader = new ReaderThread(inputPath, queue);
			WriteThread writer = new WriteThread(desPath, queue);
			new Thread(reader).start();
			new Thread(writer).start();
		}
	}
	
	private boolean parseOption(Options options, String[] args) {
	
		CommandLineParser parser = new DefaultParser();

		try {
			CommandLine cmd = parser.parse(options, args);
			
			inputPath = cmd.getOptionValue("i");
			desPath = cmd.getOptionValue("o");
			help = cmd.hasOption("h");
		} catch(Exception e) { //최상위 클래스 넣어서 한번에 처리, exception 나오면 도움말 출력  
			System.out.println("<<This is option problem>>");
			printHelp(options);
		 	System.exit(0);
		}
		return true;
	}

	private void printHelp(Options options) {
		// TODO Auto-generated method stub
		HelpFormatter Formatter = new HelpFormatter(); // 도움말 자동으로 만들어주는 클래
		String header = "JavaFinalProject";
		String footer = "";
		Formatter.printHelp("JavaFinalProject", header, options, footer, true);
	}

	// DEFINITION
	private Options createOption() {
		Options options = new Options();
		
		options.addOption(Option.builder("i").longOpt("input")
				.desc("Set a input file path") // description
				.hasArg() //값받아야 하니
				.argName("Input file name") //argument name이 어떤 걸 의미하는지 보여주는 역
				.required()
				.build()); //반드시 필요하다는 걸 의미, 안들어오면 exception 발생.
		
		options.addOption(Option.builder("o").longOpt("output")
				.desc("Set an output file path") // description
				.hasArg() //값받아야 하니
				.argName("Output file name") //argument name이 어떤 걸 의미하는지 보여주는 역
				.required()
				.build());
		
		options.addOption(Option.builder("h").longOpt("help")
				.desc("Show a Help page") // description
				.argName("Help") //argument name이 어떤 걸 의미하는지 보여주는 역       
				.build());
		
		return options;
	}
}
