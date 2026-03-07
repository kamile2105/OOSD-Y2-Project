package model;

public class Game {
    private int gameId;
    private String gameName;
    private int costPerPlay;
    private int ticketsRewarded;

    public Game() {}

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
