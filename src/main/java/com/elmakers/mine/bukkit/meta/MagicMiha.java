package com.elmakers.mine.bukkit.meta;

import com.elmakers.mine.bukkit.magic.Mage;
import com.elmakers.mine.bukkit.magic.MagicController;
import com.elmakers.mine.bukkit.meta.platform.Platform;
import com.elmakers.mine.bukkit.utility.CompatibilityLib;

public class MagicMiha {
    private final MagicController controller;
    private final Mage mage;


    public static void main(String[] args) {
        DummyPlugin plugin = new DummyPlugin();
        MagicController controller = new MagicController(plugin);
        Platform platform = new Platform(controller);
        CompatibilityLib.initialize(platform);
        MagicMiha miha = new MagicMiha(controller);
        try {
            miha.run();
        } catch (Exception ex) {
            System.out.println("An error occurred, shutting down: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private MagicMiha(MagicController controller) {
        this.controller = controller;
        mage = new Mage("Interrogator", controller);
    }

    public void run() {

    }
}
