
class Solution {
    public int minimumChanges(String s, int k) {

        int n = s.length();
        int INF = 1000000;

        // cost[i][j] = minimum changes to make s[i...j]
        // a semi-palindrome
        int[][] cost = new int[n][n];

        for (int len = 2; len <= n; len++) {

            for (int i = 0; i + len <= n; i++) {

                int j = i + len - 1;
                int minCost = INF;

                // Try every proper divisor d of len
                for (int d = 1; d < len; d++) {

                    if (len % d != 0) {
                        continue;
                    }

                    int changes = 0;

                    // Check each group formed using step d
                    for (int r = 0; r < d; r++) {

                        int left = i + r;
                        int right = i + len - d + r;

                        while (left < right) {

                            if (s.charAt(left) != s.charAt(right)) {
                                changes++;
                            }

                            left += d;
                            right -= d;
                        }
                    }

                    minCost = Math.min(minCost, changes);
                }

                cost[i][j] = minCost;
            }
        }

        // dp[i][parts] = minimum changes to partition
        // first i characters into exactly parts substrings
        int[][] dp = new int[n + 1][k + 1];

        for (int i = 0; i <= n; i++) {
            Arrays.fill(dp[i], INF);
        }

        dp[0][0] = 0;

        for (int i = 1; i <= n; i++) {

            for (int parts = 1; parts <= k; parts++) {

                // Last substring must have length >= 2
                for (int j = i - 2; j >= 0; j--) {

                    if (dp[j][parts - 1] == INF) {
                        continue;
                    }

                    dp[i][parts] = Math.min(
                        dp[i][parts],
                        dp[j][parts - 1] + cost[j][i - 1]
                    );
                }
            }
        }

        return dp[n][k];
    }
}
