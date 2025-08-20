
public class TreeNode {
     int val;
     TreeNode left;
     TreeNode right;
     TreeNode() {}
     TreeNode(int val) { this.val = val; }
     TreeNode(int val, TreeNode left, TreeNode right) {
         this.val = val;
         this.left = left;
         this.right = right;
     }
 }

class TopDown {
    public List<List<Integer>> levelOrder(TreeNode root) {
        List<List<Integer>>result=new ArrayList<>();
        if(root==null)return result;
        Queue<TreeNode>Q=new LinkedList<>();
        Q.offer(root);
        while(!Q.isEmpty()){
            int size=Q.size();
            List<Integer>level=new ArrayList<>();
            for(int i=0;i<size;i++){
                // Q.offer(node);
                TreeNode node=Q.poll();
                if(node.left!=null)Q.offer(node.left);
                if(node.right!=null)Q.offer(node.right);
                level.add(node.val);

            }
        result.add(level);
        }
        return result;
    }
}
