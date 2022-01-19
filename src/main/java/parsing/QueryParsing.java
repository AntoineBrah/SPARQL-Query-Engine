package parsing;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Stream;

import org.apache.jena.shared.Command;
import org.eclipse.rdf4j.query.algebra.Projection;
import org.eclipse.rdf4j.query.algebra.StatementPattern;
import org.eclipse.rdf4j.query.algebra.helpers.AbstractQueryModelVisitor;
import org.eclipse.rdf4j.query.algebra.helpers.StatementPatternCollector;
import org.eclipse.rdf4j.query.parser.ParsedQuery;
import org.eclipse.rdf4j.query.parser.sparql.SPARQLParser;

import Index.Index;
import dictionary.Dictionary;
import utils.CommandLine;
import utils.stats;

public class QueryParsing {

	private boolean queryParsingStatus = true;
	private static int cpt = 0;
	public QueryParsing(String path, Dictionary dictionary, Index index) throws IOException {
		
		ArrayList<String> t = new ArrayList<>(); //pour calculer le nombre de doublure

		

		try {
			long startTime = System.currentTimeMillis();

			cpt=0;
			Stream<String> lineStream = Files.lines(Paths.get(path));

			SPARQLParser sparqlParser = new SPARQLParser();
			Iterator<String> lineIterator = lineStream.iterator();
			StringBuilder queryString = new StringBuilder();
			String nameOfDict = Paths.get(path).getFileName().toString().substring(0,Paths.get(path).getFileName().toString().lastIndexOf("."));
			String outputDir = CommandLine.getInstance(null).getValueArgument("-output")+"/"+nameOfDict+"/";
			java.nio.file.Files.createDirectories(Paths.get(outputDir));
			String path1 = outputDir+ "ResultatReq";
			File file = new File(path1 + ".txt");
			BufferedWriter bf = new BufferedWriter(new FileWriter(file));

			/*
			 * On stocke plusieurs lignes jusqu'à ce que l'une d'entre elles se termine par
			 * un '}' On considère alors que c'est la fin d'une requête
			 */
			while (lineIterator.hasNext()) {
				String line = lineIterator.next();
				queryString.append(line);

				if (line.trim().endsWith("}")) {
					ParsedQuery query = sparqlParser.parseQuery(queryString.toString(), null);
					long endTime = System.currentTimeMillis();
					

					stats.timeReadQueries += endTime-startTime;
					
					if (!t.contains(queryString.toString())) {
						t.add(queryString.toString());
					}
					
					processAQuery(query, dictionary, index,bf); // Traitement de la requête, à adapter/réécrire pour votre
					// programme

					queryString.setLength(0); // Reset le buffer de la requête en chaine vide
				}
			}
			stats.numberOfDoube = cpt-t.size();
			stats.numberOfQueries = cpt;
			bf.close();
			System.out.println("[OK] Query parsing process & export result successfull");
		} catch (Exception e) {
			System.out.println("[ERROR] Error during the process query parsing :\n\t> " + e.getMessage());
			queryParsingStatus = false;
		}

	}

	public boolean getQueryParsingStatus() {
		return queryParsingStatus;
	}

	public static void processAQuery(ParsedQuery query, Dictionary dictionary, Index index,BufferedWriter bf) throws IOException {


		cpt++;
		bf.write("\n============================D�but de la requete "+ cpt+"===================");
		bf.newLine();
		bf.write(query.getSourceString());
		bf.newLine();
		List<StatementPattern> patterns = StatementPatternCollector.process(query.getTupleExpr());
		stats.QueriesWithSameConditionNumbers.put(patterns.size(), stats.QueriesWithSameConditionNumbers.containsKey(patterns.size())? stats.QueriesWithSameConditionNumbers.get(patterns.size())+1 : 1);
		//System.out.println(patterns.size());
		
		long startTime = System.nanoTime();
		

		try {
			Map<String, ArrayList<Integer>[]> Res = new TreeMap<>();
			
				for (int i = 0; i < patterns.size(); i++) {
					if (patterns.get(i).getSubjectVar().getValue() == null && patterns.get(i).getPredicateVar().getValue() != null && patterns.get(i).getObjectVar().getValue() != null ) {

						int pred = dictionary.getKeyByValue(patterns.get(i).getPredicateVar().getValue().stringValue());
						int obj = dictionary.getKeyByValue(patterns.get(i).getObjectVar().getValue().stringValue());
						addresultattomap(patterns.get(i).getSubjectVar().getName(), new ArrayList<Integer>(index.POS.get(pred).get(obj)), Res);


					}else {if (patterns.get(i).getSubjectVar().getValue() != null && patterns.get(i).getPredicateVar().getValue() != null && patterns.get(i).getObjectVar().getValue() == null ) {
						int sub = dictionary.getKeyByValue(patterns.get(i).getSubjectVar().getValue().stringValue());
						int pred = dictionary.getKeyByValue(patterns.get(i).getPredicateVar().getValue().stringValue());
						addresultattomap(patterns.get(i).getObjectVar().getName(), new ArrayList<Integer>( index.SPO.get(sub).get(pred)), Res);
					}
					else {if (patterns.get(i).getSubjectVar().getValue() != null && patterns.get(i).getPredicateVar().getValue() == null && patterns.get(i).getObjectVar().getValue() != null ) {
						int sub = dictionary.getKeyByValue(patterns.get(i).getSubjectVar().getValue().stringValue());
						int obj= dictionary.getKeyByValue(patterns.get(i).getObjectVar().getValue().stringValue());
						addresultattomap(patterns.get(i).getPredicateVar().getName(), new ArrayList<Integer>( index.SOP.get(sub).get(obj)), Res);
					}
					else {if (patterns.get(i).getSubjectVar().getValue() == null && patterns.get(i).getPredicateVar().getValue() == null && patterns.get(i).getObjectVar().getValue() != null ) {
						int obj = dictionary.getKeyByValue(patterns.get(i).getObjectVar().getValue().stringValue());
						addresultattomap(patterns.get(i).getSubjectVar().getName(), new ArrayList<Integer>( index.OSP.get(obj).keySet()), Res);
						addresultattomap(patterns.get(i).getPredicateVar().getName(), new ArrayList<Integer>( index.OPS.get(obj).keySet()), Res);
					}
					else {if (patterns.get(i).getSubjectVar().getValue() == null && patterns.get(i).getPredicateVar().getValue() != null && patterns.get(i).getObjectVar().getValue() == null ) {
						int pred = dictionary.getKeyByValue(patterns.get(i).getPredicateVar().getValue().stringValue());
						addresultattomap(patterns.get(i).getSubjectVar().getName(), new ArrayList<Integer>( index.PSO.get(pred).keySet()), Res);
						addresultattomap(patterns.get(i).getObjectVar().getName(), new ArrayList<Integer>( index.POS.get(pred).keySet()), Res);
					}
					else {if (patterns.get(i).getSubjectVar().getValue() != null && patterns.get(i).getPredicateVar().getValue() == null && patterns.get(i).getObjectVar().getValue() == null ) {
						int sub = dictionary.getKeyByValue(patterns.get(i).getSubjectVar().getValue().stringValue());
						addresultattomap(patterns.get(i).getPredicateVar().getName(), new ArrayList<Integer>( index.SPO.get(sub).keySet()), Res);
						addresultattomap(patterns.get(i).getObjectVar().getName(), new ArrayList<Integer>( index.SOP.get(sub).keySet()), Res);
					}
					}
					}
					}
					}
					}
				}
				ArrayList<String> var =new ArrayList<>(Res.keySet());
				var.forEach(v -> {
					for (int i = 0; i < Res.get(v).length - 1; i++) {
						
						if (Res.get(v)[0] != null && Res.get(v)[i + 1] != null) {
							Res.get(v)[0].retainAll(Res.get(v)[i + 1]);
						}
					}
					});
				for(int j =0 ;j<var.size();j++) {
					String v = var.get(j);
					bf.write("\n============================Resultat===================");

					//System.out.println("============================Fin de la requete===================");
					if (Res.get(v)[0] != null) {
						System.out.println("\nResultat Final de la variable "+v+ ": " + Res.get(v)[0].toString());
						bf.write("\n equivalent � :");
						for (int f : Res.get(v)[0]) {

							bf.write("\n" + dictionary.getElementByKey(f));
						}
						if(Res.get(v)[0].size()==0) {
							stats.numberOfQueriesNoRsult++;

						}
						//System.out.println("Resultat Final: " + array[0].toString());
					} else {
						//System.out.println(" Resultat final :Pas de solution");
						bf.write(" \nResultat final :Pas de solution");
						stats.numberOfQueriesNoRsult++;

					}
				}
				bf.write("\n============================Fin de la requette===================");

				
				
			}

		catch (Exception e) {
			bf.write("\nLe predicat ou l'object de la requette n'exsite pas parmi les donn�es fourni");
			stats.numberOfQueriesNoRsult++;

			//System.out.println("Le predicat ou l'object de la requette n'exsite pas parmi les donn�es fourni");

		} finally {
			try {
				long endTime = System.nanoTime();
				bf.write("\n\n\n\n*That took " + (endTime - startTime) + " nanoseconds");

			} catch (Exception e) {
				System.out.println("[ERROR] Error during the export req process :\n\t> " + e.getMessage());
			}
		}

		// Utilisation d'une classe anonyme
		query.getTupleExpr().visit(new AbstractQueryModelVisitor<RuntimeException>() {

			public void meet(Projection projection) {
				//System.out.println(projection.getProjectionElemList().getElements());
			}
		});
	}
	public static void addresultattomap(String v,ArrayList<Integer> res,Map<String, ArrayList<Integer>[]> map) {
		if(map.get(v)==null) {
			ArrayList<Integer>[] a = new ArrayList[] {res};
			map.put(v, a);
		}else {
			ArrayList<Integer>[] arr = Arrays.copyOf(map.get(v), map.get(v).length + 1);
			arr[arr.length - 1] = res;
			map.put(v, arr);
		}
	}
}
