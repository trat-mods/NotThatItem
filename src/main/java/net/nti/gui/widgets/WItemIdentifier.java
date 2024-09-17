package net.nti.gui.widgets;

import io.github.cottonmc.cotton.gui.client.BackgroundPainter;
import io.github.cottonmc.cotton.gui.widget.WWidget;
import io.github.cottonmc.cotton.gui.widget.data.InputResult;
import io.github.cottonmc.cotton.gui.widget.icon.ItemIcon;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.nti.items.FilterItem;
import net.nti.loaders.NtiLoader;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public class WItemIdentifier extends WWidget {
    private final PlayerEntity player;
    private final Consumer<Identifier> onIdChange;
    private ItemIcon icon;

    public WItemIdentifier(PlayerEntity player, Consumer<Identifier> onIdChange, @Nullable String initialId) {
        this.player = player;
        this.onIdChange = onIdChange;
        if (initialId != null) {
            var item = Registries.ITEM.get(Identifier.of(initialId));
            var icon = new ItemIcon(item).setGhost(true);
            NtiLoader.LOGGER.info("setting icon to {}", item.getName());
            this.icon = icon;
        }
    }

    @Override
    public void paint(DrawContext context, int x, int y, int mouseX, int mouseY) {
        BackgroundPainter.SLOT.paintBackground(context, x, y, this);
        if (this.icon != null) {
            this.icon.paint(context, x + 1, y + 1, 16);
        }
    }

    @Override
    public InputResult onClick(int x, int y, int button) {
        var item = player.currentScreenHandler.getCursorStack().getItem();
        var id = Registries.ITEM.getId(item);
        if (id.equals(FilterItem.ID)) {
            return InputResult.IGNORED;
        }
        if (id.equals(Registries.ITEM.getId(Items.AIR))) {
            onIdChange.accept(id);
            this.icon = null;
            return InputResult.IGNORED;
        }
        onIdChange.accept(id);
        this.icon = new ItemIcon(item).setGhost(true);
        return InputResult.PROCESSED;
    }

}
