package io.github.thebusybiscuit.slimefun4.implementation.tasks.armor;

import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

import io.github.thebusybiscuit.slimefun4.api.items.HashedArmorpiece;
import io.github.thebusybiscuit.slimefun4.api.player.PlayerProfile;
import io.github.thebusybiscuit.slimefun4.core.attributes.Radioactive;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;
import io.github.thebusybiscuit.slimefun4.implementation.items.armor.SlimefunArmorPiece;

import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;

/**
 * The {@link SlimefunArmorTask} is responsible for handling {@link PotionEffect PotionEffects} for
 * {@link Radioactive} items or any {@link SlimefunArmorPiece}.
 * It also handles the prevention of radiation through a Hazmat Suit
 * 
 * @author TheBusyBiscuit
 * @author martinbrom
 * @author Semisol
 *
 */
public class SlimefunArmorTask extends AbstractArmorTask {

    @Override
    protected void onTick() {
        // Do nothing
    }

    @Override
    @ParametersAreNonnullByDefault
    protected void onPlayerTick(Player p, PlayerProfile profile) {
        ItemStack[] armor = p.getInventory().getArmorContents();
        updateAndHandleArmor(p, armor, profile.getArmor());
    }

    @ParametersAreNonnullByDefault
    private void updateAndHandleArmor(Player p, ItemStack[] armor, HashedArmorpiece[] cachedArmor) {
        for (int slot = 0; slot < 4; slot++) {
            ItemStack item = armor[slot];
            HashedArmorpiece armorPiece = cachedArmor[slot];

            if (armorPiece.hasDiverged(item)) {
                SlimefunItem sfItem = SlimefunItem.getByItem(item);

                if (!(sfItem instanceof SlimefunArmorPiece)) {
                    // If it isn't actually Armor, then we won't care about it.
                    sfItem = null;
                }

                armorPiece.update(item, sfItem);
            }

            if (item != null && armorPiece.getItem().isPresent()) {
                SlimefunPlugin.runSync(() -> {
                    SlimefunArmorPiece sfArmorPiece = armorPiece.getItem().get();

                    if (sfArmorPiece.canUse(p, true)) {
                        onArmorPieceTick(p, sfArmorPiece, item);
                    }
                });
            }
        }
    }

    /**
     * Method to handle behavior for pieces of armor.
     * It is called per-player and per piece of armor.
     *
     * @param p
     *            The {@link Player} wearing the piece of armor
     * @param sfArmorPiece
     *            {@link SlimefunArmorPiece} Slimefun instance of the piece of armor
     * @param armorPiece
     *            The actual {@link ItemStack} of the armor piece
     */
    @ParametersAreNonnullByDefault
    protected void onArmorPieceTick(Player p, SlimefunArmorPiece sfArmorPiece, ItemStack armorPiece) {
        for (PotionEffect effect : sfArmorPiece.getPotionEffects()) {
            p.removePotionEffect(effect.getType());
            p.addPotionEffect(effect);
        }
    }
}