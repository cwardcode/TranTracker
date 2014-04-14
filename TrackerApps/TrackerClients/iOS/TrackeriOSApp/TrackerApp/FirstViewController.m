//
//  FirstViewController.m
//  TrackerApp
//
//  Created by Chris Ward on 4/2/14.
//  Copyright (c) 2014 Chris Ward. All rights reserved.
//

#import <GoogleMaps/GoogleMaps.h>
#import "FirstViewController.h"
#import "RouteHolder.h"
#import "TrackerParser.h"
@interface FirstViewController ()
@end
TrackerParser *parser;

@implementation FirstViewController {
    IBOutlet GMSMapView *mapView_;
}
GMSPolyline *polyline_;
GMSMutablePath *allCampusPath;
GMSMutablePath *hhsExpressPath;
GMSMutablePath *villagePath;
GMSMutablePath *offCampusNorth;
GMSMutablePath *offCampusSouth;
NSTimer *markerTimer;
NSTimer *timer;
int routeID;

+(void)setRouteID:(int)newRouteID{
    routeID = newRouteID;
}

-(void) refreshMarkers {
    [mapView_ clear];
    [self drawRoutes];
    [self drawStopLocations];
    parser = [[TrackerParser alloc] loadXMLByURL:@"http://tracker.cwardcode.com/static/genxml.php"];
    NSArray *shuttleArray = [parser shuttles];
    for(int i = 0; i < [shuttleArray count]; i++) {
        GMSMarker *marker = [[GMSMarker alloc]init];
        marker.position = CLLocationCoordinate2DMake([shuttleArray[i] lat], [shuttleArray[i] lng]);
        marker.title = [shuttleArray[i] title];
        marker.snippet = [NSString stringWithFormat:@"Speed: %02f", [shuttleArray[i] curSpeed]];
        marker.icon = [UIImage imageNamed:@"shuttle.png"];
        marker.map = mapView_;
    }
}
-(void) drawStopLocations {
    parser = [[TrackerParser alloc] loadXMLByURL:@"http://tracker.cwardcode.com/static/genxml.php"];
    NSArray *stopArray = [parser stopLocs];
    for (int i = 0; i < [stopArray count]; i++) {
        GMSMarker *marker = [[GMSMarker alloc]init];
        marker.position = CLLocationCoordinate2DMake([stopArray[i] stopLatitude], [stopArray[i] stopLongitude]);
        marker.icon = [UIImage imageNamed:@"stopMarker.ico"];
        marker.title = [stopArray[i] stopName];
        marker.map = mapView_;
    }
}

-(void) drawRoutes {
    if(routeID == 0) {
        allCampusPath = [RouteHolder drawAllCampusRoute];
        GMSPolyline *allCampusPoly = [GMSPolyline polylineWithPath:allCampusPath];
        allCampusPoly.strokeColor = [UIColor redColor];
        allCampusPoly.strokeWidth = 2.f;
        allCampusPoly.map = mapView_;
    } else if (routeID == 1) {
        villagePath = [RouteHolder drawVillageRoute];
        GMSPolyline *villagePoly = [GMSPolyline polylineWithPath:villagePath];
        villagePoly.strokeColor = [UIColor yellowColor];
        villagePoly.strokeWidth = 2.f;
        villagePoly.map = mapView_;
    } else if (routeID == 2) {
        hhsExpressPath = [RouteHolder drawHHSRoute];
        GMSPolyline *hhsPoly = [GMSPolyline polylineWithPath:hhsExpressPath];
        hhsPoly.strokeColor = [UIColor blueColor];
        hhsPoly.strokeWidth = 2.f;
        hhsPoly.map = mapView_;
    } else if (routeID == 3) {
        offCampusNorth = [RouteHolder drawOffCampusNorth];
        GMSPolyline *offCampusNorthPoly = [GMSPolyline polylineWithPath:offCampusNorth];
        offCampusNorthPoly.strokeColor = [UIColor greenColor];
        offCampusNorthPoly.strokeWidth = 2.f;
        offCampusNorthPoly.map = mapView_;
    } else if (routeID == 4) {
        offCampusSouth = [RouteHolder drawOffCampusSouth];
        GMSPolyline *offCampusSouthPoly = [GMSPolyline polylineWithPath:offCampusSouth];
        offCampusSouthPoly.strokeColor = [UIColor purpleColor];
        offCampusSouthPoly.strokeWidth = 2.f;
        offCampusSouthPoly.map = mapView_;
    }
}

- (void)viewDidLoad
{
    //Set first route to be all-campus by default.
    routeID = 0;
    //Setup map camera
    GMSCameraPosition *camera = [GMSCameraPosition cameraWithLatitude:35.3067
                                                            longitude:-83.1814
                                                                 zoom:14.85];
    //Draw set of Stop Markers
    [self drawStopLocations];
    //Draw first set of markers
    [self refreshMarkers];
    //Draw Route
    [self drawRoutes];
    //Refresh marker every 5 seconds.
    markerTimer = [NSTimer scheduledTimerWithTimeInterval:1 target:self selector:@selector(refreshMarkers) userInfo:Nil repeats:TRUE];
    //Set map position
    mapView_.camera = camera;
    
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

@end
