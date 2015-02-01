package org.rigato

import groovy.sql.Sql

/**
 * Created by Eddy Rigato on 1/30/2015.
 */
public class Email {
    public String getSendToAddress (String custId, Sql sql) {
        String email
        sql.eachRow("Select * from CUSTOMER where CUSTID = ${custId}") {
            email = it.EMAIL;
        }
        return (email);
    }
}