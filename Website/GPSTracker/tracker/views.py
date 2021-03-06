from chartit import DataPool, Chart
from django.shortcuts import render_to_response
from django.http import HttpResponse
from tracker.models import PeopleCount,Location, ShuttleRoute, RouteStop


def home(request):
    return render_to_response('index.html')


def demo(request):
    routes = None
    try:
        routes = ShuttleRoute.objects.all()[0].mpoly
        routeNames = ShuttleRoute.objects.order_by('name').all()
    except (UnboundLocalError, IndexError) as e:
        print "No routes in list!"
    query = 'SELECT DISTINCT ON ("VehID_id") * FROM ( SELECT * FROM tracker_location GROUP BY "VehID_id", "LocID", "Latitude" ,"Longitude" ,"Speed", "estWait", "NextStop" ORDER BY "LocID" DESC) AS tmp'; 
    shuttles = Location.objects.raw(query)
   #holder = Location.objects.distinct()
 
   # for s in holder:
   #     shuttles = s.NextStop
       
    stops = RouteStop.objects.order_by('StopName').all() 
    return render_to_response('test.html', {'routeNames': routeNames, 'routes': routes,'shuttles':shuttles,'stops': stops})

def features(request):
    return render_to_response('features.html')

def leaftest(request):
    routes = None
    try:
        routes = ShuttleRoute.objects.all()[0].mpoly
    except (UnboundLocalError, IndexError) as e:
        print "No routes in list!"
    query = 'SELECT DISTINCT ON ("VehID_id") * FROM ( SELECT * FROM tracker_location GROUP BY "VehID_id", "LocID", "Latitude" ,"Longitude" ,"Speed", "estWait", "NextStop" ORDER BY "LocID" DESC) AS tmp'; 
    shuttles = Location.objects.raw(query)
   #holder = Location.objects.distinct()
 
   # for s in holder:
   #     shuttles = s.NextStop
       
    stops = RouteStop.objects.all() 
    return render_to_response('leaftest.html', {'routes': routes,'shuttles':shuttles,'stops': stops})



def test(request):
    routes = None
    try:
        routes = ShuttleRoute.objects.all()[0].mpoly
        routeNames = ShuttleRoute.objects.order_by('name').all()
    except (UnboundLocalError, IndexError) as e:
        print "No routes in list!"
    query = 'SELECT DISTINCT ON ("VehID_id") * FROM ( SELECT * FROM tracker_location GROUP BY "VehID_id", "LocID", "Latitude" ,"Longitude" ,"Speed", "estWait", "NextStop" ORDER BY "LocID" DESC) AS tmp'; 
    shuttles = Location.objects.raw(query)
   #holder = Location.objects.distinct()
 
   # for s in holder:
   #     shuttles = s.NextStop
       
    stops = RouteStop.objects.order_by('StopName').all() 
    return render_to_response('test.html', {'routeNames': routeNames, 'routes': routes,'shuttles':shuttles,'stops': stops})


def get_routes_json(request, route_id):
    route = ShuttleRoute.objects.get(pk=route_id)
    response = route.mpoly.geojson
    return HttpResponse(response, mimetype='application/json')

def chat(request):
    return render_to_response('chat.html')


def stopNames(nameID):
    stop = RouteStop.objects.filter(StopID = nameID).values()[0]['StopName'].split(' ')[0]
    return stop


def chart(request):
    ridershipdata = \
        DataPool(
            series=
            [{'options': {
                'source': PeopleCount.objects.all()}, # PeopleCount.objects.all()},
              'terms': [
                  'Count',
                  'StopID',
                  'LocID']}
            ])
    riderchart = Chart(
        datasource=ridershipdata,
        series_options=
        [{'options': {
            'type': 'pie',
            'stacking': False},
          'terms': {
              'StopID': [
                  'Count']
          }}],
        chart_options=
        {'title': {
            'text': 'Ridership Data'},
         'xAxis': {
             'title': {
                 'text': 'Rider Count'}}},
             x_sortf_mapf_mts=(None, stopNames, False))

    return render_to_response('chart.html', {'ridershipChart': riderchart})

def about(request):
    return render_to_response('about.html')


def contact(request):
    return render_to_response('contact.html')
