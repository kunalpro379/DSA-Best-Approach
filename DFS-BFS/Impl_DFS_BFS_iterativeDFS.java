import java.util.*;

class Graph{
 private int V;//no.of vertices
  private List<List<integer>>adj;
public Graph(int V){
  this.V=V;
  adj=new ArrayList<>();
  for(int i=0;i<V;i++){
    adj.add(
      new ArrayList<>()
    );
  }
}
  public void addEdge(int u, int v){
    adj.get(u).add(v);
    adj.get(v).add(u);
  }
  public void BFS(int start){
    boolean[] visited=new boolean[V];
    Queue<Integer>queue=new LinkedList<>();
    visited[start]=true;
    queue.offer(start);
    System.out.print("BFS starting from " + start + ": ");
    while(!queue.isEmpty()){
      int node=queue.poll();
      System.out.println(node+" ");
      for(int neighbor:adj.get(node)){
        if(!visited[neighbor]){
          visited[neighbor]=true;
          queue.offer(neighbor);
        }
      }
    }System.out.println();
  }
  public void DFSUtil(int node, boolean[] visited){
    visited[node]=true;
    System.out.print(node + " ");
    for(int neighbor: adj.get(node)){
      if(!visited[neighbor])DFSUtil(neighbor, visited);
    }

  }
  public void DFSIterative(int start){
    boolean[] visited=new boolean[V];
    Stack<Integer>stack=new Stack<>();
    stack.push(start);
    System.out.print("DFS (iterative) starting from " + start + ": ");
    while(stack.isEmpty()){
      int node=stack.pop();
      if(!visited[node]){visited[node]=true;System.out.print(node + " ");}
      //push neighbors
      for(int neighbor: adj.get(node)){
        if(!visited[neighbor]){stack.push(neighbor);}
      }

    }System.out.println();
  }
  
  //------------------------RECCURSSIVE DFS------------------------------------
  public void DFS(int start){
    boolean[]visited=new boolean[V];
    System.out.print("DFS (recursive) starting from " + start + ": ");
    DFSUtil(start, visited);
    System.out.println();
  }
};
public DFSItera
public class Impl_DFS_BFS_iterativeDFS{
  public static void main(String[] args){
    Graph g=new Graph(6);
        g.addEdge(0, 1);
        g.addEdge(0, 2);
        g.addEdge(1, 3);
        g.addEdge(2, 4);
        g.addEdge(3, 5);
        g.BFS(0);
        g.DFS(0);
        g.DFSIterative(0);


  }
