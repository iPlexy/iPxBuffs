package de.iplexy.iPxBuffs.manager;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import de.iplexy.iPxBuffs.IPxBuffs;
import de.iplexy.iPxBuffs.entities.BuffUser;
import de.iplexy.iPxBuffs.enums.BuffType;
import de.iplexy.iPxBuffs.utils.TimeUtils;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.KeyedBossBar;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
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

    private static void updateBossBar() {
        KeyedBossBar bossBar;
        if (Bukkit.getBossBar(IPxBuffs.getNamespacedKey()) == null) {
            if (activeBuffs.isEmpty()) {
                return;
            }
            bossBar = Bukkit.createBossBar(IPxBuffs.getNamespacedKey(), "---", BarColor.BLUE, BarStyle.SEGMENTED_6);
        } else {
            bossBar = Bukkit.getBossBar(IPxBuffs.getNamespacedKey());
            if (activeBuffs.isEmpty()) {
                bossBar.removeAll();
                return;
            }
        }
        bossBar.setProgress(0.33 * activeBuffs.size());
        var title = new StringBuilder("§7Aktive Buffs » ");
        var iterator = activeBuffs.keys().asIterator();
        int size = activeBuffs.size();
        int count = 0;

        while (iterator.hasNext()) {
            count++;
            var buff = iterator.next();
            title.append(LegacyComponentSerializer.legacySection().serialize(buff.getName()));

            if (count < size) {
                title.append(LegacyComponentSerializer.legacySection().serialize(Component.text("§7, ")));
            }
        }

        bossBar.setTitle(title.toString());
        Bukkit.getOnlinePlayers().forEach(bossBar::addPlayer);
    }

    public static void addTimeToBuff(BuffType type, long amountToAdd, ChronoUnit unit) {
        if (activeBuffs.containsKey(type)) {
            var time = activeBuffs.get(type) + (unit.getDuration().toMillis() * amountToAdd);
            activeBuffs.put(type, time);
        }
    }

    public static void removeTimeFromBuff(BuffType type, long amountToAdd, ChronoUnit unit) {
        if (activeBuffs.containsKey(type)) {
            var time = activeBuffs.get(type) - (unit.getDuration().toMillis() * amountToAdd);
            if(time <= 0){
                deactivateBuff(type);
            }else {
                activeBuffs.put(type, time);
            }
        }
    }

    public static void activateBuff(BuffType type, OfflinePlayer executer) {
        var user = getUser(executer);
        user.addUsedBuff(type);
        user.removeAvailableBuff(type);
        activeBuffs.put(type, Instant.now().plus(1, ChronoUnit.HOURS).toEpochMilli());
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
                updateBossBar();
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

    private static File getDataFolder() {
        return new File(IPxBuffs.getPlugin().getDataFolder(), "users/");
    }

    private static File getActiveBuffFile() {
        var dataFolder = IPxBuffs.getPlugin().getDataFolder();
        if(!dataFolder.exists()){
            dataFolder.mkdirs();
        }
        var file = new File(dataFolder, "activebuffs.json");
        if(!file.exists()){
            try {
                file.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return file;
    }

    public static void save() {
        Gson gson = (new GsonBuilder()).setPrettyPrinting().disableHtmlEscaping().create();
        for (BuffUser user : users.values()) {
            File dataFolder = getDataFolder();
            if (!dataFolder.exists())
                dataFolder.mkdirs();
            String fileName = "" + user.getUuid() + ".json";
            try {
                FileWriter writer = new FileWriter(new File(dataFolder, fileName));
                try {
                    gson.toJson(user, writer);
                    writer.close();
                } catch (Throwable throwable) {
                    try {
                        writer.close();
                    } catch (Throwable throwable1) {
                        throwable.addSuppressed(throwable1);
                    }
                    throw throwable;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        saveActiveBuffs();
    }

    public static void load() {
        var dataFolder = getDataFolder();
        if (!dataFolder.exists())
            dataFolder.mkdirs();
        for (File file : dataFolder.listFiles()) {
            try {
                FileReader reader = new FileReader(file);
                Gson gson = (new GsonBuilder()).setPrettyPrinting().disableHtmlEscaping().create();
                BuffUser user = gson.fromJson(reader, BuffUser.class);
                users.put(user.getUuid(), user);
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        loadActiveBuffs();
    }

    private static void saveActiveBuffs() {
        Gson gson = (new GsonBuilder()).setPrettyPrinting().disableHtmlEscaping().create();
        try (FileWriter writer = new FileWriter(getActiveBuffFile())) {
            gson.toJson(activeBuffs, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void loadActiveBuffs() {
        Gson gson = (new GsonBuilder()).setPrettyPrinting().disableHtmlEscaping().create();
        try (FileReader reader = new FileReader(getActiveBuffFile())) {
            Type type = new TypeToken<ConcurrentHashMap<BuffType, Long>>() {
            }.getType();
            activeBuffs = gson.fromJson(reader, type);
            if(activeBuffs == null){
                activeBuffs = new ConcurrentHashMap<>();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void startAutoSave(){
        new BukkitRunnable() {
            @Override
            public void run() {
                save();
            }
        }.runTaskTimer(IPxBuffs.getPlugin(), 20L, 20L*60*5);  //20 Ticks -> Sekunden -> Minuten
    }


}
