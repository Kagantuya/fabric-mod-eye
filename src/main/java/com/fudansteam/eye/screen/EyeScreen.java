package com.fudansteam.eye.screen;

import com.fudansteam.eye.options.EyeOption;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ScreenTexts;
import net.minecraft.client.gui.widget.ButtonListWidget;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.options.Option;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.TranslatableText;

/**
 * @author : 箱子
 * Description : description
 * Created by 箱子 on 2021-02-28 10:57:22
 * Copyright 2021 HDU_IES. All rights reserved.
 */
public class EyeScreen extends Screen {
    
    private final Screen parent;
    private ButtonListWidget list;
    
    public EyeScreen(Screen parent) {
        super(new TranslatableText("eye.title"));
        this.parent = parent;
    }
    
    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.renderBackground(matrices);
        this.list.render(matrices, mouseX, mouseY, delta);
        drawCenteredText(matrices, this.textRenderer, this.title, this.width / 2, 5, 16777215);
        super.render(matrices, mouseX, mouseY, delta);
    }
    
    @Override
    protected void init() {
        this.list = new ButtonListWidget(this.client, this.width, this.height, 32, this.height - 32, 25);
        this.list.addAll(new Option[]{EyeOption.EYE_DISTANCE, EyeOption.SUPER_EYE});
        this.children.add(this.list);
        this.addButton(new ButtonWidget(this.width / 2 - 100, this.height - 27, 200, 20, ScreenTexts.DONE, (button) -> {
            if (this.client != null) {
                this.client.openScreen(this.parent);
            }
        }));
    }
    
}
