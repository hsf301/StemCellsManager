package com.huashengfu.StemCellsManager.entity.goods;

import java.io.Serializable;
import java.util.UUID;

public class Specifications implements Serializable {

    public static final String Table = "Specifications";

    public static final String Id = "id";
    public static final String Specifications = "specifications";
    public static final String Number = "number";
    public static final String Price = "price";
    public static final String Image = "image";
    public static final String NewItem = "newItem";

    private String id = UUID.randomUUID().toString();
    private String specifications;
    private int number;
    private double price;
    private String image;
    private boolean newItem;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSpecifications() {
        return specifications;
    }

    public void setSpecifications(String specifications) {
        this.specifications = specifications;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public boolean isNewItem() {
        return newItem;
    }

    public void setNewItem(boolean newItem) {
        this.newItem = newItem;
    }
}
