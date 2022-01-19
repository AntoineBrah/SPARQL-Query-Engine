package files;

import java.io.File;

public class SingleFile {
	
    private boolean recoveryStatus = true;
    
    private File file = null;
    
    private String path = "";
	
	public SingleFile(String path) {
		
		this.path = path;
		
		try {
			file = new File(path);
			
			if(!file.exists() || file.isDirectory()) {
				file = null;
				throw new Exception("File does not exist.");
			}
			
			System.out.println("[OK] file was found in the specified directory (" + path + ") :");
			  
		    System.out.println("\t> File name: " + file.getName());
		    System.out.println("\t> File path: " + file.getAbsolutePath());
		    System.out.println("\t> Size: " + file.getTotalSpace());
			
		} catch(Exception e) {
			System.out.println("[ERROR] Unable to recover the file in the specified folder : \n\t> " + e.getMessage());
			recoveryStatus = false;
		} 
		
	}
	
	public File getFile() {
		return file;
	}
	
	public String getFilePath() {
		return path;
	}
	public String getName() {
		return file.getName();
	}
	
	public boolean getRecoveryStatus() {
		return recoveryStatus;
	}
}
