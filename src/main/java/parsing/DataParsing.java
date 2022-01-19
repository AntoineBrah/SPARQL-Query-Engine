package parsing;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.eclipse.rdf4j.model.Statement;
import org.eclipse.rdf4j.query.GraphQueryResult;
import org.eclipse.rdf4j.query.QueryResults;
import org.eclipse.rdf4j.rio.RDFFormat;

import Index.Index;
import dictionary.Dictionary;
import utils.CommandLine;
import utils.stats;

public class DataParsing {

	public static Integer key = 0;

	private Dictionary dictionary = new Dictionary();
	private Index indexes = new Index();
	private boolean dataParsingStatus = true;

	public DataParsing(File dataFile) throws Exception {

		InputStream targetStream = new FileInputStream(dataFile);
		


		try {
			long startTime = System.currentTimeMillis();

			GraphQueryResult res = QueryResults.parseGraphBackground(targetStream, null, RDFFormat.NTRIPLES);
			long endTime = System.currentTimeMillis();
			stats.timeReadData=endTime-startTime;

			while (res.hasNext()) {
				Statement st = res.next();
				// //System.out.println("Sujet : " + st.getSubject().getClass() + " | Prédicat : "
				// + st.getPredicate().getClass() + " | Object : " + st.getObject());

				String s = st.getSubject().stringValue();
				String p = st.getPredicate().stringValue();
				String o = st.getObject().stringValue();
				startTime = System.nanoTime();
				if (!dictionary.containsValue(s)) {
					++key;
					dictionary.putElement(key, s);
				}

				if (!dictionary.containsValue(p)) {
					++key;
					dictionary.putElement(key, p);
				}

				if (/* st.getObject().isIRI() && */ !dictionary.containsValue(o)) {
					++key;
					dictionary.putElement(key, o);
				}
				endTime = System.nanoTime();
				stats.timeDicCreateInNano += endTime-startTime ;
				
				startTime = System.nanoTime();

				// Add triplets
				indexes.addTripletSPO(dictionary.getKeyByValue(s), dictionary.getKeyByValue(p),
						dictionary.getKeyByValue(o)); // cluster 1 : SPO
				indexes.addTripletSOP(dictionary.getKeyByValue(s), dictionary.getKeyByValue(o),
						dictionary.getKeyByValue(p)); // cluster 2 : SOP
				indexes.addTripletPSO(dictionary.getKeyByValue(p), dictionary.getKeyByValue(s),
						dictionary.getKeyByValue(o)); // cluster 3 : PSO
				indexes.addTripletOPS(dictionary.getKeyByValue(o), dictionary.getKeyByValue(p),
						dictionary.getKeyByValue(s)); // cluster 4 : OPS
				indexes.addTripletPOS(dictionary.getKeyByValue(p), dictionary.getKeyByValue(o),
						dictionary.getKeyByValue(s)); // cluster 5 : POS
				indexes.addTripletOSP(dictionary.getKeyByValue(o), dictionary.getKeyByValue(s),
						dictionary.getKeyByValue(p)); // cluster 6 : OSP
				endTime = System.nanoTime();
				stats.timeIndexesCreateInNano += endTime-startTime;


			}
			Path path1 = Paths.get(CommandLine.getInstance(null).getValueArgument("-data"));
			stats.numberOfTrRDF= (int) Files.lines(path1).count();
			
		} catch (Exception e) {
			System.out.println("[ERROR] Error during the process data parsing :\n\t> " + e.getMessage());
			dataParsingStatus = false;
		} finally {
			targetStream.close();
		}
	}

	public Dictionary getDictionary() {
		return dictionary;
	}

	public boolean getDataParsingStatus() {
		return dataParsingStatus;
	}

	public Index getIndexes() {
		return indexes;
	}

}
