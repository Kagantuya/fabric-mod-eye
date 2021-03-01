package com.fudansteam.eye.options;

import com.fudansteam.eye.config.EyeConfig;
import net.minecraft.client.options.CyclingOption;
import net.minecraft.client.options.DoubleOption;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;

/**
 * @author : 箱子
 * Description : description
 * Created by 箱子 on 2021-02-28 08:38:17
 * Copyright 2021 HDU_IES. All rights reserved.
 */
public class EyeOption {
    
    private static final Text SUPER_EYE_OPEN = new TranslatableText("eye.options.super_eye.open");
    private static final Text SUPER_EYE_CLOSE = new TranslatableText("eye.options.super_eye.off");
    
    public static final DoubleOption EYE_DISTANCE = new DoubleOption("eye.options.distance",
            1.0D, 50.0D, 1.0F,
            gameOptions -> (double) EyeConfig.distance,
            (gameOptions, distance) -> EyeConfig.distance = distance.intValue(),
            (gameOptions, option) -> new TranslatableText("eye.options.distance_s", EyeConfig.distance));
    
    public static final CyclingOption SUPER_EYE = new CyclingOption("eye.options.super_eye",
            (gameOptions, integer) -> EyeConfig.superEye = !EyeConfig.superEye,
            (gameOptions, cyclingOption) -> EyeConfig.superEye ? SUPER_EYE_OPEN : SUPER_EYE_CLOSE);
    
}
