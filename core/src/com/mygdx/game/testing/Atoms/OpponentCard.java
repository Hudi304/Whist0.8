package com.mygdx.game.testing.Atoms;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

public class OpponentCard extends Image {
    private float originPosX = 0;
    private float originPosY = 0;

    public boolean flipped = false;
    public boolean putDown = false;


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
    public boolean isPutDown() {
        return putDown;
    }
    public void setPutDown(boolean putDown) {
        this.putDown = putDown;
    }
    public boolean isFlipped() {
        return flipped;
    }

    public void setFlipped(boolean flipped) {
        this.flipped = flipped;
    }


}