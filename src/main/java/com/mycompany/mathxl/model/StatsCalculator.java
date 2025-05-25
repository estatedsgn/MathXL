package com.mycompany.mathxl.model;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.apache.commons.math3.distribution.TDistribution;
import org.apache.commons.math3.stat.correlation.Covariance;
import java.util.*;

public class StatsCalculator {
    public static Map<String, Object> calculateAllStats(List<Double> data) {
        Map<String, Object> result = new LinkedHashMap<>();
        double[] arr = toArray(data);

        result.put("Количество", data.size());
        result.put("Минимум", Collections.min(data));
        result.put("Максимум", Collections.max(data));
        result.put("Среднее арифметическое", new org.apache.commons.math3.stat.descriptive.moment.Mean().evaluate(arr));
        result.put("Среднее геометрическое", new org.apache.commons.math3.stat.descriptive.moment.GeometricMean().evaluate(arr));
        result.put("Стандартное отклонение", new org.apache.commons.math3.stat.descriptive.moment.StandardDeviation().evaluate(arr));
        result.put("Дисперсия", new org.apache.commons.math3.stat.descriptive.moment.Variance().evaluate(arr));
        result.put("Размах", result.get("Максимум") + " - " + result.get("Минимум"));
        result.put("Коэффициент вариации", (double) result.get("Стандартное отклонение") / (double) result.get("Среднее арифметическое"));

        // Доверительный интервал
        int n = data.size();
        double mean = (double) result.get("Среднее арифметическое");
        double stdDev = (double) result.get("Стандартное отклонение");
        TDistribution tDist = new TDistribution(n - 1);
        double tValue = tDist.inverseCumulativeProbability(0.975);
        double margin = tValue * stdDev / Math.sqrt(n);
        result.put("Доверительный интервал", String.format("[%f, %f]", mean - margin, mean + margin));

        return result;
    }

    private static double[] toArray(List<Double> list) {
        double[] arr = new double[list.size()];
        for (int i = 0; i < list.size(); i++) {
            arr[i] = list.get(i);
        }
        return arr;
    }
}