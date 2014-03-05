import junit.framework.Test;
import junit.framework.TestSuite;

public class Admin_tests {

  public static Test suite() {
    TestSuite suite = new TestSuite();
    suite.addTestSuite(Navigation.class);
    suite.addTestSuite(Auth > Groups: Search.class);
    suite.addTestSuite(User Search.class);
    suite.addTestSuite(Add/Delete User.class);
    suite.addTestSuite(Add/Delete Vehicle.class);
    suite.addTestSuite(Add/Delete Stop.class);
    return suite;
  }

  public static void main(String[] args) {
    junit.textui.TestRunner.run(suite());
  }
}
