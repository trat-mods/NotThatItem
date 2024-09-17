package net.nti.models;

import net.fabricmc.fabric.api.client.model.loading.v1.ModelLoadingPlugin;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.util.Identifier;
import net.nti.loaders.NtiLoader;

public class NtiModelLoadingPlugin implements ModelLoadingPlugin {
    public static final ModelIdentifier FILTER_MODEL_ID = new ModelIdentifier(Identifier.of("nti", "filter"), "inventory");

    @Override
    public void onInitializeModelLoader(Context pluginContext) {
        pluginContext.modifyModelOnLoad().register((original, context) -> {
            // This is called for every model that is loaded, so make sure we only target ours
            final ModelIdentifier id = context.topLevelId();
            if (id != null && id.equals(FILTER_MODEL_ID)) {
                NtiLoader.LOGGER.info("custom model {}", id);
                return new FilterModel(original);
            }
            else {
                // If we don't modify the model we just return the original as-is
                return original;
            }
        });
    }
}
