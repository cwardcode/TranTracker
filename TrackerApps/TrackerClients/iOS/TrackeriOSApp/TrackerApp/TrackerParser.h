//
//  TrackerParser.h
//  TrackerApp
//
//  Created by Chris Ward on 4/10/14.
//  Copyright (c) 2014 Chris Ward. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "Shuttle.h"
#import "StopLocation.h"

@interface TrackerParser : NSObject <NSXMLParserDelegate>
{
    @protected
    NSMutableString *currentNode;
    NSXMLParser     *parser;
    Shuttle         *shuttle;
    StopLocation    *stopLoc;
    bool            isShuttle;
    bool            isStopLoc;
    
    @public
    NSMutableArray  *shuttles;
    NSMutableArray  *stopLocs;

}

@property (readonly, retain) NSMutableArray *shuttles;
@property (readonly, retain) NSMutableArray *stopLocs;

-(id)   loadXMLByURL:(NSString *)url;

@end
