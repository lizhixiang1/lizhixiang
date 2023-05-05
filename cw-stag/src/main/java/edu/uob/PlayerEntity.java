package edu.uob;
import java.util.HashMap;
import java.util.Map;

public class PlayerEntity extends GameEntity{
    private int health;

    private Map<String,Integer> inventory;

    LocationEntity location;
    public PlayerEntity(String name) {
        super(name,null);
        health=3;
        inventory=new HashMap<>();
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public Map<String, Integer> getInventory() {
        return inventory;
    }

    public void setInventory(Map<String, Integer> inventory) {
        this.inventory = inventory;
    }
}
