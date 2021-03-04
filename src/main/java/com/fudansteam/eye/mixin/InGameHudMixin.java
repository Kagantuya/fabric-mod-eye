package com.fudansteam.eye.mixin;

import com.fudansteam.eye.Eye;
import com.fudansteam.eye.config.EyeConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Util;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.awt.*;

/**
 * @author : 箱子
 * Description : description
 * Created by 箱子 on 2021-02-27 00:00:26
 * Copyright 2021 HDU_IES. All rights reserved.
 */
@Mixin(InGameHud.class)
public class InGameHudMixin extends DrawableHelper {
    
    private static final int PADDING = 14;
    
    @Inject(method = "renderHeldItemTooltip", at = @At("RETURN"))
    private void render(MatrixStack matrices, CallbackInfo ci) {
        if (Eye.tips != null) {
            MinecraftClient client = MinecraftClient.getInstance();
            TextRenderer textRenderer = client.textRenderer;
            int x = client.getWindow().getScaledWidth() / 2;
            int y = client.getWindow().getScaledHeight() - 59;
            if (client.interactionManager != null && !client.interactionManager.hasStatusBars()) {
                y += PADDING;
            }
            
            if (canRender(EyeConfig.TERRIBLE)) {
                drawCenteredString(matrices, textRenderer, Eye.tips.get(EyeConfig.TERRIBLE), x, y - (textRenderer.fontHeight + PADDING) * 2, getColor(EyeConfig.TERRIBLE));
            }
            if (canRender(EyeConfig.OTHER)) {
                drawCenteredString(matrices, textRenderer, Eye.tips.get(EyeConfig.OTHER), x, y - (textRenderer.fontHeight + PADDING), getColor(EyeConfig.OTHER));
            }
        }
    }
    
    /**
     * 是否可渲染
     *
     * @param tipType 提示种类
     * @return 是否可渲染
     */
    private boolean canRender(String tipType) {
        return Eye.tips.get(tipType) != null && Eye.tipTimes.get(tipType) != null;
    }
    
    /**
     * 获取提示信息剩余时间对应的颜色值
     *
     * @param tipType 提示种类
     * @return 颜色值
     */
    private int getColor(String tipType) {
        float delta = Util.getMeasuringTimeMs() - Eye.tipTimes.get(tipType);
        // 渐变淡出
        int p = MathHelper.floor(MathHelper.clampedLerp(25.0D, 255.0D, (EyeConfig.DISAPPEAR_TIME - delta) / EyeConfig.DISAPPEAR_TIME));
        return Eye.shouldWarn && tipType.equals(EyeConfig.TERRIBLE) ? new Color(255, 0, 0, p).getRGB() : new Color(255, 255, 255, p).getRGB();
    }
    
}
