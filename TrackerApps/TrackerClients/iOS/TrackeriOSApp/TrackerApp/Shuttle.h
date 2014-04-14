//
//  Shuttle.h
//  TrackerApp
//
//  Created by Chris Ward on 4/10/14.
//  Copyright (c) 2014 Chris Ward. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface Shuttle : NSObject {
    NSString    *VID;
    NSString    *title;
    double    latitude;
    double    longitude;
    double    curSpeed;
}

@property (nonatomic, retain) NSString *VID;
@property (nonatomic, retain) NSString *title;
@property (nonatomic, assign) double lat;
@property (nonatomic, assign) double lng;
@property (nonatomic, assign) double curSpeed;


@end
