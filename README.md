Toronto AddressPoints open data set Exporter
============================================

The Toronto municipal Open Data site publishes a data set comprising all the 500k+ addresses found within the city, and includes their geo-location (latitude and longitude) and category of use, among other fields. This data set will be of use by many Toronto based apps and services, however, the ESRI Shapefile format that it is distributed in might be a barrier for some smaller projects.   

http://www1.toronto.ca/wps/portal/open_data/open_data_item_details?vgnextoid=91415f9cd70bb210VgnVCM1000003dd60f89RCRD&vgnextchannel=6e886aa8cc819210VgnVCM10000067d60f89RCRD

This tool makes it possible to extract the data from the published ESRI Shapefiles in a more accessible format for other projects to make use of. Initially there is is support for CSV files and writing to a MongoDB and it should be straightforward to extend it to export to further formats.


Requirements
------------

Java 1.5+
Maven 2.1+


Download Data Set
-----------------

Download the Address Points(Municipal) - April 2012 (WGS84)

http://www1.toronto.ca/City_Of_Toronto/Information_&_Technology/Open_Data/Data_Sets/Assets/Files/address_points_wgs84_apr2012.zip

(or the relevant download from: http://www1.toronto.ca/wps/portal/open_data/open_data_item_details?vgnextoid=91415f9cd70bb210VgnVCM1000003dd60f89RCRD&vgnextchannel=6e886aa8cc819210VgnVCM10000067d60f89RCRD )

Unzip the file to a new directory.


Building and Running
--------------------

To build the tool with Maven:

cd toronto-addresspoints-exporter
mvn package

To confirm the basics are functioning:

java -jar target/torontoopendata-addresspoints-exporter-1.0-SNAPSHOT.jar address_points_wgs84_apr2012/address_point_wgs84.shp 

To output the addresses to a CSV file:

java -jar target/torontoopendata-addresspoints-exporter-1.0-SNAPSHOT.jar address_points_wgs84_apr2012/address_point_wgs84.shp  --csv out.csv

To add the addresses to a Mongo DB:

java -jar target/torontoopendata-addresspoints-1.0-SNAPSHOT.jar address_points_wgs84_apr2012/address_point_wgs84.shp  --mongo mongodb://mongo/toronto-addresspoints


Licence
-------

This tool is made available under the MIT licence.
http://opensource.org/licenses/mit-license.php

The Dataset is made available under the Open Data Licence for City of Toronto Datasets.
http://www1.toronto.ca/wps/portal/open_data/open_data_fact_sheet_details?vgnextoid=59986aa8cc819210VgnVCM10000067d60f89RCRD


David Jones
