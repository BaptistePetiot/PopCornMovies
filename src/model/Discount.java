package model;

public class Discount {
    private int id;
    private String name;
    private int amount;
    private char unit;
    private String status;
    private boolean isActive;

    public Discount(int id, String name, int amount, char unit, String status) {
        this.id = id;
        this.name = name;
        this.amount = amount;
        this.unit = unit;
        this.status = status;
        this.isActive = status.equals("Active");

    }

    public int getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public int getAmount() {
        return this.amount;
    }

    public char getUnit() {
        return this.unit;
    }

    public String getStatus() {
        return this.status;
    }
}
