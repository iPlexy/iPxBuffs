package de.iplexy.iPxBuffs.events;

import de.iplexy.iPxBuffs.enums.BuffType;
import de.iplexy.iPxBuffs.manager.BuffManager;
import de.iplexy.iPxBuffs.utils.RarityItemsUtils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import java.util.Random;

public class BlockBreakListener implements Listener {

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if(BuffManager.isBuffActive(BuffType.ITEM_DROP)){
            var random = new Random();
            if (random.nextInt(100) < 5) {
                event.getBlock().getWorld().dropItemNaturally(event.getBlock().getLocation(), RarityItemsUtils.getRarityItem());
            }
        }
    }
}
