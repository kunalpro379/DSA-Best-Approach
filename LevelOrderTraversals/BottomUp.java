public class TreeNode {
      int val;
      TreeNode left;
      TreeNode right;
      TreeNode() {}
      TreeNode(int val) { this.val = v{al; }
      TreeNode(int val, TreeNode left, TreeNode right) {
          this.val = val;
          this.left = left;
         this.right = right;
      }
  }
 
class BottomUp {
    public List<List<Integer>> levelOrderBottom(TreeNode root) {
        List<List<Integer>>result=new LinkedList<>();
        if(root==null)return result;
        Queue<TreeNode>Q=new LinkedList<>();
        Q.offer(root); 

        while(!Q.isEmpty()){
            int size=Q.size();
            List<Integer>level=new ArrayList<>();
            for(int i=0;i<size;i++){
                TreeNode node=Q.poll();
                level.add(node.val);
                if(node.left!=null)Q.offer(node.left);
                if(node.right!=null)Q.offer(node.right);
            }
            result.addFirst(level);
        }return result;
    }
}
