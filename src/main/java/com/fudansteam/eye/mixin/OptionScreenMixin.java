package com.fudansteam.eye.mixin;

import com.fudansteam.eye.screen.EyeScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.options.OptionsScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * @author : 箱子
 * Description : description
 * Created by 箱子 on 2021-02-28 08:28:36
 * Copyright 2021 HDU_IES. All rights reserved.
 */
@Mixin(OptionsScreen.class)
public class OptionScreenMixin extends Screen {
    
    protected OptionScreenMixin(Text title) {
        super(title);
    }
    
    @Inject(method = "init", at = @At("RETURN"))
    private void init(CallbackInfo ci) {
        TranslatableText title = new TranslatableText("eye.menu");
        this.addButton(new ButtonWidget(this.width / 2 - 155, this.height / 6 + 144 - 6, 150, 20, title, (button) -> {
            if (this.client != null) {
                this.client.openScreen(new EyeScreen(this, title));
            }
        }));
    }
    
}
