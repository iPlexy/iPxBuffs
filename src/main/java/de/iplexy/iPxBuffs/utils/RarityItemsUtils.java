package de.iplexy.iPxBuffs.utils;

import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.inventory.ItemRarity;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class RarityItemsUtils {

    @Getter
    private static HashMap<ItemRarity, List<Material>> rarityItems = new HashMap<>();

    public static HashMap<ItemRarity, List<Material>> loadRarityItems() {
        rarityItems.computeIfAbsent(ItemRarity.UNCOMMON, k -> new ArrayList<>()).add(Material.CREEPER_BANNER_PATTERN);
        rarityItems.computeIfAbsent(ItemRarity.UNCOMMON, k -> new ArrayList<>()).add(Material.SKULL_BANNER_PATTERN);
        rarityItems.computeIfAbsent(ItemRarity.UNCOMMON, k -> new ArrayList<>()).add(Material.EXPERIENCE_BOTTLE);
        rarityItems.computeIfAbsent(ItemRarity.UNCOMMON, k -> new ArrayList<>()).add(Material.DRAGON_BREATH);
        rarityItems.computeIfAbsent(ItemRarity.UNCOMMON, k -> new ArrayList<>()).add(Material.ELYTRA);
        rarityItems.computeIfAbsent(ItemRarity.UNCOMMON, k -> new ArrayList<>()).add(Material.HEART_OF_THE_SEA);
        rarityItems.computeIfAbsent(ItemRarity.UNCOMMON, k -> new ArrayList<>()).add(Material.NETHER_STAR);
        rarityItems.computeIfAbsent(ItemRarity.UNCOMMON, k -> new ArrayList<>()).add(Material.TOTEM_OF_UNDYING);


        rarityItems.computeIfAbsent(ItemRarity.RARE, k -> new ArrayList<>()).add(Material.GOLDEN_APPLE);
        Arrays.stream(Material.values())
                .filter(Tag.ITEMS_CREEPER_DROP_MUSIC_DISCS::isTagged)
                .forEach(material -> rarityItems.computeIfAbsent(ItemRarity.RARE, k -> new ArrayList<>()).add(material));
        rarityItems.computeIfAbsent(ItemRarity.RARE, k -> new ArrayList<>()).add(Material.CONDUIT);

        rarityItems.computeIfAbsent(ItemRarity.EPIC, k -> new ArrayList<>()).add(Material.DRAGON_EGG);
        rarityItems.computeIfAbsent(ItemRarity.EPIC, k -> new ArrayList<>()).add(Material.ENCHANTED_GOLDEN_APPLE);
        rarityItems.computeIfAbsent(ItemRarity.EPIC, k -> new ArrayList<>()).add(Material.COMMAND_BLOCK);
        rarityItems.computeIfAbsent(ItemRarity.EPIC, k -> new ArrayList<>()).add(Material.CHAIN_COMMAND_BLOCK);
        rarityItems.computeIfAbsent(ItemRarity.EPIC, k -> new ArrayList<>()).add(Material.REPEATING_COMMAND_BLOCK);
        rarityItems.computeIfAbsent(ItemRarity.EPIC, k -> new ArrayList<>()).add(Material.STRUCTURE_BLOCK);
        rarityItems.computeIfAbsent(ItemRarity.EPIC, k -> new ArrayList<>()).add(Material.STRUCTURE_VOID);
        rarityItems.computeIfAbsent(ItemRarity.EPIC, k -> new ArrayList<>()).add(Material.LIGHT);
        rarityItems.computeIfAbsent(ItemRarity.EPIC, k -> new ArrayList<>()).add(Material.BARRIER);
        rarityItems.computeIfAbsent(ItemRarity.EPIC, k -> new ArrayList<>()).add(Material.COMMAND_BLOCK_MINECART);
        rarityItems.computeIfAbsent(ItemRarity.EPIC, k -> new ArrayList<>()).add(Material.DEBUG_STICK);
        rarityItems.computeIfAbsent(ItemRarity.EPIC, k -> new ArrayList<>()).add(Material.BEACON);
        return rarityItems;
    }

    public static ItemStack getRarityItem() {
        var rand = new Random();
        int chance = rand.nextInt(100);

        List<Material> items;

        if (chance < 60) { // 60% Wahrscheinlichkeit
            items = rarityItems.get(ItemRarity.UNCOMMON);
        } else if (chance < 95) { // 35% Wahrscheinlichkeit (60–94)
            items = rarityItems.get(ItemRarity.RARE);
        } else { // 5% Wahrscheinlichkeit (95–99)
            items = rarityItems.get(ItemRarity.EPIC);
        }

        return ItemStack.of(items.get(rand.nextInt(items.size())));

    }
}
