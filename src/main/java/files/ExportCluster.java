package files;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public abstract class ExportCluster {

    final static String outputDir = "output/";
    static String path = "";

    public static void exportCluster(Map<Integer, Map<Integer, ArrayList<Integer>>> triplet, String filename) {

        path = outputDir + filename;

        File file = new File(path + ".txt");

        BufferedWriter bf = null;

        try {

            bf = new BufferedWriter(new FileWriter(file));

            for (Map.Entry<Integer, Map<Integer, ArrayList<Integer>>> t : triplet.entrySet()) {
                for (Map.Entry<Integer, ArrayList<Integer>> tt : t.getValue().entrySet()) {
                    for (Integer ttt : tt.getValue()) {
                        bf.write("(" + t.getKey() + "," + tt.getKey() + "," + ttt + ")");
                        bf.newLine();

                    }
                }
            }

            bf.flush();
            System.out.println("[OK] Cluster export successful : " + path);
        } catch (IOException e) {
            System.out.println("[ERROR] Error during the export cluster process :\n\t> " + e.getMessage());
        } finally {
            try {
                bf.close();
            } catch (Exception e) {
                System.out.println("[ERROR] Error during the export cluster process :\n\t> " + e.getMessage());
            }
        }
    }

}
