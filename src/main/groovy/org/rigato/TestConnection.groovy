package org.rigato

import java.sql.*;

public class TestConnection {
    public static void test() throws Exception {
            Class.forName("org.firebirdsql.jdbc.FBDriver");
            Connection connection = DriverManager.getConnection(
                    "jdbc:firebirdsql:win-hk193u7gfvi/3050:C:/Binwatch/db/t9.fdb",
                    "SYSDBA", "masterkey");
            // do something here
    }
}
