package me.sfiguz7.extratools;

import org.bukkit.Bukkit;

/**
 * 一次性检测运行环境是否为 Folia,结果缓存在静态字段中。
 * 本插件自身不做调度(节奏全部由 Slimefun 的 ticker 驱动),
 * 此检测结果用于启动日志与后续需要区分环境的检查。
 */
public final class FoliaCompat {

    private static volatile Boolean folia;

    private FoliaCompat() {
    }

    public static boolean isFolia() {
        Boolean cached = folia;
        if (cached == null) {
            try {
                Class.forName("io.papermc.paper.threadedregions.RegionizedServer");
                cached = true;
            } catch (ClassNotFoundException e) {
                cached = false;
            }
            folia = cached;
        }
        return cached;
    }

    public static String platformName() {
        return isFolia() ? "Folia" : Bukkit.getName();
    }
}
