<?php
require("config.php");

function parseToXML($htmlStr) 
{ 
    $xmlStr=str_replace('<','&lt;',$htmlStr); 
    $xmlStr=str_replace('>','&gt;',$xmlStr); 
    $xmlStr=str_replace('"','&quot;',$xmlStr); 
    $xmlStr=str_replace("'",'&#39;',$xmlStr); 
    $xmlStr=str_replace("&",'&amp;',$xmlStr); 
    return $xmlStr; 
} 

// Opens a connection to a MySQL server
    $connection=pg_connect ("host=$hostname dbname=$database user=$username 
                       port=$port password=$password", PGSQL_CONNECT_FORCE_NEW);
if(!$connection) {
die("Couldn't create database connection!");
}

// Select the most recent rows added to Location table
$query = "SELECT DISTINCT ON(\"VehID_id\") \"VehID_id\", \"LocID\", 
         \"Latitude\" ,\"Longitude\" ,\"Speed\", \"estWait\", \"NextStop\"
FROM (
    SELECT *
    FROM tracker_location
    ORDER BY \"LocID\" DESC
) AS tmp
GROUP BY \"VehID_id\", \"LocID\", \"Latitude\" ,\"Longitude\" ,\"Speed\",
         \"estWait\", \"NextStop\";";
$newQuery = 
    "SELECT DISTINCT ON (\"VehID_id\") *
       FROM (
              SELECT *
                 FROM tracker_location
                 GROUP BY \"VehID_id\", \"LocID\", \"Latitude\" ,\"Longitude\" ,\"Speed\", \"estWait\", \"NextStop\"
                 ORDER BY \"LocID\" DESC) AS tmp;";
$result = pg_query($connection, $newQuery);

if (!$result) {
  die('Invalid query: ' . $query);
}

//Create XML header
header("Content-type: text/xml");

// Start XML file, echo parent node
$xml = '<markers>';
// Iterate through the rows, printing XML nodes for each
while ($row = @pg_fetch_assoc($result)){
$vehNameQuery = "SELECT \"Title\" from tracker_vehicle where \"VehID\" ='" . $row['VehID_id']."'";
$vehNameResult = pg_query($vehNameQuery);
$vehNameActual = @pg_fetch_assoc($vehNameResult);
  // ADD TO XML DOCUMENT NODE
  $xml .= "<marker>";
  $xml .= "<VID>".parseToXML($row['VehID_id'])."</VID>";
  $xml .= "<title>".$vehNameActual['Title']."</title>";
  $xml .= "<latitude>".$row['Latitude']."</latitude>";
  $xml .= "<longitude>".$row['Longitude']."</longitude>";
  $xml .= "<speed>".$row['Speed']."</speed>";
  $xml .= "<estwait>".$row['estWait']."</estwait>";
  $xml .= "<nextstop>".$row['NextStop']."</nextstop>";
  $xml .= "</marker>";
}

//Handle Stop Locations
$query2 = "SELECT *
FROM tracker_stoplocation";

$result2 = pg_query($query2);

if (!$result2) {
    die('Invalid query: ' . $query2);
}


while ($row = @pg_fetch_assoc($result2)){
    $stopNameQuery = "SELECT \"StopName\" from tracker_stoplocation where \"StopID\"='" . $row['StopID']."'";
    $stopNameResult = pg_query($stopNameQuery);
    $stopNameActual = @pg_fetch_array($stopNameResult);
    $stopIDQuery = "SELECT \"StopID\" from tracker_stoplocation where \"StopName\" = '".$stopNameActual[0]."'";
    $stopIDResult = pg_query($stopIDQuery);
    $xml .= "<stop>";  
    $xml .= "<stopID>".$row['StopID']."</stopID>";    
    $xml .= "<stopName>".$stopNameActual[0]."</stopName>";    
    $xml .= "<stopLat>".parseToXML($row['Latitude'])."</stopLat>";
    $xml .= "<stopLong>".parseToXML($row['Longitude'])."</stopLong>";
    $xml .= "</stop>"; 
}
// End XML file
$xml .='</markers>';
$sxe = new SimpleXMLElement($xml);
echo $sxe->asXML();
?>
