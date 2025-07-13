# Topological Sorting - Theory and Implementation

## Problem Statement

Given a directed acyclic graph (DAG) representing tasks with dependencies, we need to find a linear ordering of vertices (tasks) such that for every directed edge (u, v), vertex u comes before vertex v in the ordering. This is called a **topological sort**.

Design a route optimization system for cargo delivery across major Indian cities using a Directed Acyclic Graph (DAG). Given a complex topological map of 20 cities and weighted one-way paths representing travel times/distances, determine the most efficient order of deliveries and detect any unreachable destinations. You must apply topological sorting to identify valid delivery orders, and optionally find the longest/shortest path from a source city to all other reachable cities.

## Theory

### What is Topological Sorting?

Topological sorting is an algorithm that orders the vertices of a directed acyclic graph (DAG) in such a way that for every directed edge (u, v), vertex u comes before vertex v in the ordering.

### Key Concepts

1. **Directed Acyclic Graph (DAG)**: A directed graph with no cycles
2. **In-degree**: Number of incoming edges to a vertex
3. **Out-degree**: Number of outgoing edges from a vertex
4. **Topological Order**: A linear ordering of vertices respecting dependencies

### Algorithm Approaches

#### 1. Kahn's Algorithm (BFS-based)
- Uses breadth-first search
- Maintains a queue of vertices with in-degree 0
- Processes vertices level by level

**Steps:**
1. Calculate in-degree for all vertices
2. Add all vertices with in-degree 0 to a queue
3. While queue is not empty:
   - Remove a vertex from queue
   - Add it to result
   - Decrease in-degree of all adjacent vertices by 1
   - If any vertex's in-degree becomes 0, add it to queue
4. If result contains all vertices, return result; else graph has cycle

#### 2. DFS-based Algorithm
- Uses depth-first search with recursion
- Uses a stack to build the result

**Steps:**
1. For each unvisited vertex:
   - Mark as visited
   - Recursively visit all adjacent vertices
   - Add current vertex to stack
2. Reverse the stack to get topological order

### Time and Space Complexity
- **Time Complexity**: O(V + E) where V = vertices, E = edges
- **Space Complexity**: O(V) for storing the result and auxiliary data structures

## Implementation Strategy

For our task scheduling problem, we'll implement:
1. **Task class** to represent each task with its properties
2. **Graph representation** using adjacency list
3. **Kahn's algorithm** for topological sorting
4. **Additional analysis** like:
   - Critical path calculation
   - Resource utilization analysis
   - Parallel execution possibilities

## Data Structure

Our JSON data represents:
- **Task**: Unique identifier for each task
- **depends_on**: List of task dependencies
- **duration_ms**: Time required to complete the task
- **resource_type**: Type of resource needed (CPU, GPU, Disk, etc.)
- **memory_mb**: Memory requirement in MB

This creates a dependency graph where edges represent "must complete before" relationships. 



import java.util.*;
import java.io.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 * CargoRouteOptimizer - A comprehensive route optimization system for cargo delivery
 * across major Indian cities using Directed Acyclic Graph (DAG) and Topological Sorting.
 * 
 * This system implements:
 * 1. Topological Sorting using Kahn's Algorithm
 * 2. Shortest Path using Dijkstra's Algorithm
 * 3. Longest Path using modified Topological Sort
 * 4. Cycle detection and unreachable destination identification
 * 5. Route optimization based on city capacity and type
 * 
 * Time Complexity: O(V + E) for topological sort, O((V + E) log V) for shortest path
 * Space Complexity: O(V + E) for graph representation
 */
public class CargoRouteOptimizer {
    
    /**
     * City class represents each city in the cargo delivery network.
     * Each city has:
     * - id: Unique identifier (city name)
     * - type: Classification (metro/city/hub) for capacity planning
     * - capacity: Maximum cargo capacity the city can handle
     */
    static class City {
        String id;
        String type;
        int capacity;
        
        public City(String id, String type, int capacity) {
            this.id = id;
            this.type = type;
            this.capacity = capacity;
        }
        
        @Override
        public String toString() {
            return id + "(" + type + ", cap:" + capacity + ")";
        }
    }
    
    /**
     * Route class represents a directed edge between two cities.
     * Each route has:
     * - from: Source city
     * - to: Destination city
     * - weight: Travel time/distance (used for path optimization)
     */
    static class Route {
        String from;
        String to;
        int weight; // travel time/distance in units (hours/days)
        
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
    
    // Graph representation using adjacency list for efficient traversal
    private Map<String, City> cities;                    // Map of city ID to City object
    private Map<String, List<Route>> adjacencyList;      // Adjacency list: city -> list of outgoing routes
    private Map<String, Integer> inDegree;               // In-degree count for each city (for topological sort)
    
    /**
     * Constructor initializes the data structures for the graph representation.
     * Uses HashMap for O(1) average case lookup and insertion.
     */
    public CargoRouteOptimizer() {
        this.cities = new HashMap<>();
        this.adjacencyList = new HashMap<>();
        this.inDegree = new HashMap<>();
    }
    
    /**
     * Loads the cargo delivery network data from a JSON file.
     * Parses both cities (nodes) and routes (edges) to build the graph.
     * 
     * @param filename Path to the JSON file containing network data
     */
    public void loadData(String filename) {
        try {
            // Parse JSON file using json-simple library
            JSONParser parser = new JSONParser();
            JSONObject data = (JSONObject) parser.parse(new FileReader(filename));
            
            // Step 1: Load all cities (nodes) from JSON
            JSONArray nodes = (JSONArray) data.get("nodes");
            for (Object node : nodes) {
                JSONObject cityObj = (JSONObject) node;
                String id = (String) cityObj.get("id");
                String type = (String) cityObj.get("type");
                int capacity = ((Long) cityObj.get("capacity")).intValue();
                
                // Create City object and add to data structures
                City city = new City(id, type, capacity);
                cities.put(id, city);
                adjacencyList.put(id, new ArrayList<>());  // Initialize empty adjacency list
                inDegree.put(id, 0);                       // Initialize in-degree to 0
            }
            
            // Step 2: Load all routes (edges) from JSON
            JSONArray edges = (JSONArray) data.get("edges");
            for (Object edge : edges) {
                JSONObject routeObj = (JSONObject) edge;
                String from = (String) routeObj.get("from");
                String to = (String) routeObj.get("to");
                int weight = ((Long) routeObj.get("weight")).intValue();
                
                // Create Route object and add to adjacency list
                Route route = new Route(from, to, weight);
                adjacencyList.get(from).add(route);        // Add to outgoing routes
                inDegree.put(to, inDegree.get(to) + 1);    // Increment in-degree of destination
            }
            
            // Print loading summary
            System.out.println("Data loaded successfully!");
            System.out.println("Cities: " + cities.size());
            System.out.println("Routes: " + edges.size());
            
        } catch (Exception e) {
            System.err.println("Error loading data: " + e.getMessage());
        }
    }
    
    /**
     * Implements Kahn's Algorithm for Topological Sorting.
     * 
     * ALGORITHM STEPS:
     * 1. Calculate in-degree for all vertices
     * 2. Add all vertices with in-degree 0 to a queue
     * 3. While queue is not empty:
     *    - Remove a vertex from queue
     *    - Add it to result
     *    - Decrease in-degree of all adjacent vertices by 1
     *    - If any vertex's in-degree becomes 0, add it to queue
     * 4. If result contains all vertices, return result; else graph has cycle
     * 
     * TIME COMPLEXITY: O(V + E) where V = vertices, E = edges
     * SPACE COMPLEXITY: O(V) for queue and result
     * 
     * @return List of cities in topological order, or null if graph has cycles
     */
    public List<String> topologicalSort() {
        List<String> result = new ArrayList<>();
        Queue<String> queue = new LinkedList<>();
        
        // Create a copy of in-degree map to avoid modifying original data
        Map<String, Integer> tempInDegree = new HashMap<>(inDegree);
        
        // Step 1: Add all cities with in-degree 0 to queue (entry points)
        for (Map.Entry<String, Integer> entry : tempInDegree.entrySet()) {
            if (entry.getValue() == 0) {
                queue.offer(entry.getKey());
            }
        }
        
        // Step 2: Process queue until empty
        while (!queue.isEmpty()) {
            String currentCity = queue.poll();
            result.add(currentCity);  // Add to topological order
            
            // Step 3: Process all outgoing routes from current city
            for (Route route : adjacencyList.get(currentCity)) {
                String adjacentCity = route.to;
                // Decrease in-degree of adjacent city
                tempInDegree.put(adjacentCity, tempInDegree.get(adjacentCity) - 1);
                
                // If in-degree becomes 0, add to queue for processing
                if (tempInDegree.get(adjacentCity) == 0) {
                    queue.offer(adjacentCity);
                }
            }
        }
        
        // Step 4: Check if all cities are included (cycle detection)
        if (result.size() != cities.size()) {
            System.out.println("Warning: Graph contains cycles! Not all cities can be reached.");
            return null;  // Return null to indicate cycle presence
        }
        
        return result;
    }
    
    /**
     * Finds unreachable destinations using BFS from entry points.
     * 
     * ALGORITHM:
     * 1. Start BFS from all cities with in-degree 0 (entry points)
     * 2. Mark all reachable cities during BFS
     * 3. Cities not marked are unreachable
     * 
     * TIME COMPLEXITY: O(V + E)
     * SPACE COMPLEXITY: O(V) for visited set and queue
     * 
     * @return Set of unreachable city IDs
     */
    public Set<String> findUnreachableDestinations() {
        Set<String> reachable = new HashSet<>();
        Set<String> unreachable = new HashSet<>();
        
        // Step 1: Start BFS from cities with in-degree 0 (entry points)
        Queue<String> queue = new LinkedList<>();
        for (Map.Entry<String, Integer> entry : inDegree.entrySet()) {
            if (entry.getValue() == 0) {
                queue.offer(entry.getKey());
                reachable.add(entry.getKey());
            }
        }
        
        // Step 2: BFS to find all reachable cities
        while (!queue.isEmpty()) {
            String currentCity = queue.poll();
            
            // Explore all adjacent cities
            for (Route route : adjacencyList.get(currentCity)) {
                String adjacentCity = route.to;
                if (!reachable.contains(adjacentCity)) {
                    reachable.add(adjacentCity);
                    queue.offer(adjacentCity);
                }
            }
        }
        
        // Step 3: Identify unreachable cities
        for (String cityId : cities.keySet()) {
            if (!reachable.contains(cityId)) {
                unreachable.add(cityId);
            }
        }
        
        return unreachable;
    }
    
    /**
     * Implements Dijkstra's Algorithm for finding shortest paths from a source city.
     * 
     * ALGORITHM STEPS:
     * 1. Initialize distances: source = 0, others = infinity
     * 2. Use priority queue to always process closest unvisited city
     * 3. For each city, relax all outgoing edges
     * 4. Continue until all cities are processed
     * 
     * TIME COMPLEXITY: O((V + E) log V) due to priority queue operations
     * SPACE COMPLEXITY: O(V) for distances and priority queue
     * 
     * @param source Starting city for shortest path calculation
     * @return Map of city ID to shortest distance from source
     */
    public Map<String, Integer> shortestPath(String source) {
        Map<String, Integer> distances = new HashMap<>();
        // Priority queue to always process closest unvisited city
        PriorityQueue<Map.Entry<String, Integer>> pq = new PriorityQueue<>(
            (a, b) -> a.getValue().compareTo(b.getValue())
        );
        
        // Step 1: Initialize distances
        for (String cityId : cities.keySet()) {
            distances.put(cityId, Integer.MAX_VALUE);  // Infinity for all cities
        }
        distances.put(source, 0);  // Distance to source is 0
        
        // Add source to priority queue
        pq.offer(new AbstractMap.SimpleEntry<>(source, 0));
        
        // Step 2: Process cities in order of increasing distance
        while (!pq.isEmpty()) {
            Map.Entry<String, Integer> current = pq.poll();
            String currentCity = current.getKey();
            int currentDistance = current.getValue();
            
            // Skip if we already found a shorter path to this city
            if (currentDistance > distances.get(currentCity)) {
                continue;
            }
            
            // Step 3: Relax all outgoing edges from current city
            for (Route route : adjacencyList.get(currentCity)) {
                String adjacentCity = route.to;
                int newDistance = currentDistance + route.weight;
                
                // If we found a shorter path, update distance and add to queue
                if (newDistance < distances.get(adjacentCity)) {
                    distances.put(adjacentCity, newDistance);
                    pq.offer(new AbstractMap.SimpleEntry<>(adjacentCity, newDistance));
                }
            }
        }
        
        return distances;
    }
    
    /**
     * Finds longest paths from a source city using modified topological sort.
     * 
     * ALGORITHM:
     * 1. Get topological order of cities
     * 2. Initialize distances: source = 0, others = negative infinity
     * 3. Process cities in topological order
     * 4. For each city, update distances to adjacent cities
     * 
     * TIME COMPLEXITY: O(V + E)
     * SPACE COMPLEXITY: O(V)
     * 
     * Note: This works only for DAGs (no cycles). For graphs with cycles,
     * longest path problem becomes NP-hard.
     * 
     * @param source Starting city for longest path calculation
     * @return Map of city ID to longest distance from source
     */
    public Map<String, Integer> longestPath(String source) {
        List<String> topoOrder = topologicalSort();
        Map<String, Integer> distances = new HashMap<>();
        
        // Step 1: Initialize distances
        for (String cityId : cities.keySet()) {
            distances.put(cityId, Integer.MIN_VALUE);  // Negative infinity for all cities
        }
        distances.put(source, 0);  // Distance to source is 0
        
        // Step 2: Process cities in topological order
        for (String city : topoOrder) {
            // Only process cities that are reachable from source
            if (distances.get(city) != Integer.MIN_VALUE) {
                // Update distances to all adjacent cities
                for (Route route : adjacencyList.get(city)) {
                    String adjacentCity = route.to;
                    int newDistance = distances.get(city) + route.weight;
                    
                    // Update if we found a longer path
                    if (newDistance > distances.get(adjacentCity)) {
                        distances.put(adjacentCity, newDistance);
                    }
                }
            }
        }
        
        return distances;
    }
    
    /**
     * Optimizes delivery route starting from a specific city.
     * Uses topological sort to ensure dependencies are respected.
     * 
     * ALGORITHM:
     * 1. Get topological order of all cities
     * 2. Find position of start city in topological order
     * 3. Return cities from start position onwards
     * 
     * This ensures that:
     * - Dependencies are respected (prerequisites come before dependents)
     * - Route is optimal for cargo delivery sequence
     * - No backtracking is required
     * 
     * @param startCity City to start delivery route from
     * @return List of cities in optimal delivery order
     */
    public List<String> optimizeDeliveryRoute(String startCity) {
        List<String> topoOrder = topologicalSort();
        List<String> optimizedRoute = new ArrayList<>();
        
        // Check if topological sort is possible (no cycles)
        if (topoOrder == null) {
            System.out.println("Cannot optimize route due to cycles in graph.");
            return optimizedRoute;
        }
        
        // Find position of start city in topological order
        int startIndex = topoOrder.indexOf(startCity);
        if (startIndex == -1) {
            System.out.println("Start city not found in topological order.");
            return optimizedRoute;
        }
        
        // Add cities from start city onwards (respecting dependencies)
        for (int i = startIndex; i < topoOrder.size(); i++) {
            optimizedRoute.add(topoOrder.get(i));
        }
        
        return optimizedRoute;
    }
    
    /**
     * Prints comprehensive analysis of the cargo delivery network.
     * Includes:
     * 1. Topological sort (optimal delivery order)
     * 2. Unreachable destinations
     * 3. Shortest paths from major metros
     * 4. Longest paths from major metros
     * 5. Optimized delivery routes from major metros
     */
    public void printAnalysis() {
        System.out.println("\n=== CARGO DELIVERY ROUTE ANALYSIS ===\n");
        
        // Analysis 1: Topological Sort - Shows optimal delivery order
        System.out.println("1. TOPOLOGICAL SORT (Optimal Delivery Order):");
        System.out.println("   This shows the most efficient order to visit cities");
        System.out.println("   respecting all dependencies and avoiding cycles.\n");
        
        List<String> topoOrder = topologicalSort();
        if (topoOrder != null) {
            for (int i = 0; i < topoOrder.size(); i++) {
                City city = cities.get(topoOrder.get(i));
                System.out.printf("   %2d. %s\n", i + 1, city);
            }
        } else {
            System.out.println("   Graph contains cycles - cannot perform topological sort!");
            System.out.println("   This means there are circular dependencies in the route network.");
        }
        
        // Analysis 2: Unreachable Destinations - Identifies isolated cities
        System.out.println("\n2. UNREACHABLE DESTINATIONS:");
        System.out.println("   Cities that cannot be reached from any entry point.\n");
        
        Set<String> unreachable = findUnreachableDestinations();
        if (unreachable.isEmpty()) {
            System.out.println("   All cities are reachable! ✓");
        } else {
            System.out.println("   The following cities are unreachable:");
            for (String cityId : unreachable) {
                City city = cities.get(cityId);
                System.out.println("   - " + city);
            }
        }
        
        // Analysis 3: Shortest Paths - Minimum travel time from major metros
        System.out.println("\n3. SHORTEST PATHS FROM MAJOR METROS:");
        System.out.println("   Minimum travel time (in units) from each major metro to all other cities.\n");
        
        String[] metros = {"Mumbai", "Delhi", "Kolkata", "Hyderabad", "Ahmedabad"};
        for (String metro : metros) {
            if (cities.containsKey(metro)) {
                System.out.println("   From " + metro + " (Shortest Paths):");
                Map<String, Integer> shortestPaths = shortestPath(metro);
                for (Map.Entry<String, Integer> entry : shortestPaths.entrySet()) {
                    if (entry.getValue() != Integer.MAX_VALUE) {
                        System.out.printf("     %s: %d units\n", entry.getKey(), entry.getValue());
                    } else {
                        System.out.printf("     %s: Unreachable\n", entry.getKey());
                    }
                }
                System.out.println();
            }
        }
        
        // Analysis 4: Longest Paths - Maximum travel time from major metros
        System.out.println("\n4. LONGEST PATHS FROM MAJOR METROS:");
        System.out.println("   Maximum travel time (in units) from each major metro to all other cities.\n");
        
        for (String metro : metros) {
            if (cities.containsKey(metro)) {
                System.out.println("   From " + metro + " (Longest Paths):");
                Map<String, Integer> longestPaths = longestPath(metro);
                for (Map.Entry<String, Integer> entry : longestPaths.entrySet()) {
                    if (entry.getValue() != Integer.MIN_VALUE) {
                        System.out.printf("     %s: %d units\n", entry.getKey(), entry.getValue());
                    } else {
                        System.out.printf("     %s: Unreachable\n", entry.getKey());
                    }
                }
                System.out.println();
            }
        }
        
        // Analysis 5: Optimized Delivery Routes - Best delivery sequence from each metro
        System.out.println("\n5. OPTIMIZED DELIVERY ROUTES:");
        System.out.println("   Best delivery sequence starting from each major metro.\n");
        
        for (String metro : metros) {
            if (cities.containsKey(metro)) {
                System.out.println("   Starting from " + metro + " (Optimal Sequence):");
                List<String> optimizedRoute = optimizeDeliveryRoute(metro);
                for (int i = 0; i < optimizedRoute.size(); i++) {
                    City city = cities.get(optimizedRoute.get(i));
                    System.out.printf("     %2d. %s\n", i + 1, city);
                }
                System.out.println();
            }
        }
        
        // Summary statistics
        System.out.println("=== SUMMARY STATISTICS ===");
        System.out.println("Total Cities: " + cities.size());
        System.out.println("Total Routes: " + adjacencyList.values().stream().mapToInt(List::size).sum());
        System.out.println("Metro Cities: " + cities.values().stream().filter(c -> c.type.equals("metro")).count());
        System.out.println("Hub Cities: " + cities.values().stream().filter(c -> c.type.equals("hub")).count());
        System.out.println("Regular Cities: " + cities.values().stream().filter(c -> c.type.equals("city")).count());
        System.out.println("Average Capacity: " + cities.values().stream().mapToInt(c -> c.capacity).average().orElse(0));
    }
    
    /**
     * Main method to demonstrate the cargo route optimization system.
     * Loads data and performs comprehensive analysis.
     */
    public static void main(String[] args) {
        System.out.println("=== CARGO DELIVERY ROUTE OPTIMIZATION SYSTEM ===");
        System.out.println("Using Topological Sorting and Graph Algorithms\n");
        
        CargoRouteOptimizer optimizer = new CargoRouteOptimizer();
        optimizer.loadData("data.json");
        optimizer.printAnalysis();
    }
}

import java.util.*;

/**
 * Main class providing an interactive interface for the Cargo Route Optimizer.
 * Allows users to query specific routes and perform custom analysis.
 */
public class Main {
    public static void main(String[] args) {
        System.out.println("=== CARGO DELIVERY ROUTE OPTIMIZATION SYSTEM ===");
        System.out.println("Using Topological Sorting and Graph Algorithms\n");
        
        // Initialize the optimizer and load data
        CargoRouteOptimizer optimizer = new CargoRouteOptimizer();
        optimizer.loadData("data.json");
        
        // Run comprehensive analysis
        optimizer.printAnalysis();
        
        // Interactive demo for custom queries
        runInteractiveDemo(optimizer);
    }
    
    /**
     * Runs an interactive demo allowing users to query specific routes.
     * 
     * @param optimizer The initialized CargoRouteOptimizer instance
     */
    private static void runInteractiveDemo(CargoRouteOptimizer optimizer) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("\n=== INTERACTIVE DEMO ===");
        System.out.println("You can now query specific routes and analysis.");
        System.out.println("Available commands:");
        System.out.println("- 'shortest <city>': Find shortest paths from a city");
        System.out.println("- 'longest <city>': Find longest paths from a city");
        System.out.println("- 'route <city>': Get optimized delivery route from a city");
        System.out.println("- 'unreachable': Show unreachable destinations");
        System.out.println("- 'topo': Show topological sort order");
        System.out.println("- 'quit': Exit the program");
        System.out.println();
        
        while (true) {
            System.out.print("Enter command: ");
            String input = scanner.nextLine().trim();
            
            if (input.equalsIgnoreCase("quit")) {
                break;
            }
            
            String[] parts = input.split("\\s+", 2);
            String command = parts[0].toLowerCase();
            
            switch (command) {
                case "shortest":
                    if (parts.length > 1) {
                        showShortestPaths(optimizer, parts[1]);
                    } else {
                        System.out.println("Please specify a city name.");
                    }
                    break;
                    
                case "longest":
                    if (parts.length > 1) {
                        showLongestPaths(optimizer, parts[1]);
                    } else {
                        System.out.println("Please specify a city name.");
                    }
                    break;
                    
                case "route":
                    if (parts.length > 1) {
                        showOptimizedRoute(optimizer, parts[1]);
                    } else {
                        System.out.println("Please specify a city name.");
                    }
                    break;
                    
                case "unreachable":
                    showUnreachableDestinations(optimizer);
                    break;
                    
                case "topo":
                    showTopologicalSort(optimizer);
                    break;
                    
                default:
                    System.out.println("Unknown command. Type 'quit' to exit.");
                    break;
            }
            System.out.println();
        }
        
        scanner.close();
        System.out.println("Thank you for using the Cargo Route Optimizer!");
    }
    
    /**
     * Displays shortest paths from a specified city.
     */
    private static void showShortestPaths(CargoRouteOptimizer optimizer, String cityName) {
        if (!optimizer.cities.containsKey(cityName)) {
            System.out.println("City '" + cityName + "' not found in the network.");
            return;
        }
        
        System.out.println("\nShortest paths from " + cityName + ":");
        Map<String, Integer> shortestPaths = optimizer.shortestPath(cityName);
        
        for (Map.Entry<String, Integer> entry : shortestPaths.entrySet()) {
            if (entry.getValue() != Integer.MAX_VALUE) {
                System.out.printf("  %s: %d units\n", entry.getKey(), entry.getValue());
            } else {
                System.out.printf("  %s: Unreachable\n", entry.getKey());
            }
        }
    }
    
    /**
     * Displays longest paths from a specified city.
     */
    private static void showLongestPaths(CargoRouteOptimizer optimizer, String cityName) {
        if (!optimizer.cities.containsKey(cityName)) {
            System.out.println("City '" + cityName + "' not found in the network.");
            return;
        }
        
        System.out.println("\nLongest paths from " + cityName + ":");
        Map<String, Integer> longestPaths = optimizer.longestPath(cityName);
        
        for (Map.Entry<String, Integer> entry : longestPaths.entrySet()) {
            if (entry.getValue() != Integer.MIN_VALUE) {
                System.out.printf("  %s: %d units\n", entry.getKey(), entry.getValue());
            } else {
                System.out.printf("  %s: Unreachable\n", entry.getKey());
            }
        }
    }
    
    /**
     * Displays optimized delivery route from a specified city.
     */
    private static void showOptimizedRoute(CargoRouteOptimizer optimizer, String cityName) {
        if (!optimizer.cities.containsKey(cityName)) {
            System.out.println("City '" + cityName + "' not found in the network.");
            return;
        }
        
        System.out.println("\nOptimized delivery route starting from " + cityName + ":");
        List<String> optimizedRoute = optimizer.optimizeDeliveryRoute(cityName);
        
        if (optimizedRoute.isEmpty()) {
            System.out.println("  No valid route found.");
        } else {
            for (int i = 0; i < optimizedRoute.size(); i++) {
                CargoRouteOptimizer.City city = optimizer.cities.get(optimizedRoute.get(i));
                System.out.printf("  %2d. %s\n", i + 1, city);
            }
        }
    }
    
    /**
     * Displays unreachable destinations.
     */
    private static void showUnreachableDestinations(CargoRouteOptimizer optimizer) {
        System.out.println("\nUnreachable destinations:");
        Set<String> unreachable = optimizer.findUnreachableDestinations();
        
        if (unreachable.isEmpty()) {
            System.out.println("  All cities are reachable! ✓");
        } else {
            for (String cityId : unreachable) {
                CargoRouteOptimizer.City city = optimizer.cities.get(cityId);
                System.out.println("  - " + city);
            }
        }
    }
    
    /**
     * Displays topological sort order.
     */
    private static void showTopologicalSort(CargoRouteOptimizer optimizer) {
        System.out.println("\nTopological sort order (optimal delivery sequence):");
        List<String> topoOrder = optimizer.topologicalSort();
        
        if (topoOrder != null) {
            for (int i = 0; i < topoOrder.size(); i++) {
                CargoRouteOptimizer.City city = optimizer.cities.get(topoOrder.get(i));
                System.out.printf("  %2d. %s\n", i + 1, city);
            }
        } else {
            System.out.println("  Graph contains cycles - cannot perform topological sort!");
        }
    }
}