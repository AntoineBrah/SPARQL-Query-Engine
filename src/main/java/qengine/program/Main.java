package qengine.program;

import parsing.DataParsing;
import parsing.QueryParsing;
import utils.CommandLine;
import utils.stats;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

import files.ExportCluster;
import files.ExportDictionary;
import files.SingleFile;

/**
 * Doc RDF4J : https://rdf4j.org/documentation/
 * Doc Jena : 
 */
final class Main {

	public static void main(String[] args) throws Exception {
	
		// 1. Analysis and processing of command line arguments
		CommandLine cmdLine = CommandLine.getInstance(args);

		if(!cmdLine.getArgStatus()) {
			System.out.println("[MAIN] Arguments provided on the command line are wrong");
			System.out.println("\t> End of process.");
			return;
		}

		final String queryPath = cmdLine.getValueArgument("-queries");
		final String dataPath = cmdLine.getValueArgument("-data");
		final String outputPath = cmdLine.getValueArgument("-output");
		
		long startTime1 = System.currentTimeMillis();

		// 2. Recovery and analysis of files (data and queries)
		SingleFile queryFile = new SingleFile(queryPath); 
		SingleFile dataFile = new SingleFile(dataPath); 
		stats.nameofdata = dataFile.getName();
		stats.nameOfQuer = queryFile.getName();

		

		if(!queryFile.getRecoveryStatus() || !dataFile.getRecoveryStatus()) {
			System.out.println("[MAIN] All files could not be recovered");
			System.out.println("\t> End of process.");
			return;
		}
		BufferedWriter bf = null;
		String path = outputPath + "stats" ;
		File file = new File(path + ".txt");
		bf = new BufferedWriter(new FileWriter(file));
		long startTime = System.currentTimeMillis();
		// 3. Parsing and generating dictionary
		DataParsing dataParsing = new DataParsing(dataFile.getFile());
		long endTime = System.currentTimeMillis();
		bf.write(" \"dataParsing\" :"+(endTime - startTime) + " milliseconds");

		if(!dataParsing.getDataParsingStatus()) {
			System.out.println("[MAIN] Process parsing of the data file failed :");
			System.out.println("\t> End of process.");
			return;
		}

		// 4. Print & Export dictionary/indexes
		startTime = System.currentTimeMillis();
		//System.out.println("Dictionary = " + dataParsing.getDictionary());
		ExportDictionary.exportDictionary(dataParsing.getDictionary().getInstance());

		//System.out.println("SPO = " + dataParsing.getIndexes().SPO.toString());
		ExportCluster.exportCluster(dataParsing.getIndexes().SPO, "SPO");

		//System.out.println("SOP = " + dataParsing.getIndexes().SOP.toString());
		ExportCluster.exportCluster(dataParsing.getIndexes().SOP, "SOP");

		//System.out.println("PSO = " + dataParsing.getIndexes().PSO.toString());
		ExportCluster.exportCluster(dataParsing.getIndexes().PSO, "PSO");

		//System.out.println("OPS = " + dataParsing.getIndexes().OPS.toString());
		ExportCluster.exportCluster(dataParsing.getIndexes().OPS, "OPS");

		//System.out.println("POS = " + dataParsing.getIndexes().POS.toString());
		ExportCluster.exportCluster(dataParsing.getIndexes().POS, "POS");

		//System.out.println("OSP = " + dataParsing.getIndexes().OSP.toString());
		ExportCluster.exportCluster(dataParsing.getIndexes().OSP, "OSP");
		endTime = System.currentTimeMillis();
		bf.write("\n \"L'export\" :"+(endTime - startTime) + " milliseconds");
		startTime = System.currentTimeMillis();
		// 5. Parse queries
		QueryParsing queryParsing = new QueryParsing(queryFile.getFilePath(), dataParsing.getDictionary(),dataParsing.getIndexes());
		endTime = System.currentTimeMillis();
		bf.write("\n \"queryParsing\" :"+(endTime - startTime) + " milliseconds");
		stats.timeofWorkLoad = endTime-startTime;
		long endTime1 = System.currentTimeMillis();
		bf.write("\n \"Total\" :"+(endTime1 - startTime1)/1000 + " seconds");
		bf.close();
		stats.timeTotal = endTime1-startTime1;
		stats.export_csv();
		if(!queryParsing.getQueryParsingStatus()) {
			System.out.println("[MAIN] Process parsing of the query file failed :");
			System.out.println("\t> End of process.");
			return;
		}


	}

}
