package com.elmakers.mine.bukkit.miha.dummy;

import java.util.Iterator;
import java.util.stream.Stream;

import org.bukkit.Keyed;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class DummyRegistry<B extends Keyed> implements Registry<B> {

    public DummyRegistry(Class<? super B> classType) {

    }

    @Nullable
    @Override
    public B get(@NotNull NamespacedKey namespacedKey) {
        return null;
    }

    @NotNull
    @Override
    public Stream<B> stream() {
        return Stream.empty();
    }

    @NotNull
    @Override
    public Iterator<B> iterator() {
        return null;
    }

    public static <B extends Keyed> Registry<?> createRegistry(Class<? super B> bukkitClass) {
        if (bukkitClass == PotionEffectType.class) {
            return new DummyPotionEffectRegistry();
        }

        return new DummyRegistry<>(bukkitClass);
    }
}
