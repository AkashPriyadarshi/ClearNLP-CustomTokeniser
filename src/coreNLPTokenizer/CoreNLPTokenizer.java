package coreNLPTokenizer;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.log4j.Priority;
import org.apache.log4j.PropertyConfigurator;

import clearNLP.ClearNLP;
import edu.stanford.nlp.ling.HasWord;
import edu.stanford.nlp.process.DocumentPreprocessor;


public class CoreNLPTokenizer {
	final static Logger logger = Logger.getLogger(CoreNLPTokenizer.class);
	final static String clerNLPModelType="general-en";
	public static void main(String[] args) throws IOException {
		PropertyConfigurator.configure(CoreNLPTokenizer.class.getResourceAsStream("log4j.properties"));
		File inputDir = new File(args[0]);			
		CoreNLPTokenizer coreNLPTokenizer = new CoreNLPTokenizer();
		System.out.println("loading ClearNLP packages \n");
		
		ClearNLP clearNLP = new ClearNLP(clerNLPModelType);
		
		System.out.println("All ClearNLP packages are loaded\n");
		//input file List
		File[] listOfFiles = inputDir.listFiles();
		System.out.println(".........................");
		 for (int i = 0; i < listOfFiles.length; i++) {
		      if (listOfFiles[i].isFile() && getFileExtension(listOfFiles[i].getName()).equals("txt")) {
		    	  System.out.println("processing File:"+listOfFiles[i].getCanonicalPath());
		    	  coreNLPTokenizer.processFile(clearNLP, listOfFiles[i], args[1]);
		      }
		}
		return;		
	}
	public static String getFileExtension(String fileName) {	    
	    int dotIndex = fileName.lastIndexOf('.');
	    return (dotIndex == -1) ? "" : fileName.substring(dotIndex + 1);
	}
	
	private void processFile(ClearNLP clearNLP, File inputFile, String outputDir) {
		try {
		BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(outputDir+"/"+inputFile.getName()+".srl.xls"));		
		int counter=0;
		for(List<String> sentence : tokenize(inputFile.getCanonicalPath())) {
			System.out.println("\nprocessing Sentence :"+counter++);
			clearNLP.dependencyTreeParser(sentence,bufferedWriter);
		}		
		bufferedWriter.close();
		}catch(Exception e) {
			System.out.println("Error During processing of file :"+inputFile.toString());
			e.printStackTrace();
		}
	}

	private List<List<String>> tokenize(String inputFile) {
		List<List<String>> listofSetences = new ArrayList<List<String>>();
		List<String> tokenizedSentence = null;
		DocumentPreprocessor dp = new DocumentPreprocessor(inputFile);
		for (List<HasWord> sentence : dp) {
			tokenizedSentence = new ArrayList<String>();
			for(HasWord word : sentence)
				tokenizedSentence.add(word.toString());
			listofSetences.add(tokenizedSentence);    	  
		}
		return listofSetences;
	}
}