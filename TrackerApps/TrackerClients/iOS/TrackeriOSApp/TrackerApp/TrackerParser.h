//
//  TrackerParser.h
//  TrackerApp
//
//  Created by Chris Ward on 4/10/14.
//  Copyright (c) 2014 Chris Ward. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "Shuttle.h"
@interface TrackerParser : NSObject <NSXMLParserDelegate>
{
    @protected
    NSMutableString *currentNode;
    NSXMLParser     *parser;
    Shuttle         *shuttle;
    bool            isShuttle;
    @public
    NSMutableArray  *shuttles;

}

@property (readonly, retain) NSMutableArray *shuttles;
-(id)   loadXMLByURL:(NSString *)url;

@end
