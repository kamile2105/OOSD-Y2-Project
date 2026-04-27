// Kamile Kacinskaite
// Arcade Ticketing System Project
// C00312390
// Date submitted: 21/04/2026

package model;

/**
 * represents a prize that customers can redeem using their tickets.
 * tracks the prize name, how many tickets it costs, and how many are left in stock.
 */

public class Prize {
    private int prizeId;
    private String prizeName;
    private int ticketCost;
    private int stock;

    // Default constructor required for manual object construction (e.g. when reading from ResultSet)
    public Prize() {}

    // Full constructor for creating a Prize with all fields.
    public Prize(int prizeId, String prizeName, int ticketCost, int stock) {
        this.prizeId = prizeId;
        this.prizeName = prizeName;
        this.ticketCost = ticketCost;
        this.stock = stock;
    }

    // getters and setters for class prize
    public int getPrizeId()
    { return prizeId; }

    public void setPrizeId(int prizeId)
    { this.prizeId = prizeId; }

    public String getPrizeName()
    { return prizeName; }

    public void setPrizeName(String prizeName)
    { this.prizeName = prizeName; }

    public int getTicketCost()
    { return ticketCost; }

    public void setTicketCost(int ticketCost)
    { this.ticketCost = ticketCost; }

    public int getStock()
    { return stock; }

    public void setStock(int stock)
    { this.stock = stock; }
}