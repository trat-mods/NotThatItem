package net.nti.models;

import net.fabricmc.fabric.api.renderer.v1.mesh.MutableQuadView;
import net.fabricmc.fabric.api.renderer.v1.model.FabricBakedModel;
import net.fabricmc.fabric.api.renderer.v1.render.RenderContext;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.model.*;
import net.minecraft.client.render.model.json.ItemModelGenerator;
import net.minecraft.client.render.model.json.JsonUnbakedModel;
import net.minecraft.client.render.model.json.ModelOverrideList;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.nti.loaders.NtiLoader;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

public class FilterModel implements UnbakedModel, BakedModel, FabricBakedModel {

    static final ItemModelGenerator itemModelGenerator = new ItemModelGenerator();
    private static final SpriteIdentifier spriteId = new SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, Identifier.of("nti", "item/filter"));
    private ModelBakeSettings modelBakeSettings;
    private Baker baker;
    private Function<SpriteIdentifier, Sprite> textureGetter;
    private JsonUnbakedModel itemHandheld;
    private UnbakedModel original;
    private BakedModel bakedModel;

    public FilterModel(UnbakedModel original) {
        this.original = original;
    }

    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction face, Random random) {
        return List.of();
    }

    @Override
    public boolean useAmbientOcclusion() {
        return false;
    }

    @Override
    public boolean hasDepth() {
        return false;
    }

    @Override
    public boolean isSideLit() {
        return false;
    }

    @Override
    public boolean isBuiltin() {
        return false;
    }

    @Override
    public Sprite getParticleSprite() {
        return textureGetter.apply(spriteId);
    }

    @Override
    public ModelTransformation getTransformation() {
        return itemHandheld.getTransformations();
    }

    @Override
    public ModelOverrideList getOverrides() {
        return ModelOverrideList.EMPTY;
    }

    @Override
    public boolean isVanillaAdapter() {
        return false;
    }


    @Override
    public void emitItemQuads(ItemStack stack, Supplier<Random> randomSupplier, RenderContext context) {
        var emitter = context.getEmitter();
        bakedModel.emitItemQuads(stack, randomSupplier, context);

        var id = stack.get(NtiLoader.FILTER_ITEM_ID_COMPONENT);
        //NtiLoader.LOGGER.info("data: {}", id);
        if (id != null) {

            var idStack = new ItemStack(Registries.ITEM.get(Identifier.of(id)));

            var path = Identifier.ofVanilla("block/" + Identifier.of(id).getPath());
            NtiLoader.LOGGER.info(path.toString());

            var sprite = new SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, path);
            var dirs = new ArrayList<Direction>() {
                {
                    add(Direction.NORTH);
                    add(Direction.SOUTH);
                }
            };
            for (Direction direction : dirs) {
                emitter.square(direction, 0.375f, 0.375f, 0.625f, 0.625f, 0.455f);
                emitter.spriteBake(sprite.getSprite(), MutableQuadView.BAKE_LOCK_UV);
                emitter.color(-1, -1, -1, -1);
                emitter.emit();
            }
            //            var model = MinecraftClient.getInstance().getItemRenderer().getModels().getModel(Registries.ITEM.get(Identifier.of(id)));
            //            if (model != null) {
            //
            //                //model.emitItemQuads(stack, randomSupplier, context);
            //            }
        }
    }

    @Override
    public Collection<Identifier> getModelDependencies() {
        return List.of();
    }

    @Override
    public void setParents(Function<Identifier, UnbakedModel> modelLoader) {
        itemHandheld = (JsonUnbakedModel) modelLoader.apply(Identifier.of("minecraft", "item/handheld"));
    }

    @Nullable
    @Override
    public BakedModel bake(Baker baker, Function<SpriteIdentifier, Sprite> textureGetter, ModelBakeSettings rotationContainer) {
        this.textureGetter = textureGetter;
        this.modelBakeSettings = rotationContainer;
        this.baker = baker;
        this.bakedModel = itemModelGenerator
                .create(textureGetter, (JsonUnbakedModel) original)
                .bake(baker, (JsonUnbakedModel) this.original, textureGetter, modelBakeSettings, false);
        return this;
    }
}
