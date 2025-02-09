package de.iplexy.iPxBuffs.events;

import de.iplexy.iPxBuffs.IPxBuffs;
import de.iplexy.iPxBuffs.enums.BuffType;
import de.iplexy.iPxBuffs.manager.BuffManager;
import net.kyori.adventure.text.Component;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

public class JoinListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        var player = event.getPlayer();
        if(BuffManager.isBuffActive(BuffType.FLY)){
            if(!player.isFlying()){
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        player.setAllowFlight(true);
                        player.setFlying(true);
                        player.sendMessage(IPxBuffs.getPrefix().append(Component.text("§7Dein Flugmodus wurde §aaktiviert§7.")));
                    }
                }.runTaskLater(IPxBuffs.getPlugin(),20L*4);
            }
        }else{
            if(player.isFlying() && !player.hasPermission("ipxbuffs.fly")){
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        player.setFlying(false);
                        player.setAllowFlight(false);
                        player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_FALLING, 10, 3, false, false));
                    }
                }.runTaskLater(IPxBuffs.getPlugin(),21L*4);
            }
        }
        if(BuffManager.isBuffActive(BuffType.MINING)) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    player.sendMessage(IPxBuffs.getPrefix().append(Component.text("§7Du hast nun §eEile IV§7.")));
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            if (!player.isOnline()) {
                                cancel();
                                return;
                            }
                            if (BuffManager.isBuffActive(BuffType.MINING)) {
                                player.addPotionEffect(new PotionEffect(PotionEffectType.HASTE, 25, 3, false, false));
                            } else {
                                cancel();
                            }
                        }
                    }.runTaskTimer(IPxBuffs.getPlugin(), 0L, 20L);
                }
            }.runTaskLater(IPxBuffs.getPlugin(), 20L * 4);

        }
    }
}
