package com.mygdx.game.presentationLayer.renderObjects;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

public abstract class AbstractCard extends Image {

    Vector2 spawnPosition;
    Vector2 originalPosition;

    public AbstractCard(TextureRegion cardImage, Vector2 spawnPosition, Vector2 originalPosition) {
        super(cardImage);
        this.spawnPosition = spawnPosition;
        this.originalPosition = originalPosition;
        this.setOriginX((spawnPosition.x + getWidth())/2);
        this.setOriginY((spawnPosition.y + getHeight())/2);
        this.setPosition(spawnPosition.x,spawnPosition.y);

    }



}
