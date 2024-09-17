package net.nti.gui;

import io.github.cottonmc.cotton.gui.ItemSyncedGuiDescription;
import io.github.cottonmc.cotton.gui.widget.WPlainPanel;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.StackReference;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.nti.gui.widgets.WItemIdentifier;
import net.nti.loaders.NtiLoader;
import net.nti.network.packet.FilterStackIdChangePayload;

public class FilterGui extends ItemSyncedGuiDescription {

    public FilterGui(int syncId, PlayerInventory playerInventory, StackReference owner, String initialComponentId) {
        super(NtiLoader.FILTER_SCREEN_HANDLER, syncId, playerInventory, owner);

        WPlainPanel root = new WPlainPanel();
        root.setSize(178, 134);
        setRootPanel(root);
        NtiLoader.LOGGER.info("data equal to {}", initialComponentId);
        WItemIdentifier identifier = new WItemIdentifier(playerInventory.player, this::updateFilteringComponentData, initialComponentId);
        root.add(identifier, 34, 17);

        //root.setInsets(Insets.ROOT_PANEL);
        root.add(this.createPlayerInventoryPanel(), 8, 40);
        root.validate(this);
    }

    @Override
    public boolean canUse(PlayerEntity entity) {
        return Registries.ITEM.getId(ownerStack.getItem()).equals(Registries.ITEM.getId(owner.get().getItem()));
    }

    private void updateFilteringComponentData(Identifier id) {
        //ownerStack.set(NtiItems.FILTER_ITEM_ID_COMPONENT, id.toString());
        if (!playerInventory.player.getWorld().isClient) return;
        new FilterStackIdChangePayload(id.toString()).send();
        // This must be done on server. Either create a packet, send to server and do the modification on the server callback
        NtiLoader.LOGGER.info("filtering id changed {}", id);
    }
}
