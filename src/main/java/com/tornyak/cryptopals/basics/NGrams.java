package com.tornyak.cryptopals.basics;

import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Scanner;

public class NGrams {
    private Map<Integer, Map<String, Long>> nGramCounts = new HashMap<>();
    private Map<Integer, Double> totalCounts = new HashMap<>();

    public static NGrams loadFromFiles(Path... paths) throws IOException {
        NGrams result = new NGrams();
        for (Path path : paths) {
            result.loadFromFile(path);
        }
        return result;
    }

    public void loadFromFile(Path path) throws IOException {
        try (Scanner scanner = new Scanner(path)) {

            int n = getNgramLengthFromHeader(scanner);

            Map<String, Long> countMap = nGramCounts.computeIfAbsent(n, i -> new HashMap<>());
            double totalCount = 0;

            scanner.nextLine();
            while (scanner.hasNext()) {
                String ngram = scanner.next();
                long count = scanner.nextLong();
                totalCount += count;
                countMap.merge(ngram, count, Long::sum);
                scanner.nextLine();
            }

            totalCounts.merge(n, totalCount, Double::sum);
        }
    }

    private int getNgramLengthFromHeader(Scanner scanner) {
        String next = scanner.next();
        return Integer.parseInt(next.substring(0, 1));
    }

    public double getFrequency(String s) {
        if (Objects.isNull(s)) {
            throw new IllegalArgumentException("s is null");
        } else if (s.isBlank()) {
            return 0;
        }

        int n = s.length();
        Map<String, Long> countMap = nGramCounts.get(n);
        if (countMap == null) {
            return 0;
        }

        return countMap.getOrDefault(s.toUpperCase(), 0L) / totalCounts.get(n);
    }
}
