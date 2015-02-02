package org.rigato;

public class formBean {
    private String emailText;
    private String reportLocation;

    public formBean() {
    }

    public String getEmailText() {
        return emailText;
    }

    public void setEmailText(final String emailText) {
        this.emailText = emailText;
    }

    public String getReportLocation() {
        return reportLocation;
    }

    public void setReportLocation(final String reportLocation) {
        this.reportLocation = reportLocation;
    }
}