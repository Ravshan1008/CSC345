import java.util.*;

/**
 * Author: Ravshanbek Temurbekov
 * Course: CSC345, Program#2
 * 
 * Territory class represents a territory on the game map.
 * Each territory is owned by a player and has a certain number of dice.
 * The class also provides methods of getters and setters like owner, dice count and ID.
 * The map that this territory is a part of is also maintained.
 */
public class Territory {

    private Map map;  // The map this territory belongs to
    private int dice;  // Number of dice in this territory
    private int idNum;  // Unique ID number of the territory 
    private Player owner;  // The player who owns this territory

    /**
     * Constructor: Initializes a new territory with no owner and uninitialized dice and ID.
     *
     * @param map The map this territory is part of
     * Precondition: Map object must be initialized
     * Postcondition: Territory object created with default values
     */
    public Territory(Map map) {
        this.map = map;
        this.dice = -1;  
        this.idNum = -1;  
        this.owner = null; 
    }

    /**
     * Constructor: Initializes a new territory with a specified owner, dice count, and ID.
     *
     * @param map The map this territory is part of
     * @param owner The player who owns the territory
     * @param dice The number of dice in the territory
     * @param idNum The unique ID of the territory
     * Precondition: Map, owner, and valid dice count and ID needs be provided
     * Postcondition: Territory object created with the given attributes
     */
    public Territory(Map map, Player owner, int dice, int idNum) {
        this.map = map;
        this.owner = owner;
        this.dice = dice;
        this.idNum = idNum;
    }
    
    /**
     * Get the number of dice in this territory.
     *
     * @return The number of dice in this territory
     */
    public int getDice() {
        return dice;
    }

    /**
     * Get the unique ID number of this territory.
     *
     * @return The ID number of this territory
     */
    public int getIdNum() {
        return idNum;
    }

    /**
     * Get the map this territory belongs to.
     *
     * @return The map object associated with this territory
     */
    public Map getMap() {
        return map;
    }

    /**
     * Get the owner of this territory.
     *
     * @return The player who owns this territory
     */
    public Player getOwner() {
        return owner;
    }

    // Setters (Mutators)

    /**
     * Set the number of dice for this territory.
     *
     * @param dice The new number of dice to assign
     * Precondition: Dice count should be a non-negative integer
     * Postcondition: The dice count of this territory is updated
     */
    public void setDice(int dice) {
        this.dice = dice;
    }

    /**
     * Set the ID number for this territory.
     *
     * @param idNum The unique ID number for this territory
     * Precondition: ID number must be non-negative
     * Postcondition: The ID of this territory is updated
     */
    public void setIdNum(int idNum) {
        this.idNum = idNum;
    }

    /**
     * Set the owner for this territory.
     *
     * @param owner The new owner (Player) of this territory
     * Precondition: Owner should be a valid Player object
     * Postcondition: The owner of this territory is updated
     */
    public void setOwner(Player owner) {
        this.owner = owner;
    }

    /**
     * Get the row number of this territory based on its ID number.
     * 
     * @return The row number of the territory
     * Precondition: ID number must be a valid non-negative integer
     * Postcondition: Returns the calculated row number
     */
    public int getRow() {
        return idNum / 8;
    }

    /**
     * Get the column number of this territory based on its ID number.
     *
     * @return The column number of the territory
     * Precondition: ID number must be a valid non-negative integer
     * Postcondition: Returns the calculated column number
     */
    public int getCol() {
        return idNum % 8;
    }
}
