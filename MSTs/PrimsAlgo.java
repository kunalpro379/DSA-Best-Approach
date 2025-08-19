import java.util.*;

class PrimsAlgo{
static class Edge{
  int destiny;
  Edge(int d, int w){destiny=d;weight=w;}
}
  static void PRIMMST(int V, List<List<Edge>>adj){
    boolean vis=new boolean[V];
    PriorityQueue<int[]>pq=new PriorityQueue<>((a,b)->a[1]-b[1]);
    pq.add(new int[](0,0));
    int totalWeight=0;
    System.out.println("Prim's MST edges:");
    while(!pq.isEmpty()){
      int[] current=pq.poll();
      int u=current[0], w=current[1];
      vis[u]=true;
      totalWeight+=w;
      System.out.println("Include " + u + " with weight " + w);
      for(Edge e: adj.get(u)){
        if(!vis[e.destiny])pq.add(new int[](e.destiny, e.weight));
      }
    }
    System.out.println("Total MST weight = " + totalWeight);

  }
    public static void main(String[] args) {
        int V = 5;
        List<List<Edge>> adj = new ArrayList<>();
        for (int i = 0; i < V; i++) adj.add(new ArrayList<>());

        adj.get(0).add(new Edge(1, 2));
        adj.get(0).add(new Edge(3, 6));
        adj.get(1).add(new Edge(2, 3));
        adj.get(1).add(new Edge(3, 8));
        adj.get(1).add(new Edge(4, 5));
        adj.get(2).add(new Edge(4, 7));
        adj.get(3).add(new Edge(4, 9));

        PRIMMST(V, adj);
    }
}
