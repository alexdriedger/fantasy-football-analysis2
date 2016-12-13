package com.company;

import org.apache.commons.lang3.SystemUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static com.company.ScheduleGenerator.getPossibleWeeks;

/**
 * Time Tester
 */
public class TimeTester {

    public static void main(String[] args) {

        int timesToRun = 6;
        int numTeams = 12;

        List<Long> performanceTimems = new ArrayList<>();

        for (int i = 0; i < timesToRun; i++) {
            System.out.println("Working on run " + i + " of " + timesToRun);
            long startTime = System.currentTimeMillis();
            Set<Set<ImmutablePair<Integer, Integer>>> weeks = ScheduleGenerator.getPossibleWeeks(numTeams);

            int product = 1;
            for (int j = numTeams - 1; j > 0; j = j - 2) {
                product *= j;
            }
            assert weeks.size() == product;

            performanceTimems.add(System.currentTimeMillis() - startTime);
        }

        System.out.println("Test was run " + timesToRun + " times\n" +
                "Test was run for " + numTeams + " teams\n" +
                "All times were " + performanceTimems + "\n" +
                "Average time was " + performanceTimems.stream().mapToInt(Long::intValue).sum() / timesToRun);
    }
}
