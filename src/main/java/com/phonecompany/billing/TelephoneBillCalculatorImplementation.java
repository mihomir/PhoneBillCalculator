package com.phonecompany.billing;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

public class TelephoneBillCalculatorImplementation implements TelephoneBillCalculator {

    private static final String DATE_FORMAT = "dd-MM-yyyy HH:mm:ss";
    private static final int expensiveStart = 8;
    private static final int expensiveEnd = 16;
    private static final BigDecimal expensiveDuration = new BigDecimal("5");
    private static final BigDecimal expensiveCost = BigDecimal.ONE;
    private static final BigDecimal cheapCost = new BigDecimal("0.5");
    private static final BigDecimal longCost = new BigDecimal("0.2");
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
        totalCost = phoneBillEntries.stream().filter(e -> !e.getPhoneNumber().equals(mostCommonCaller))
                .map(this::calculateCost).reduce(totalCost, BigDecimal::add);
        totalCost = totalCost.setScale(1, RoundingMode.HALF_UP);
        return totalCost;
    }

    private BigDecimal calculateCost(PhoneBillEntry callBillEntry) {

        BigDecimal callCost = BigDecimal.ZERO;
        int startHour = callBillEntry.getCallStart().getHours();
        int endHour = callBillEntry.getCallEnd().getHours();

        long durationInMs = Math.abs(callBillEntry.getCallStart().getTime() - callBillEntry.getCallEnd().getTime());
        long durationInSeconds = durationInMs / 1000;
        BigDecimal durationInMinutes = new BigDecimal(durationInSeconds);
        durationInMinutes = durationInMinutes.divide(new BigDecimal(60), RoundingMode.UP);
        BigDecimal longPartOfCall = BigDecimal.ZERO;
        if (durationInMinutes.compareTo(expensiveDuration) > 0) {
            longPartOfCall = longPartOfCall.add(durationInMinutes.subtract(expensiveDuration).multiply(longCost));
            durationInMinutes = expensiveDuration;
        }

        if (startHour >= expensiveEnd || endHour < expensiveStart) {
            callCost = callCost.add(durationInMinutes.multiply(cheapCost));
        }

        if (startHour >= expensiveStart && endHour <= expensiveEnd) {
            callCost = callCost.add(durationInMinutes.multiply(expensiveCost));
        }
        //This is assuming there are no calls longer than 24 hours
        if (startHour < expensiveStart && endHour >= expensiveStart) {
            int startMinutes = callBillEntry.getCallStart().getMinutes();
            int startSeconds = callBillEntry.getCallStart().getSeconds();
            int durationBeforeExpensiveStartInSeconds = 60 - startSeconds + (59-startMinutes)*60;
            BigDecimal cheapPartDurationInMinutes = new BigDecimal(durationBeforeExpensiveStartInSeconds);
            cheapPartDurationInMinutes = cheapPartDurationInMinutes.divide(new BigDecimal(60), RoundingMode.UP);
            callCost = callCost.add(
                    cheapPartDurationInMinutes.multiply(cheapCost)
                    .add(durationInMinutes.subtract(cheapPartDurationInMinutes).multiply(expensiveCost))
            );

//            int endMinutes = callBillEntry.getCallEnd().getMinutes();
//            int endSeconds = callBillEntry.getCallEnd().getSeconds();
        }

        return callCost.add(longPartOfCall);
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
