//
//  SecondViewController.h
//  TrackerApp
//
//  Created by Chris Ward on 4/2/14.
//  Copyright (c) 2014 Chris Ward. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface SecondViewController : UIViewController  <UIPickerViewDataSource,UIPickerViewDelegate>

@property (strong, nonatomic) IBOutlet UIPickerView *picker;
@property (strong, nonatomic)          NSArray *routeArray;
@end