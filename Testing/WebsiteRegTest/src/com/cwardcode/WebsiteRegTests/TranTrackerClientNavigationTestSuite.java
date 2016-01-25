package com.cwardcode.WebsiteRegTests;

import junit.framework.Test;
import junit.framework.TestSuite;

public class TranTrackerClientNavigationTestSuite {

    public static Test suite() {
        TestSuite suite = new TestSuite();
        suite.addTestSuite(TranTrackerClientNavigationTestCase.class);
        return suite;
    }

    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }
}
