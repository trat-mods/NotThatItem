package net.nti.loaders;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.inventory.StackReference;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.resource.featuretoggle.FeatureFlags;
import net.minecraft.screen.ScreenHandlerType;
import net.nti.gui.FilterGui;
import net.nti.initializers.NtiItems;
import net.nti.items.FilterItem;
import net.nti.network.packet.FilterStackIdChangePayload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NtiLoader implements ModInitializer {
    public static final String MOD_ID = "nti";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    public static ScreenHandlerType<FilterGui> FILTER_SCREEN_HANDLER = Registry.register(Registries.SCREEN_HANDLER, FilterItem.ID, new ScreenHandlerType<>(
            (syncId, inventory) -> new FilterGui(syncId, inventory, StackReference.of(inventory::getMainHandStack, (s) -> NtiLoader.LOGGER.info(s.toString()))),
            FeatureFlags.VANILLA_FEATURES));

    @Override
    public void onInitialize() {
        NtiItems.initialize();
        PayloadTypeRegistry.playC2S().register(FilterStackIdChangePayload.ID, FilterStackIdChangePayload.CODEC);
        ServerPlayNetworking.registerGlobalReceiver(FilterStackIdChangePayload.ID, (payload, context) -> {
            NtiLoader.LOGGER.info("packet received on server, setting component data of player {} to {}", context.player().getDisplayName(), payload.id());
            context.player().getInventory().getMainHandStack().set(NtiItems.FILTER_ITEM_ID_COMPONENT, payload.id());
        });
    }
}