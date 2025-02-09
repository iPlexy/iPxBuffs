package de.iplexy.iPxBuffs.events;

import de.iplexy.iPxBuffs.IPxBuffs;
import de.iplexy.iPxBuffs.enums.BuffType;
import de.iplexy.iPxBuffs.manager.BuffManager;
import net.kyori.adventure.text.Component;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import su.nightexpress.coinsengine.api.CoinsEngineAPI;
import su.nightexpress.coinsengine.api.event.ChangeBalanceEvent;

public class ChangeBalanceListener implements Listener {

    @EventHandler
    public void onChangeBalance(ChangeBalanceEvent e) {
        if (!BuffManager.isBuffActive(BuffType.COIN)) {
            return;
        }
        if (e.getNewAmount() < e.getOldAmount()) {
            return;
        }
        if (!e.getCurrency().getName().equalsIgnoreCase("money")) {
            return;
        }
        //TODO CHeck event reason somehow
        //var addedBalance = e.getNewAmount() - e.getOldAmount();
        //var bonusBalance = addedBalance / 2;
        //CoinsEngineAPI.addBalance(e.getPlayer(), e.getCurrency(), bonusBalance);
       // e.getPlayer().sendMessage(IPxBuffs.getPrefix().append(Component.text("§7Du hast §e%f §7Bonus-Coins bekommen.".formatted(bonusBalance))));

    }
}
