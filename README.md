Toronto Address Points Open Dataset Exporter
============================================

The Toronto municipal Open Data site publishes a [data set](http://www1.toronto.ca/wps/portal/open_data/open_data_item_details?vgnextoid=91415f9cd70bb210VgnVCM1000003dd60f89RCRD&vgnextchannel=6e886aa8cc819210VgnVCM10000067d60f89RCRD) comprising all the 500,000+ addresses found within the city. Each location includes its geo-location (latitude and longitude) and category of use, among other fields. This dataset could be useful for many Toronto focused apps and APIs, however, the ESRI Shapefile format that it is distributed in will be a barrier for some.   

[Toronto Open Data - Address Points](http://www1.toronto.ca/wps/portal/open_data/open_data_item_details?vgnextoid=91415f9cd70bb210VgnVCM1000003dd60f89RCRD&vgnextchannel=6e886aa8cc819210VgnVCM10000067d60f89RCRD)

This tool makes it possible to extract the data from the published ESRI Shapefiles in a more accessible format for projects to use. Currently there is support for converting to CSV files and writing to Mongo databases. It should be straightforward to extend the tool to export to additional formats.

You may also be interested in paired GitHub project that provides access to the same data with a simple REST based web service:
[torontoopendata-addresspoints-geocoder](https://github.com/Intelliware/torontoopendata-addresspoints-geocoder)


Requirements
------------

* Java 1.5+
* Maven 2.1+


Building and Running
--------------------

Build the tool with Maven:

    git clone https://github.com/Intelliware/torontoopendata-addresspoints-exporter.git
    cd torontoopendata-addresspoints-exporter
    mvn package

Confirm the basic functioning of the tool:

    java -jar target/addresspoints-exporter.jar -v

To output the addresses to a CSV file:

    java -jar target/addresspoints-exporter.jar --csv out.csv -v

To add the addresses to a Mongo DB:

    java -jar target/addresspoints-exporter.jar --mongo mongodb://mongo/toronto-addresspoints -v


Command Line Options
--------------------
    java -jar target/addresspoints-exporter.jar [options...] SHP-FILE
     SHP-FILE               : read addresses from Toronto open data address points
                              ESRI .shp file
     -c (--csv) CSV-FILE    : write addresses to the specified CSV file
     -m (--mongo) MONGO-URI : write addresses to the specified mongo database URI in the addresses collection
     -s (--schema)          : dump out all the fields from the source file
     -v (--verbose)         : log each address point read


Licence
-------

This tool is made available under the [MIT licence](https://github.com/Intelliware/torontoopendata-addresspoints-exporter/blob/master/LICENSE).

Contains public sector Datasets made available under the [City of Toronto's Open Data Licence v2.0](http://www1.toronto.ca/wps/portal/open_data/open_data_fact_sheet_details?vgnextoid=59986aa8cc819210VgnVCM10000067d60f89RCRD).



David Jones
