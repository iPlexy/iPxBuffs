package de.iplexy.iPxBuffs.entities;

import de.iplexy.iPxBuffs.enums.BuffType;
import lombok.*;

import java.util.HashMap;
import java.util.UUID;

@Getter
@Setter
@RequiredArgsConstructor
@NoArgsConstructor
public class BuffUser {

    private @NonNull UUID uuid;

    private HashMap<BuffType, Integer> availableBuffs = new HashMap<>();

    private HashMap<BuffType, Integer> usedBuffs = new HashMap<>();

    public int getAvailableBuff(BuffType type) {
        return availableBuffs.getOrDefault(type, 0);
    }
    public int getUsedBuff(BuffType type) {
        return usedBuffs.getOrDefault(type,0);
    }
    public void setAvailableBuff(BuffType type, Integer amount) {
        availableBuffs.put(type,amount);
    }
    public void setUsedBuff(BuffType type, Integer amount) {
        usedBuffs.put(type,amount);
    }
    public void addAvailableBuffs(BuffType type, Integer amount) {
        availableBuffs.put(type,availableBuffs.getOrDefault(type,0) + amount);
    }
    public void addUsedBuffs(BuffType type, Integer amount) {
        usedBuffs.put(type,usedBuffs.getOrDefault(type,0) + amount);
    }
    public void addAvailableBuff(BuffType type) {
        addAvailableBuffs(type,1);
    }
    public void addUsedBuff(BuffType type) {
        addUsedBuffs(type,1);
    }

    public boolean hasAvailableBuff(BuffType type) {
        return availableBuffs.getOrDefault(type,0) > 0;
    }
    public void removeAvailableBuff(BuffType type) {
        availableBuffs.put(type, availableBuffs.getOrDefault(type,1)-1);
    }
    public void removeUsedBuff(BuffType type) {
        usedBuffs.put(type, usedBuffs.getOrDefault(type,1)-1);
    }

}
