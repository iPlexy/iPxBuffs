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
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.time.temporal.ChronoUnit;

@CommandAlias("adminbuff|abuff")
@CommandPermission("ipxbuffs.admin")
public class AdminBuffCommand extends BaseCommand {

    @Subcommand("addbuff")
    private void addBuff(CommandSender player, OfflinePlayer target, BuffType type, Integer amount) {
        var user = BuffManager.getUser(target);
        user.addAvailableBuffs(type, amount);
        player.sendMessage(IPxBuffs.getPrefix().append(Component.text("§7Du hast dem Spieler §3%s §e%dx ".formatted(target.getName(), amount))).append(type.getName()).append(Component.text("§7 gegeben")));
    }

    @Subcommand("addtime")
    private void addTime(Player player, BuffType type, Integer amount, ChronoUnit unit) {
        if (!BuffManager.isBuffActive(type)) {
            player.sendMessage(IPxBuffs.getPrefix().append(Component.text("§cAktuell ist kein ").append(type.getName()).append(Component.text("§c aktiv."))));
            player.playSound(player, Sound.BLOCK_REDSTONE_TORCH_BURNOUT, 0.5F, 2F);
            return;
        }
        BuffManager.addTimeToBuff(type, amount, unit);
        player.sendMessage(IPxBuffs.getPrefix().append(Component.text("§7Du hast §e%d %s §7zum ".formatted(amount, unit.name())).append(type.getName())).append(Component.text("§7 hinzugefügt.")));
    }

    @Subcommand("removetime")
    private void removeTime(Player player, BuffType type, Integer amount, ChronoUnit unit) {
        if (!BuffManager.isBuffActive(type)) {
            player.sendMessage(IPxBuffs.getPrefix().append(Component.text("§cAktuell ist kein ").append(type.getName()).append(Component.text("§c aktiv."))));
            player.playSound(player, Sound.BLOCK_REDSTONE_TORCH_BURNOUT, 0.5F, 2F);
            return;
        }
        BuffManager.removeTimeFromBuff(type, amount, unit);
        player.sendMessage(IPxBuffs.getPrefix().append(Component.text("§7Du hast §e%d %s §7von ".formatted(amount, unit.name())).append(type.getName())).append(Component.text("§7 entfernt.")));
    }

    @Subcommand("deactivate")
    private void deactivate(Player player, BuffType type) {
        if (!BuffManager.isBuffActive(type)) {
            player.sendMessage(IPxBuffs.getPrefix().append(Component.text("§cAktuell ist kein ").append(type.getName()).append(Component.text("§c aktiv."))));
            player.playSound(player, Sound.BLOCK_REDSTONE_TORCH_BURNOUT, 0.5F, 2F);
            return;
        }
        BuffManager.deactivateBuff(type);
        player.sendMessage(IPxBuffs.getPrefix().append(Component.text("§7Du hast ").append(type.getName())).append(Component.text("§7 deaktiviert.")));
    }

}
