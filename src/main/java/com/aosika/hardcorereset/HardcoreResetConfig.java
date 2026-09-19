package com.aosika.hardcorereset;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;

import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;

public class HardcoreResetConfig {
    private static final Path CONFIG_PATH = FabricLoader.getInstance().getConfigDir().resolve("hardcore_reset.json");
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public static class Data {
        public int currentAttempt = 1;
        public long lastRunDurationSeconds = 0;
    }

    private static Data data = new Data();
    private static long currentRunStartTime = System.currentTimeMillis();

    public static void load() {
        try {
            if (Files.exists(CONFIG_PATH)) {
                try (Reader reader = Files.newBufferedReader(CONFIG_PATH)) {
                    Data loaded = GSON.fromJson(reader, Data.class);
                    if (loaded != null) {
                        data = loaded;
                        if (data.currentAttempt < 1) {
                            data.currentAttempt = 1;
                        }
                    }
                }
            } else {
                save();
            }
        } catch (Exception e) {
            HardcoreResetMod.LOGGER.error("[Hardcore Reset] Error al cargar la configuración: ", e);
        }
    }

    public static void save() {
        try {
            if (!Files.exists(CONFIG_PATH.getParent())) {
                Files.createDirectories(CONFIG_PATH.getParent());
            }
            try (Writer writer = Files.newBufferedWriter(CONFIG_PATH)) {
                GSON.toJson(data, writer);
            }
        } catch (Exception e) {
            HardcoreResetMod.LOGGER.error("[Hardcore Reset] Error al guardar la configuración: ", e);
        }
    }

    public static int getCurrentAttempt() {
        return data.currentAttempt;
    }

    private static volatile boolean runActive = true;
    private static volatile int deathCountdownSeconds = -1;

    public static void startNewRun() {
        currentRunStartTime = System.currentTimeMillis();
        runActive = true;
        deathCountdownSeconds = -1;
    }

    public static boolean isRunActive() {
        return runActive;
    }

    public static int getDeathCountdownSeconds() {
        return deathCountdownSeconds;
    }

    public static void setDeathCountdownSeconds(int seconds) {
        deathCountdownSeconds = seconds;
    }

    public static long getRunStartTime() {
        return currentRunStartTime;
    }

    public static long getCurrentRunDurationSeconds() {
        if (!runActive) {
            return data.lastRunDurationSeconds;
        }
        return Math.max(0, (System.currentTimeMillis() - currentRunStartTime) / 1000);
    }

    public static long getLastRunDurationSeconds() {
        return data.lastRunDurationSeconds;
    }

    public static void recordDeathAndAdvanceAttempt() {
        data.lastRunDurationSeconds = getCurrentRunDurationSeconds();
        data.currentAttempt++;
        runActive = false;
        save();
    }

    public static void resetCounter() {
        data.currentAttempt = 1;
        data.lastRunDurationSeconds = 0;
        currentRunStartTime = System.currentTimeMillis();
        save();
    }

    public static String formatDuration(long totalSeconds) {
        long hours = totalSeconds / 3600;
        long minutes = (totalSeconds % 3600) / 60;
        long seconds = totalSeconds % 60;
        if (hours > 0) {
            return String.format("%02d:%02d:%02d", hours, minutes, seconds);
        } else {
            return String.format("%02d:%02d", minutes, seconds);
        }
    }

    public static String getFormattedElapsedTime() {
        return formatDuration(getCurrentRunDurationSeconds());
    }
}
