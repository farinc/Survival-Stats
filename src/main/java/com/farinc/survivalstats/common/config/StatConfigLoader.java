package com.farinc.survivalstats.common.config;

import java.util.List;

import com.farinc.survivalstats.SurvivalStats;
import com.google.common.collect.Lists;

import org.apache.commons.lang3.tuple.Pair;

import net.minecraftforge.common.ForgeConfigSpec;

public class StatConfigLoader {
    public static final ForgeConfigSpec clientSpec;
    public static final Client CLIENT;
    public static final ForgeConfigSpec commonSpec;
    public static final Common COMMON;
    private static final String CONFIG_PREFIX = "gui." + SurvivalStats.MODID + ".config.";

    static {
        final Pair<Client, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(Client::new);
        clientSpec = specPair.getRight();
        CLIENT = specPair.getLeft();
    }

    static {
        final Pair<Common, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(Common::new);
        commonSpec = specPair.getRight();
        COMMON = specPair.getLeft();
    }

    public static class Common {

        //for now this is empty

        Common(ForgeConfigSpec.Builder builder) {

            builder.push("common");
            builder.pop();
        }
    }

    public static class Client {

        public final ForgeConfigSpec.IntValue buttonXOffset;
        public final ForgeConfigSpec.IntValue buttonYOffset;
        public final ForgeConfigSpec.IntValue creativeButtonXOffset;
        public final ForgeConfigSpec.IntValue creativeButtonYOffset;

        Client(ForgeConfigSpec.Builder builder) {

        builder.comment("Client settings").push("client");

        buttonXOffset = builder.comment("The x offset for the survival inventory")
            .translation(CONFIG_PREFIX + "x-offset-survival")
            .defineInRange("x-offset-survival", 0, -100, 100);
        buttonYOffset = builder.comment("The y offset for the survival inventory")
            .translation(CONFIG_PREFIX + "y-offset-survival")
            .defineInRange("y-offset-survival", 0, -100, 100);
        creativeButtonXOffset = builder.comment("The x offset for the creative inventory")
            .translation(CONFIG_PREFIX + "x-offset-creative")
            .defineInRange("x-offset-creative", 0, -100, 100);
        creativeButtonYOffset = builder.comment("The y offset for the creative inventory")
            .translation(CONFIG_PREFIX + "y-offset-creative")
            .defineInRange("y-offset-creative", 0, -100, 100);

        builder.pop();
        }
    }
}