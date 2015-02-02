package org.rigato

import com.sun.org.apache.bcel.internal.generic.Select

import javax.swing.JFrame
import java.awt.Panel
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

        // Start Main Form

        JFrame frame = new JFrame("GenerateEmails");
        JPanel panel = new GenerateEmails().panel1;

        frame.setContentPane(panel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

        // Read file
        /*
        def a = new File('foobar')
        println a.text
        a.text << "append a new line"
        */

        // ui
        }
    }
