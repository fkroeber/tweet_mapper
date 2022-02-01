package eot_aribisala_kroeber_sramo;

import net.sf.geographiclib.Geodesic;

public class KMLElements {

	public String WMSGroundOverlay(String img_path, String extent) {
		String kml =
	    "<GroundOverlay>\n" +
	      "<name>wms overlay</name>\n" +
	      "<color>4Dffffff</color>\n" + 
	      "<Icon>\n" +
	        "<href>" + img_path + "</href>\n" +
	      "</Icon>\n" +
	      "<LatLonBox>\n" +
	        "<north>" + extent.split(",")[3] + "</north>\n" +
	        "<south>" + extent.split(",")[1] + "</south>\n" +
	        "<east>" + extent.split(",")[2] + "</east>\n" +
	        "<west>" + extent.split(",")[0] + "</west>\n" +
	      "</LatLonBox>\n" +
	    "</GroundOverlay>\n";
		
		return kml;
	}

    
	public String TweetsVis(TwitterCSVReader twitter_reader) {
		String kml = "";
		
		for (int i=0; i<twitter_reader.lineCount-1; i++) {
			String tweet_id = twitter_reader.data.get("id")[i];
			String tweet = twitter_reader.data.get("tweet")[i];
			String created_at = twitter_reader.data.get("created_at")[i];
			String user_id = twitter_reader.data.get("user_id")[i];
			String lat = twitter_reader.data.get("lat")[i];
			String lon = twitter_reader.data.get("lng")[i];
			String tweet_length = twitter_reader.data.get("tweet_length")[i];
			String election_related = twitter_reader.data.get("election_related")[i];
						
			kml +=
			"<Style id='pyramid_style'>" +
		      "<LineStyle>" +
		        "<width>1</width>" +
		      "</LineStyle>" +
		     "<PolyStyle>" +
		     	pyramid_color(election_related) +
		      "</PolyStyle>" +
		    "</Style>" +
			"<Placemark>\n" +
		    "<name>Tweet_ID: "+ tweet_id +"</name>\n" +
		    "<description>" +
	        "<![CDATA[" +
	          "<p>" + tweet +"</p>" +
	          "<p><i>user_id: " + user_id + "</br>" +
	                "created at: " + created_at.split("\\+")[0] + "</i></p>" +
	        "]]>" +
	        "</description>" +
	        "<TimeStamp><when>" + created_at.replace(' ', 'T') + ":00" + "</when></TimeStamp>" +
		    "<styleUrl>#pyramid_style</styleUrl>" +
		    "<MultiGeometry>\n" +
		    "<Polygon>\n"+
		      "<extrude>0</extrude>\n"+
		      "<altitudeMode>relativeToGround</altitudeMode>\n"+
		      "<outerBoundaryIs>\n"+
		        "<LinearRing>\n"+
		          "<coordinates>\n"+
			      pyramid_vertices_coords(lat, lon, tweet_length, "bottom") + "\n" +
			      pyramid_vertices_coords(lat, lon, tweet_length, "top_north_east") + "\n" +
			      pyramid_vertices_coords(lat, lon, tweet_length, "top_north_west") + "\n" +
			      pyramid_vertices_coords(lat, lon, tweet_length, "bottom") + "\n" +
		          "</coordinates>\n"+
		        "</LinearRing>\n"+
		      "</outerBoundaryIs>\n"+
		    "</Polygon>\n" +
		    "<Polygon>\n"+
		      "<extrude>0</extrude>\n"+
		      "<altitudeMode>relativeToGround</altitudeMode>\n"+
		      "<outerBoundaryIs>\n"+
		        "<LinearRing>\n"+
		          "<coordinates>\n"+
			      pyramid_vertices_coords(lat, lon, tweet_length, "bottom") + "\n" +
			      pyramid_vertices_coords(lat, lon, tweet_length, "top_north_west") + "\n" +
			      pyramid_vertices_coords(lat, lon, tweet_length, "top_south_west") + "\n" +
			      pyramid_vertices_coords(lat, lon, tweet_length, "bottom") + "\n" +
		          "</coordinates>\n"+
		        "</LinearRing>\n"+
		      "</outerBoundaryIs>\n"+
		    "</Polygon>\n" +
		    "<Polygon>\n"+
		      "<extrude>0</extrude>\n"+
		      "<altitudeMode>relativeToGround</altitudeMode>\n"+
		      "<outerBoundaryIs>\n"+
		        "<LinearRing>\n"+
		          "<coordinates>\n"+
			      pyramid_vertices_coords(lat, lon, tweet_length, "bottom") + "\n" +
			      pyramid_vertices_coords(lat, lon, tweet_length, "top_south_west") + "\n" +
			      pyramid_vertices_coords(lat, lon, tweet_length, "top_south_east") + "\n" +
			      pyramid_vertices_coords(lat, lon, tweet_length, "bottom") + "\n" +
		          "</coordinates>\n"+
		        "</LinearRing>\n"+
		      "</outerBoundaryIs>\n"+
		    "</Polygon>\n" +
		    "<Polygon>\n"+
		      "<extrude>0</extrude>\n"+
		      "<altitudeMode>relativeToGround</altitudeMode>\n"+
		      "<outerBoundaryIs>\n"+
		        "<LinearRing>\n"+
		          "<coordinates>\n"+
			      pyramid_vertices_coords(lat, lon, tweet_length, "bottom") + "\n" +
			      pyramid_vertices_coords(lat, lon, tweet_length, "top_south_east") + "\n" +
			      pyramid_vertices_coords(lat, lon, tweet_length, "top_north_east") + "\n" +
			      pyramid_vertices_coords(lat, lon, tweet_length, "bottom") + "\n" +
		          "</coordinates>\n"+
		        "</LinearRing>\n"+
		      "</outerBoundaryIs>\n"+
		    "</Polygon>\n" +
		    "<Polygon>\n"+
		      "<extrude>0</extrude>\n"+
		      "<altitudeMode>relativeToGround</altitudeMode>\n"+
		      "<outerBoundaryIs>\n"+
		        "<LinearRing>\n"+
		          "<coordinates>\n"+
			      pyramid_vertices_coords(lat, lon, tweet_length, "top_north_west") + "\n" +
			      pyramid_vertices_coords(lat, lon, tweet_length, "top_south_west") + "\n" +
			      pyramid_vertices_coords(lat, lon, tweet_length, "top_south_east") + "\n" +
			      pyramid_vertices_coords(lat, lon, tweet_length, "top_south_west") + "\n" +
			      pyramid_vertices_coords(lat, lon, tweet_length, "top_north_west") + "\n" +
		          "</coordinates>\n"+
		        "</LinearRing>\n"+
		      "</outerBoundaryIs>\n"+
		    "</Polygon>\n" +
		    "</MultiGeometry>\n" +
		    "</Placemark>\n";
		}
		
	    return kml;
	};
	
	// auxiliary function to specify pyramid geometry
	private String pyramid_vertices_coords(String lat, String lon, String size_var, String type) {
		// define
		double volume =  Double.parseDouble(size_var) * 500;
		double ratio_h_a = 2;
		
		double a = Math.pow(3*volume/ratio_h_a,1.0/3.0);
		String h = Double.toString(a*ratio_h_a);
		
		String north_x = Double.toString(Geodesic.WGS84.Direct(Double.parseDouble(lat), Double.parseDouble(lon), 0.0, 0.5*a).lat2);
		String east_y = Double.toString(Geodesic.WGS84.Direct(Double.parseDouble(lat), Double.parseDouble(lon), 90.0, 0.5*a).lon2);
		String south_x = Double.toString(Geodesic.WGS84.Direct(Double.parseDouble(lat), Double.parseDouble(lon), 180.0, 0.5*a).lat2);
		String west_y = Double.toString(Geodesic.WGS84.Direct(Double.parseDouble(lat), Double.parseDouble(lon), 270.0, 0.5*a).lon2);

		switch(type) {
		case "top_north_east": return east_y + "," + north_x + "," + h;
		case "top_south_east": return east_y + "," + south_x + "," + h;
		case "top_south_west": return west_y + "," + south_x + "," + h;
		case "top_north_west": return west_y + "," + north_x + "," + h;
		case "bottom": return lon + "," + lat + ",0";
		default: return "";
		}
	};
	
	
	private String pyramid_color(String color_var) {
		if (color_var == "probably") {
			return "<color>e050e7fc</color>";
		}
		else {
			return "<color>e0835d12</color>";
		}
	}
	
}

 
