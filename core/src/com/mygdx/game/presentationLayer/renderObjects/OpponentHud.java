package com.mygdx.game.presentationLayer.renderObjects;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.mygdx.game.dataLayer.repositories.CardsTextureRepository;

public class OpponentHud {

    private CardsTextureRepository cardsTexture;
    private Stage stage;

    //ADD HERE MORE DATA

    public OpponentHud(CardsTextureRepository cardsTexture, Stage stage) {
        this.cardsTexture = cardsTexture;
        this.stage = stage;
        //INIT DATA HERE
    }


    public Group getUpdatedHUD(){
        Group group = new Group();
        //manage all states here


        //add actors here;


        return group;


    }
}
