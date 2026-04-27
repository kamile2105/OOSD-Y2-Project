// Kamile Kacinskaite
// Arcade Ticketing System Project
// C00312390
// Date submitted: 21/04/2026

package model;

/**
 * represents a customer in the arcade system.
 * stores personal details and the number of tickets the customer currently holds.
 */
public class Customer {
    private int customerId;         // Unique ID assigned by the database
    private String name;            // Full name of the customer
    private String email;           // Email address, used for login
    private int tickets;            // Current ticket balance

    // Default constructor required for manual object construction (e.g. when reading from ResultSet)
    public Customer() {}

    // Full constructor for creating a Customer with all fields.
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