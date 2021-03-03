package com.fudansteam.eye.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
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
 * Created by 箱子 on 2021-03-03 11:25:40
 * Copyright 2021 HDU_IES. All rights reserved.
 */
@Mixin(TitleScreen.class)
public class TitleScreenMixin extends Screen {
    
    private long backgroundFadeStart;
    private final boolean doBackgroundFade;
    
    protected TitleScreenMixin(Text title) {
        super(title);
        doBackgroundFade = true;
    }
    
    @Inject(method = "render", at = @At("RETURN"))
    private void onRender(MatrixStack matrices, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        if (this.backgroundFadeStart == 0L && this.doBackgroundFade) {
            this.backgroundFadeStart = Util.getMeasuringTimeMs();
        }
        float f = this.doBackgroundFade ? (float) (Util.getMeasuringTimeMs() - this.backgroundFadeStart) / 1000.0F : 1.0F;
        float g = this.doBackgroundFade ? MathHelper.clamp(f - 1.0F, 0.0F, 1.0F) : 1.0F;
        int l = MathHelper.ceil(g * 255.0F) << 24;
        if ((l & -67108864) != 0) {
            TranslatableText splashText = new TranslatableText("eye.splash_text");
            RenderSystem.pushMatrix();
            RenderSystem.translatef((float) (this.width / 2 + 90), 70.0F, 0.0F);
            RenderSystem.rotatef(-20.0F, 0.0F, 0.0F, 1.0F);
            float h = 1.8F - MathHelper.abs(MathHelper.sin((float) (Util.getMeasuringTimeMs() % 1000L) / 1000.0F * 6.2831855F) * 0.1F);
            h = h * 100.0F / (float) (this.textRenderer.getWidth(splashText) + 32);
            RenderSystem.scalef(h, h, h);
            drawCenteredText(matrices, this.textRenderer, splashText, 0, 3, 16776960 | l);
            RenderSystem.popMatrix();
        }
    }
    
}
