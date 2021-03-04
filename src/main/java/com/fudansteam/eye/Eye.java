package com.fudansteam.eye;

import net.fabricmc.api.ModInitializer;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author : 箱子
 * Description : description
 * Created by 箱子 on 2021-02-26 16:40:16
 * Copyright 2021 HDU_IES. All rights reserved.
 */
public class Eye implements ModInitializer {
    
    public static ConcurrentHashMap<String, String> tips = new ConcurrentHashMap<>();
    public static ConcurrentHashMap<String, Long> tipTimes = new ConcurrentHashMap<>();
    public static boolean shouldWarn = false;
    public static double originGamma = -1;
    
    @Override
    public void onInitialize() {
    }
    
}
