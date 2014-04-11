//
//  SecondViewController.m
//  TrackerApp
//
//  Created by Chris Ward on 4/2/14.
//  Copyright (c) 2014 Chris Ward. All rights reserved.
//

#import "FirstViewController.h"
#import "SecondViewController.h"


@interface SecondViewController ()

@end

@implementation SecondViewController

- (void)viewDidLoad
{
    [super viewDidLoad];
    self.routeArray = [[NSArray alloc] initWithObjects:@"All-Campus", @"Village Express", @"HHS Express", @"Off-Campus North", @"Off-Campus South",nil];

}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}


-(NSString *)pickerView:(UIPickerView *)pickerView titleForRow:(NSInteger)row   forComponent:(NSInteger)component
{
    
    return [self.routeArray objectAtIndex:row];
    
}


- (void)pickerView:(UIPickerView *)pickerView didSelectRow:(NSInteger)row   inComponent:(NSInteger)component{
    
    NSLog(@"Selected Row %ld", (long)row);
    switch(row)
    {
            
        case 0:
            NSLog(@"All-Campus Sel'd");
            [FirstViewController setRouteID:0];
            break;
        case 1:
            NSLog(@"Village Sel'd");
            [FirstViewController setRouteID:1];
            break;
        case 2:
            NSLog(@"HHS Exp Sel'd");
            [FirstViewController setRouteID:2];
            break;
        case 3:
            NSLog(@"OCN Sel'd");
            [FirstViewController setRouteID:3];
            break;
        case 4:
            NSLog(@"OCS Sel'd");
            [FirstViewController setRouteID:4];
            break;
    }
    
}


// returns the number of 'columns' to display.
- (NSInteger)numberOfComponentsInPickerView:(UIPickerView *)pickerView
{
    //Only need one column
    return 1;
}

// returns the # of rows in each component..
- (NSInteger)pickerView:(UIPickerView *)pickerView numberOfRowsInComponent: (NSInteger)component
{
    //One row for each route
    return 5;
}


@end
