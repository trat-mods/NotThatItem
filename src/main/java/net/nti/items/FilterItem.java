package net.nti.items;

import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.StackReference;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.registry.Registries;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import net.nti.gui.FilterGui;
import net.nti.loaders.NtiLoader;
import net.nti.network.packet.FilterStackIdChangePayload;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static net.nti.loaders.NtiLoader.FILTER_ITEM_ID_COMPONENT;

public class FilterItem extends Item implements ExtendedScreenHandlerFactory<FilterStackIdChangePayload> {
    public static final Identifier ID = Identifier.of(NtiLoader.MOD_ID, "filter");

    public FilterItem() {
        super(new Item.Settings().component(FILTER_ITEM_ID_COMPONENT, Registries.ITEM.getId(Items.AIR.asItem()).toString()));
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if (!hand.equals(Hand.MAIN_HAND)) {
            return TypedActionResult.pass(user.getStackInHand(hand));
        }
        user.openHandledScreen(this);
        return TypedActionResult.success(user.getStackInHand(hand));
    }

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
        var item = stack.getOrDefault(FILTER_ITEM_ID_COMPONENT, null);
        if (item == null) {
            return;
        }

        tooltip.add(Text.translatable("item.nti.filter.info", Registries.ITEM.get(Identifier.of(item)).getName()).formatted(Formatting.GOLD));
    }

    @Override
    public FilterStackIdChangePayload getScreenOpeningData(ServerPlayerEntity player) {
        return new FilterStackIdChangePayload(player.getMainHandStack().get(FILTER_ITEM_ID_COMPONENT));
    }

    @Override
    public Text getDisplayName() {
        return Text.translatable(this.getTranslationKey());
    }

    @Nullable
    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
        var stack = player.getMainHandStack();
        return new FilterGui(syncId, playerInventory, StackReference.of(() -> stack, (s) -> {}), stack.get(FILTER_ITEM_ID_COMPONENT));
    }
}