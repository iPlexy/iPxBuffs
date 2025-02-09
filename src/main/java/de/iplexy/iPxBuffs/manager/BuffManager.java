package de.iplexy.iPxBuffs.manager;

import de.iplexy.iPxBuffs.IPxBuffs;
import de.iplexy.iPxBuffs.entities.BuffUser;
import de.iplexy.iPxBuffs.enums.BuffType;
import de.iplexy.iPxBuffs.utils.TimeUtils;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class BuffManager {

    private static HashMap<UUID, BuffUser> users = new HashMap<>();

    @Getter
    private static ConcurrentHashMap<BuffType, Long> activeBuffs = new ConcurrentHashMap<>();

    public static BuffUser getUser(OfflinePlayer offlinePlayer) {
        if (users.containsKey(offlinePlayer.getUniqueId())) {
            return users.get(offlinePlayer.getUniqueId());
        }
        return createUser(offlinePlayer);
    }

    private static BuffUser createUser(OfflinePlayer player) {
        var user = new BuffUser(player.getUniqueId());
        users.put(player.getUniqueId(), user);
        return user;
    }

    public static void activateBuff(BuffType type, OfflinePlayer executer) {
        var user = getUser(executer);
        user.addUsedBuff(type);
        user.removeAvailableBuff(type);
        activeBuffs.put(type, Instant.now().plus(1, ChronoUnit.MINUTES).toEpochMilli());
        Bukkit.getOnlinePlayers().forEach(player -> {
            player.sendMessage(IPxBuffs.getPrefix().append(Component.text("§7Der Spieler §3%s §7hat einen ".formatted(executer.getName())).append(type.getName()).append(Component.text("§7 gestartet!"))));
            player.playSound(player, Sound.UI_TOAST_CHALLENGE_COMPLETE, 0.5F, 2F);
        });
        if (type.equals(BuffType.FLY)) {
            Bukkit.getOnlinePlayers().stream()
                    .filter(player -> !player.isFlying())
                    .forEach(player -> {
                        player.setAllowFlight(true);
                        player.setFlying(true);
                        player.sendMessage(IPxBuffs.getPrefix().append(Component.text("§7Dein Flugmodus wurde §aaktiviert§7.")));
                    });
        } else if (type.equals(BuffType.MINING)) {
            Bukkit.getOnlinePlayers().stream()
                    .filter(player -> !player.isFlying())
                    .forEach(player -> {
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
                        player.sendMessage(IPxBuffs.getPrefix().append(Component.text("§7Du hast nun §eEile IV§7.")));
                    });
        }
    }

    public static Long getRemainingTime(BuffType type) {
        return activeBuffs.getOrDefault(type, 0L);
    }

    public static boolean isBuffActive(BuffType type) {
        return activeBuffs.containsKey(type);
    }

    public static void deactivateBuff(BuffType type) {
        Bukkit.getOnlinePlayers().forEach(player -> {
            player.sendMessage(IPxBuffs.getPrefix().append(Component.text("§7Der ")).append(type.getName()).append(Component.text("§7 ist ausgelaufen! §c:(")));
        });
        if (type.equals(BuffType.FLY)) {
            Bukkit.getOnlinePlayers().stream()
                    .filter(player -> !player.hasPermission("ipxbuffs.fly"))
                    .forEach(player -> {
                        player.setAllowFlight(false);
                        player.setFlying(false);
                        player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_FALLING, 20 * 10, 0, false, false));
                    });
        }
        activeBuffs.remove(type);
    }

    public static void startBuffChecker() {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (activeBuffs == null)
                    return;
                if (activeBuffs.isEmpty())
                    return;
                activeBuffs.forEach((buff, time) -> {
                    if (TimeUtils.getRemainingTime(time) <= 0) {
                        deactivateBuff(buff);
                    }
                });
            }
        }.runTaskTimer(IPxBuffs.getPlugin(), 0L, 20L);
    }


}
