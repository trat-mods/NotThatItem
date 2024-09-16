package net.nti.initializers;

import com.mojang.serialization.Codec;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.component.ComponentType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.nti.items.FilterItem;
import net.nti.loaders.NtiLoader;

public final class NtiItems {
    public static final ComponentType<String> FILTER_ITEM_ID_COMPONENT = Registry.register(Registries.DATA_COMPONENT_TYPE, Identifier.of(NtiLoader.MOD_ID, "filter_item_id"),
                                                                                           ComponentType.<String>builder().codec(Codec.STRING).build());
    public static Item FILTER_ITEM = Registry.register(Registries.ITEM, FilterItem.ID, new FilterItem());

    public static void initialize() {
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.REDSTONE).register(content -> content.add(FILTER_ITEM));
    }
}
