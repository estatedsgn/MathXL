package com.mycompany.mathxl.model;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.apache.commons.math3.stat.correlation.Covariance;
import org.apache.commons.math3.distribution.TDistribution;
import java.util.*;

public class StatsCalculator {
    public static Map<String, Map<String, Object>> calculateAllStatsForColumns(Map<String, List<Double>> columns) {
        Map<String, Map<String, Object>> result = new LinkedHashMap<>();

        // Расчёт статистики для каждого столбца
        for (Map.Entry<String, List<Double>> entry : columns.entrySet()) {
            String columnName = entry.getKey();
            List<Double> data = entry.getValue();
            result.put(columnName, calculateSingleColumnStats(data));
        }

        // Расчёт ковариации между всеми парами столбцов
        List<String> columnNames = new ArrayList<>(columns.keySet());
        for (int i = 0; i < columnNames.size(); i++) {
            for (int j = i + 1; j < columnNames.size(); j++) {
                String col1 = columnNames.get(i);
                String col2 = columnNames.get(j);
                double covariance = calculateCovariance(columns.get(col1), columns.get(col2));
                String key = col1 + " - " + col2;
                result.get(key).put("Ковариация", covariance);
            }
        }

        return result;
    }

    private static Map<String, Object> calculateSingleColumnStats(List<Double> data) {
        Map<String, Object> stats = new LinkedHashMap<>();
        double[] arr = toArray(data);

        stats.put("Количество", data.size());
        stats.put("Минимум", Collections.min(data));
        stats.put("Максимум", Collections.max(data));
        stats.put("Среднее арифметическое", new org.apache.commons.math3.stat.descriptive.moment.Mean().evaluate(arr));
        stats.put("Среднее геометрическое", new org.apache.commons.math3.stat.descriptive.moment.GeometricMean().evaluate(arr));
        stats.put("Стандартное отклонение", new org.apache.commons.math3.stat.descriptive.moment.StandardDeviation().evaluate(arr));
        stats.put("Дисперсия", new org.apache.commons.math3.stat.descriptive.moment.Variance().evaluate(arr));
        stats.put("Размах", (double) stats.get("Максимум") - (double) stats.get("Минимум"));
        stats.put("Коэффициент вариации", (double) stats.get("Стандартное отклонение") / (double) stats.get("Среднее арифметическое"));

        int n = data.size();
        double mean = (double) stats.get("Среднее арифметическое");
        double stdDev = (double) stats.get("Стандартное отклонение");
        TDistribution tDist = new TDistribution(n - 1);
        double tValue = tDist.inverseCumulativeProbability(0.975);
        double margin = tValue * stdDev / Math.sqrt(n);
        stats.put("Доверительный интервал", String.format("[%f, %f]", mean - margin, mean + margin));

        return stats;
    }

    public static double calculateCovariance(List<Double> data1, List<Double> data2) {
        if (data1.size() != data2.size()) {
            throw new IllegalArgumentException("Длины выборок должны совпадать");
        }

        return new Covariance().covariance(toArray(data1), toArray(data2));
    }

    private static double[] toArray(List<Double> list) {
        double[] arr = new double[list.size()];
        for (int i = 0; i < list.size(); i++) {
            arr[i] = list.get(i);
        }
        return arr;
    }
}