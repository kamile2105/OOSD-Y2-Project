package model;

public class Customer {
    private int customerId;
    private String name;
    private String email;
    private int tickets;

    public Customer() {}

    public Customer(int customerId, String name, String email, int tickets) {
        this.customerId = customerId;
        this.name = name;
        this.email = email;
        this.tickets = tickets;
    }

    // getters and setters for the class customer
    public int getCustomerId()
    { return customerId; }

    public void setCustomerId(int customerId)
    { this.customerId = customerId; }

    public String getName()
    { return name; }

    public void setName(String name)
    { this.name = name; }

    public String getEmail()
    { return email; }

    public void setEmail(String email)
    { this.email = email; }

    public int getTickets()
    { return tickets; }

    public void setTickets(int tickets)
    { this.tickets = tickets; }
}