package eot_aribisala_kroeber_sramo;

import java.io.BufferedReader;
import java.io.FileReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedHashMap;
import java.util.Map;

public class TwitterCSVReader {

	// define basic instance variables	
	Map<String, String[]> data = new LinkedHashMap<String, String[]>();
	int lineCount;
	
	// constructor to initialise WMSImageDownlaoder
	// parse data into instance variable data
	public TwitterCSVReader(String _file_source) { 
		try { 
			// open reader to read csv file
			BufferedReader reader = new BufferedReader(new FileReader(_file_source, Charset.forName("UTF-8")));
			// examine length of csv file
			lineCount = (int) Files.lines(Paths.get(_file_source)).count();
			
			// parse data into hashmap
			String seperator = ";";
			String line = null;
			int count = 0;
			
			while ((line = reader.readLine()) != null) {
				// get csv header to define the hashmap keys
				if(count == 0) {
					for (int i = 0; i < line.split(seperator).length; i++) {
						String var_name = line.split(seperator)[i];
						String[] values = new String[lineCount-1];
						data.put(var_name, values);
					}
				// parse rest of file into the value arrays for each key
			    } else {
			    	for (int i = 0; i < line.split(seperator).length; i++) {
			    		 String entry = data.keySet().stream().skip(i).findFirst().get();
			    		 data.get(entry)[count-1] = line.split(seperator)[i];
			    	}
			    }
			    count++;
			}
			// close reader to release memory & print info to console
			reader.close();
			System.out.println("Twitter file with " + lineCount + " entries successfully parsed.");
		}  
		catch (Exception e) {  
			e.printStackTrace();  
		}  
	}
	
	// deriving length variable for visualisation
	public void calc_tweet_length(String tweet_varname) {
		String[] tweets = data.get(tweet_varname);
		String[] values = new String[lineCount-1];
		data.put("tweet_length", values);
	    for (int i = 1; i<lineCount; i++) {
	    	data.get("tweet_length")[i-1] = String.valueOf(tweets[i-1].length());
	    }
	}
	
	// check if vote related or not
	// to do: colorise polygons accordingly
	public void check_election_related(String tweet_varname) {
		// create list of words that are likely to be election-related
		String[] election_words = {"vote", "voting", "election", "president", "barack", "obama", "mitt", "romney"};
        // get tweets
		String[] tweets = data.get(tweet_varname);
		// create new kvp for current var
		String[] values = new String[lineCount-1];
		data.put("election_related", values);
		// fill values for var by checking each tweet for occurence of election words
	    for (int i = 1; i<lineCount; i++) {
	    	for (int j = 0; j < election_words.length; j++) {
		    	if (tweets[i-1].toLowerCase().contains(election_words[j])) {
		    		data.get("election_related")[i-1] = "probably";
		    		break;
		    	}
		    	else {
		    		data.get("election_related")[i-1] = "probably not";
		    	}	
	    	}
	    }
//		// print result
//		for (String name: data.keySet()) {
//		    String key = name.toString();
//		    String[] value = data.get(name);
//		    System.out.println(key + ": ");
//		    for (int i = 0; i<10; i++) {
//			    System.out.println(value[i]); 	
//		    }
//		    System.out.println("----------------");
//		}
	}
}
