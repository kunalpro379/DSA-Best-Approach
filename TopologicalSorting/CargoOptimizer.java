import java.io.*;
import java.util.*;
import java.nio.file.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class CargoOptimizer {
     // class city
     static class City {
          String id;
          String type;
          int capacity;

          public City(
                    String id,
                    String type,
                    int capacity) {
               this.id = id;
               this.type = type;
               this.capacity = capacity;
          }

          @Override
          public String toString() {
               return id + "(" + type + ", cap:" + capacity + ")";
          }
     }

     // class edge
     static class Route {
          String from;
          String to;
          int weight;

          public Route(String from, String to, int weight) {
               this.from = from;
               this.to = to;
               this.weight = weight;
          }

          @Override
          public String toString() {
               return from + " -> " + to + " (weight: " + weight + ")";
          }
     }

     /// Graph representation using adjancacy list
     private Map<String, City> cities;
     // Map of city id to city objects
     private Map<String, List<Route>> adjacencyList;
     // Adjacnecy list of city-> list of outgoing routes
     private Map<String, Integer> indegree;
     // Indegree count for each city

     // Constructor
     /// O(1) lookup hashmap
     public CargoOptimizer() {
          this.cities = new HashMap<>();
          this.adjacencyList = new HashMap<>();
          this.indegree = new HashMap<>();
     }

     // loads data
     // nodes and edges to build graph
     public void loadData(String filename) {
          try {
               JSONParser parser = new JSONParser();
               JsonObject data = (JsonObject) parser.parse(new FileReader(filename));
               // Load all cities(nide)
               JSONArray nodes = (JSONArray) data.get("nodes");
               for (Object node : nodes) {
                    JSONObject cityObj = (JSONObject) node;
                    String id = (String) cityObj.get("id");
                    String type = (String) cityObj.get("type");
                    int capacity = ((Long) cityObj.get("capacity")).intValue();
                    City city = new City(id, type, capacity);
                    cities.put(id, city);
                    adjacencyList.put(id, new ArrayList<>());// initally empty List
                    indegree.put(id, 0);
               }
               JSONArray edges = (JSONArray) data.get("edges");
               for (Object edge : edges) {
                    JSONObject edgeObj = (JSONObject) edge;
                    String from = (String) edgeObj.get("from");
                    String to = (String) edgeObj.get("to");
                    int weight = ((Long) edgeObj.get("weight")).intValue();
                    Route route = new Route(from, to, weight);
                    adjacencyList.get(from).add(route);
               }
          } catch (Exception e) {
               return e.getMessage();
          }
     }

}
