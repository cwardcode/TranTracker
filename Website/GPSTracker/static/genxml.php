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
$connection=mysql_connect (localhost, $username, $password);
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
    FROM Location
    ORDER BY LocID DESC
) AS tmp
GROUP BY `VehicleID`";
$result = mysql_query($query);

if (!$result) {
  die('Invalid query: ' . mysql_error());
}

header("Content-type: text/xml");

// Start XML file, echo parent node
echo '<markers>';

// Iterate through the rows, printing XML nodes for each
while ($row = @mysql_fetch_assoc($result)){
  // ADD TO XML DOCUMENT NODE
  echo '<marker ';
  echo 'VID="' . parseToXML($row['VehicleID']) . '" ';
  echo 'latitude="' . $row['Latitude'] . '" ';
  echo 'longitude="' . $row['Longitude'] . '" ';
  echo 'speed="' . $row['Speed'] . '" ';
  echo 'title="' . $row['Title'] . '" ';
  echo '/>';
}

// End XML file
echo '</markers>';
?>
