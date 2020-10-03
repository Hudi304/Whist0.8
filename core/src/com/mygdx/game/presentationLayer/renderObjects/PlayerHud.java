package com.mygdx.game.presentationLayer.renderObjects;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.mygdx.game.dataLayer.repositories.CardsTextureRepository;

import java.util.ArrayList;
import java.util.List;

public class PlayerHud {

    private CardsTextureRepository cardsTexture;
    private Stage stage;
    private List<Actor> playerCard;



    public PlayerHud(CardsTextureRepository cardsTexture, Stage stage) {
        this.cardsTexture = cardsTexture;
        this.stage = stage;
        playerCard = new ArrayList<>();

    }

    public Group getUpdatedHUD(){
        Group group = new Group();
        for(Actor card: playerCard){
            group.addActor(card);
        }

        return group;

    }
    //adding bidding HUD;
}
