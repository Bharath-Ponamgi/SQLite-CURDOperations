package com.example.bharath.stock;

/**
 * Created by Bharath on 12/14/2016
 */

class Model {

    private String name;
    private int inStock;
    private int outStock;
    private int totStock;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getInStock() {
        return inStock;
    }

    public void setInStock(int inStock) {
        this.inStock = inStock;
    }

    public int getOutStock() {
        return outStock;
    }

    public void setOutStock(int outStock) {
        this.outStock = outStock;
    }

    public int getTotStock() {
        return totStock;
    }

    public void setTotStock(int totStock) {
        this.totStock = totStock;
    }
}
