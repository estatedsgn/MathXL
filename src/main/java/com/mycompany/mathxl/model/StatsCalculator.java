/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author nyaku
 */
package com.mycompany.mathxl.model; // Изменено

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.apache.commons.math3.stat.correlation.Covariance;
import org.apache.commons.math3.distribution.TDistribution;
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
