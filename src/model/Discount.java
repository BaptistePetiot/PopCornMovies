package model;

/**
 * Discount class, unrelated to any other, just contains all the information about a discount
 * @author Baptiste Petiot
 */
public class Discount {
    private int id;
    private String name;
    private int amount;
    private char unit;
    private String status;
    private boolean isActive;

    /**
     *
     * @param id : int
     * @param name : String
     * @param amount : int
     * @param unit : char
     * @param status : status
     */
    public Discount(int id, String name, int amount, char unit, String status) {
        this.id = id;
        this.name = name;
        this.amount = amount;
        this.unit = unit;
        this.status = status;
        this.isActive = status.equals("Active");

    }

    /**
     * allows to get the discount id
     * @return id : int
     */
    public int getId() {
        return this.id;
    }

    /**
     * allows to get the discount name
     * @return name : String
     */
    public String getName() {
        return this.name;
    }

    /**
     * allows to get the discount amount
     * @return amount : int
     */
    public int getAmount() {
        return this.amount;
    }

    /**
     * allows to get the discount unit
     * @return unit : char
     */
    public char getUnit() {
        return this.unit;
    }

    /**
     * allows to get the discount status
     * @return status : String
     */
    public String getStatus() {
        return this.status;
    }
}
