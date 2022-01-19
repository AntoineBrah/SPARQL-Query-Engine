package utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class CommandLine {

	private static CommandLine cmd = null;
	private final ArrayList<String> mandatoryArgs = new ArrayList<String>(Arrays.asList("-queries", "-data", "-output"));

	private boolean argStatus = true;

	private HashMap<String, String> arguments = new HashMap<String, String>();

	private CommandLine(String[] args) {

		try {

			Arrays.stream(args)
			.reduce((str1, str2) -> { 
				if(str1.startsWith("-")) arguments.put(str1, str2);
				return str2; 
			});

			for(String arg : mandatoryArgs) {
				if(!arguments.containsKey(arg))
					throw new Exception("Some mandatory parameters are not found.");
			}


		} catch(Exception e) {
			argStatus = false;
			System.out.println("[ERROR] " + e.getMessage() + " Please respect the following command line to run the program :");
			System.out.println("\t> java -jar rdfengine -queries [queries_path] -data [data_path] -output [output_path]");
		}

	}
	public static CommandLine getInstance(String[] args) {
		if(cmd!=null) {
			return cmd;
		}else {
			cmd = new CommandLine(args);
			return cmd;
		}
	}
	

	public boolean getArgStatus() {
		return argStatus;
	}

	public HashMap<String, String> getArguments() {
		return arguments;
	}

	public String getValueArgument(String arg) {
		return arguments.get(arg);
	}

}
