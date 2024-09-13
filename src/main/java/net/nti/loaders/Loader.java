package net.multihopper.loaders;

import net.fabricmc.api.ModInitializer;
import net.multihopper.initializers.Blocks;
import net.multihopper.initializers.Entities;
import net.multihopper.initializers.Items;
import net.multihopper.initializers.ScreenHandlers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Loader implements ModInitializer {
    public static final String MOD_ID = "nti";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        Items.initialize();
        Blocks.initialize();
        Entities.initialize();
        ScreenHandlers.initialize();
    }
}