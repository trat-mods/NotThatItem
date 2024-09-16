package net.nti.gui;

import io.github.cottonmc.cotton.gui.client.CottonInventoryScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;

public class FilterScreen extends CottonInventoryScreen<FilterGui> {

    public FilterScreen(FilterGui description, PlayerInventory inventory, Text title) {
        super(description, inventory, title);
    }
}
