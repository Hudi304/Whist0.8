package com.mygdx.game.presentationLayer.renderObjects;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.mygdx.game.dataLayer.repositories.CardsTextureRepository;

public class TableHud {
    private CardsTextureRepository cardsTexture;
    private Stage stage;

    public TableHud(CardsTextureRepository cardsTexture, Stage stage) {
        this.cardsTexture = cardsTexture;
        this.stage = stage;
    }

    public Group getUpdatedHUD(){
        Group group = new Group();
        //add here actors;

        return group;
    }
}
