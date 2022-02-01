/* To-Do
 * Error handling test -> test suite https://www.tutorialspoint.com/junit/junit_overview.htm
 * Remarks/Documentation
 * */

package eot_aribisala_kroeber_sramo;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;

import javax.imageio.ImageIO;

import org.apache.commons.lang3.ArrayUtils;
import org.geotools.ows.ServiceException;
import org.geotools.ows.wms.Layer;
import org.geotools.ows.wms.WMSCapabilities;
import org.geotools.ows.wms.WMSUtils;
import org.geotools.ows.wms.WebMapServer;
import org.geotools.ows.wms.request.GetMapRequest;
import org.geotools.ows.wms.response.GetMapResponse;

import net.sf.geographiclib.Geodesic;
import net.sf.geographiclib.GeodesicMask;

public class WMSImageDownloader {

	// define basic instance variables
	// a). general wms connection variables
	String wms_service;
	WebMapServer wms_server;
	WMSCapabilities wms_capabilities;
	Layer[] wms_layers;
	// b.) wms getmap request variables
	String bbox;
	Layer layer;
	Dimension img_size;
	BufferedImage img;
	// c.) save map variables	
	String download_dir;
	
	// constructor to initialise WMSImageDownlaoder
	public WMSImageDownloader(String _wms_service) {
		// set values for instance variables as part of initialisation procedure
		wms_service = _wms_service;
		// establish connection to server
		wms_server = connectwmsserver();
		// performing GetCapabilitiesRequest
		wms_capabilities = wms_server.getCapabilities();
		// print out basic information about server
	    String serverTitle = wms_capabilities.getService().getTitle().toString();
	    // System.out.println(wms_capabilities.getLayerList().get(1).getName());
	    wms_layers = WMSUtils.getNamedLayers(wms_capabilities);
	    System.out.println("WMS-Capabilities sucessfully retrieved from service: " + serverTitle);
	    System.out.println("Available layers: " + Arrays.toString(wms_layers));
	}
	
	
	// method for WMS GetMap request
	public void getmap(String _bbox, String _layername) {
		// set values for instance variables: bbox, layer, dimensions
		bbox = _bbox;
  		img_size = calc_img_size(_bbox);
		layer = convert_name_title(_layername);
		// requesting map
		boolean retry = true;
		while(retry) {
			try {
			  // in general do not retry in case of failure
			  retry = false;
			  // constructing the GetMap request by populating it with the parameter values
			  GetMapRequest request = wms_server.createGetMapRequest();
			  request.setFormat("image/png");
			  request.setTransparent(true);
			  request.setDimensions(img_size);
			  request.setSRS("EPSG:4326");
			  request.setBBox(bbox);
			  request.addLayer(layer);
			  // executing the GetMap request and parsing the response as a buffered image
			  GetMapResponse response = (GetMapResponse) wms_server.issueRequest(request);
			  img = ImageIO.read(response.getInputStream());
			} catch (ServiceException e) {
				if (e.getMessage().equals("image size too large")) {
					System.out.println("GetMapRequest failed due to image size: "
							           + "Retrying using smaller resolution");
					img_size = new Dimension((int) img_size.width/2, (int) img_size.height/2);
					retry = true;
				} else {
					e.printStackTrace();
				};
			} catch (IOException e2) {
				e2.printStackTrace();
			}	
		}
	}
	
	// method for saving requested map to drive
	public void savemap(String _download_dir) {
	  // set values for instance variables: bbox, layer, dimensions
	  download_dir = _download_dir;
	  // part four
      try {
        File f = new File(download_dir);
        ImageIO.write(this.img, "png", f);
        System.out.println("File written to disk.");
      } catch(IOException e){
        System.out.println("Error: "+ e);
      }
	}
	
	// auxiliary function to establish wms server connection
	private WebMapServer connectwmsserver() {
		try { 
			URL url = new URL(wms_service + "?VERSION=1.1.1&Request=GetCapabilities&Service=WMS");
			wms_server = new WebMapServer(url);
		} catch (ServiceException | IOException e) {
			e.printStackTrace();
		}
		return wms_server;
	}
	
	// auxiliary function to set dimension based on specified bbox & desired resolution
	private Dimension calc_img_size(String bbox) {
		double req_resolution = 5;
		double lon1 = Double.parseDouble(bbox.split(",")[0]);
		double lat1 = Double.parseDouble(bbox.split(",")[1]);
		double lon2 = Double.parseDouble(bbox.split(",")[2]);
		double lat2 = Double.parseDouble(bbox.split(",")[3]);
		double mean_lat = (lat1+lat2)/2;
		double mean_lon = (lon1+lon2)/2;
		double map_height = Geodesic.WGS84.Inverse(lat1, mean_lon, lat2, mean_lon, GeodesicMask.DISTANCE).s12;
		double map_width = Geodesic.WGS84.Inverse(mean_lat, lon1, mean_lat, lon2, GeodesicMask.DISTANCE).s12;
		Dimension img_dims = new Dimension((int) (map_width/req_resolution), (int) (map_height/req_resolution));
		return img_dims;
	}
	
	// auxiliary function to allow specification of layer titles as well as names
	private Layer convert_name_title(String specified_layer) {
		// check for existence of specified layer title 
		// 
		try {
			if (Arrays.toString(wms_layers).contains(specified_layer)) {
				String[] arr_layers = new String[wms_capabilities.getLayerList().size()-1];
				for (int i = 1; i < wms_capabilities.getLayerList().size(); i++) {
					arr_layers[i-1] = wms_capabilities.getLayerList().get(i).toString();
				}
				int idx_layer = ArrayUtils.indexOf(arr_layers, specified_layer);
				return wms_layers[idx_layer];	
			} else {
				String[] layers_names = new String[wms_capabilities.getLayerList().size()-1];
				for (int i = 1; i < wms_capabilities.getLayerList().size(); i++) {
					layers_names[i-1] = wms_capabilities.getLayerList().get(i).getName();
				};
				int idx_layer = ArrayUtils.indexOf(layers_names, specified_layer);
				return wms_layers[idx_layer];
			}
		} catch (Exception e){
			throw new IllegalArgumentException("Layer not offered by the WMS. " +
											   "Please take a look at the list of available layers.");
			}
		}

}
