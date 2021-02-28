package com.fudansteam.eye.options;

import com.fudansteam.eye.config.EyeConfig;
import net.minecraft.client.options.DoubleOption;
import net.minecraft.text.TranslatableText;

/**
 * @author : 箱子
 * Description : description
 * Created by 箱子 on 2021-02-28 08:38:17
 * Copyright 2021 HDU_IES. All rights reserved.
 */
public class EyeOption {
    
    public static final DoubleOption EYE_DISTANCE = new DoubleOption("eye.config.options.distance", 1.0D, 50.0D, 1.0F,
            gameOptions -> (double) EyeConfig.distance,
            (gameOptions, distance) -> EyeConfig.distance = distance.intValue(),
            (gameOptions, option) -> new TranslatableText("Eye Distance:%s", EyeConfig.distance));
    
}
