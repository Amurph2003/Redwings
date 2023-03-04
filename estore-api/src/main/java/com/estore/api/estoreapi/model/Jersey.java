package com.estore.api.estoreapi.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents a hockey jersey being sold.
 * 
 * @author Team Red Wings
 */
public class Jersey {

    static final String STRING_FORMAT = "Jersey [id=%d, name=%s, number=%d, price=%f, size=%s, color=%s, quantity=%d, imageUrl=%s]";

    @JsonProperty("id") private int id;
    @JsonProperty("name") private String name;
    @JsonProperty("number") private int number;
    @JsonProperty("price") private double price;
    @JsonProperty("size") private Size size;
    @JsonProperty("color") private Color color;
    @JsonProperty("quantity") private int quantity;
    @JsonProperty("imageUrl") private String imageUrl;

    /**
     * Create a hockey jersey.
     * @param name The player name on the back of the jersey.
     * @param number The player number on the jersey.
     * @param price The price of the jersey.
     * @param size The jersey size.
     * @param color The jersey color.
     * @param quantity The number of jerseys of this type there are.
     * @param imageUrl The URL of the image representing this jersey within the project.
     */
    public Jersey(@JsonProperty("id") int id, @JsonProperty("name") String name, @JsonProperty("number") int number, @JsonProperty("price") double price, @JsonProperty("size") Size size, @JsonProperty("color") Color color, @JsonProperty("quantity") int quantity, @JsonProperty("imageUrl") String imageUrl) {
        this.id = id;
        this.name = name;
        this.number = number;
        this.price = price;
        this.size = size;
        this.color = color;
        this.quantity = quantity;
        this.imageUrl = imageUrl;
    }

    // Will write getters and setters as needed

    public int getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public int getNumber() {
        return this.number;
    }

    public double getPrice() {
        return this.price;
    }

    public Size getSize() {
        return this.size;
    }

    public Color getColor() {
        return this.color;
    }

    public int getQuantity() {
        return this.quantity;
    }

    public String getImageUrl() {
        return this.imageUrl;
    }

    public void setColor(Color c){
        this.color = c;
    }

    public void setSize(Size s){
        this.size = s;
    }

    public void setImageUrl(String imageUrl){
        this.imageUrl = imageUrl;
    }

    public void addToQuantity(int add) {
        this.quantity += add;
    }

    public void decrementQuantity() {
        if(this.quantity > 0) {
            this.quantity--;
        }
    }

    /**
     * Jersey Conflicts Method
     * @param jersey is new jersey to compare to current
     * @return true/false whether a jersey conflicts with this one
     */
    public boolean conflicts(Object o) {
        if(o instanceof Jersey) {
            Jersey other = (Jersey)o;
            if(this.number == other.getNumber()
                && !this.name.equals(other.getName())) {
                    return true;
                } else {
                    return false;
                }
        } else {
            return false;
        }
    }

    @Override
    public boolean equals(Object o) {
        if(o instanceof Jersey) {
            Jersey other = (Jersey)o;
            if(this.name.equals(other.getName())
                && this.number == other.getNumber()
                && this.price == other.getPrice()
                && this.size.equals(other.getSize())
                && this.color.equals(other.getColor())) {
                    return true;
                } else {
                    return false;
                }
        } else {
            return false;
        }
    }


    @Override
    public String toString() {return String.format(STRING_FORMAT, id, name, number, price, size, color, quantity, imageUrl);}

}  