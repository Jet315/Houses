package me.jet315.houses.utils.files;

import org.bukkit.inventory.ItemStack;

public abstract class GUIItem {

    protected String itemName;
    protected ItemStack item;
    protected int slotID;

    GUIItem(String itemName, ItemStack item, int slotID){
        this.itemName = itemName;
        this.item = item;
        this.slotID = slotID;
    }


    public String getItemName() {
        return itemName;
    }

    public ItemStack getItem() {
        return item;
    }

    public int getSlotID() {
        return slotID;
    }

    /**
     * Method to check whether the item being parsed in is equal to the item in stored in this class
     */
    public boolean isItemEqual(ItemStack possibleItem) {

        if (possibleItem.getType() == item.getType()) {

            if (possibleItem.getDurability() == item.getDurability()) {

                if (possibleItem.getItemMeta().getDisplayName().equals(item.getItemMeta().getDisplayName())) {

                    return true;

                }
            }
        }
        return false;
    }
}
