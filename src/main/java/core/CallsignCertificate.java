package core;

public class CallsignCertificate {
    private String issueDate;
    private String endDate;
    private String certificateSerialNumber;
    private String callSign;
    private String radioModel;
    private String radioSerialNumber;

    public void setIssueDate(String issueDate) {
        this.issueDate = issueDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public void setCertificateSerialNumber(String certificateSerialNumber) {
        this.certificateSerialNumber = certificateSerialNumber;
    }

    public void setCallSign(String callSign) {
        this.callSign = callSign;
    }

    public void setRadioModel(String radioModel) {
        this.radioModel = radioModel;
    }

    public void setRadioSerialNumber(String radioSerialNumber) {
        this.radioSerialNumber = radioSerialNumber;
    }

    @Override
    public String toString() {
        return String.format("%nРЭС сухопутной подвижной службы, %s, %s, %s, %s от %s", radioModel, radioSerialNumber, callSign, certificateSerialNumber, issueDate);
    }
}
