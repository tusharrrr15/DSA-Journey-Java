
class Solution {
    public int[] findSubtreeSizes(int[] parent, String s) {
        int n = parent.length;

        // Build the original tree
        List<Integer>[] tree = new ArrayList[n];

        for (int i = 0; i < n; i++) {
            tree[i] = new ArrayList<>();
        }

        for (int i = 1; i < n; i++) {
            tree[parent[i]].add(i);
        }

        // Store the nearest ancestor for each character
        int[] nearest = new int[26];
        Arrays.fill(nearest, -1);

        // Construct the modified tree
        List<Integer>[] newTree = new ArrayList[n];

        for (int i = 0; i < n; i++) {
            newTree[i] = new ArrayList<>();
        }

        buildTree(0, tree, newTree, s, nearest);

        // Calculate subtree sizes
        int[] answer = new int[n];
        calculateSize(0, newTree, answer);

        return answer;
    }

    private void buildTree(int node,
                           List<Integer>[] tree,
                           List<Integer>[] newTree,
                           String s,
                           int[] nearest) {

        int ch = s.charAt(node) - 'a';

        // Save previous nearest ancestor
        int previous = nearest[ch];

        // Update nearest ancestor
        nearest[ch] = node;

        for (int child : tree[node]) {

            int childChar = s.charAt(child) - 'a';

            // Find closest ancestor with same character
            int newParent = nearest[childChar];

            if (newParent == -1) {
                newParent = node;
            }

            newTree[newParent].add(child);

            buildTree(child, tree, newTree, s, nearest);
        }

        // Backtrack to restore previous ancestor
        nearest[ch] = previous;
    }

    private int calculateSize(int node,
                              List<Integer>[] tree,
                              int[] answer) {

        int size = 1;

        for (int child : tree[node]) {
            size += calculateSize(child, tree, answer);
        }

        answer[node] = size;

        return size;
    }
}
