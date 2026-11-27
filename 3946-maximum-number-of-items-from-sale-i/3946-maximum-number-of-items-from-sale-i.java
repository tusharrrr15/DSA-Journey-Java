
import java.util.*;

class Solution {
    public int maximumSaleItems(int[][] items, int budget) {

        int n = items.length;
        int[] gain = new int[n];

        // Calculate benefit of each item
        for (int i = 0; i < n; i++) {

            gain[i] = 1;

            for (int j = 0; j < n; j++) {

                if (i != j &&
                    items[j][0] % items[i][0] == 0) {

                    gain[i]++;
                }
            }
        }

        // Sort items by price in descending order
        Integer[] order = new Integer[n];

        for (int i = 0; i < n; i++) {
            order[i] = i;
        }

        Arrays.sort(order, (a, b) ->
            Integer.compare(items[b][1], items[a][1])
        );

        int NEG = -1000000000;

        // DP for items with higher prices
        int[] dp = new int[budget + 1];
        Arrays.fill(dp, NEG);
        dp[0] = 0;

        int ans = 0;
        int idx = 0;

        while (idx < n) {

            int price = items[order[idx]][1];

            // At least one item of current price must be selected
            int[] with = new int[budget + 1];
            Arrays.fill(with, NEG);

            int end = idx;

            // Process all items having the same price
            while (end < n &&
                   items[order[end]][1] == price) {

                int id = order[end];

                // 0/1 knapsack
                for (int c = budget; c >= price; c--) {

                    if (dp[c - price] != NEG) {
                        with[c] = Math.max(
                            with[c],
                            dp[c - price] + gain[id]
                        );
                    }

                    if (with[c - price] != NEG) {
                        with[c] = Math.max(
                            with[c],
                            with[c - price] + gain[id]
                        );
                    }
                }

                end++;
            }

            // Calculate answer using remaining budget
            for (int c = 0; c <= budget; c++) {

                if (with[c] != NEG) {

                    int extra = (budget - c) / price;

                    ans = Math.max(
                        ans,
                        with[c] + extra
                    );
                }
            }

            // Add current price group to normal DP
            for (int k = idx; k < end; k++) {

                int id = order[k];

                for (int c = budget; c >= price; c--) {

                    if (dp[c - price] != NEG) {
                        dp[c] = Math.max(
                            dp[c],
                            dp[c - price] + gain[id]
                        );
                    }
                }
            }

            idx = end;
        }

        return ans;
    }
}
