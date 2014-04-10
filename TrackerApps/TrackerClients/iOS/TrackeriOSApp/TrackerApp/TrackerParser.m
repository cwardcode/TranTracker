//
//  TrackerParser.m
//  TrackerApp
//
//  Created by Chris Ward on 4/10/14.
//  Copyright (c) 2014 Chris Ward. All rights reserved.
//

#import "TrackerParser.h"

@implementation TrackerParser
@synthesize shuttles = shuttles;

-(id) loadXMLByURL:(NSString *)url
{
    shuttles        = [[NSMutableArray alloc]   init];
    NSURL   *xmlURL = [NSURL URLWithString:url];
    NSData  *data   = [[NSData alloc] initWithContentsOfURL:xmlURL];
    parser          =[[NSXMLParser alloc] initWithData:data];
    parser.delegate = self;
    [parser parse];

    return self;
}

- (void) parser:(NSXMLParser *)parser foundCharacters:(NSString *)string
{
    currentNode = (NSMutableString *) [string stringByTrimmingCharactersInSet:[NSCharacterSet whitespaceAndNewlineCharacterSet]];
}

- (void) parser:(NSXMLParser *)parser didStartElement:(NSString *)elementName namespaceURI:(NSString *)namespaceURI qualifiedName:(NSString *)qName attributes:(NSDictionary *)attributeDict
{
    if ([elementName isEqualToString:@"marker"]) {
        shuttle = [Shuttle alloc];
        isShuttle  = true;
    }
    
}

-(void) parser:(NSXMLParser *)parser didEndElement:(NSString *)elementName namespaceURI:(NSString *)namespaceURI qualifiedName:(NSString *)qName
{
    if(isShuttle) {
        if([elementName isEqualToString:@"VID"])
        {
            shuttle.VID = currentNode;
        }
        if([elementName isEqualToString:@"title"])
        {
            shuttle.title = currentNode;
        }
        if([elementName isEqualToString:@"latitude"])
        {
            shuttle.lat = [currentNode doubleValue];
        }
        if([elementName isEqualToString:@"longitude"])
        {
            shuttle.lng = [currentNode doubleValue];
        }
        if([elementName isEqualToString:@"speed"])
        {
            shuttle.speed = [currentNode doubleValue];
        }
    }
    if ([elementName isEqualToString:@"marker"]) {
        [shuttles addObject:shuttle];
        shuttle = nil;
        currentNode = nil;
    }
}
@end
