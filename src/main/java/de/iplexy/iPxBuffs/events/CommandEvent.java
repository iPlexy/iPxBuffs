package de.iplexy.iPxBuffs.events;

import de.iplexy.iPxBuffs.IPxBuffs;
import de.iplexy.iPxBuffs.enums.BuffType;
import de.iplexy.iPxBuffs.manager.BuffManager;
import net.kyori.adventure.text.Component;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class CommandEvent implements Listener {

    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent e) {
        if (e.getMessage().equalsIgnoreCase("/fly")) {
            if (BuffManager.isBuffActive(BuffType.FLY)) {
                e.setCancelled(true);
                var player = e.getPlayer();
                if (player.isFlying()) {
                    player.setFlying(false);
                    player.setAllowFlight(false);
                    player.sendMessage(IPxBuffs.getPrefix().append(Component.text("§7Dein Flugmodus wurde §cdeaktiviert§7.")));
                } else {
                    player.setAllowFlight(true);
                    player.setFlying(true);
                    player.sendMessage(IPxBuffs.getPrefix().append(Component.text("§7Dein Flugmodus wurde §aaktiviert§7.")));
                }
            }
        }
    }
}
