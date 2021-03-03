package io.github.thebusybiscuit.slimefun4.api.events;

import io.github.thebusybiscuit.slimefun4.implementation.items.tools.ExplosiveTool;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;

/**
 * This {@link Event} is called when a {@link Block} is destroyed by an {@link ExplosiveTool}.
 *
 * @author GallowsDove
 *
 */
public class ExplosiveToolBreakBlockEvent extends PlayerEvent implements Cancellable {

    private static final HandlerList handlers = new HandlerList();

    private final ItemStack tool;
    private final ExplosiveTool explosiveTool;
    private final ArrayList<Block> blocks;
    private boolean cancelled;

    @ParametersAreNonnullByDefault
    public ExplosiveToolBreakBlockEvent(Player player, ArrayList<Block> blocks, ItemStack item, ExplosiveTool explosiveTool) {
        super(player);
        this.blocks = blocks;
        this.tool = item;
        this.explosiveTool = explosiveTool;
    }

    /**
     * Gets the {@link Block} ArrayList of blocks destroyed in the event
     *
     */
    @Nonnull
    public ArrayList<Block> getBlocks() {
        return this.blocks;
    }

    /**
     * Gets the {@link ExplosiveTool} which triggered this event
     *
     */
    @Nonnull
    public ExplosiveTool getExplosiveTool() {
        return this.explosiveTool;
    }

    /**
     * Gets the {@link ItemStack} of the tool used to destroy this block
     *
     */
    @Nonnull
    public ItemStack getTool() {
        return this.tool;
    }



    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean b) {
        this.cancelled = true;
    }

    @Nonnull
    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Nonnull
    @Override
    public HandlerList getHandlers() {
        return getHandlerList();
    }
}
