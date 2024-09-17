package net.nti.loaders;

import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.nti.gui.FilterGui;
import net.nti.gui.FilterScreen;

public class NtiClientLoader implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        HandledScreens.<FilterGui, FilterScreen>register(NtiLoader.FILTER_SCREEN_HANDLER, FilterScreen::new);
        //ModelLoadingPlugin.register(new NtiModelLoadingPlugin());
    }
}
