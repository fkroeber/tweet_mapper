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
	public Map<String, String[]> data = new LinkedHashMap<String, String[]>();
	public int linecount;

	// constructor to initialize TwitterCSVReader
	public TwitterCSVReader(String _file_source) { 
		try { 
			// initialize reader to read twitter file
			BufferedReader reader = new BufferedReader(new FileReader(_file_source, Charset.forName("UTF-8")));
			// examine length of csv file
			linecount = (int) Files.lines(Paths.get(_file_source)).count();
			// define csv separator
			String separator = ";";
			// iterating through csv lines & parse them into hashmap
			String line = null;
			int count = 0;
			while ((line = reader.readLine()) != null) {
				// get csv header to define the hashmap keys
				if(count == 0) {
					for (int i = 0; i < line.split(separator).length; i++) {
						String var_name = line.split(separator)[i];
						String[] values = new String[linecount-1];
						data.put(var_name, values);
					}
				// parse rest of file into the value arrays for each key
				} else {
					for (int i = 0; i < line.split(separator).length; i++) {
						String entry = data.keySet().stream().skip(i).findFirst().get();
						data.get(entry)[count-1] = line.split(separator)[i];
					}
				}
				count++;
			}
			// close reader to release memory & print info to console
			reader.close();
			System.out.println("Twitter file with " + linecount + " entries successfully parsed.");
		}  
		catch (Exception e) {  
			e.printStackTrace();  
		}  
	}

	// method for deriving tweet length for visualization
	public void calc_tweet_length(String _tweet_varname) {
		// get tweets
		String[] tweets = data.get(_tweet_varname);
		// create new key-value-pair (kvp) for tweet length
		String[] values = new String[linecount-1];
		data.put("tweet_length", values);
		// calculate length values for each tweet & populate the corresponding kvp
		for (int i = 1; i<linecount; i++) {
			data.get("tweet_length")[i-1] = String.valueOf(tweets[i-1].length());
		}
	}

	// method to check if tweet is election-related or not
	public void check_election_related(String _tweet_varname) {
		// create list of words that are likely to be election-related
		String[] election_words = {"vote", "voting", "election", "president", "barack", "obama", "mitt", "romney"};
		// get tweets
		String[] tweets = data.get(_tweet_varname);
		// create new key-value-pair (kvp) for references to election
		String[] values = new String[linecount-1];
		data.put("election_related", values);
		// fill values of kvp by checking each tweet for occurrence of election words
		for (int i = 1; i<linecount; i++) {
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
	}
	
	// code snippet to examine hashmap
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
