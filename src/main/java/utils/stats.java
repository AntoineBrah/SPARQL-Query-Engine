package utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Stream;

import org.eclipse.rdf4j.query.parser.ParsedQuery;
import org.eclipse.rdf4j.query.parser.sparql.SPARQLParser;

import Index.Index;
import dictionary.Dictionary;

public class stats {
	public static String nameofdata ="";
	public static String nameOfQuer ="";
	public static int numberOfTrRDF = 0;
	public static int numberOfQueries = 0;
	public static long timeReadData = 0;
	public static long timeReadQueries = 0;
	public static long timeDicCreateInNano = 0;
	public static long timeIndexesCreateInNano = 0;
	public static long timeofWorkLoad = 0;
	public static long timeTotal = 0;
	public static int numberOfIndexes = 6;
	public static int numberOfQueriesNoRsult = 0;
	public static int numberOfDoube = 0;
	public static int queriesWithSameCondition = 0;
	
	public static Map<Integer, Integer> QueriesWithSameConditionNumbers = new TreeMap<>();
	public static long timeparsedata = 0;
	
	public static void export_csv() throws IOException {
		BufferedWriter bf1 = new BufferedWriter(new FileWriter(CommandLine.getInstance(null).getValueArgument("-output")+"/statistiques.csv"));
		bf1.write("nom du fichier de donn�es,nom du dossier des requ�tes ,nombre de triplets RDF,nombre de requ�tes, temps de lecture des donn�es (ms), temps de lecture des requ�tes (ms),temps cr�ation dico (ms),nombre d�index,temps de cr�ation des index (ms), temps total d��valuation du workload (ms), temps total,Nombre de req non r�solu,Nombre de doublure ");
		bf1.newLine();
		bf1.write(stats.nameofdata);bf1.write(",");
		bf1.write(stats.nameOfQuer);bf1.write(",");
		bf1.write(stats.numberOfTrRDF+"");bf1.write(",");
		bf1.write(stats.numberOfQueries+"");bf1.write(",");
		bf1.write(stats.timeReadData+"");bf1.write(",");
		bf1.write(stats.timeReadQueries+"");bf1.write(",");
		bf1.write(stats.timeDicCreateInNano/1000000+"");bf1.write(",");
		bf1.write(stats.numberOfIndexes+"");bf1.write(",");
		bf1.write(stats.timeIndexesCreateInNano/1000000+"");bf1.write(",");
		bf1.write(stats.timeofWorkLoad+"");bf1.write(",");
		bf1.write(stats.timeTotal+"");bf1.write(",");
		bf1.write(stats.numberOfQueriesNoRsult+"");bf1.write(",");
		bf1.write(stats.numberOfDoube+"");
		bf1.close();
		
		System.out.println("x=y : x = nombre de conditions par requêtes | y = nombre de requêtes ayant x condition");
		System.out.println(QueriesWithSameConditionNumbers);
	}
	public static void export_csv_jena() throws IOException {
		BufferedWriter bf1 = new BufferedWriter(new FileWriter("output/statistiquesJena.csv"));
		bf1.write("temps de lecture des donn�es (ms), temps de lecture des requ�tes (ms),temps parsedata (ms),temps total d��valuation du workload (ms), temps total");
		bf1.newLine();
		bf1.write(stats.timeReadData+"");bf1.write(",");
		bf1.write(stats.timeReadQueries+"");bf1.write(",");
		bf1.write(stats.timeparsedata+"");bf1.write(",");
		bf1.write(stats.timeofWorkLoad+"");bf1.write(",");
		bf1.write(stats.timeTotal+"");bf1.write(",");
		bf1.close();
		
		System.out.println("x=y : x = nombre de conditions par requêtes | y = nombre de requêtes ayant x condition");
		System.out.println(QueriesWithSameConditionNumbers);
	}
	
}
