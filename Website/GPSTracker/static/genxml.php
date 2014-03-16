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

// End XML file
$xml .='</markers>';
$sxe = new SimpleXMLElement($xml);
echo $sxe->asXML();
?>
