var totReqe = 0;
function getDistance(lat,lng) {
    var directionsService = new google.maps.DirectionsService();
    var totDist = -1;
    var request = {
        origin: new google.maps.LatLng(35.3112808,-83.1828630),
        destination: new google.maps.LatLng(lat,lng),
        travelMode: google.maps.DirectionsTravelMode.DRIVING
    };
    if(totReqe < 100) {
        directionsService.route(request, function(response, status) {
            if(status == google.maps.DirectionsStatus.OK){
                totDist +=
            response.routes[0].legs[0].distance.value + " meters";
        console.log(response.routes[0].legs[0].distance.value);
            }
            else {
                console.log(status + request.destination);
            }

        });
        totReqe++;
    }
    return totDist;
}
