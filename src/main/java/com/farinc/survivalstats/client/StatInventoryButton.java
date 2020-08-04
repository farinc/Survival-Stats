package com.farinc.survivalstats.client;

import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.network.play.client.CCloseWindowPacket;

public class StatInventoryButton extends Button{

    public StatInventoryButton(int widthIn, int heightIn, int width, int height, String text, IPressable onPress) {
        super(widthIn, heightIn, width, height, text, onPress);
    }

    
}