package net.nti.items;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.StackReference;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.registry.Registries;
import net.minecraft.screen.SimpleNamedScreenHandlerFactory;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import net.nti.gui.FilterGui;
import net.nti.initializers.NtiItems;
import net.nti.loaders.NtiLoader;

import java.util.List;

public class FilterItem extends Item {
    public static final Identifier ID = Identifier.of(NtiLoader.MOD_ID, "filter");

    public FilterItem() {
        super(new Item.Settings());
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        user.openHandledScreen(new SimpleNamedScreenHandlerFactory((syncId, playerInventory, player) -> new FilterGui(syncId, playerInventory, new StackReference() {
            @Override
            public ItemStack get() {
                return user.getStackInHand(hand);
            }

            @Override
            public boolean set(ItemStack stack) {
                return false;
            }
        }), Text.translatable(this.getTranslationKey())));
        return TypedActionResult.pass(user.getStackInHand(hand));
    }

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
        var item = stack.getOrDefault(NtiItems.FILTER_ITEM_ID_COMPONENT, null);
        if (item == null) {
            return;
        }

        tooltip.add(Text.translatable("item.nti.filter.info", Registries.ITEM.get(Identifier.of(item)).getName()).formatted(Formatting.GOLD));
    }

}