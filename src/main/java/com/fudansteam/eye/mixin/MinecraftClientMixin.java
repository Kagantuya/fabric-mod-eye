package com.fudansteam.eye.mixin;

import com.fudansteam.eye.Eye;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * @author : 箱子
 * Description : description
 * Created by 箱子 on 2021-02-26 17:54:04
 * Copyright 2021 HDU_IES. All rights reserved.
 */
@Mixin(MinecraftClient.class)
public abstract class MinecraftClientMixin {
    
    @Inject(method = "setWorld", at = @At("HEAD"))
    private void onSetWorld(ClientWorld world, CallbackInfo ci) {
        if (Eye.world == null) {
            Eye.world = world;
        }
    }
    
}
