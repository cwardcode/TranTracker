<?php
include './config.php';

$con = mysql_connect('localhost', "gpstracker", "tracker") or die (mysql_error());

mysql_select_db($database) or die (mysql_error());

// array for json response
$response = array();
$response["vehicles"] = array();

// Mysql select query
$result = mysql_query("SELECT * FROM tracker_vehicle");

while($row = mysql_fetch_array($result)){
    // temporary array to create single category
    $tmp = array();
    $tmp["id"] = $row["VehID"];
    $tmp["name"] = $row["Title"];
    // push category to final json array
    array_push($response["vehicles"], $tmp);
}

// keeping response header to json
header('Content-Type: application/json');

// echoing json result
echo json_encode($response);

mysql_close($con);
?>
