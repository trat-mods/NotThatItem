package net.nti.mixin;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static net.nti.loaders.NtiLoader.FILTER_ITEM_ID_COMPONENT;

@Mixin(PlayerInventory.class)
public class PlayerInventoryMixin {
    @Inject(method = "insertStack(Lnet/minecraft/item/ItemStack;)Z", at = @At("HEAD"), cancellable = true)
    private void insertStack(ItemStack stack, CallbackInfoReturnable<Boolean> info) {
        PlayerInventory inv = (PlayerInventory) (Object) this;
        var offHandComponent = inv.offHand.getFirst().getOrDefault(FILTER_ITEM_ID_COMPONENT, null);
        var mainHandComponent = inv.getMainHandStack().getOrDefault(FILTER_ITEM_ID_COMPONENT, null);
        var stackItemId = Registries.ITEM.getId(stack.getItem()).toString();
        if (stackItemId.equals(offHandComponent) || stackItemId.equals(mainHandComponent)) {
            info.setReturnValue(false);
            info.cancel();
        }
    }
}
