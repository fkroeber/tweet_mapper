package eot_aribisala_kroeber_sramo;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;

public class GoogleEarthTweetMapper {

	public static void main(String[] args) {

		// intial setup - setting workdir
		System.out.println("--- Setup ---");
		String work_dir = new WorkDirSetter().workdir;

		// part I - WMS Map Acquisition & Storage
		// works for example also with the following wms service:
		// String _wms_service = "http://giswebservices.massgis.state.ma.us/geoserver/wms";
		// String _layer = "Census 2000 Tracts";
		System.out.println();
		System.out.println("--- Step I: Retrieving basemap ---");
		String wms_service = "https://maps.heigit.org/osm-wms/service";
		String bbox = "-74.10,40.70,-73.85,40.80"; // bbox new york instead of boston: -71.13,42.32,-71.03,42.42;
		String download_dir = work_dir + File.separatorChar + "basemap.png";
		String layer = "osm_auto:all"; // equivalent to String _layer = "OSM WMS - osm-wms.de";
		WMSImageDownloader wms = new WMSImageDownloader(wms_service);
		wms.getmap(bbox, layer);
		wms.savemap(download_dir);

		// part II - twitter data preparation
		System.out.println();
		System.out.println("--- Step II: Reading twitter data ---");
		String twitter_file = work_dir + File.separatorChar + "twitter.csv";
		String tweet_varname = "tweet";
		TwitterCSVReader twitter_data = new TwitterCSVReader(twitter_file);
		twitter_data.calc_tweet_length(tweet_varname);
		twitter_data.check_election_related(tweet_varname);

		// part III - kml file creation
		System.out.println();
		System.out.println("--- Step III: Creating KML file ---");
		String basemap = new KMLElements().wmsgroundoverlay(download_dir, bbox);
		String markers = new KMLElements().tweetsvis(twitter_data);
		KMLGenerator kml = new KMLGenerator(new String[] {basemap, markers});
		String save_dir = work_dir + File.separatorChar + "tweets_mapped.kml";
		kml.savefile(save_dir);

		// part IV - launch GoogleEarth	with default application
		System.out.println();
		System.out.println("--- Step IV: Opening KML file ---");
		File kml_file = new File(save_dir);
		try {
			Desktop.getDesktop().open(kml_file);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
