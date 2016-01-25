//
//  StopLocation.h
//  TrackerApp
//
//  Created by Chris Ward on 4/13/14.
//  Copyright (c) 2014 Chris Ward. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface StopLocation : NSObject {
NSString    *stopName;
double    stopLatitude;
double    stopLongitude;
}

@property (nonatomic, retain) NSString *stopName;
@property (nonatomic, assign) double stopLatitude;
@property (nonatomic, assign) double stopLongitude;


@end
