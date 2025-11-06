package com.elmakers.mine.bukkit.miha.dummy;

import org.bukkit.NamespacedKey;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class DummyPotionEffectRegistry extends DummyRegistry<PotionEffectType> {

    public DummyPotionEffectRegistry() {
        super(PotionEffectType.class);
    }

    @Nullable
    @Override
    public PotionEffectType get(@NotNull NamespacedKey namespacedKey) {
        return new DummyPotionEffectType(namespacedKey);
    }
}
