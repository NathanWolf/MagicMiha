package com.elmakers.mine.bukkit.miha.dummy;

import org.bukkit.Color;
import org.bukkit.NamespacedKey;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;

public class DummyPotionEffectType extends PotionEffectType {
    private final NamespacedKey key;

    public DummyPotionEffectType(NamespacedKey key) {
        this.key = key;
    }

    @NotNull
    @Override
    public PotionEffect createEffect(int i, int i1) {
        return null;
    }

    @Override
    public boolean isInstant() {
        return false;
    }

    @NotNull
    @Override
    public Color getColor() {
        return null;
    }

    @Override
    public double getDurationModifier() {
        return 0;
    }

    @Override
    public int getId() {
        return 0;
    }

    @NotNull
    @Override
    public String getName() {
        return key.getKey();
    }

    @NotNull
    @Override
    public NamespacedKey getKey() {
        return key;
    }

    @NotNull
    @Override
    public String getTranslationKey() {
        return "";
    }
}
