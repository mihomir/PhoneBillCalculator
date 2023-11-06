package com.phonecompany.billing;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

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
        BigDecimal totalCost = new BigDecimal(0);
        String mostCommonCaller = findMostCommonCaller(callFrequency);
        totalCost = phoneBillEntries.stream().filter(e -> e.getPhoneNumber().equals(mostCommonCaller))
                .map(this::calculateCost).reduce(totalCost, BigDecimal::add);
        return totalCost;
    }

    private BigDecimal calculateCost(PhoneBillEntry callBillEntry) {
        return new BigDecimal(0);
    }

    private String findMostCommonCaller(Map<String, Integer> callFrequency) {
        Optional<Integer> max = callFrequency.values().stream().max(Integer::compareTo);
        if (max.isEmpty()) {
            throw new RuntimeException("No Calls detected");
        }
        Set<String> mostCommonPhoneNumbers =
                callFrequency.entrySet().stream()
                        .filter(e -> e.getValue().equals(max.get()))
                        .map(Map.Entry::getKey).collect(Collectors.toSet());
        if (mostCommonPhoneNumbers.isEmpty()) {
            throw new RuntimeException();
        }
        return mostCommonPhoneNumbers.stream().max(String::compareTo).get();
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
