package de.iplexy.iPxBuffs.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Material;

import java.util.List;

@Getter
@AllArgsConstructor
public enum BuffType {

    FLY(2,3,Material.WIND_CHARGE, MiniMessage.miniMessage().deserialize("<gradient:#0ED2F7:#B2FEFA>ꜰʟʏ-ʙᴜꜰꜰ</gradient>").decoration(TextDecoration.ITALIC,false),List.of(
            MiniMessage.miniMessage().deserialize("<gray>Erhalte für <dark_aqua>60</dark_aqua> Minuten</gray>").decoration(TextDecoration.ITALIC,false),
            MiniMessage.miniMessage().deserialize("<gray>die Chance zu <yellow>fliegen.</gray>").decoration(TextDecoration.ITALIC,false)
    ) ),

    COIN(2,4,Material.RAW_GOLD, MiniMessage.miniMessage().deserialize("<#fffc00>ʜᴀʀᴍᴜᴛ-ʙᴜꜰꜰ</#fffc00>").decoration(TextDecoration.ITALIC,false),List.of(
            MiniMessage.miniMessage().deserialize("<gray>Erhalte für <dark_aqua>60</dark_aqua> Minuten</gray>").decoration(TextDecoration.ITALIC,false),
            MiniMessage.miniMessage().deserialize("<gray>das <yellow>1.5 Fache</yellow> an Coins bei Harmut.</gray>").decoration(TextDecoration.ITALIC,false)
    ) ),

    MINING(2,6,Material.DIAMOND_PICKAXE, MiniMessage.miniMessage().deserialize("<gradient:#304382:#d7d2cc>ᴍɪɴɪɴɢ-ʙᴜꜰꜰ</gradient>").decoration(TextDecoration.ITALIC,false),List.of(
            MiniMessage.miniMessage().deserialize("<gray>Erhalte für <dark_aqua>60</dark_aqua> Minuten</gray>").decoration(TextDecoration.ITALIC,false),
            MiniMessage.miniMessage().deserialize("<gray><yellow>Eile IV</yellow>.</gray>").decoration(TextDecoration.ITALIC,false)
    ) ),

    JOB(2,7,Material.CLOCK, MiniMessage.miniMessage().deserialize("<gradient:#a8ff78:#78ffd6>ᴊᴏʙ-ʙᴜꜰꜰ</gradient>").decoration(TextDecoration.ITALIC,false),List.of(
            MiniMessage.miniMessage().deserialize("<gray>Erhalte für <dark_aqua>60</dark_aqua> Minuten</gray>").decoration(TextDecoration.ITALIC,false),
            MiniMessage.miniMessage().deserialize("<gray>das <yellow>doppelte</yellow> an Job-XP & Job Shards.</gray>").decoration(TextDecoration.ITALIC,false)
    ) ),

    ITEM_DROP(3,4,Material.DROPPER, MiniMessage.miniMessage().deserialize("<gradient:#7f7fd6:#86a8e7:#91eae4>ɪᴛᴇᴍᴅʀᴏᴘ-ʙᴜꜰꜰ</gradient>").decoration(TextDecoration.ITALIC,false),List.of(
            MiniMessage.miniMessage().deserialize("<gray>Erhalte für <dark_aqua>60</dark_aqua> Minuten</gray>").decoration(TextDecoration.ITALIC,false),
            MiniMessage.miniMessage().deserialize("<gray>die Chance, dass ein <yellow>besonderes Item</yellow></gray>").decoration(TextDecoration.ITALIC,false),
            MiniMessage.miniMessage().deserialize("<gray>droppt, wenn du einen Block abbaust.</gray>").decoration(TextDecoration.ITALIC,false)
    ) ),

    VOTE(3,6,Material.AMETHYST_CLUSTER, MiniMessage.miniMessage().deserialize("<gradient:#fdc830:#f37335>ᴠᴏᴛᴇ-ʙᴜꜰꜰ</gradient>").decoration(TextDecoration.ITALIC,false),List.of(
            MiniMessage.miniMessage().deserialize("<gray>Erhalte für <dark_aqua>60</dark_aqua> Minuten</gray>").decoration(TextDecoration.ITALIC,false),
            MiniMessage.miniMessage().deserialize("<gray><yellow>doppelte</yellow> Vote-Belohnungen.</gray>").decoration(TextDecoration.ITALIC,false)
    ) );

    int row;
    int column;
    Material icon;
    Component name;
    List<Component> description;
}

