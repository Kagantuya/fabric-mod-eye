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
    
    public static ConcurrentHashMap<String, String> tips = new ConcurrentHashMap<>(4);
    public static ConcurrentHashMap<String, Long> tipTimes = new ConcurrentHashMap<>(tips.size());
    public static int preClosestEntityId = -1;
    
    @Override
    public void onInitialize() {
    }
    
}
