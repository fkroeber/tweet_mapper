# Google Earth Tweet Mapper

This repo documents the outcome of the end of term assignment in the software development course at Paris Lodron University Salzburg (PLUS).  It contains an app that maps a timeseries of tweets in GoogleEarth. The sample .csv file also provided contains tweets sent in the New York area on 5 November 2012 on the eve of the presidential election. The app represents tweets as markers whose size is proportional to the length of the tweets. Election-related and non election-related tweets are distinguished through different marker colours.

![mapped tweets](./tweets_mapped.png)

The app is structured as follows:
* GoogleEarthTweetMapper: main class
* WorkDirSetter: class to let the user specify the working directory
* WMSImageDownloader: class that downloads a basemap
* TwitterCSVReader: class that reads & parses tweets from a .csv file
* KMLElements: class that specifies how to represent the tweets in the kml file
* KMLGenerator: class that provides the skeleton for the kml file generation

The main class calls all other classes and finally opens the created kml file using the local installation of GoogleEarthPro if available. For a more detailed documentation of the structure & design of the application see: *to be added*
