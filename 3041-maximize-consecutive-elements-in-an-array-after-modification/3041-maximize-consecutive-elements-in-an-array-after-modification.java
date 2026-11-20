
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

class Solution {
    public int maxSelectedElements(int[] nums) {

        Arrays.sort(nums);

        Map<Integer, Integer> dp = new HashMap<>();

        int ans = 0;

        for (int num : nums) {

            int len = dp.getOrDefault(num - 1, 0) + 1;

            int increasedLen = dp.getOrDefault(num, 0) + 1;

            dp.put(num, Math.max(dp.getOrDefault(num, 0), len));

            dp.put(num + 1, Math.max(
                dp.getOrDefault(num + 1, 0),
                increasedLen
            ));

            ans = Math.max(ans, Math.max(len, increasedLen));
        }

        return ans;
    }
}
