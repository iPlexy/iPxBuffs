package de.iplexy.iPxBuffs;

import co.aikar.commands.PaperCommandManager;
import de.iplexy.iPxBuffs.commands.AdminBuffCommand;
import de.iplexy.iPxBuffs.commands.BuffCommand;
import de.iplexy.iPxBuffs.events.BlockBreakListener;
import de.iplexy.iPxBuffs.events.ChangeBalanceListener;
import de.iplexy.iPxBuffs.events.CommandEvent;
import de.iplexy.iPxBuffs.events.JoinListener;
import de.iplexy.iPxBuffs.manager.BuffManager;
import de.iplexy.iPxBuffs.placeholders.BuffPlaceholder;
import de.iplexy.iPxBuffs.utils.RarityItemsUtils;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public final class IPxBuffs extends JavaPlugin {

    @Getter
    private static Component prefix;

    @Getter
    private static IPxBuffs plugin;

    @Getter
    private static NamespacedKey namespacedKey;

    @Override
    public void onEnable() {
        plugin = this;
        prefix = MiniMessage.miniMessage().deserialize("<dark_gray>┃ <#f56f42>ʙᴜꜰꜰꜱ</#f56f42> »</dark_gray> <gray>");
        namespacedKey = new NamespacedKey(this, "iPxBuffs");
        loadRarityItems();
        registerCommands();
        registerEvents();
        registerPlaceholders();
        BuffManager.load();
        BuffManager.startBuffChecker();
        BuffManager.startAutoSave();
    }

    @Override
    public void onDisable() {
        BuffManager.save();
        // Plugin shutdown logic
    }

    private void loadRarityItems(){
        Bukkit.getConsoleSender().sendMessage(prefix.append(MiniMessage.miniMessage().deserialize("<gray>RarityItems werden geladen...</gray>")));
        var items = RarityItemsUtils.loadRarityItems();
        var sum = items.values().stream()
                .mapToInt(List::size)
                .sum();
        Bukkit.getConsoleSender().sendMessage(prefix.append(MiniMessage.miniMessage().deserialize("<gray>Es wurden <yellow>%d</yellow> RarityItems geladen.</gray>".formatted(sum))));
    }

    private void registerCommands() {
        var commandManager = new PaperCommandManager(this);
        commandManager.registerCommand(new BuffCommand());
        commandManager.registerCommand(new AdminBuffCommand());
    }

    private void registerEvents() {
        getServer().getPluginManager().registerEvents(new JoinListener(), this);
        getServer().getPluginManager().registerEvents(new ChangeBalanceListener(), this);
        getServer().getPluginManager().registerEvents(new CommandEvent(), this);
        getServer().getPluginManager().registerEvents(new BlockBreakListener(), this);
    }

    private void registerPlaceholders(){
        new BuffPlaceholder().register();
    }
}
