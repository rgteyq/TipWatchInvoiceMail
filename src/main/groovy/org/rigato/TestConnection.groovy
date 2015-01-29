package org.rigato

import java.sql.Connection
import java.sql.DatabaseMetaData
import java.sql.Driver
import java.sql.DriverManager
import java.sql.ResultSet
import java.sql.ResultSetMetaData
import java.sql.SQLException
import java.sql.Statement

// Original version of this file was part of InterClient 2.01 examples
//
// Copyright InterBase Software Corporation, 1998.
// Written by com.inprise.interbase.interclient.r&d.PaulOstler :-)
//
// Code was modified by Roman Rokytskyy to show that Firebird JCA-JDBC driver
// does not introduce additional complexity in normal driver usage scenario.
//
// A small application to demonstrate basic, but not necessarily simple, JDBC features.
//
// Note: you will need to hardwire the path to your copy of employee.gdb
//       as well as supply a user/password in the code below at the
//       beginning of method main().
public class TestConnection {
        // Make a connection to an employee.gdb on your local machine,
        // and demonstrate basic JDBC features.
        // Notice that main() uses its own local variables rather than
        // static class variables, so it need not be synchronized.
    static Closure showSQLException

    public static void test () throws Exception
        {
            // Modify the following hardwired settings for your environment.
            // Note: localhost is a TCP/IP keyword which resolves to your local machine's IP address.
            // If localhost is not recognized, try using your local machine's name or
            // the loopback IP address 127.0.0.1 in place of localhost.
            // String databaseURL = "jdbc:firebirdsql:localhost/3050:c:/database/employee.gdb";
            // String databaseURL = "jdbc:firebirdsql:native:localhost/3050:c:/database/employee.gdb";
            // String databaseURL = "jdbc:firebirdsql:local:c:/database/employee.gdb";
            // String databaseURL = "jdbc:firebirdsql:embedded:c:/database/employee.fdb?lc_ctype=WIN1251";

            String databaseURL = "jdbc:firebirdsql:192.168.43.17/3050:C:/Binwatch/db/t9.fdb?sql_dialect=3";
            String user = "SYSDBA";
            String password = "masterkey";
            String driverName = "org.firebirdsql.jdbc.FBDriver";

            // As an exercise to the reader, add some code which extracts databaseURL,
            // user, and password from the program args[] to main().
            // As a further exercise, allow the driver name to be passed as well,
            // and modify the code below to use driverName rather than the hardwired
            // string "org.firebirdsql.jdbc.FBDriver" so that this code becomes
            // driver independent.  However, the code will still rely on the
            // predefined table structure of employee.gdb.

            // Here are the JDBC objects we're going to work with.
            // We're defining them outside the scope of the try block because
            // they need to be visible in a finally clause which will be used
            // to close everything when we are done.
            // The finally clause will be executed even if an exception occurs.
            Driver d = null;
            Connection c = null;
            Statement s = null;
            ResultSet rs = null;

            // Any return from this try block will first execute the finally clause
            // towards the bottom of this file.
            try {

                // Let's try to register the Firebird JCA-JDBC driver with the driver manager
                // using one of various registration alternatives...
                int registrationAlternative = 2;
                switch (registrationAlternative) {

                    case 1:
                        // This is the standard alternative and simply loads the driver class.
                        // Class.forName() instructs the java class loader to load
                        // and initialize a class.  As part of the class initialization
                        // any static clauses associated with the class are executed.
                        // Every driver class is required by the jdbc specification to automatically
                        // create an instance of itself and register that instance with the driver
                        // manager when the driver class is loaded by the java class loader
                        // (this is done via a static clause associated with the driver class).
                        //
                        // Notice that the driver name could have been supplied dynamically,
                        // so that an application is not hardwired to any particular driver
                        // as would be the case if a driver constructor were used, eg.
                        // new org.firebirdsql.jdbc.FBDriver().
                        try {
                            Class.forName("org.firebirdsql.jdbc.FBDriver");
                        }
                        catch (ClassNotFoundException e) {
                            // A call to Class.forName() forces us to consider this exception :-)...
                            System.out.println("Firebird JCA-JDBC driver not found in class path");
                            System.out.println(e.getMessage());
                            return;
                        }
                        break;

                    case 2:
                        // There is a bug in some JDK 1.1 implementations, eg. with Microsoft
                        // Internet Explorer, such that the implicit driver instance created during
                        // class initialization does not get registered when the driver is loaded
                        // with Class.forName().
                        // See the FAQ at http://java.sun.com/jdbc for more info on this problem.
                        // Notice that in the following workaround for this bug, that if the bug
                        // is not present, then two instances of the driver will be registered
                        // with the driver manager, the implicit instance created by the driver
                        // class's static clause and the one created explicitly with newInstance().
                        // This alternative should not be used except to workaround a JDK 1.1
                        // implementation bug.
                        try {
                            DriverManager.registerDriver(
                                    (Driver) Class.forName("org.firebirdsql.jdbc.FBDriver").newInstance()
                            );
                        }
                        catch (ClassNotFoundException e) {
                            // A call to Class.forName() forces us to consider this exception :-)...
                            System.out.println("Driver not found in class path");
                            System.out.println(e.getMessage());
                            return;
                        }
                        catch (IllegalAccessException e) {
                            // A call to newInstance() forces us to consider this exception :-)...
                            System.out.println("Unable to access driver constructor, this shouldn't happen!");
                            System.out.println(e.getMessage());
                            return;
                        }
                        catch (InstantiationException e) {
                            // A call to newInstance() forces us to consider this exception :-)...
                            // Attempt to instantiate an interface or abstract class.
                            System.out.println("Unable to create an instance of driver class, this shouldn't happen!");
                            System.out.println(e.getMessage());
                            return;
                        }
                        catch (SQLException e) {
                            // A call to registerDriver() forces us to consider this exception :-)...
                            System.out.println("Driver manager failed to register driver");
                            showSQLException(e);
                            return;
                        }
                        break;

                    case 3:
                        // Add the Firebird JCA-JDBC driver name to your system's jdbc.drivers property list.
                        // The driver manager will load drivers from this system property list.
                        // System.getProperties() may not be allowed for applets in some browsers.
                        // For applets, use one of the Class.forName() alternatives above.
                        Properties sysProps = System.getProperties();
                        StringBuffer drivers = new StringBuffer("org.firebirdsql.jdbc.FBDriver");
                        String oldDrivers = sysProps.getProperty("jdbc.drivers");
                        if (oldDrivers != null)
                            drivers.append(":" + oldDrivers);
                        sysProps.put("jdbc.drivers", drivers.toString());
                        System.setProperties(sysProps);
                        break;
//                    case 4:
                // Advanced: This is a non-standard alternative, and is tied to
                // a particular driver implementation, but is very flexible.
                //
                // It may be possible to configure a driver explicitly, either thru
                // the use of non-standard driver constructors, or non-standard
                // driver "set" methods which somehow tailor the driver to behave
                // differently from the default driver instance.
                // Under this alternative, a driver instance is created explicitly
                // using a driver specific constructor.  The driver may then be
                // tailored differently from the default driver instance which is
                // created automatically when the driver class is loaded by the java class loader.
                // For example, perhaps a driver instance could be created which
                // is to behave like some older version of the driver.
                //
                // d = new org.firebirdsql.jdbc.FBDriver ();
                // DriverManager.registerDriver (d);
                // c = DriverManager.getConnection (...);
                //
                // Since two drivers, with differing behavior, are now registered with
                // the driver manager, they presumably must recognize different jdbc
                // subprotocols.  For example, the tailored driver may only recognize
                // "jdbc:interbase:old_version://...", whereas the default driver instance
                // would recognize the standard "jdbc:interbase://...".
                // There are currently no methods, such as the hypothetical setVersion(),
                // for tailoring an Firebird JCA-JDBC driver so this 4th alternative is academic
                // and not necessary for Firebird JCA-JDBC driver.
                //
                // It is also possible to create a tailored driver instance which
                // is *not* registered with the driver manager as follows
                //
                // d = new org.firebirdsql.jdbc.FBDriver ();
                // c = d.connect (...);
                //
                // this is the most usual case as this does not require differing
                // jdbc subprotocols since the connection is obtained thru the driver
                // directly rather than thru the driver manager.
//                        d = new org.firebirdsql.jdbc.FBDriver();
                }

                // At this point the driver should be registered with the driver manager.
                // Try to find the registered driver that recognizes interbase URLs...
                try {
                    // We pass the entire database URL, but we could just pass "jdbc:interbase:"
                    d = DriverManager.getDriver(databaseURL);
                    System.out.println("Firebird JCA-JDBC driver version " +
                            d.getMajorVersion() +
                            "." +
                            d.getMinorVersion() +
                            " registered with driver manager.");
                }
                catch (SQLException e) {
                    System.out.println("Unable to find Firebird JCA-JDBC driver among the registered drivers.");
                    showSQLException(e);
                    return;
                }
            }
            // This finally clause will be executed even if "return" was called in case of any exceptions above.
            finally {
                System.out.println ("Closing database resources and rolling back any changes we made to the database.");

                // Now that we're all finished, let's release database resources.
                try { if (rs!=null) rs.close (); } catch (SQLException e) { showSQLException (e); }
                try { if (s!=null) s.close (); } catch (SQLException e) { showSQLException (e); }

                // Before we close the connection, let's rollback any changes we may have made.
                try { if (c!=null) c.rollback (); } catch (SQLException e) { showSQLException (e); }
                try { if (c!=null) c.close (); } catch (SQLException e) { showSQLException (e); }
            }
        }

        // Display an SQLException which has occured in this application.
        private static void showSQLException (SQLException e)
        {
            // Notice that a SQLException is actually a chain of SQLExceptions,
            // let's not forget to print all of them...
            SQLException next = e;
            while (next != null) {
                System.out.println (next.getMessage ());
                System.out.println ("Error Code: " + next.getErrorCode ());
                System.out.println ("SQL State: " + next.getSQLState ());
                next = next.getNextException ();
            }
        }
    }
