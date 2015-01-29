package org.rigato

import com.sun.org.apache.bcel.internal.generic.Select

import java.sql.Connection
import groovy.sql.Sql;
/**
 * Created by L065295 on 28/01/2015.
 */
class Main {

    public static void main(String[] args) {
        // Commands: gradle tasks dependencies clean cleanIdea idea test distZip
        // http://search.maven.org/#artifactdetails%7Corg.springframework%7Cspring-test%7C4.1.4.RELEASE%7Cjar
        // SwingBuilder: http://groovy.codehaus.org/Swing+Builder

        // setup connection to TipWatch database
        // Connection connection = new TipWatch().retrieveConnection();
        def sql = Sql.newInstance( 'jdbc:firebirdsql:win-hk193u7gfvi/3050:C:/Binwatch/db/t9.fdb', 'SYSDBA', 'masterkey', 'org.firebirdsql.jdbc.FBDriver' );

        // Read files in Folder
        def custId;
        def emailList = [];
        new File('C:\\Tipwatch\\invoices_290114').eachFile {
            String fileName = it.name;
            String invoiceNo = fileName.substring(0, fileName.indexOf('_'));
            sql.execute("Select CUSTID from INVOICE where INVNO = ${invoiceNo}");
            sql.eachRow("Select * from INVOICE where INVNO = ${invoiceNo}" ) {
                emailList.add(it.CUSTID);
            }
        }
        for (int j=0;j<emailList.size();j++){
            sql.eachRow("Select * from CUSTOMER where CUSTID = ${emailList[j].toString()}"){
                println(it);
            }
        }

        // Read file
        /*
        def a = new File('foobar')
        println a.text
        a.text << "append a new line"
        */

        // ui
        }
    }
