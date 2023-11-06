package com.phonecompany.billing;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class TelephoneBillCalculatorImplementation implements TelephoneBillCalculator {

    private static final String DATE_FORMAT = "dd-MM-yyyy HH:mm:ss";
    @Override
    public BigDecimal calculate(String phoneLog) {

        List<String> callEntriesLines = List.of(phoneLog.split("\n"));
        Map<String, Integer> callFrequency = new HashMap<>();

        List<PhoneBillEntry> phoneBillEntries = new ArrayList<>();
        for (String callLogLine : callEntriesLines ) {
            PhoneBillEntry currentEntry = parseCallLogLine(callLogLine);
            phoneBillEntries.add(currentEntry);
            callFrequency.put(currentEntry.getPhoneNumber(), callFrequency.getOrDefault(currentEntry.getPhoneNumber(), 0) + 1);
        }
        return null;
    }

    private PhoneBillEntry parseCallLogLine(String callLogLine) {
        List<String> entry = List.of(callLogLine.split(","));
        SimpleDateFormat dateFormat = new SimpleDateFormat();
        dateFormat.applyPattern(DATE_FORMAT);
        try {
            return new PhoneBillEntry(entry.get(0), dateFormat.parse(entry.get(1)), dateFormat.parse(entry.get(2)));
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }
}
