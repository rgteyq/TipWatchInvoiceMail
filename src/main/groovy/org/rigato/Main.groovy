package org.rigato

import com.sun.org.apache.bcel.internal.generic.Select

import javax.swing.JFrame
import java.sql.Connection
import groovy.sql.Sql;
import javax.swing.*;

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

        // Start Main Form

        JFrame frame = new JFrame("GenerateEmails");
        frame.setContentPane(new GenerateEmails().panel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);


        // Read files in Folder
        String custId;
        def emailList = [];
        new File('C:\\Tipwatch\\invoices_290114').eachFile {
            String fileName = it.name;
            String invoiceNo = fileName.substring(0, fileName.indexOf('_'));
            sql.eachRow("Select * from INVOICE where INVNO = ${invoiceNo}" ) {
                custId = it.CUSTID;
                emailList.add(custId)
            }
        }
        for (int j=0;j<emailList.size();j++){
            Email email = new Email();
            String toAddress = email.getSendToAddress(emailList[j].toString(), sql);
            println(toAddress)
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
