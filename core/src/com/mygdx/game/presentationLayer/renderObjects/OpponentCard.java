package com.mygdx.game.presentationLayer.renderObjects;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class OpponentCard extends AbstractCard {
    public OpponentCard(TextureRegion cardImage, Vector2 spawnPosition, Vector2 originalPosition) {
        super(cardImage, spawnPosition, originalPosition);
    }
}
