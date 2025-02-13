package de.iplexy.iPxBuffs.ui;

import de.iplexy.iPxBuffs.IPxBuffs;
import de.iplexy.iPxBuffs.entities.BuffUser;
import de.iplexy.iPxBuffs.enums.BuffType;
import de.iplexy.iPxBuffs.manager.BuffManager;
import de.iplexy.iPxBuffs.utils.TimeUtils;
import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.Gui;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BuffGui {

    public static void open(Player player) {
        var user = BuffManager.getUser(player);

        var gui = Gui.gui()
                .rows(4)
                .disableAllInteractions()
                .title(IPxBuffs.getPrefix().append(Component.text("§7Übersicht")))
                .create();

        fillGui(gui);

        gui.setItem(4, 9, ItemBuilder.from(Material.REDSTONE_TORCH)
                .name(Component.text("§7» §cInformationen"))
                .lore(List.of(
                        Component.empty(),
                        Component.text("§7Buffs sind §eEffekte§7, die jeder, der"),
                        Component.text("§7auf dem Server §aonline §7ist, bekommt."),
                        Component.text("§7Diese laufen immer nur eine bestimmte Zeit.")
                ))
                .asGuiItem());

        gui.setItem(4,1,ItemBuilder.from(Material.BARRIER)
                .name(Component.text("§7» §cMenü schließen"))
                .lore(List.of(
                        Component.empty(),
                        Component.text("§7Klicke hier, um das Menü zu schließen.")
                ))
                .asGuiItem(event -> gui.close(player)));

        buildBuffItems(player, gui, user);

        gui.setOpenGuiAction(event ->
                refreshGui(player, gui, user));

        gui.open(player);
    }

    private static void buildBuffItems(Player player, Gui gui, BuffUser user) {
        Arrays.stream(BuffType.values()).forEach(buff -> {
            gui.setItem(buff.getRow(), buff.getColumn(), ItemBuilder.from(buff.getIcon())
                    .name(Component.text("» ").decoration(TextDecoration.ITALIC, false).color(NamedTextColor.GRAY).append(buff.getName()).append(getActivityState(buff)))
                    .lore(createLore(buff, user))
                    .asGuiItem(event -> {
                        if (BuffManager.isBuffActive(buff)) {
                            player.sendMessage(IPxBuffs.getPrefix().append(Component.text("§cEs ist bereits ein ").append(buff.getName()).append(Component.text("§c aktiv."))));
                            player.playSound(player, Sound.BLOCK_REDSTONE_TORCH_BURNOUT, 0.5F, 2F);
                            return;
                        }
                        if (!user.hasAvailableBuff(buff)) {
                            player.sendMessage(IPxBuffs.getPrefix().append(Component.text("§cDu hast keinen ").append(buff.getName()).append(Component.text("§c mehr."))));
                            player.playSound(player, Sound.BLOCK_REDSTONE_TORCH_BURNOUT, 0.5F, 2F);
                            return;
                        }
                        if(BuffManager.getActiveBuffs().size() >= 3){
                            player.sendMessage(IPxBuffs.getPrefix().append(Component.text("§cEs können nur maximal §e3 Buffs §caktiv sein.")));
                            player.playSound(player, Sound.BLOCK_REDSTONE_TORCH_BURNOUT, 0.5F, 2F);
                            return;
                        }
                        BuffManager.activateBuff(buff, player);
                    })
            );
        });
    }

    private static void refreshGui(Player player, Gui gui, BuffUser user) {
        new BukkitRunnable() {
            @Override
            public void run() {
                buildBuffItems(player, gui, user);
                gui.update();
                if (!player.getOpenInventory().getTopInventory().equals(gui.getInventory())) {
                    cancel();
                }
            }
        }.runTaskTimer(IPxBuffs.getPlugin(), 0, 20);
    }

    private static List<Component> createLore(BuffType type, BuffUser user) {
        var lore = new ArrayList<Component>(type.getDescription());
        lore.add(Component.empty());
        lore.add(Component.text("§7In Besitz: §b%d Stück".formatted(user.getAvailableBuff(type))));
        lore.add(Component.text("§7Bereits verwendet: §b%d Stück".formatted(user.getUsedBuff(type))));
        return lore;
    }

    private static Component getActivityState(BuffType buff) {
        if (BuffManager.isBuffActive(buff)) {
            return MiniMessage.miniMessage().deserialize("<gray> » <#40a832>ᴀᴋᴛɪᴠ </#40a832></gray>").append(getRemainingTime(buff));
        }
        return MiniMessage.miniMessage().deserialize("<gray> » <#c23030>ɪɴᴀᴋᴛɪᴠ</#c23030></gray>");
    }

    private static Component getRemainingTime(BuffType buff) {
        return MiniMessage.miniMessage().deserialize("<dark_gray>(<yellow>%s</yellow> <gray>verbleibend</gray>)</dark_gray>".formatted(TimeUtils.getRemainingTimeFormatted(BuffManager.getRemainingTime(buff))));
    }


    private static void fillGui(Gui gui) {
        gui.getFiller().fill(ItemBuilder.from(Material.LIGHT_GRAY_STAINED_GLASS_PANE)
                .name(Component.empty())
                .flags(ItemFlag.HIDE_ADDITIONAL_TOOLTIP)
                .asGuiItem());

        var cornerFiller = ItemBuilder.from(Material.GRAY_STAINED_GLASS_PANE)
                .name(Component.empty())
                .flags(ItemFlag.HIDE_ADDITIONAL_TOOLTIP)
                .asGuiItem();

        gui.getFiller().fillBetweenPoints(1, 1, 1, 2, cornerFiller);
        gui.getFiller().fillBetweenPoints(1, 8, 1, 9, cornerFiller);
        gui.getFiller().fillBetweenPoints(2, 1, 2, 1, cornerFiller);
        gui.getFiller().fillBetweenPoints(2, 9, 2, 9, cornerFiller);
        gui.getFiller().fillBetweenPoints(3, 1, 3, 1, cornerFiller);
        gui.getFiller().fillBetweenPoints(3, 9, 3, 9, cornerFiller);
        gui.getFiller().fillBetweenPoints(4, 1, 4, 2, cornerFiller);
        gui.getFiller().fillBetweenPoints(4, 8, 4, 9, cornerFiller);
    }
}
