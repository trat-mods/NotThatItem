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
            var item = Registries.ITEM.getOrEmpty(Identifier.of(initialId));
            item.ifPresent(value -> this.icon = new ItemIcon(value).setGhost(true));
        }

    }

    @Override
    public void paint(DrawContext context, int x, int y, int mouseX, int mouseY) {
        BackgroundPainter.SLOT.paintBackground(context, x, y, this);
        if (icon != null) {
            icon.paint(context, x + 1, y + 1, 16);
        }
    }

    @Override
    public InputResult onClick(int x, int y, int button) {
        var item = player.currentScreenHandler.getCursorStack().getItem();
        var id = Registries.ITEM.getId(item);
        if (id.equals(Registries.ITEM.getId(Items.AIR))) {

            onIdChange.accept(id);
            this.icon = null;
            return InputResult.IGNORED;
        }
        onIdChange.accept(id);
        this.icon = new ItemIcon(item);
        //player.getInventory().insertStack(player.currentScreenHandler.getCursorStack());
        return InputResult.PROCESSED;
    }

}
