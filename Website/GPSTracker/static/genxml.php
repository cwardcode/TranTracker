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
$connection=mysql_connect ("localhost", $username, $password);
if (!$connection) {
  die('Not connected : ' . mysql_error());
}

// Set the active MySQL database
$db_selected = mysql_select_db($database, $connection);
if (!$db_selected) {
  die ('Can\'t use db : ' . mysql_error());
}

// Select the most recent rows added to Location table
$query = "SELECT * 
FROM (
    SELECT *
    FROM tracker_location
    ORDER BY LocID DESC
) AS tmp
GROUP BY `VehID_id`";

$result = mysql_query($query);

if (!$result) {
  die('Invalid query: ' . mysql_error());
}

$query2 = "SELECT *
FROM tracker_stoplocation";

$result2 = mysql_query($query2);

if (!$result2) {
    die('Invalid query: ' . mysql_error());
}

header("Content-type: text/xml");

// Start XML file, echo parent node
$xml = '<markers>';
// Iterate through the rows, printing XML nodes for each
while ($row = @mysql_fetch_assoc($result)){
$vehNameQuery = "SELECT Title from tracker_vehicle where VehID ='" . $row['VehID_id']."'";
$vehNameResult = mysql_query($vehNameQuery);
$vehNameActual = @mysql_fetch_assoc($vehNameResult);
  // ADD TO XML DOCUMENT NODE
  $xml .= "<marker>";
  $xml .= "<VID>".parseToXML($row['VehID_id'])."</VID>";
  $xml .= "<title>".$vehNameActual['Title']."</title>";
  $xml .= "<latitude>".$row['Latitude']."</latitude>";
  $xml .= "<longitude>".$row['Longitude']."</longitude>";
  $xml .= "<speed>".$row['Speed']."</speed>";
  $xml .= "</marker>";
}
while ($row = @mysql_fetch_assoc($result2)){
    $stopNameQuery = "SELECT StopName from tracker_stoplocation where StopID='" . $row['StopID']."'";
    $stopNameResult = mysql_query($stopNameQuery);
    $stopNameActual = @mysql_fetch_array($stopNameResult);
    $stopIDQuery = "SELECT StopID from tracker_stoplocation where StopName = ".$stopNameActual[0]."'";
    $stopIDResult = mysql_query($stopIDQuery);
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
