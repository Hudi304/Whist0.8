package com.mygdx.game.testing;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.AlphaAction;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class OpponentCard extends Image {
    private float originPosX = 0;
    private float originPosY = 0;


    public boolean flipped;


    public OpponentCard(TextureRegion image) {
        super(image);
        setPosition(originPosX, originPosY);
        setOriginX(getWidth() / 2);
        setOriginY(getHeight() / 2);
        setScale(0.8f);
        setRotation(0);
    }

    public void setOrigPos(float x, float y) {
        this.originPosX = x;
        this.originPosY = y;
    }

    public boolean isFlipped() {
        return flipped;
    }

    public void setFlipped(boolean flipped) {
        this.flipped = flipped;
    }


}