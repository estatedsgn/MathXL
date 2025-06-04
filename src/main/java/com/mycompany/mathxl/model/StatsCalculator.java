package com.mycompany.mathxl.model;

import org.apache.commons.math3.stat.correlation.Covariance;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.apache.commons.math3.stat.descriptive.moment.Mean;
import org.apache.commons.math3.stat.descriptive.moment.StandardDeviation;
import org.apache.commons.math3.stat.descriptive.moment.Variance;
import org.apache.commons.math3.stat.descriptive.rank.Max;
import org.apache.commons.math3.stat.descriptive.rank.Min;
import org.apache.commons.math3.distribution.TDistribution;

import java.util.*;

public class StatsCalculator {
    public static Map<String, Object> calculateSingleColumnStats(String columnName, List<Double> data) {
        Map<String, Object> stats = new LinkedHashMap<>();
        double[] arr = toArray(data);

        int n = data.size();
        if (n == 0) {
            return stats;
        }

        stats.put(columnName + " Количество", n);
        stats.put(columnName + " Минимум", new Min().evaluate(arr));
        stats.put(columnName + " Максимум", new Max().evaluate(arr));
        stats.put(columnName + " Среднее арифметическое", new Mean().evaluate(arr));
        stats.put(columnName + " Среднее геометрическое", calculateGeometricMean(data));
        stats.put(columnName + " Стандартное отклонение", new StandardDeviation().evaluate(arr));
        stats.put(columnName + " Дисперсия", new Variance().evaluate(arr));
        stats.put(columnName + " Размах", (double) stats.get(columnName + " Максимум") - (double) stats.get(columnName + " Минимум"));
        
            double mean = (double) stats.get(columnName + " Среднее арифметическое");
            double stdDev = (double) stats.get(columnName + " Стандартное отклонение");
        if (n > 1) {
            TDistribution tDist = new TDistribution(n - 1);
            double tValue = tDist.inverseCumulativeProbability(0.975);
            double margin = tValue * stdDev / Math.sqrt(n);
            stats.put(columnName + " Доверительный интервал", String.format("[%f, %f]", mean - margin, mean + margin));
        } else {
            stats.put(columnName + " Доверительный интервал", "Недостаточно данных");
        }

        if (n > 0 && stdDev > 0) {
            stats.put(columnName + " Коэффициент вариации", stdDev / mean);
        } else {
            stats.put(columnName + " Коэффициент вариации", "Недостаточно данных");
        }

        return stats;
    }

    public static double calculateCovariance(List<Double> data1, List<Double> data2) {
        if (data1.size() != data2.size()) {
            throw new IllegalArgumentException("Длины выборок должны совпадать");
        }

        if (data1.size() < 2) {
            throw new IllegalArgumentException("Недостаточно данных для расчёта ковариации");
        }

        return new Covariance().covariance(toArray(data1), toArray(data2));
    }

    private static double calculateGeometricMean(List<Double> data) {
        double product = 1.0;
        for (Double value : data) {
            product *= value;
        }
        return Math.pow(product, 1.0 / data.size());
    }

    private static double[] toArray(List<Double> list) {
        double[] arr = new double[list.size()];
        for (int i = 0; i < list.size(); i++) {
            arr[i] = list.get(i);
        }
        return arr;
    }
}