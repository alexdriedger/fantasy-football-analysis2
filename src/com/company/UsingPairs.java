package com.company;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.paukov.combinatorics3.Generator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static com.company.ScheduleGenerator.getPossibleMatchups;
import static com.company.ScheduleGenerator.getPossibleWeeks;

public class UsingPairs {

    public static void main(String[] args) {

        final int numTeams = 10;

        final long startTime = System.currentTimeMillis();

        // Create all possible combination of matchups of the two teams
        Set<ImmutablePair<Integer, Integer>> matchups = ScheduleGenerator.getPossibleMatchups(numTeams);
        matchups.forEach(System.out::println);

        int sum = 0;
        for (int i = numTeams - 1; i > 0; i--) {
            sum += i;
        }
        assert matchups.size() == sum;

        System.out.println("There were " + matchups.size() + " possible matchups\n");

        // Create all possible combinations of weeks
        Set<Set<ImmutablePair<Integer, Integer>>> weeks = getPossibleWeeks(numTeams);
        weeks.forEach(System.out::println);

        int product = 1;
        for (int i = numTeams - 1; i > 0; i = i - 2) {
            product *= i;
        }
        assert weeks.size() == product;

        System.out.println("There were " + weeks.size() + " possible weeks\n");

        System.out.println("Creating all the matchups and weeks took " +
                (System.currentTimeMillis() - startTime)  + " milliseconds!");
    }
}
