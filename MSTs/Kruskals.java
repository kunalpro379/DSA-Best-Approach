class Kruskals{
  static class Edge implements Comparable<Edge>{
    int u,v,w;
    Edge(int u, int v, int w) { this.u = u; this.v = v; this.w = w; }
    public int compareTo(Edge other){
      return this.w - other.w;
    }
  }
  static class DSU{
    int[] parent, rank;
    for(int i=0;i<n;i++)parent[i]=i;
    int find(int x){
      return parent[x]==x?x:(parent[x]=find(parent[x]));
//kya x ka parent x khud hai? agar nahi hai toh recrssively x ke parent ke parent ko dhund....
    }
    boolean union(int a, int b){
    a=find(a);
    b=find(b);
    if(a==b)return false;//already in same set
    if(rank[a]<rank[b])parent[a]=b;
    else if(rank[b]<rank[a])parent[b]=a;
    else {
      parent[b]=a;
      rank[a]++;
    }
    return true;
    }
  }
  static void KruskalMST(int V, List<Edge>edges){
    Collections.sort(edges);
    DSU dsu=new DSU(V);
    int total=0;
    System.out.println("Kruskal's MST edges:");
    for(Edge e: edges){
      if(dsu.union(e.u, e.v)){
          System.out.println(e.u + " - " + e.v + " : " + e.w);
          total += e.w;
      }
    }
    System.out.println("Total MST weight = " + total); 
  }
  public static void main(String[] args){
    int V=4;
    List<Edge> edges = Arrays.asList(
            new Edge(0, 1, 10),
            new Edge(0, 2, 6),
            new Edge(0, 3, 5),
            new Edge(1, 3, 15),
            new Edge(2, 3, 4)
        );
    kruskalMST(V, edges);
  }
}
