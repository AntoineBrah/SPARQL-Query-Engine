package files;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.collections4.BidiMap;
import org.eclipse.rdf4j.model.Value;

public abstract class ExportDictionary {

    final static String outputDir = "output/";

    final static String fileName = "dictionary.txt";

    final static String path = outputDir + fileName;

    public static void exportDictionary(BidiMap<Integer, String> bidiMap) {

        File file = new File(path);

        BufferedWriter bf = null;

        try {

            bf = new BufferedWriter(new FileWriter(file));

            for (Map.Entry<Integer, String> entry : bidiMap.entrySet()) {
                bf.write(entry.getKey() + ":" + entry.getValue());
                bf.newLine();
            }

            bf.flush();
            System.out.println("[OK] Dictionary export successful : " + path);
        } catch (IOException e) {
            System.out.println("[ERROR] Error during the export dictionary process :\n\t> " + e.getMessage());
        } finally {
            try {
                bf.close();
            } catch (Exception e) {
                System.out.println("[ERROR] Error during the export dictionary process :\n\t> " + e.getMessage());
            }
        }
    }

}
