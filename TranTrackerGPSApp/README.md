Author: Chris Ward

Version: 08/31/2013

TranTracker
===========

An Android-based GPS Transmitter applicaiton. On start, it queries the MySQL database for possible trackable vehicles. The app uses a BroadcastReceiver to retreive the location of an android device, then sends the location to an external database server. The app also runs as a service to allow the program to run in the background even if the app loses focus.

TODO:
=====
1] Secure MySQL Communications by implementing SSL connection through JDBC
