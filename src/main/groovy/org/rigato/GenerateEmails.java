package org.rigato;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;

/**
 * Created by Eddy Rigato on 2/1/2015.
 */
public class GenerateEmails {
    private JTabbedPane tabbedPane1;
    private JPanel panel1;
    private JButton generateEmailButton;
    private JTextArea emailText;
    private JButton selectReportButton;
    private JTextField reportLocation;
    private JButton selectInvoiceDirButton;
    private JTextField selectInvoiceDir;
    private JTextField serverLocation;
    private JTextField dataBasePath;
    public String generatePressed;

    public GenerateEmails() {
        generateEmailButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new ReadDirectory().retrieveFiles(dataBasePath.getText(),serverLocation.getText(),emailText.getText(),reportLocation.getText(),selectInvoiceDir.getText());
            }
        });

        selectReportButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                int returnValue = fileChooser.showOpenDialog(null);
                if (returnValue == JFileChooser.APPROVE_OPTION)
                {
                    File selectedFile = fileChooser.getSelectedFile();
                    reportLocation.setText(selectedFile.getAbsolutePath());
                }

            }
        });
        selectInvoiceDirButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
               JFileChooser folderChooser = new JFileChooser();
                folderChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                int returnValue = folderChooser.showOpenDialog(null);
                if (returnValue == JFileChooser.APPROVE_OPTION)
                {
                    File selectedFile = folderChooser.getSelectedFile();
                    selectInvoiceDir.setText(selectedFile.getAbsolutePath());
                }

            }
        });
    }

    public void setData(formBean data) {
        emailText.setText(data.getEmailText());
        reportLocation.setText(data.getReportLocation());
    }

    public void getData(formBean data) {
        data.setEmailText(emailText.getText());
        data.setReportLocation(reportLocation.getText());
    }

    public boolean isModified(formBean data) {
        if (emailText.getText() != null ? !emailText.getText().equals(data.getEmailText()) : data.getEmailText() != null)
            return true;
        if (reportLocation.getText() != null ? !reportLocation.getText().equals(data.getReportLocation()) : data.getReportLocation() != null)
            return true;
        return false;
    }
}
