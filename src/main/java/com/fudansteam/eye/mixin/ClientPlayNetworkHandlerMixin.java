package com.fudansteam.eye.mixin;

import com.fudansteam.eye.Eye;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * @author : 箱子
 * Description : description
 * Created by 箱子 on 2021-03-02 12:06:02
 * Copyright 2021 HDU_IES. All rights reserved.
 */
@Mixin(ClientPlayNetworkHandler.class)
public class ClientPlayNetworkHandlerMixin {
    
    @Inject(method = "clearWorld", at = @At("HEAD"))
    private void onClearWorld(CallbackInfo ci) {
        // 清除所有数据
        Eye.tips.clear();
        Eye.tipTimes.clear();
        Eye.preClosestEntityId = -1;
    }
    
}
