package files;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SetOfFiles {
	
	private static final String fileExtension = "queryset";
	
	private FilenameFilter filter = new FilenameFilter() {
        @Override
        public boolean accept(File f, String name) {
            return name.endsWith(fileExtension);
        }
    }; 
    
    private boolean queriesRecoveryStatus = false;
    
    private List<File> queriesFiles = null;
	
	public SetOfFiles(String path) {
		
		try {
			File directoryPath = new File(path);
			
			queriesFiles = new ArrayList<File>(Arrays.asList(directoryPath.listFiles(filter)));
			
			if(queriesFiles.size() == 0) throw new Exception("There are no .queryset files in the specified path");
			
			System.out.println("[OK] List of .queryset files in the specified directory (" + path + ") :");
			  
			for(File file : queriesFiles) {
			   System.out.println("\t> File name: " + file.getName());
			   System.out.println("\t> File path: " + file.getAbsolutePath());
			   System.out.println("\t> Size: " + file.getTotalSpace());
			   System.out.println();
			}
			
		} catch(Exception e) {
			System.out.println("[ERROR] Unable to recover .queryset files in the specified folder : \n\t> " + e.getMessage());
		} 
			
		if(queriesFiles != null && queriesFiles.size() > 0) queriesRecoveryStatus = true;
	}
	
	public List<File> getQueriesFiles() {
		return queriesFiles;
	}
	
	public int getQueriesFilesNumber() {
		return queriesFiles.size();
	}
	
	public boolean getQueriesRecoveryStatus() {
		return queriesRecoveryStatus;
	}
}
