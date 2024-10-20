import java.util.*;

/**
 * Class: Map
 * Author: Ravshanbek Temurbekov
 * Course: CSC345, PROGRAM#2
 * 
 * Purpose: This class represents a graph structure using an adjacency matrix 
 * to manage edges between vertices.
 */

public class Graph {
    private int numVertices; // The number of vertices in the graph
    private boolean[][] adjacencyMatrix; // Adjacency matrix to store edges between vertices
    private Set<Integer> inactiveVertices; // Set of inactive (unplayable) vertices

    /**
     * Constructor: Initializes the graph with a given number of vertices.
     * 
     * @param numVertices The number of vertices in the graph. Must be positive.
     * Preconditions: numVertices > 0
     * Postconditions: Graph is initialized with no edges and no inactive vertices.
     */
    public Graph(int numVertices) {
        this.numVertices = numVertices;
        this.adjacencyMatrix = new boolean[numVertices][numVertices]; // Initially, no edges
        this.inactiveVertices = new HashSet<>();
    }

    /**
     * Method: addEdge
     * Purpose: Adds an undirected edge between two vertices.
     * 
     * @param source The source vertex (input).
     * @param destination The destination vertex (input).
     * Preconditions: source and destination are valid vertex indices.
     * Postconditions: An undirected edge is added between source and destination.
     */
    public void addEdge(int source, int destination) {
        validateVertex(source);
        validateVertex(destination);
        if (source != destination) { // Avoid self-loops
            adjacencyMatrix[source][destination] = true;
            adjacencyMatrix[destination][source] = true;
        }
    }

    /**
     * Method: removeEdge
     * Purpose: Removes an undirected edge between two vertices.
     * 
     * @param source The source vertex (input).
     * @param destination The destination vertex (input).
     * Preconditions: source and destination are valid vertex indices.
     * Postconditions: The edge between source and destination is removed.
     */
    public void removeEdge(int source, int destination) {
        validateVertex(source);
        validateVertex(destination);
        adjacencyMatrix[source][destination] = false;
        adjacencyMatrix[destination][source] = false;
    }

    /**
     * Method: isEdge
     * Purpose: Checks if there is an edge between two vertices.
     * 
     * @param source The source vertex (input).
     * @param destination The destination vertex (input).
     * @return True if there is an edge, false otherwise.
     * Preconditions: source and destination are valid vertex indices.
     */
    public boolean isEdge(int source, int destination) {
        validateVertex(source);
        validateVertex(destination);
        return adjacencyMatrix[source][destination];
    }

    /**
     * Method: getAdjacent
     * Purpose: Retrieves adjacent (neighboring) vertices of a given vertex, excluding inactive vertices.
     * 
     * @param vertex The vertex whose neighbors are to be retrieved (input).
     * @return A list of neighboring vertices that are active.
     * Preconditions: vertex is a valid vertex index.
     */
    public List<Integer> getAdjacent(int vertex) {
        validateVertex(vertex);
        List<Integer> neighbors = new ArrayList<>();
        for (int i = 0; i < numVertices; i++) {
            if (adjacencyMatrix[vertex][i] && !inactiveVertices.contains(i)) {
                neighbors.add(i);
            }
        }
        return neighbors;
    }

    /**
     * Method: removeVertex
     * Purpose: Marks a vertex as inactive (unplayable) and removes its edges.
     * 
     * @param vertex The vertex to be marked as inactive (input).
     * Preconditions: vertex is a valid vertex index.
     * Postconditions: Vertex is marked as inactive and all edges associated with it are removed.
     */
    public void removeVertex(int vertex) {
        validateVertex(vertex);
        inactiveVertices.add(vertex);
        // Remove all edges associated with this vertex
        for (int i = 0; i < numVertices; i++) {
            adjacencyMatrix[vertex][i] = false;
            adjacencyMatrix[i][vertex] = false;
        }
    }

    /**
     * Method: getUnusedVertices
     * Purpose: Returns a list of inactive (unused) vertices.
     * 
     * @return A list of vertices marked as inactive.
     */
    public List<Integer> getUnusedVertices() {
        return new ArrayList<>(inactiveVertices);
    }

    /**
     * Method: connected
     * Purpose: Checks if the graph is fully connected (all active vertices can reach each other).
     * 
     * @return True if the graph is connected, false otherwise.
     */
    public boolean connected() {
        int startVertex = getFirstActiveVertex();
        if (startVertex == -1) {
            return true; // If no active vertices, the graph is trivially connected
        }

        boolean[] visited = new boolean[numVertices];
        performBFS(startVertex, visited);

        // Ensure all active vertices were visited
        for (int i = 0; i < numVertices; i++) {
            if (!inactiveVertices.contains(i) && !visited[i]) {
                return false; // Found an unvisited active vertex
            }
        }
        return true;
    }

    /**
     * Method: degree
     * Purpose: Returns the degree (number of connections) of a vertex, excluding inactive vertices.
     * 
     * @param vertex The vertex whose degree is to be calculated (input).
     * @return The degree of the vertex.
     * Preconditions: vertex is a valid vertex index.
     */
    public int degree(int vertex) {
        validateVertex(vertex);
        int degree = 0;
        for (int i = 0; i < numVertices; i++) {
            if (adjacencyMatrix[vertex][i] && !inactiveVertices.contains(i)) {
                degree++;
            }
        }
        return degree;
    }

    /**
     * Method: isInGraph
     * Purpose: Checks if a vertex is active in the graph.
     * 
     * @param vertex The vertex to be checked (input).
     * @return True if the vertex is active, false otherwise.
     */
    public boolean isInGraph(int vertex) {
        validateVertex(vertex);
        return !inactiveVertices.contains(vertex);
    }

    /**
     * Method: performBFS
     * Purpose: Performs a Breadth-First Search (BFS) and marks visited vertices.
     * 
     * @param startVertex The starting vertex for BFS (input).
     * @param visited Array to keep track of visited vertices (input/output).
     */
    private void performBFS(int startVertex, boolean[] visited) {
        Queue<Integer> queue = new LinkedList<>();
        queue.add(startVertex);
        visited[startVertex] = true;

        while (!queue.isEmpty()) {
            int current = queue.poll();
            for (int neighbor : getAdjacent(current)) {
                if (!visited[neighbor]) {
                    visited[neighbor] = true;
                    queue.add(neighbor);
                }
            }
        }
    }

    /**
     * Method: getFirstActiveVertex
     * Purpose: Finds the first active vertex in the graph.
     * 
     * @return The index of the first active vertex, or -1 if none are found.
     */
    private int getFirstActiveVertex() {
        for (int i = 0; i < numVertices; i++) {
            if (!inactiveVertices.contains(i)) {
                return i;
            }
        }
        return -1; // No active vertex found
    }

    /**
     * Method: validateVertex
     * Purpose: Validates that a vertex index is within bounds.
     * 
     * @param vertex The vertex to validate (input).
     * @throws IllegalArgumentException if the vertex is out of bounds.
     */
    private void validateVertex(int vertex) {
        if (vertex < 0 || vertex >= numVertices) {
            throw new IllegalArgumentException("Invalid vertex index: " + vertex);
        }
    }
}