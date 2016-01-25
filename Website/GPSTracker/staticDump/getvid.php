<?php
/**
 * Allows the GPSTracker Mobile App to obtain the current list of vehicles
 * on start. This way, there will always be an up-to-date vehicle list.
 */

include './config.php';
//Connect to database
$con=pg_connect ("host=$hostname dbname=$database user=$username 
                port=$port password=$password", PGSQL_CONNECT_FORCE_NEW) 
                                                   or die("Could not connect!");
// array for json response
$response = array();
$response["vehicles"] = array();

// select all the vehicles from Postgres database
$result = pg_query("SELECT * FROM tracker_vehicle");

//Get list of vids
while($row = pg_fetch_array($result)){
    $tmp = array();
    $tmp["id"] = $row["VehID"];   //vehicle id
    $tmp["name"] = $row["Title"]; //vehicle title
    // push category to final json array
    array_push($response["vehicles"], $tmp);
}

//set header encoding to json
header('Content-Type: application/json');

//print json array
echo json_encode($response);
//close connection
pg_close($con);
?>
