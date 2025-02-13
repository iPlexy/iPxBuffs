package de.iplexy.iPxBuffs.placeholders;

import de.iplexy.iPxBuffs.enums.BuffType;
import de.iplexy.iPxBuffs.manager.BuffManager;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

public class BuffPlaceholder extends PlaceholderExpansion {
    @Override
    public @NotNull String getIdentifier() {
        return "ipxbuffs";
    }

    @Override
    public @NotNull String getAuthor() {
        return "iPlexy";
    }

    @Override
    public @NotNull String getVersion() {
        return "1.1";
    }

    @Override
    public String onRequest(OfflinePlayer player, @NotNull String params) {
        if (params.equalsIgnoreCase("FLY")) {
            return "%b".formatted(BuffManager.isBuffActive(BuffType.FLY));
        } else if (params.equalsIgnoreCase("COIN")) {
            return "%b".formatted(BuffManager.isBuffActive(BuffType.COIN));
        } else if (params.equalsIgnoreCase("MINING")) {
            return "%b".formatted(BuffManager.isBuffActive(BuffType.MINING));
        } else if (params.equalsIgnoreCase("JOB")) {
            return "%b".formatted(BuffManager.isBuffActive(BuffType.JOB));
        } else if (params.equalsIgnoreCase("ITEM_DROP")) {
            return "%b".formatted(BuffManager.isBuffActive(BuffType.ITEM_DROP));
        } else if (params.equalsIgnoreCase("VOTE")) {
            return "%b".formatted(BuffManager.isBuffActive(BuffType.VOTE));
        }
        return "false";
    }
}
