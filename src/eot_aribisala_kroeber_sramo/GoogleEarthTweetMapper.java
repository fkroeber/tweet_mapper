/* Overall aim: Design submodules that are generic & reusable in a way 
 * that different tweet attributes can be mapped for a variety of AoIs
 * -> mention that for perfect trade-off between modularity & conciseness
 * a prior definition of goals & use cases is necessary, be specific: use
 * for large-scale mapping using a WMS that supports version 1.1.1
 * 
 * WMS class pretty generic -> allows a variety of requests to be handled
 * as csv preperation is pretty specific a higher degree of modularity/parameters does not make sense
 * for kml integration in turn displaying single layers modular designed again
 * 
 * Check which object variables are accessible from main -> restrict to private if necessary
 * 
 * No further data cleaning done, e.g. w/ denotes that others are also addressed
 * 
 * Design choice to keep rather generic modules as far as possible and to keep essential parameter specifications in main
 * 
 * */

package eot_aribisala_kroeber_sramo;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;

public class GoogleEarthTweetMapper {

	public static void main(String[] args) {
		
		// intial setup
		System.out.println("--- Setup ---");
		String work_dir = new WorkDirSetter().workdir;
		       
		// part I - WMS Map Acquisition & Storage
		// Works for example also with the following wms service:
		// String _wms_service = "http://giswebservices.massgis.state.ma.us/geoserver/wms";
		// String _layer = "Census 2000 Tracts";
		System.out.println();
		System.out.println("--- Step I: Retrieving basemap ---");
		String _wms_service = "https://maps.heigit.org/osm-wms/service";
		String _bbox = "-71.13,42.32,-71.03,42.42";
		String _download_dir = work_dir + File.separatorChar + "basemap.png";
		String _layer = "osm_auto:all"; // equivalent to String _layer = "OSM WMS - osm-wms.de";
		WMSImageDownloader wms = new WMSImageDownloader(_wms_service);
		wms.getmap(_bbox, _layer);
		wms.savemap(_download_dir);
		
		// part II - Twitter data preparation
		System.out.println("--- Step II: Reading twitter data ---");
		String _twitter_file = work_dir + File.separatorChar + "twitter.csv";
		String _tweet_varname = "tweet";
		TwitterCSVReader twitter_data = new TwitterCSVReader(_twitter_file);
		twitter_data.calc_tweet_length(_tweet_varname);
		twitter_data.check_election_related(_tweet_varname);
		
		// part III - kml file creation
		System.out.println();
		System.out.println("--- Step III: Creating KML file ---");
		String _basemap = new KMLElements().WMSGroundOverlay(_download_dir, _bbox);
		String _polygons = new KMLElements().TweetsVis(twitter_data);
		KMLGenerator kml = new KMLGenerator(new String[] {_basemap, _polygons});
		String _save_dir = work_dir + File.separatorChar + "tweets_mapped.kml";
		kml.savefile(_save_dir);
		
		// part IV - launch GoogleEarth	with default application
		// superior to: Runtime.getRuntime().exec(new String[] {google_earth_binary, _save_dir})
		System.out.println();
		System.out.println("--- Step IV: Opening KML file ---");
		File _kml_file = new File(_save_dir);
		try {
			Desktop.getDesktop().open(_kml_file);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

}
