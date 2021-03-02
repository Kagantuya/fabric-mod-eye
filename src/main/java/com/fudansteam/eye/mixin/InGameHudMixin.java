package com.fudansteam.eye.mixin;

import com.fudansteam.eye.Eye;
import com.fudansteam.eye.config.EyeConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Util;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * @author : 箱子
 * Description : description
 * Created by 箱子 on 2021-02-27 00:00:26
 * Copyright 2021 HDU_IES. All rights reserved.
 */
@Mixin(InGameHud.class)
public class InGameHudMixin {
    
    private static final int PADDING = 14;
    
    @Inject(method = "renderHeldItemTooltip", at = @At("RETURN"))
    private void render(MatrixStack matrices, CallbackInfo ci) {
        if (Eye.tips != null) {
            MinecraftClient client = MinecraftClient.getInstance();
            TextRenderer textRenderer = client.textRenderer;
            long now = Util.getMeasuringTimeMs();
            int y = client.getWindow().getScaledHeight() - 59;
            if (client.interactionManager != null && !client.interactionManager.hasStatusBars()) {
                y += PADDING;
            }
            
            if (canRender(EyeConfig.TERRIBLE, now)) {
                TranslatableText text = new TranslatableText(Eye.tips.get(EyeConfig.TERRIBLE));
                textRenderer.drawWithShadow(
                        matrices,
                        text,
                        (float) ((client.getWindow().getScaledWidth() - textRenderer.getWidth(text)) / 2),
                        y - (textRenderer.fontHeight + PADDING) * 2,
                        getColor(EyeConfig.TERRIBLE, now));
            }
            if (canRender(EyeConfig.OTHER, now)) {
                TranslatableText text = new TranslatableText(Eye.tips.get(EyeConfig.OTHER));
                textRenderer.drawWithShadow(
                        matrices,
                        text,
                        (float) ((client.getWindow().getScaledWidth() - textRenderer.getWidth(text)) / 2),
                        y - (textRenderer.fontHeight + PADDING),
                        getColor(EyeConfig.OTHER, now));
            }
        }
    }
    
    /**
     * 是否可渲染
     *
     * @param tipType 提示种类
     * @param now     当前时间
     * @return 是否可渲染
     */
    private boolean canRender(String tipType, long now) {
        String tip = Eye.tips.get(tipType);
        Long tipTime = Eye.tipTimes.get(tipType);
        return tip != null && tipTime != null && now - tipTime < EyeConfig.ALL_TIME;
    }
    
    /**
     * 获取提示信息剩余时间对应的颜色值
     *
     * @param tipType 提示种类
     * @param now     当前时间
     * @return 颜色值
     */
    private int getColor(String tipType, long now) {
        float delta = now - Eye.tipTimes.get(tipType);
        // 仍在有效持续时间之内则显示白色
        if (delta < EyeConfig.KEEP_TIME) {
            return -1;
        } else {
            // 渐变淡出
            int p = MathHelper.floor(MathHelper.clampedLerp(75.0D, 255.0D, (EyeConfig.ALL_TIME - delta) / EyeConfig.DISAPPEAR_TIME));
            int q = p << 16 | p << 8 | p;
            return q + -16777216;
        }
    }
    
}
