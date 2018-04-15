package me.jet315.houses.utils.files;

import org.bukkit.inventory.ItemStack;

public class RentItem extends GUIItem{

    private int rentDays;

    RentItem(String itemName, ItemStack item, int slotID,int rentDays) {
        super(itemName, item, slotID);
        this.rentDays = rentDays;
    }

    public int getRentDays() {
        return rentDays;
    }
}
