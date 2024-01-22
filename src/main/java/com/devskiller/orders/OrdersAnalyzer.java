package com.devskiller.orders;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrdersAnalyzer {

    static class Order {
        LocalDateTime creationDate;
        List<OrderLine> orderLines;
    }

    static class OrderLine {
        int quantity;
        BigDecimal unitPrice;
    }

    public static Map<DayOfWeek, Integer> averageDailySales(List<Order> orders) {
        Map<DayOfWeek, BigDecimal> totalValuesByDay = new HashMap<>();
        Map<DayOfWeek, Integer> orderCountByDay = new HashMap<>();

        // Calculate total values and order counts for each day
        for (Order order : orders) {
            DayOfWeek dayOfWeek = order.creationDate.getDayOfWeek();
            BigDecimal totalValue = order.orderLines.stream()
                    .map(orderLine -> orderLine.unitPrice.multiply(BigDecimal.valueOf(orderLine.quantity)))
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            totalValuesByDay.merge(dayOfWeek, totalValue, BigDecimal::add);
            orderCountByDay.merge(dayOfWeek, 1, Integer::sum);
        }

        // Calculate average values and round to the nearest integer
        Map<DayOfWeek, Integer> averageValuesByDay = new HashMap<>();

        for (DayOfWeek dayOfWeek : DayOfWeek.values()) {
            BigDecimal totalValue = totalValuesByDay.getOrDefault(dayOfWeek, BigDecimal.ZERO);
            int orderCount = orderCountByDay.getOrDefault(dayOfWeek, 0);

            BigDecimal averageValue = orderCount > 0
                    ? totalValue.divide(BigDecimal.valueOf(orderCount), 0, BigDecimal.ROUND_HALF_UP)
                    : BigDecimal.ZERO; // Default value if orderCount is 0

            averageValuesByDay.put(dayOfWeek, averageValue.intValue());
        }
        return averageValuesByDay;
    }
}
