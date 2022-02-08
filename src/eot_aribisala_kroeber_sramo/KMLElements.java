package eot_aribisala_kroeber_sramo;

import net.sf.geographiclib.Geodesic;

public class KMLElements {

	// compose ground overlay element for wms map
	public String wmsgroundoverlay(String _img_path, String _extent) {
		String kml =
				"<GroundOverlay>\n" +
					"<name>wms overlay</name>\n" +
					"<color>4Dffffff</color>\n" + // hex color code for 70% transparency
					"<Icon>\n" +
						"<href>" + _img_path + "</href>\n" +
					"</Icon>\n" +
					"<LatLonBox>\n" +
						"<north>" + _extent.split(",")[3] + "</north>\n" +
						"<south>" + _extent.split(",")[1] + "</south>\n" +
						"<east>" + _extent.split(",")[2] + "</east>\n" +
						"<west>" + _extent.split(",")[0] + "</west>\n" +
					"</LatLonBox>\n" +
				"</GroundOverlay>\n";
		return kml;
	}

	// compose marker elements for tweets
	public String tweetsvis(TwitterCSVReader _twitter_reader) {
		String kml = "";
		// loop over all tweets
		for (int i=0; i<_twitter_reader.linecount-1; i++) {
			// get all relevant parameters for a single tweet
			String tweet_id = _twitter_reader.data.get("id")[i];
			String tweet = _twitter_reader.data.get("tweet")[i];
			String created_at = _twitter_reader.data.get("created_at")[i];
			String user_id = _twitter_reader.data.get("user_id")[i];
			String lat = _twitter_reader.data.get("lat")[i];
			String lon = _twitter_reader.data.get("lng")[i];
			String tweet_length = _twitter_reader.data.get("tweet_length")[i];
			String election_related = _twitter_reader.data.get("election_related")[i];
			// compose kml snippet for each tweet
			kml +=  // create style for marker (edge width & color)
					"<Style id='pyramid_style'>\n" +
					"<LineStyle>\n" +
						"<width>1</width>\n" +
					"</LineStyle>\n" +
					"<PolyStyle>\n" +
						pyramid_color(election_related) +
					"</PolyStyle>\n" +
					"</Style>\n" +
					// create placemark
					"<Placemark>\n" +
						"<name>Tweet_ID: "+ tweet_id +"</name>\n" +
						// create description
						"<description>" +
							"<![CDATA[" +
							"<p>" + tweet +"</p>" +
							"<p><i>user_id: " + user_id + "</br>" +
							"created at: " + created_at.split("\\+")[0] + "</i></p>" +
							"]]>" +
						"</description>" +
						// enable time series visualization
						"<TimeStamp><when>" + created_at.replace(' ', 'T') + ":00" + "</when></TimeStamp>" +
						// apply style
						"<styleUrl>#pyramid_style</styleUrl>" +
						// define geometry of marker by putting together all sides of a pyramid
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
											pyramid_vertices_coords(lat, lon, tweet_length, "top_north_east") + "\n" +
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

	// auxiliary function to specify marker geometry
	private String pyramid_vertices_coords(String lat, String lon, String size_var, String type) {
		// define basic geometry of markers in terms of volume and edge length/height ratio
		// volume proportional to size_var
		double volume =  Double.parseDouble(size_var) * 500;
		double ratio_h_a = 2;
		// derive edge length and height in m
		double a = Math.pow(3*volume/ratio_h_a,1.0/3.0);
		String h = Double.toString(a*ratio_h_a);
		// calculate edge coordinates of pyramid marker based on central point's lat/lon & edge length in m
		String north_x = Double.toString(Geodesic.WGS84.Direct(Double.parseDouble(lat), Double.parseDouble(lon), 0.0, 0.5*a).lat2);
		String east_y = Double.toString(Geodesic.WGS84.Direct(Double.parseDouble(lat), Double.parseDouble(lon), 90.0, 0.5*a).lon2);
		String south_x = Double.toString(Geodesic.WGS84.Direct(Double.parseDouble(lat), Double.parseDouble(lon), 180.0, 0.5*a).lat2);
		String west_y = Double.toString(Geodesic.WGS84.Direct(Double.parseDouble(lat), Double.parseDouble(lon), 270.0, 0.5*a).lon2);
		// return kml-conform coordinates depending on the request
		switch(type) {
		case "top_north_east": return east_y + "," + north_x + "," + h;
		case "top_south_east": return east_y + "," + south_x + "," + h;
		case "top_south_west": return west_y + "," + south_x + "," + h;
		case "top_north_west": return west_y + "," + north_x + "," + h;
		case "bottom": return lon + "," + lat + ",0";
		default: return "";
		}
	};

	// auxiliary function to specify marker color
	private String pyramid_color(String color_var) {
		// if tweet is likely to be election related set color to yellow
		if (color_var == "probably") {
			return "<color>e050e7fc</color>";
		}
		// otherwise set color to blue
		else {
			return "<color>e0835d12</color>";
		}
	}

}


