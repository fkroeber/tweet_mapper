package eot_aribisala_kroeber_sramo;

import java.io.FileWriter;
import java.io.IOException;

public class KMLGenerator {

	// define basic instance variables
	String kmlstring;
	
	public KMLGenerator(String[] kml_elements) {
		// compose kml body
		String kml_body = "";
		for (int i=0; i<kml_elements.length; i++) {
			kml_body += kml_elements[i];
		}
		// embed body in kml skeleton
		kmlstring = "" +
		"<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
		"<kml xmlns=\"http://www.opengis.net/kml/2.2\">\n" +
		"<Document>\n" +
		kml_body + 
		"\n</Document>\n" +
		"</kml>\n";
	}
	
	public void savefile(String _save_dir) {
		FileWriter fileWriter;
		try {
			fileWriter = new FileWriter(_save_dir);
			fileWriter.write(kmlstring);
			fileWriter.close();	
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
