package io.github.thebusybiscuit.slimefun4.implementation.tasks.armor;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.cscorelib2.chat.ChatColors;
import io.github.thebusybiscuit.slimefun4.api.player.PlayerProfile;
import io.github.thebusybiscuit.slimefun4.core.attributes.ProtectionType;
import io.github.thebusybiscuit.slimefun4.core.attributes.Radioactive;
import io.github.thebusybiscuit.slimefun4.core.radiation.RadiationSymptom;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;
import io.github.thebusybiscuit.slimefun4.implementation.items.RadioactiveItem;
import io.github.thebusybiscuit.slimefun4.utils.RadiationUtils;

import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;

/**
 * The {@link RadiationTask} handles radioactivity for
 * {@link Radioactive} items.
 *
 * @author Semisol
 *
 */
public class RadiationTask extends AbstractArmorTask {

    private final RadiationSymptom[] symptoms = RadiationSymptom.values();

    @Override
    @ParametersAreNonnullByDefault
    protected void onPlayerTick(@Nonnull Player p, @Nonnull PlayerProfile profile) {
        int exposureTotal = 0;

        if (!profile.hasFullProtectionAgainst(ProtectionType.RADIATION)) {
            for (ItemStack item : p.getInventory()) {
                if (item == null || item.getType().isAir()) {
                    continue;
                }
                SlimefunItem sfItem = SlimefunItem.getByItem(item);
                if (sfItem instanceof RadioactiveItem) {
                    exposureTotal += item.getAmount() * ((RadioactiveItem) sfItem).getRadioactivity().getExposureModifier();
                }
            }
        }

        int exposureLevelBefore = RadiationUtils.getExposure(p);

        if (exposureTotal > 0) {
            if (exposureLevelBefore == 0) {
                SlimefunPlugin.getLocalization().sendMessage(p, "messages.radiation");
            }

            RadiationUtils.addExposure(p, exposureTotal);
        } else if (exposureLevelBefore > 0) {
            RadiationUtils.removeExposure(p, 1);
        }

        int exposureLevelAfter = RadiationUtils.getExposure(p);

        for (RadiationSymptom symptom : symptoms) {
            if (symptom.shouldApply(exposureLevelAfter)) {
                symptom.apply(p);
            }
        }

        if (exposureLevelAfter > 0 || exposureLevelBefore > 0) {
            String msg = SlimefunPlugin.getLocalization().getMessage(p, "actionbar.radiation").replace("%level%", "" + exposureLevelAfter);
            BaseComponent[] components = new ComponentBuilder().append(ChatColors.color(msg)).create();
            p.spigot().sendMessage(ChatMessageType.ACTION_BAR, components);
        }
    }
}