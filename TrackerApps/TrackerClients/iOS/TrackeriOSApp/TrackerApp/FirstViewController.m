//
//  FirstViewController.m
//  TrackerApp
//
//  Created by Chris Ward on 4/2/14.
//  Copyright (c) 2014 Chris Ward. All rights reserved.
//

#import "FirstViewController.h"
#import "RouteHolder.h"
#import <GoogleMaps/GoogleMaps.h>
#import "TrackerParser.h"
@interface FirstViewController ()
@end

@implementation FirstViewController {
    IBOutlet GMSMapView *mapView_;
    GMSPolyline *polyline_;
    GMSMutablePath *path;
    NSTimer *timer;
}

TrackerParser *parser;
-(void) refreshmarkers {
    [mapView_ clear];
    [self drawRoute];
    parser = [[TrackerParser alloc] loadXMLByURL:@"http://tracker.cwardcode.com/static/genxml.php"];
    NSArray *shuttleArray = [parser shuttles];
    for(int i = 0; i < [shuttleArray count]; i++) {
        GMSMarker *marker = [[GMSMarker alloc]init];
        marker.position = CLLocationCoordinate2DMake([shuttleArray[i] lat], [shuttleArray[i] lng]);
        NSLog(@"Lat: %f Lng: %f",[shuttleArray[i] lat],[shuttleArray[i] lng]);
        marker.title = [shuttleArray[i] title];
        marker.map = mapView_;
    }
}

-(void) drawRoute {
    path = [GMSMutablePath path];
    [path addCoordinate:CLLocationCoordinate2DMake(35.3029, -83.18177)];
    [path addCoordinate:CLLocationCoordinate2DMake(35.302780000000006, -83.18175000000001)];
    [path addCoordinate:CLLocationCoordinate2DMake(35.30266, -83.18181000000001)];
    [path addCoordinate:CLLocationCoordinate2DMake(35.302550000000004, -83.18190000000001)];
    [path addCoordinate:CLLocationCoordinate2DMake(35.30246, -83.18191)];
    [path addCoordinate:CLLocationCoordinate2DMake(35.302330000000005, -83.18189000000001)];
    [path addCoordinate:CLLocationCoordinate2DMake(35.302260000000004, -83.18177)];
    [path addCoordinate:CLLocationCoordinate2DMake(35.30169, -83.1811)];
    [path addCoordinate:CLLocationCoordinate2DMake(35.301680000000005, -83.18104000000001)];
    [path addCoordinate:CLLocationCoordinate2DMake(35.301770000000005, -83.18090000000001)];
    [path addCoordinate:CLLocationCoordinate2DMake(35.30176, -83.18082000000001)];
    [path addCoordinate:CLLocationCoordinate2DMake(35.30111, -83.18008)];
    [path addCoordinate:CLLocationCoordinate2DMake(35.30095, -83.17997000000001)];
    [path addCoordinate:CLLocationCoordinate2DMake(35.30073, -83.17993000000001)];
    [path addCoordinate:CLLocationCoordinate2DMake(35.300560000000004, -83.17994)];
    [path addCoordinate:CLLocationCoordinate2DMake(35.30015, -83.18005000000001)];
    [path addCoordinate:CLLocationCoordinate2DMake(35.299240000000005, -83.17989)];
    [path addCoordinate:CLLocationCoordinate2DMake(35.29919, -83.1803)];
    [path addCoordinate:CLLocationCoordinate2DMake(35.29992, -83.18043)];
    [path addCoordinate:CLLocationCoordinate2DMake(35.29997, -83.18039)];
    [path addCoordinate:CLLocationCoordinate2DMake(35.3001, -83.18007)];
    [path addCoordinate:CLLocationCoordinate2DMake(35.30031, -83.18001000000001)];
    [path addCoordinate:CLLocationCoordinate2DMake(35.30057, -83.17994)];
    [path addCoordinate:CLLocationCoordinate2DMake(35.30073, -83.17993000000001)];
    [path addCoordinate:CLLocationCoordinate2DMake(35.30095, -83.17997000000001)];
    [path addCoordinate:CLLocationCoordinate2DMake(35.301010000000005, -83.18001000000001)];
    [path addCoordinate:CLLocationCoordinate2DMake(35.30111, -83.18008)];
    [path addCoordinate:CLLocationCoordinate2DMake(35.30176, -83.18082000000001)];
    [path addCoordinate:CLLocationCoordinate2DMake(35.301770000000005, -83.18088)];
    [path addCoordinate:CLLocationCoordinate2DMake(35.301680000000005, -83.18104000000001)];
    [path addCoordinate:CLLocationCoordinate2DMake(35.30169, -83.1811)];
    [path addCoordinate:CLLocationCoordinate2DMake(35.30227, -83.18177)];
    [path addCoordinate:CLLocationCoordinate2DMake(35.302330000000005, -83.18189000000001)];
    [path addCoordinate:CLLocationCoordinate2DMake(35.30246, -83.18191)];
    [path addCoordinate:CLLocationCoordinate2DMake(35.302550000000004, -83.18190000000001)];
    [path addCoordinate:CLLocationCoordinate2DMake(35.30266, -83.18181000000001)];
    [path addCoordinate:CLLocationCoordinate2DMake(35.302780000000006, -83.18175000000001)];
    [path addCoordinate:CLLocationCoordinate2DMake(35.3029, -83.18177)];
    [path addCoordinate:CLLocationCoordinate2DMake(35.302980000000005, -83.18182)];
    [path addCoordinate:CLLocationCoordinate2DMake(35.302980000000005, -83.18185000000001)];
    [path addCoordinate:CLLocationCoordinate2DMake(35.30221, -83.18263)];
    [path addCoordinate:CLLocationCoordinate2DMake(35.30221, -83.18269000000001)];
    [path addCoordinate:CLLocationCoordinate2DMake(35.30234, -83.18288000000001)];
    [path addCoordinate:CLLocationCoordinate2DMake(35.3025, -83.18299)];
    [path addCoordinate:CLLocationCoordinate2DMake(35.302800000000005, -83.18317)];
    [path addCoordinate:CLLocationCoordinate2DMake(35.304950000000005, -83.18406)];
    [path addCoordinate:CLLocationCoordinate2DMake(35.305640000000004, -83.18427000000001)];
    [path addCoordinate:CLLocationCoordinate2DMake(35.306000000000004, -83.18432000000001)];
    [path addCoordinate:CLLocationCoordinate2DMake(35.306290000000004, -83.18424)];
    [path addCoordinate:CLLocationCoordinate2DMake(35.306470000000004, -83.1845)];
    [path addCoordinate:CLLocationCoordinate2DMake(35.30702, -83.18504)];
    [path addCoordinate:CLLocationCoordinate2DMake(35.307750000000006, -83.18599)];
    [path addCoordinate:CLLocationCoordinate2DMake(35.30778, -83.18602000000001)];
    [path addCoordinate:CLLocationCoordinate2DMake(35.30875, -83.18464)];
    [path addCoordinate:CLLocationCoordinate2DMake(35.30897, -83.18441)];
    [path addCoordinate:CLLocationCoordinate2DMake(35.309110000000004, -83.18429)];
    [path addCoordinate:CLLocationCoordinate2DMake(35.30942, -83.18419)];
    [path addCoordinate:CLLocationCoordinate2DMake(35.309830000000005, -83.18411)];
    [path addCoordinate:CLLocationCoordinate2DMake(35.31036, -83.18594)];
    [path addCoordinate:CLLocationCoordinate2DMake(35.310500000000005, -83.18567)];
    [path addCoordinate:CLLocationCoordinate2DMake(35.310610000000004, -83.1854)];
    [path addCoordinate:CLLocationCoordinate2DMake(35.310750000000006, -83.18482)];
    [path addCoordinate:CLLocationCoordinate2DMake(35.31082, -83.18469)];
    [path addCoordinate:CLLocationCoordinate2DMake(35.310970000000005, -83.18455)];
    [path addCoordinate:CLLocationCoordinate2DMake(35.3112, -83.18445000000001)];
    [path addCoordinate:CLLocationCoordinate2DMake(35.31154, -83.18438)];
    [path addCoordinate:CLLocationCoordinate2DMake(35.31176, -83.18433)];
    [path addCoordinate:CLLocationCoordinate2DMake(35.31208, -83.18431000000001)];
    [path addCoordinate:CLLocationCoordinate2DMake(35.31232, -83.18437)];
    [path addCoordinate:CLLocationCoordinate2DMake(35.312850000000005, -83.18455)];
    [path addCoordinate:CLLocationCoordinate2DMake(35.31317, -83.18459)];
    [path addCoordinate:CLLocationCoordinate2DMake(35.314310000000006, -83.18428)];
    [path addCoordinate:CLLocationCoordinate2DMake(35.31494, -83.18413000000001)];
    [path addCoordinate:CLLocationCoordinate2DMake(35.31512, -83.18423000000001)];
    [path addCoordinate:CLLocationCoordinate2DMake(35.31523, -83.18436000000001)];
    [path addCoordinate:CLLocationCoordinate2DMake(35.315540000000006, -83.18530000000001)];
    [path addCoordinate:CLLocationCoordinate2DMake(35.31559, -83.18539000000001)];
    [path addCoordinate:CLLocationCoordinate2DMake(35.315940000000005, -83.18578000000001)];
    [path addCoordinate:CLLocationCoordinate2DMake(35.316050000000004, -83.18594)];
    [path addCoordinate:CLLocationCoordinate2DMake(35.31611, -83.18608)];
    [path addCoordinate:CLLocationCoordinate2DMake(35.316120000000005, -83.1863)];
    [path addCoordinate:CLLocationCoordinate2DMake(35.31611, -83.18677000000001)];
    [path addCoordinate:CLLocationCoordinate2DMake(35.31609, -83.18697)];
    [path addCoordinate:CLLocationCoordinate2DMake(35.316010000000006, -83.18742)];
    [path addCoordinate:CLLocationCoordinate2DMake(35.316050000000004, -83.1876)];
    [path addCoordinate:CLLocationCoordinate2DMake(35.31604, -83.18771000000001)];
    [path addCoordinate:CLLocationCoordinate2DMake(35.31593, -83.18777)];
    [path addCoordinate:CLLocationCoordinate2DMake(35.315900000000006, -83.18777)];
    [path addCoordinate:CLLocationCoordinate2DMake(35.315870000000004, -83.18773)];
    [path addCoordinate:CLLocationCoordinate2DMake(35.315850000000005, -83.18768)];
    [path addCoordinate:CLLocationCoordinate2DMake(35.315850000000005, -83.1876)];
    [path addCoordinate:CLLocationCoordinate2DMake(35.31589, -83.18751)];
    [path addCoordinate:CLLocationCoordinate2DMake(35.31602, -83.18746)];
    [path addCoordinate:CLLocationCoordinate2DMake(35.316010000000006, -83.18742)];
    [path addCoordinate:CLLocationCoordinate2DMake(35.31609, -83.18697)];
    [path addCoordinate:CLLocationCoordinate2DMake(35.31611, -83.18677000000001)];
    [path addCoordinate:CLLocationCoordinate2DMake(35.316120000000005, -83.18632000000001)];
    [path addCoordinate:CLLocationCoordinate2DMake(35.316100000000006, -83.18608)];
    [path addCoordinate:CLLocationCoordinate2DMake(35.316050000000004, -83.18594)];
    [path addCoordinate:CLLocationCoordinate2DMake(35.315940000000005, -83.18578000000001)];
    [path addCoordinate:CLLocationCoordinate2DMake(35.31559, -83.18539000000001)];
    [path addCoordinate:CLLocationCoordinate2DMake(35.315540000000006, -83.18530000000001)];
    [path addCoordinate:CLLocationCoordinate2DMake(35.31523, -83.18436000000001)];
    [path addCoordinate:CLLocationCoordinate2DMake(35.31512, -83.18423000000001)];
    [path addCoordinate:CLLocationCoordinate2DMake(35.31494, -83.18413000000001)];
    [path addCoordinate:CLLocationCoordinate2DMake(35.314310000000006, -83.18428)];
    [path addCoordinate:CLLocationCoordinate2DMake(35.31317, -83.18459)];
    [path addCoordinate:CLLocationCoordinate2DMake(35.312850000000005, -83.18455)];
    [path addCoordinate:CLLocationCoordinate2DMake(35.31232, -83.18437)];
    [path addCoordinate:CLLocationCoordinate2DMake(35.31208, -83.18431000000001)];
    [path addCoordinate:CLLocationCoordinate2DMake(35.31176, -83.18433)];
    [path addCoordinate:CLLocationCoordinate2DMake(35.31175, -83.18424)];
    [path addCoordinate:CLLocationCoordinate2DMake(35.31166, -83.18390000000001)];
    [path addCoordinate:CLLocationCoordinate2DMake(35.3117, -83.1833)];
    [path addCoordinate:CLLocationCoordinate2DMake(35.311800000000005, -83.18248000000001)];
    [path addCoordinate:CLLocationCoordinate2DMake(35.31188, -83.18227)];
    [path addCoordinate:CLLocationCoordinate2DMake(35.312020000000004, -83.18155)];
    [path addCoordinate:CLLocationCoordinate2DMake(35.312070000000006, -83.18145000000001)];
    [path addCoordinate:CLLocationCoordinate2DMake(35.31215, -83.18135000000001)];
    [path addCoordinate:CLLocationCoordinate2DMake(35.312490000000004, -83.18115)];
    [path addCoordinate:CLLocationCoordinate2DMake(35.312650000000005, -83.1811)];
    [path addCoordinate:CLLocationCoordinate2DMake(35.31298, -83.1811)];
    [path addCoordinate:CLLocationCoordinate2DMake(35.313120000000005, -83.18106)];
    [path addCoordinate:CLLocationCoordinate2DMake(35.31333, -83.18090000000001)];
    [path addCoordinate:CLLocationCoordinate2DMake(35.31365, -83.18048)];
    [path addCoordinate:CLLocationCoordinate2DMake(35.314, -83.18006000000001)];
    [path addCoordinate:CLLocationCoordinate2DMake(35.31398, -83.18002000000001)];
    [path addCoordinate:CLLocationCoordinate2DMake(35.31342, -83.17941)];
    [path addCoordinate:CLLocationCoordinate2DMake(35.312810000000006, -83.17887)];
    [path addCoordinate:CLLocationCoordinate2DMake(35.31241, -83.17930000000001)];
    [path addCoordinate:CLLocationCoordinate2DMake(35.312400000000004, -83.17909)];
    [path addCoordinate:CLLocationCoordinate2DMake(35.312450000000005, -83.17868)];
    [path addCoordinate:CLLocationCoordinate2DMake(35.31244, -83.17828)];
    [path addCoordinate:CLLocationCoordinate2DMake(35.312380000000005, -83.17805000000001)];
    [path addCoordinate:CLLocationCoordinate2DMake(35.31215, -83.17757)];
    [path addCoordinate:CLLocationCoordinate2DMake(35.312050000000006, -83.17744)];
    [path addCoordinate:CLLocationCoordinate2DMake(35.31166, -83.17717)];
    [path addCoordinate:CLLocationCoordinate2DMake(35.311220000000006, -83.17706000000001)];
    [path addCoordinate:CLLocationCoordinate2DMake(35.311, -83.17668)];
    [path addCoordinate:CLLocationCoordinate2DMake(35.31082, -83.17656000000001)];
    [path addCoordinate:CLLocationCoordinate2DMake(35.31073, -83.17647000000001)];
    [path addCoordinate:CLLocationCoordinate2DMake(35.310680000000005, -83.17638000000001)];
    [path addCoordinate:CLLocationCoordinate2DMake(35.31062, -83.17618)];
    [path addCoordinate:CLLocationCoordinate2DMake(35.31056, -83.17569)];
    [path addCoordinate:CLLocationCoordinate2DMake(35.310480000000005, -83.17549000000001)];
    [path addCoordinate:CLLocationCoordinate2DMake(35.31042, -83.17537)];
    [path addCoordinate:CLLocationCoordinate2DMake(35.31042, -83.17533)];
    [path addCoordinate:CLLocationCoordinate2DMake(35.310590000000005, -83.17496000000001)];
    [path addCoordinate:CLLocationCoordinate2DMake(35.31056, -83.17490000000001)];
    [path addCoordinate:CLLocationCoordinate2DMake(35.31053, -83.17488)];
    [path addCoordinate:CLLocationCoordinate2DMake(35.31024, -83.17474)];
    [path addCoordinate:CLLocationCoordinate2DMake(35.310370000000006, -83.17490000000001)];
    [path addCoordinate:CLLocationCoordinate2DMake(35.31044, -83.17492)];
    [path addCoordinate:CLLocationCoordinate2DMake(35.31058, -83.17500000000001)];
    [path addCoordinate:CLLocationCoordinate2DMake(35.31042, -83.17533)];
    [path addCoordinate:CLLocationCoordinate2DMake(35.31042, -83.17537)];
    [path addCoordinate:CLLocationCoordinate2DMake(35.310480000000005, -83.17549000000001)];
    [path addCoordinate:CLLocationCoordinate2DMake(35.31056, -83.17569)];
    [path addCoordinate:CLLocationCoordinate2DMake(35.31062, -83.17620000000001)];
    [path addCoordinate:CLLocationCoordinate2DMake(35.310680000000005, -83.17638000000001)];
    [path addCoordinate:CLLocationCoordinate2DMake(35.31073, -83.17647000000001)];
    [path addCoordinate:CLLocationCoordinate2DMake(35.31082, -83.17657000000001)];
    [path addCoordinate:CLLocationCoordinate2DMake(35.311, -83.17668)];
    [path addCoordinate:CLLocationCoordinate2DMake(35.31121, -83.17706000000001)];
    [path addCoordinate:CLLocationCoordinate2DMake(35.311110000000006, -83.17717)];
    [path addCoordinate:CLLocationCoordinate2DMake(35.31101, -83.17734)];
    [path addCoordinate:CLLocationCoordinate2DMake(35.31089, -83.17768000000001)];
    [path addCoordinate:CLLocationCoordinate2DMake(35.31087, -83.17784)];
    [path addCoordinate:CLLocationCoordinate2DMake(35.310860000000005, -83.17828)];
    [path addCoordinate:CLLocationCoordinate2DMake(35.31082, -83.17855)];
    [path addCoordinate:CLLocationCoordinate2DMake(35.31091, -83.17909)];
    [path addCoordinate:CLLocationCoordinate2DMake(35.31096, -83.17958)];
    [path addCoordinate:CLLocationCoordinate2DMake(35.311130000000006, -83.17993000000001)];
    [path addCoordinate:CLLocationCoordinate2DMake(35.31123, -83.18005000000001)];
    [path addCoordinate:CLLocationCoordinate2DMake(35.31132, -83.18011000000001)];
    [path addCoordinate:CLLocationCoordinate2DMake(35.31141, -83.18015000000001)];
    [path addCoordinate:CLLocationCoordinate2DMake(35.311530000000005, -83.18016)];
    [path addCoordinate:CLLocationCoordinate2DMake(35.31167, -83.18012)];
    [path addCoordinate:CLLocationCoordinate2DMake(35.31177, -83.18007)];
    [path addCoordinate:CLLocationCoordinate2DMake(35.311930000000004, -83.17993000000001)];
    [path addCoordinate:CLLocationCoordinate2DMake(35.311980000000005, -83.18003)];
    [path addCoordinate:CLLocationCoordinate2DMake(35.31195, -83.18016)];
    [path addCoordinate:CLLocationCoordinate2DMake(35.31185, -83.18025)];
    [path addCoordinate:CLLocationCoordinate2DMake(35.31157, -83.18043)];
    [path addCoordinate:CLLocationCoordinate2DMake(35.311490000000006, -83.18044)];
    [path addCoordinate:CLLocationCoordinate2DMake(35.311370000000004, -83.18042000000001)];
    [path addCoordinate:CLLocationCoordinate2DMake(35.311240000000005, -83.18035)];
    [path addCoordinate:CLLocationCoordinate2DMake(35.311170000000004, -83.18028000000001)];
    [path addCoordinate:CLLocationCoordinate2DMake(35.31112, -83.18019000000001)];
    [path addCoordinate:CLLocationCoordinate2DMake(35.311110000000006, -83.18002000000001)];
    [path addCoordinate:CLLocationCoordinate2DMake(35.31109, -83.17997000000001)];
    [path addCoordinate:CLLocationCoordinate2DMake(35.31089, -83.17969000000001)];
    [path addCoordinate:CLLocationCoordinate2DMake(35.3106, -83.17913)];
    [path addCoordinate:CLLocationCoordinate2DMake(35.31, -83.17815)];
    [path addCoordinate:CLLocationCoordinate2DMake(35.309470000000005, -83.17719000000001)];
    [path addCoordinate:CLLocationCoordinate2DMake(35.309020000000004, -83.17647000000001)];
    [path addCoordinate:CLLocationCoordinate2DMake(35.308530000000005, -83.176)];
    [path addCoordinate:CLLocationCoordinate2DMake(35.30836, -83.17589000000001)];
    [path addCoordinate:CLLocationCoordinate2DMake(35.308370000000004, -83.17578)];
    [path addCoordinate:CLLocationCoordinate2DMake(35.30856, -83.17529)];
    [path addCoordinate:CLLocationCoordinate2DMake(35.30863, -83.17518000000001)];
    [path addCoordinate:CLLocationCoordinate2DMake(35.3087, -83.17511)];
    [path addCoordinate:CLLocationCoordinate2DMake(35.30876, -83.1751)];
    [path addCoordinate:CLLocationCoordinate2DMake(35.30881, -83.1751)];
    [path addCoordinate:CLLocationCoordinate2DMake(35.309050000000006, -83.17531000000001)];
    [path addCoordinate:CLLocationCoordinate2DMake(35.309200000000004, -83.17547)];
    [path addCoordinate:CLLocationCoordinate2DMake(35.30937, -83.17563000000001)];
    [path addCoordinate:CLLocationCoordinate2DMake(35.30969, -83.17582)];
    [path addCoordinate:CLLocationCoordinate2DMake(35.30955, -83.17631)];
    [path addCoordinate:CLLocationCoordinate2DMake(35.30953, -83.17632)];
    [path addCoordinate:CLLocationCoordinate2DMake(35.3093, -83.17639000000001)];
    [path addCoordinate:CLLocationCoordinate2DMake(35.30924, -83.17641)];
    [path addCoordinate:CLLocationCoordinate2DMake(35.30913, -83.17654)];
    [path addCoordinate:CLLocationCoordinate2DMake(35.30912, -83.17662)];
    [path addCoordinate:CLLocationCoordinate2DMake(35.309470000000005, -83.17720000000001)];
    [path addCoordinate:CLLocationCoordinate2DMake(35.31, -83.17815)];
    [path addCoordinate:CLLocationCoordinate2DMake(35.3106, -83.17913)];
    [path addCoordinate:CLLocationCoordinate2DMake(35.31078, -83.17948000000001)];
    [path addCoordinate:CLLocationCoordinate2DMake(35.310810000000004, -83.17957000000001)];
    [path addCoordinate:CLLocationCoordinate2DMake(35.31076, -83.17959)];
    [path addCoordinate:CLLocationCoordinate2DMake(35.310680000000005, -83.17972)];
    [path addCoordinate:CLLocationCoordinate2DMake(35.31051, -83.18019000000001)];
    [path addCoordinate:CLLocationCoordinate2DMake(35.31031, -83.18083)];
    [path addCoordinate:CLLocationCoordinate2DMake(35.31016, -83.18123000000001)];
    [path addCoordinate:CLLocationCoordinate2DMake(35.31004, -83.18144000000001)];
    [path addCoordinate:CLLocationCoordinate2DMake(35.309810000000006, -83.18163000000001)];
    [path addCoordinate:CLLocationCoordinate2DMake(35.30969, -83.18168)];
    [path addCoordinate:CLLocationCoordinate2DMake(35.309380000000004, -83.18172000000001)];
    [path addCoordinate:CLLocationCoordinate2DMake(35.30886, -83.18171000000001)];
    [path addCoordinate:CLLocationCoordinate2DMake(35.30863, -83.18181000000001)];
    [path addCoordinate:CLLocationCoordinate2DMake(35.307950000000005, -83.18231)];
    [path addCoordinate:CLLocationCoordinate2DMake(35.30781, -83.18236)];
    [path addCoordinate:CLLocationCoordinate2DMake(35.307430000000004, -83.18234000000001)];
    [path addCoordinate:CLLocationCoordinate2DMake(35.30702, -83.18235000000001)];
    [path addCoordinate:CLLocationCoordinate2DMake(35.306920000000005, -83.18236)];
    [path addCoordinate:CLLocationCoordinate2DMake(35.306740000000005, -83.18244)];
    [path addCoordinate:CLLocationCoordinate2DMake(35.30659, -83.18257000000001)];
    [path addCoordinate:CLLocationCoordinate2DMake(35.3065, -83.18271)];
    [path addCoordinate:CLLocationCoordinate2DMake(35.306400000000004, -83.18294)];
    [path addCoordinate:CLLocationCoordinate2DMake(35.30626, -83.18344)];
    [path addCoordinate:CLLocationCoordinate2DMake(35.30623, -83.18363000000001)];
    [path addCoordinate:CLLocationCoordinate2DMake(35.30624, -83.18402)];
    [path addCoordinate:CLLocationCoordinate2DMake(35.306270000000005, -83.1842)];
    [path addCoordinate:CLLocationCoordinate2DMake(35.30626, -83.18426000000001)];
    GMSPolyline *polyline = [GMSPolyline polylineWithPath:path];
    polyline.strokeColor = [UIColor redColor];
    polyline.strokeWidth = 2.f;
    polyline.map = mapView_;
}
- (void)viewDidLoad
{
    
    GMSCameraPosition *camera = [GMSCameraPosition cameraWithLatitude:35.3067
                                                            longitude:-83.1814
                                                                 zoom:14.85];
    //Draw Route
    [self drawRoute];
    //Refresh marker every 5 seconds.
    timer = [NSTimer scheduledTimerWithTimeInterval:5 target:self selector:@selector(refreshmarkers) userInfo:Nil repeats:TRUE];
    //Set map position
    mapView_.camera = camera;
    
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

@end
