//
//  FirstViewController.m
//  TrackerApp
//
//  Created by Chris Ward on 4/2/14.
//  Copyright (c) 2014 Chris Ward. All rights reserved.
//

#import "FirstViewController.h"
#import <GoogleMaps/GoogleMaps.h>
@interface FirstViewController ()

@end

@implementation FirstViewController {
    IBOutlet GMSMapView *aaa;
}

- (void)viewDidLoad
{
        GMSCameraPosition *camera = [GMSCameraPosition cameraWithLatitude:-33.8683
                                                            longitude:151.2086
                                                                 zoom:6];
    aaa = [GMSMapView mapWithFrame:CGRectZero camera:camera];
    aaa.mapType = kGMSTypeNone;
    //make marker
    GMSMarker *marker = [[GMSMarker alloc]init];
    marker.position = CLLocationCoordinate2DMake(-83.1836, 35.3097);
    marker.title =@"Cullowhee";
    marker.snippet = @"NC";
    marker.map = aaa;
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

@end
