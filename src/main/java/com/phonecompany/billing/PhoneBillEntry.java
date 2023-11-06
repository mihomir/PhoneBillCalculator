package com.phonecompany.billing;

import java.sql.Timestamp;
import java.util.Date;

public class PhoneBillEntry {
    private String phoneNumber;
    private Date callStart;
    private Date callEnd;

    public PhoneBillEntry(String phoneNumber, Date callStart, Date callEnd) {
        this.phoneNumber = phoneNumber;
        this.callStart = callStart;
        this.callEnd = callEnd;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Date getCallStart() {
        return callStart;
    }

    public void setCallStart(Timestamp callStart) {
        this.callStart = callStart;
    }

    public Date getCallEnd() {
        return callEnd;
    }

    public void setCallEnd(Timestamp callEnd) {
        this.callEnd = callEnd;
    }
}
