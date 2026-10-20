package com.motorny;

/**
 * Standalone comparison of Greedy vs Hungarian assignment algorithms.
 * No Spring context, no database — pure algorithmic comparison.
 *
 * Cost matrix represents cost(courier_i, order_j) values
 * calculated using the same formula as AssignmentServiceImpl:
 * cost = α·distance + β·workload + γ·(1/priority) - δ·rating
 */
public class AlgorithmComparisonTest {

    // Same coefficients as AssignmentServiceImpl
    private static final double ALPHA = 0.5;
    private static final double BETA  = 0.3;
    private static final double GAMMA = 0.2;
    private static final double DELTA = 0.1;

    public static void main(String[] args) {
        System.out.println("=== Greedy vs Hungarian Algorithm Comparison ===\n");

        // Test case: 4 couriers x 4 orders
        // Matrix rows = couriers, columns = orders
        // Values calculated via cost formula
        double[][] costMatrix = {
            // order1  order2  order3  order4
            {  2.50,   3.10,   1.80,   2.90 },  // Courier 1 (Nastya,  rating=3.4, workload=5)
            {  2.20,   1.95,   3.40,   2.10 },  // Courier 2 (Vika,    rating=4.1, workload=6)
            {  1.66,   2.30,   1.66,   3.10 },  // Courier 3 (Katya,   rating=0.0, workload=1)
            {  3.10,   1.70,   2.80,   1.90 },  // Courier 4 (Veronika,rating=0.0, workload=1)
        };

        String[] courierNames = {"Nastya", "Vika", "Katya", "Veronika"};
        String[] orderNames   = {"Order A", "Order B", "Order C", "Order D"};

        System.out.println("--- Cost Matrix ---");
        printMatrix(costMatrix, courierNames, orderNames);

        // Run Greedy
        long greedyStart = System.currentTimeMillis();
        int[] greedyResult = greedy(costMatrix);
        long greedyTime = System.currentTimeMillis() - greedyStart;

        System.out.println("\n--- Greedy Result ---");
        double greedyTotal = printResult(greedyResult, costMatrix, courierNames, orderNames);
        System.out.printf("Total cost: %.3f | Time: %d ms%n", greedyTotal, greedyTime);

        // Run Hungarian
        long hungarianStart = System.currentTimeMillis();
        int[] hungarianResult = hungarian(costMatrix);
        long hungarianTime = System.currentTimeMillis() - hungarianStart;

        System.out.println("\n--- Hungarian Result ---");
        double hungarianTotal = printResult(hungarianResult, costMatrix, courierNames, orderNames);
        System.out.printf("Total cost: %.3f | Time: %d ms%n", hungarianTotal, hungarianTime);

        // Summary
        System.out.println("\n--- Summary ---");
        System.out.printf("Greedy   total cost: %.3f | time: %d ms%n", greedyTotal, greedyTime);
        System.out.printf("Hungarian total cost: %.3f | time: %d ms%n", hungarianTotal, hungarianTime);
        double improvement = (greedyTotal - hungarianTotal) / greedyTotal * 100;
        System.out.printf("Hungarian improvement: %.1f%%%n", improvement);
    }

    // -------------------------------------------------------------------------
    // Greedy: for each order pick the courier with minimum cost
    // -------------------------------------------------------------------------
    private static int[] greedy(double[][] cost) {
        int n = cost.length;
        int[] assignment = new int[n]; // assignment[courier] = order
        boolean[] orderTaken = new boolean[n];

        for (int courier = 0; courier < n; courier++) {
            double minCost = Double.MAX_VALUE;
            int bestOrder = -1;
            for (int order = 0; order < n; order++) {
                if (!orderTaken[order] && cost[courier][order] < minCost) {
                    minCost = cost[courier][order];
                    bestOrder = order;
                }
            }
            assignment[courier] = bestOrder;
            if (bestOrder != -1) orderTaken[bestOrder] = true;
        }
        return assignment;
    }

    // -------------------------------------------------------------------------
    // Hungarian algorithm (Kuhn-Munkres) — finds globally optimal assignment
    // -------------------------------------------------------------------------
    private static int[] hungarian(double[][] originalCost) {
        int n = originalCost.length;
        double[][] cost = new double[n][n];
        for (int i = 0; i < n; i++)
            cost[i] = originalCost[i].clone();

        double[] u = new double[n + 1];
        double[] v = new double[n + 1];
        int[] p = new int[n + 1];
        int[] way = new int[n + 1];

        for (int i = 1; i <= n; i++) {
            p[0] = i;
            int j0 = 0;
            double[] minVal = new double[n + 1];
            boolean[] used = new boolean[n + 1];
            java.util.Arrays.fill(minVal, Double.MAX_VALUE);

            do {
                used[j0] = true;
                int i0 = p[j0], j1 = -1;
                double delta = Double.MAX_VALUE;

                for (int j = 1; j <= n; j++) {
                    if (!used[j]) {
                        double cur = cost[i0 - 1][j - 1] - u[i0] - v[j];
                        if (cur < minVal[j]) {
                            minVal[j] = cur;
                            way[j] = j0;
                        }
                        if (minVal[j] < delta) {
                            delta = minVal[j];
                            j1 = j;
                        }
                    }
                }

                for (int j = 0; j <= n; j++) {
                    if (used[j]) { u[p[j]] += delta; v[j] -= delta; }
                    else minVal[j] -= delta;
                }
                j0 = j1;
            } while (p[j0] != 0);

            do {
                int j1 = way[j0];
                p[j0] = p[j1];
                j0 = j1;
            } while (j0 != 0);
        }

        int[] result = new int[n];
        for (int j = 1; j <= n; j++)
            if (p[j] != 0) result[p[j] - 1] = j - 1;

        return result;
    }

    // -------------------------------------------------------------------------
    // Helpers
    // -------------------------------------------------------------------------
    private static void printMatrix(double[][] matrix, String[] rows, String[] cols) {
        System.out.printf("%-12s", "");
        for (String col : cols) System.out.printf("%-10s", col);
        System.out.println();
        for (int i = 0; i < rows.length; i++) {
            System.out.printf("%-12s", rows[i]);
            for (double val : matrix[i]) System.out.printf("%-10.3f", val);
            System.out.println();
        }
    }

    private static double printResult(int[] assignment, double[][] cost,
                                      String[] couriers, String[] orders) {
        double total = 0;
        for (int i = 0; i < assignment.length; i++) {
            int j = assignment[i];
            if (j >= 0) {
                System.out.printf("  %s → %s (cost=%.3f)%n", couriers[i], orders[j], cost[i][j]);
                total += cost[i][j];
            }
        }
        return total;
    }
}
