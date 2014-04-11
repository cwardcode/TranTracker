//
//  RouteHolder.h
//  TrackerApp
//
//  Created by Chris Ward on 4/10/14.
//  Copyright (c) 2014 Chris Ward. All rights reserved.
//

#import <Foundation/Foundation.h>
@class GMSMutablePath;
@interface RouteHolder : NSObject
+(GMSMutablePath*) drawAllCampusRoute;
+(GMSMutablePath*) drawHHSRoute;
+(GMSMutablePath*) drawVillageRoute;
+(GMSMutablePath*) drawOffCampusNorth;
+(GMSMutablePath*) drawOffCampusSouth;

@end
