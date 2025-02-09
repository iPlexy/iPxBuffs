package de.iplexy.iPxBuffs.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Subcommand;
import de.iplexy.iPxBuffs.IPxBuffs;
import de.iplexy.iPxBuffs.enums.BuffType;
import de.iplexy.iPxBuffs.manager.BuffManager;
import net.kyori.adventure.text.Component;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

@CommandAlias("adminbuff|abuff")
@CommandPermission("ipxbuffs.admin")
public class AdminBuffCommand extends BaseCommand {

    @Subcommand("addbuff")
    private void addBuff(Player player, OfflinePlayer target, BuffType type, Integer amount) {
        var user = BuffManager.getUser(target);
        user.addAvailableBuffs(type, amount);
        player.sendMessage(IPxBuffs.getPrefix().append(Component.text("§7Du hast dem Spieler §3%s §e%dx ".formatted(target.getName(),amount))).append(type.getName()).append(Component.text("§7 gegeben")));
    }

}
