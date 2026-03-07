package model;

public class Prize {
    private int prizeId;
    private String prizeName;
    private int ticketCost;
    private int stock;

    public Prize() {}

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