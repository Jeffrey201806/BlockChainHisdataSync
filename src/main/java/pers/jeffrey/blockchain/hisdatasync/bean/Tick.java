package pers.jeffrey.blockchain.hisdatasync.bean;

/**
 * @author zengjianfeng
 * @name
 * @desc
 * @jdk
 * @group
 * @os
 * @date 2018-06-08
 */
public class Tick {
    private String id;
    private double price;
    private double amount;
    private String direction;
    private long ts;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public long getTs() {
        return ts;
    }

    public void setTs(long ts) {
        this.ts = ts;
    }

    @Override
    public String toString() {
        return "Tick{" +
                "id=" + id +
                ", price=" + price +
                ", amount=" + amount +
                ", direction='" + direction + '\'' +
                ", ts=" + ts +
                '}';
    }
}
