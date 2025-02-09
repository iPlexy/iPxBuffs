package de.iplexy.iPxBuffs.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Subcommand;
import de.iplexy.iPxBuffs.IPxBuffs;
import de.iplexy.iPxBuffs.manager.BuffManager;
import de.iplexy.iPxBuffs.ui.BuffGui;
import de.iplexy.iPxBuffs.utils.TimeUtils;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

@CommandAlias("buff|boost|booster")
public class BuffCommand extends BaseCommand {

    @Default
    public void onDefault(Player player) {
        BuffGui.open(player);
    }

    @Subcommand("listbuffs")
    public void onListBuffs(Player player) {
        BuffManager.getActiveBuffs().forEach((buff, time) -> {
            player.sendMessage(IPxBuffs.getPrefix().append(Component.text("§7Buff §9%s§7: §e%d§7 verbleibend".formatted(buff.name(), TimeUtils.getRemainingTime(time)))));
        });
    }
}
