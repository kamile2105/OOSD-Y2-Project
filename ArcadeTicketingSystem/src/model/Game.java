// Kamile Kacinskaite
// Arcade Ticketing System Project
// C00312390
// Date submitted: 21/04/2026

package model;

/**
 * represents an arcade game available for customers to play.
 * tracks how many credits it costs to play and how many tickets it awards on a win/play.
 */

public class Game {
    private int gameId;
    private String gameName;
    private int costPerPlay;
    private int ticketsRewarded;

    // Default constructor required for manual object construction (e.g. when reading from ResultSet)

    public Game() {}

    // Full constructor for creating a Game with all fields.

    public Game(int gameId, String gameName, int costPerPlay, int ticketsRewarded) {
        this.gameId = gameId;
        this.gameName = gameName;
        this.costPerPlay = costPerPlay;
        this.ticketsRewarded = ticketsRewarded;
    }

    // getters and setters for the class game
    public int getGameId()
    { return gameId; }

    public void setGameId(int gameId)
    { this.gameId = gameId; }

    public String getGameName()
    { return gameName; }

    public void setGameName(String gameName)
    { this.gameName = gameName; }

    public int getCostPerPlay()
    { return costPerPlay; }

    public void setCostPerPlay(int costPerPlay)
    { this.costPerPlay = costPerPlay; }

    public int getTicketsRewarded()
    { return ticketsRewarded; }

    public void setTicketsRewarded(int ticketsRewarded)
    { this.ticketsRewarded = ticketsRewarded; }
}
