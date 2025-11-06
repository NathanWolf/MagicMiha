package com.elmakers.mine.bukkit.miha.platform;

import java.net.URL;
import java.util.UUID;

import org.bukkit.inventory.ItemStack;

import com.elmakers.mine.bukkit.utility.platform.Platform;
import com.elmakers.mine.bukkit.utility.platform.base.InventoryUtilsBase;

public class InventoryUtils extends InventoryUtilsBase {
    public InventoryUtils(Platform platform) {
        super(platform);
    }

    @Override
    public ItemStack setSkullURL(ItemStack itemStack, URL url, UUID id, String name) {
        return null;
    }

    @Override
    public boolean isSkull(ItemStack item) {
        return false;
    }
}
