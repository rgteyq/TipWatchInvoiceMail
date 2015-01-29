package org.rigato

import groovy.sql.Sql

/**
 * Created by L065295 on 28/01/2015.
 */
class Main {

    public static void main(String[] args) {
        // Commands: gradle tasks dependencies clean cleanIdea idea test distZip

        // http://search.maven.org/#artifactdetails%7Corg.springframework%7Cspring-test%7C4.1.4.RELEASE%7Cjar

        // SwingBuilder: http://groovy.codehaus.org/Swing+Builder

        // Read files in Folder
        new File('/Users/eddyr').eachFile { 
            println it
        }

        // Read file
        /*
        def a = new File('foobar')
        println a.text
        a.text << "append a new line"
        */

        // database
        new TestConnection().test();

        def sql = Sql.newInstance("jdbc:firebirdsql:192.168.43.17/3050:C:/Binwatch/db/t9.fdb")
        sql.eachRow("select * from foo".toString()){ row ->
            println row.column_name
        }

        // ui
        }
    }
