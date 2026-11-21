class Solution {
    public int[] findSubtreeSizes(int[] parent, String s) {
        int n = parent.length;

        // Build children lists using arrays
        int[] head = new int[n];
        int[] next = new int[n];
        java.util.Arrays.fill(head, -1);
        for (int i = 1; i < n; i++) {
            next[i] = head[parent[i]];
            head[parent[i]] = i;
        }

        int[] newParent = new int[n];
        newParent[0] = -1;
        int[] order = new int[n];      // pre-order visit sequence
        int cnt = 0;

        int[] last = new int[26];
        java.util.Arrays.fill(last, -1);

        int[] stack = new int[n];
        int[] it = new int[n];         // iterator pointer per node
        int[] saved = new int[n];      // previous last[c] to restore on exit
        int sp = 0;

        // enter root
        int rc = s.charAt(0) - 'a';
        saved[0] = last[rc];
        last[rc] = 0;
        order[cnt++] = 0;
        stack[sp++] = 0;
        it[0] = head[0];

        while (sp > 0) {
            int u = stack[sp - 1];
            int v = it[u];
            if (v != -1) {
                it[u] = next[v];
                int c = s.charAt(v) - 'a';
                newParent[v] = last[c] != -1 ? last[c] : parent[v];
                saved[v] = last[c];
                last[c] = v;
                order[cnt++] = v;
                it[v] = head[v];
                stack[sp++] = v;
            } else {
                last[s.charAt(u) - 'a'] = saved[u];
                sp--;
            }
        }

        int[] ans = new int[n];
        java.util.Arrays.fill(ans, 1);
        for (int i = n - 1; i > 0; i--) {
            int x = order[i];
            ans[newParent[x]] += ans[x];
        }
        return ans;
    }
}