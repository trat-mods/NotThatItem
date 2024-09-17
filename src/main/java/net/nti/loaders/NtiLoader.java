package net.nti.loaders;

import com.mojang.serialization.Codec;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.minecraft.component.ComponentType;
import net.minecraft.inventory.StackReference;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.nti.gui.FilterGui;
import net.nti.items.FilterItem;
import net.nti.network.packet.FilterStackIdChangePayload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NtiLoader implements ModInitializer {
    public static final String MOD_ID = "nti";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static final ComponentType<String> FILTER_ITEM_ID_COMPONENT = Registry.register(Registries.DATA_COMPONENT_TYPE, Identifier.of(NtiLoader.MOD_ID, "filter_item_id"),
                                                                                           ComponentType.<String>builder().codec(Codec.STRING).build());
    public static Item FILTER_ITEM = Registry.register(Registries.ITEM, FilterItem.ID, new FilterItem());
    public static ExtendedScreenHandlerType<FilterGui, FilterStackIdChangePayload> FILTER_SCREEN_HANDLER = Registry.register(Registries.SCREEN_HANDLER, FilterItem.ID,
                                                                                                                             new ExtendedScreenHandlerType<>(
                                                                                                                                     (syncId, inventory, data) -> new FilterGui(
                                                                                                                                             syncId, inventory,
                                                                                                                                             StackReference.EMPTY, data.id()),
                                                                                                                                     FilterStackIdChangePayload.CODEC));

    @Override
    public void onInitialize() {
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.REDSTONE).register(content -> content.add(FILTER_ITEM));

        PayloadTypeRegistry.playC2S().register(FilterStackIdChangePayload.ID, FilterStackIdChangePayload.CODEC);
        ServerPlayNetworking.registerGlobalReceiver(FilterStackIdChangePayload.ID, (payload, context) -> {
            NtiLoader.LOGGER.info("packet received on server, setting component data of player {} to {}", context.player().getDisplayName(), payload.id());
            var stack = context.player().getMainHandStack();
            stack.set(FILTER_ITEM_ID_COMPONENT, payload.id());
        });
    }
}