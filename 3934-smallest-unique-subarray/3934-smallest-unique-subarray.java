
import java.util.*;

class Solution {

    public int smallestUniqueSubarray(int[] nums) {

        int n = nums.length;
        int N = n + 1;

        // Build suffix array with a unique sentinel (0)
        int[] s = new int[N];
        System.arraycopy(nums, 0, s, 0, n);

        int[] sa = new int[N];
        int[] rank = new int[N];
        int[] temp = new int[N];
        int[] count = new int[Math.max(100001, N)];

        // Initial counting sort
        for (int x : s) {
            count[x]++;
        }

        for (int i = 1; i < count.length; i++) {
            count[i] += count[i - 1];
        }

        for (int i = N - 1; i >= 0; i--) {
            sa[--count[s[i]]] = i;
        }

        int classes = 1;
        rank[sa[0]] = 0;

        for (int i = 1; i < N; i++) {
            if (s[sa[i]] != s[sa[i - 1]]) {
                classes++;
            }
            rank[sa[i]] = classes - 1;
        }

        // Build suffix array using doubling
        for (int len = 1; len < N; len <<= 1) {

            for (int i = 0; i < N; i++) {
                temp[i] = sa[i] - len;

                if (temp[i] < 0) {
                    temp[i] += N;
                }
            }

            Arrays.fill(count, 0, classes, 0);

            for (int i = 0; i < N; i++) {
                count[rank[temp[i]]]++;
            }

            for (int i = 1; i < classes; i++) {
                count[i] += count[i - 1];
            }

            for (int i = N - 1; i >= 0; i--) {
                int x = temp[i];
                sa[--count[rank[x]]] = x;
            }

            int[] newRank = new int[N];
            int newClasses = 1;

            newRank[sa[0]] = 0;

            for (int i = 1; i < N; i++) {

                int cur = sa[i];
                int prev = sa[i - 1];

                int mid1 = (cur + len) % N;
                int mid2 = (prev + len) % N;

                if (rank[cur] != rank[prev] ||
                    rank[mid1] != rank[mid2]) {
                    newClasses++;
                }

                newRank[cur] = newClasses - 1;
            }

            rank = newRank;
            classes = newClasses;

            if (classes == N) {
                break;
            }
        }

        // Build inverse suffix array
        int[] pos = new int[N];

        for (int i = 0; i < N; i++) {
            pos[sa[i]] = i;
        }

        // Kasai's algorithm to calculate LCP
        int[] lcp = new int[N];
        int k = 0;

        for (int i = 0; i < N; i++) {

            int r = pos[i];

            if (r == 0) {
                continue;
            }

            int j = sa[r - 1];

            while (i + k < N &&
                   j + k < N &&
                   s[i + k] == s[j + k]) {
                k++;
            }

            lcp[r] = k;

            if (k > 0) {
                k--;
            }
        }

        // Find shortest unique subarray
        int ans = n;

        for (int i = 0; i < n; i++) {

            int r = pos[i];

            int maxLcp = 0;

            // Previous suffix
            if (r > 0) {
                maxLcp = Math.max(maxLcp, lcp[r]);
            }

            // Next suffix
            if (r + 1 < N) {
                maxLcp = Math.max(maxLcp, lcp[r + 1]);
            }

            int uniqueLength = maxLcp + 1;

            if (uniqueLength <= n - i) {
                ans = Math.min(ans, uniqueLength);
            }
        }

        return ans;
    }
}
