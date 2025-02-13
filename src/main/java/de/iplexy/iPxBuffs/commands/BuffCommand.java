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

    @Subcommand("listactive")
    public void onListBuffs(Player player) {
        player.sendMessage(IPxBuffs.getPrefix().append(Component.text("§7Folgende Buffs sind aktuell aktiv:")));
        BuffManager.getActiveBuffs().forEach((buff, time) ->
                player.sendMessage(buff.getName().append(Component.text("§7 » ")).append(Component.text("§e%s".formatted(TimeUtils.getRemainingTimeFormatted(time)))))
        );
    }
}
