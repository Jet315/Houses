package me.jet315.houses.utils.files;

import org.bukkit.inventory.ItemStack;

public class HouseItem extends GUIItem {

    private String commandToPerform;

    public HouseItem(String itemName, ItemStack item, int slotID, String commandToPerform) {
        super(itemName,item,slotID);
        this.itemName = itemName;
        this.item = item;
        this.slotID = slotID;
        this.commandToPerform = commandToPerform;
    }


    public String getCommandToPerform() {
        return commandToPerform;
    }

}
