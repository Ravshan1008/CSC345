import java.util.ArrayList;
import java.util.Random;
import java.util.Queue;
import java.util.LinkedList;

/**
 * Class: Map
 * Author: Ravshanbek Temurbekov
 * Course: CSC345, PROGRAM#2
 * Purpose: This class CONSTRUCTS a game map made of territories. It manages the initialization
 * of the game board, assigning territories to players, distributing dice, and ensuring the
 * connectivity of territories using a graph representation.
 */

public class Map {
    // Constructing representation of dimensions and properties of the game board
    public final int ROWS; // Number of rows in the game board
    public final int COLUMNS; // Number of columns in the game board
    public final int VICTIMS; // Number of territories marked as inactive
    public final int NUMTERRITORIES; // Total number of territories in the game
    public final int OCCUPIED; // Number of active territories
    public final int MAXDICE; // Maximum number of dice a territory can hold

    // Instance variables representing the game state
    private Territory[][] map; // 2D array of the game board as territories
    private Graph graph; // Graph representing relationships between territories
    private ArrayList<Player> players; // List of players in the game
    private ArrayList<Integer> inactiveTerritories; // IDs of inactive victims

    /**
     * Constructor: Initializes the game map, graph, and territories.
     * @param players List of players participating in the game
     * @param rows Number of rows in the game board
     * @param columns Number of columns in the game board
     * @param victims Number of territories marked as inactive
     * @param maxDice Maximum number of dice allowed per territory
     * Preconditions: 'players' must not be null and 'rows', 'columns', and 'maxDice' must be positive.
     * Postconditions: The map is initialized, territories and dice are distributed.
     */
    public Map(ArrayList<Player> players, int rows, int columns, int victims, int maxDice) {
        this.players = players;
        this.ROWS = rows;
        this.COLUMNS = columns;
        this.VICTIMS = victims;
        this.NUMTERRITORIES = ROWS * COLUMNS;
        this.OCCUPIED = NUMTERRITORIES - VICTIMS;
        this.MAXDICE = maxDice;
        this.inactiveTerritories = new ArrayList<>();

        // Initialize the game board (2D array of territories)
        map = new Territory[ROWS][COLUMNS];
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLUMNS; col++) {
                int id = getTerritoryId(row, col);
                map[row][col] = new Territory(this);
                map[row][col].setIdNum(id);
            }
        }

        // Initialize the graph representing territory neighbors
        graph = constructGraph(ROWS, COLUMNS, VICTIMS);

        // Partition territories to players and distribute dice
        partitionTerritories();
        distributeDice();
    }

    /**
     * Method to access for the list of players.
     * @return The list of players.
     */
    public ArrayList<Player> getPlayers() {
        return players;
    }

    /**
     * Method to access for the map (territory array).
     * @return The 2D array representing the game map.
     */
    public Territory[][] getMap() {
        return map;
    }

    /**
     * Method to access for the graph representing the territory connections.
     * @return The graph representing the map's territory relationships.
     */
    public Graph getGraph() {
        return graph;
    }

    /**
     * Method: getTerritory
     * Purpose: get a territory using row and column indices.
     * @param row The row index of the territory.
     * @param column The column index of the territory.
     * @return The territory at the specified row and column.
     */
    public Territory getTerritory(int row, int column) {
        return map[row][column];
    }

    /**
     * Method: getTerritoryId
     * Purpose: gets the unique ID of a territory based on its row and column.
     * @param row The row index of the territory.
     * @param column The column index of the territory.
     * @return The computed territory ID.
     */
    public int getTerritoryId(int row, int column) {
        return row * COLUMNS + column;
    }

    /**
     * Method: countTerritories
     * Purpose: Counts the number of territories owned by a given player.
     * @param player The player whose territories are being counted.
     * @return The number of territories owned by the player.
     */
    public int countTerritories(Player player) {
        int count = 0;
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLUMNS; col++) {
                if (map[row][col].getOwner() == player) {
                    count++;
                }
            }
        }
        return count;
    }

    /**
     * Method: countDice
     * Purpose: Counts the total number of dice owned by a given player.
     * @param player The player whose dice count is being calculated.
     * @return The total dice count of the player.
     */
    public int countDice(Player player) {
        int diceCount = 0;
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLUMNS; col++) {
                if (map[row][col].getOwner() == player) {
                    diceCount += map[row][col].getDice();
                }
            }
        }
        return diceCount;
    }

    /**
     * Method: getNeighbors
     * Purpose: Retrieves neighboring territories of a given territory within the board boundaries.
     * @param cell The territory whose neighbors are being fetched.
     * @return A list of neighboring territories.
     */
    public ArrayList<Territory> getNeighbors(Territory cell) {
        ArrayList<Territory> neighbors = new ArrayList<>();
        int row = cell.getRow();
        int col = cell.getCol();

        // Check neighbors (up, down, left, right) within the board boundaries
        if (row > 0) neighbors.add(map[row - 1][col]); // Up
        if (row < ROWS - 1) neighbors.add(map[row + 1][col]); // Down
        if (col > 0) neighbors.add(map[row][col - 1]); // Left
        if (col < COLUMNS - 1) neighbors.add(map[row][col + 1]); // Right

        return neighbors;
    }

    /**
     * Method: partitionTerritories
     * Purpose: Distributes territories randomly among players.
     * Preconditions: Players must be initialized.
     * Postconditions: Territories are assigned to players.
     */
    private void partitionTerritories() {
        ArrayList<Territory> unownedTerritories = new ArrayList<>();

        // Collect non-victim territories
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLUMNS; col++) {
                int id = getTerritoryId(row, col);
                Territory territory = map[row][col];
                if (!inactiveTerritories.contains(id) && territory.getOwner() == null) {
                    unownedTerritories.add(territory);
                }
            }
        }

        Random rand = new Random();
        int totalTerritories = unownedTerritories.size();
        int territoriesPerPlayer = totalTerritories / players.size();
        int extraTerritories = totalTerritories % players.size();

        // Assign territories equally among players
        for (Player player : players) {
            for (int i = 0; i < territoriesPerPlayer; i++) {
                Territory territory = unownedTerritories.remove(rand.nextInt(unownedTerritories.size()));
                territory.setOwner(player);
            }
        }

        // Distribute any leftover territories
        for (int i = 0; i < extraTerritories; i++) {
            Territory territory = unownedTerritories.remove(rand.nextInt(unownedTerritories.size()));
            Player randomPlayer = players.get(rand.nextInt(players.size()));
            territory.setOwner(randomPlayer);
        }
    }

    /**
     * Method: distributeDice
     * Purpose: Distributes dice randomly across each player's territories.
     * Preconditions: Players and their territories must be initialized.
     * Postconditions: Dice are distributed among territories.
     */
    private void distributeDice() {
        Random rand = new Random();

        // Step 1: Determine the number of dice each player should receive
        int minTerritories = Integer.MAX_VALUE;
        for (Player player : players) {
            minTerritories = Math.min(minTerritories, getPropertyOf(player).size());
        }
        int totalDicePerPlayer = minTerritories * 3;

        for (Player player : players) {
            ArrayList<Territory> playerTerritories = getPropertyOf(player);
            int numTerritories = playerTerritories.size();
            int totalDice = totalDicePerPlayer; // Fixed dice number for every player

            for (Territory territory : playerTerritories) {
                territory.setDice(1);
                totalDice--; 
            }

            boolean territoryMaxDice = false;

            while (totalDice > 0 && !territoryMaxDice) {
                Territory selectedTerritory = playerTerritories.get(rand.nextInt(numTerritories));
                if (selectedTerritory.getDice() < MAXDICE) {
                    selectedTerritory.setDice(selectedTerritory.getDice() + 1);
                    totalDice--;
                }

                // Update the territoryMaxDice variable to check if all territories have reached MAXDICE
                territoryMaxDice = playerTerritories.stream().allMatch(t -> t.getDice() == MAXDICE);
            }
        }
    }

    // Method to get a list of territories owned by a player
    public ArrayList<Territory> getPropertyOf(Player player) {
        ArrayList<Territory> ownedTerritories = new ArrayList<>();
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLUMNS; col++) {
                Territory territory = map[row][col];
                if (territory.getOwner() == player && !isVictim(territory.getIdNum())) {
                    ownedTerritories.add(territory);
                }
            }
        }
        return ownedTerritories;
    }

    // Construct the graph representing the map's territory relationships
    public Graph constructGraph(int rows, int cols, int victims) {
        int totalTerritories = rows * cols;
        Graph graph = new Graph(totalTerritories);
        Random rand = new Random();

        // Step 1: Randomly select victim territories
        while (inactiveTerritories.size() < victims) {
            int victimId = rand.nextInt(totalTerritories);
            if (!inactiveTerritories.contains(victimId)) {
                inactiveTerritories.add(victimId);  // Add the victim territory
            }
        }

        // Step 2: Ensure all territories are connected using a spanning tree
        boolean[] visited = new boolean[totalTerritories];
        ArrayList<Integer> activeTerritories = new ArrayList<>();
        
        // Collect all active (non-victim) territories
        for (int i = 0; i < totalTerritories; i++) {
            if (!inactiveTerritories.contains(i)) {
                activeTerritories.add(i);
            }
        }
        
        // If no active territories are available, return early
        if (activeTerritories.isEmpty()) {
            System.out.println("No active territories available.");
            return graph;
        }

        // Start constructing the spanning tree from the first active territory
        Queue<Integer> queue = new LinkedList<>();
        queue.add(activeTerritories.get(0));
        visited[activeTerritories.get(0)] = true;

        while (!queue.isEmpty()) {
            int currentId = queue.poll();
            int row = currentId / cols;
            int col = currentId % cols;

            // Connect neighbors (ensure that new territories are visited as part of the spanning tree)
            if (col + 1 < cols && !inactiveTerritories.contains(currentId + 1) && !visited[currentId + 1]) {
                graph.addEdge(currentId, currentId + 1);
                queue.add(currentId + 1);
                visited[currentId + 1] = true;
            }
            if (row + 1 < rows && !inactiveTerritories.contains(currentId + cols) && !visited[currentId + cols]) {
                graph.addEdge(currentId, currentId + cols);
                queue.add(currentId + cols);
                visited[currentId + cols] = true;
            }
            if (col - 1 >= 0 && !inactiveTerritories.contains(currentId - 1) && !visited[currentId - 1]) {
                graph.addEdge(currentId, currentId - 1);
                queue.add(currentId - 1);
                visited[currentId - 1] = true;
            }
            if (row - 1 >= 0 && !inactiveTerritories.contains(currentId - cols) && !visited[currentId - cols]) {
                graph.addEdge(currentId, currentId - cols);
                queue.add(currentId - cols);
                visited[currentId - cols] = true;
            }
        }

        // Step 3: Connect any remaining isolated territories if needed
        graphConnectivity(graph, rows, cols);

        return graph;
    }

    // Ensure all active territories are connected in the graph
    private void graphConnectivity(Graph graph, int rows, int cols) {
        boolean[] visited = new boolean[rows * cols];
        ArrayList<Integer> activeTerritories = new ArrayList<>();

        // Step 1: Collect all active (non-victim) territories
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                int currentId = getTerritoryId(row, col);
                if (!inactiveTerritories.contains(currentId)) {
                    activeTerritories.add(currentId);
                }
            }
        }

        if (activeTerritories.isEmpty()) {
            System.out.println("No active territories available.");
            return;
        }

        // Step 2: Perform BFS from the first active territory to mark connected components
        bfs(graph, activeTerritories.get(0), visited);

        // Step 3: Identify isolated territories and forcefully connect them
        for (int i = 0; i < activeTerritories.size(); i++) {
            int territoryId = activeTerritories.get(i);
            if (!visited[territoryId]) {
                // Connect isolated territory to a random visited, connected territory
                for (int j = 0; j < activeTerritories.size(); j++) {
                    if (visited[activeTerritories.get(j)]) {
                        graph.addEdge(activeTerritories.get(j), territoryId);
                        System.out.println("Forcefully connecting isolated territory " + territoryId + " to " + activeTerritories.get(j));
                        bfs(graph, territoryId, visited); // Re-run BFS to ensure it's connected
                        break;
                    }
                }
            }
        }

        // Step 4: Final validation: Check if all territories are connected
        boolean allConnected = true;
        for (int i = 0; i < activeTerritories.size(); i++) {
            if (!visited[activeTerritories.get(i)]) {
                System.out.println("Error: Territory " + activeTerritories.get(i) + " is still isolated.");
                allConnected = false;
            }
        }

        if (allConnected) {
            System.out.println("All territories are successfully connected!");
        } else {
            System.out.println("Warning: Some territories are still isolated.");
        }
    }

    // BFS to traverse the graph and mark visited territories
    private void bfs(Graph graph, int startId, boolean[] visited) {
        Queue<Integer> queue = new LinkedList<>();
        queue.add(startId);
        visited[startId] = true;

        while (!queue.isEmpty()) {
            int currentId = queue.poll();
            for (int neighbor : graph.getAdjacent(currentId)) {
                if (!visited[neighbor]) {
                    visited[neighbor] = true;
                    queue.add(neighbor);
                }
            }
        }
    }

    // Method to check if a territory is a victim
    public boolean isVictim(int id) {
        return inactiveTerritories.contains(id);
    }

    // Method to get enemy neighbors of a given territory
    public ArrayList<Territory> getEnemyNeighbors(Territory territory) {
        ArrayList<Territory> enemyNeighbors = new ArrayList<>();
        for (Territory neighbor : getNeighbors(territory)) {
            if (neighbor.getOwner() != null && neighbor.getOwner() != territory.getOwner()) {
                enemyNeighbors.add(neighbor);
            }
        }
        return enemyNeighbors;
    }

    public Territory getTerritoryById(int id) {
        int row = id / COLUMNS;
        int col = id % COLUMNS;
        return map[row][col];
    }

    // Method to count the largest connected cluster of territories owned by a player
    public int countConnected(Player player) {
        boolean[][] visited = new boolean[ROWS][COLUMNS];  // Track visited territories
        int maxConnected = 0;  // Maximum size of the connected cluster

        // Iterate through all territories and start a BFS/DFS if it belongs to the player
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLUMNS; col++) {
                Territory territory = map[row][col];
                if (!visited[row][col] && territory.getOwner() == player) {
                    // If the territory belongs to the player and is not visited, perform BFS/DFS
                    int clusterSize = bfs(player, row, col, visited);
                    maxConnected = Math.max(maxConnected, clusterSize);
                }
            }
        }

        return maxConnected;
    }

    // Helper method to perform BFS to calculate the size of a connected component
    private int bfs(Player player, int startRow, int startCol, boolean[][] visited) {
        int[] rowOffsets = {-1, 1, 0, 0};  // Up, Down, Left, Right
        int[] colOffsets = {0, 0, -1, 1};

        Queue<int[]> queue = new LinkedList<>();
        queue.add(new int[]{startRow, startCol});
        visited[startRow][startCol] = true;

        int connectedSize = 0;

        while (!queue.isEmpty()) {
            int[] current = queue.poll();
            int currentRow = current[0];
            int currentCol = current[1];
            connectedSize++;

            // Explore all 4 possible neighboring directions
            for (int i = 0; i < 4; i++) {
                int newRow = currentRow + rowOffsets[i];
                int newCol = currentCol + colOffsets[i];

                if (isValid(newRow, newCol) && !visited[newRow][newCol]) {
                    Territory neighbor = map[newRow][newCol];
                    if (neighbor.getOwner() == player) {
                        // If the neighbor belongs to the player, add it to the queue
                        queue.add(new int[]{newRow, newCol});
                        visited[newRow][newCol] = true;
                    }
                }
            }
        }

        return connectedSize;
    }

    // Helper method to check if a row and column are within the board boundaries
    private boolean isValid(int row, int col) {
        return row >= 0 && row < ROWS && col >= 0 && col < COLUMNS;
    }
}