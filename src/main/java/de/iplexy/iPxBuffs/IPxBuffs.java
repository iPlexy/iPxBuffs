package de.iplexy.iPxBuffs;

import co.aikar.commands.PaperCommandManager;
import de.iplexy.iPxBuffs.commands.AdminBuffCommand;
import de.iplexy.iPxBuffs.commands.BuffCommand;
import de.iplexy.iPxBuffs.events.ChangeBalanceListener;
import de.iplexy.iPxBuffs.events.CommandEvent;
import de.iplexy.iPxBuffs.events.JoinListener;
import de.iplexy.iPxBuffs.manager.BuffManager;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.plugin.java.JavaPlugin;

public final class IPxBuffs extends JavaPlugin {

    @Getter
    private static Component prefix;

    @Getter
    private static IPxBuffs plugin;

    @Override
    public void onEnable() {
        plugin = this;
        prefix = MiniMessage.miniMessage().deserialize("<dark_gray>┃ <#f56f42>ʙᴜꜰꜰꜱ</#f56f42> »</dark_gray> <gray>");
        registerCommands();
        registerEvents();
        BuffManager.startBuffChecker();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
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
    }
}
