package com.mygdx.game.presentationLayer.renderObjects;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.mygdx.game.businessLayer.others.Constants;
import com.mygdx.game.dataLayer.generics.Card;
import com.mygdx.game.dataLayer.generics.Card2;
import com.mygdx.game.dataLayer.repositories.CardsTextureRepository;
import com.mygdx.game.presentationLayer.screens.NewGameScreen;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PlayerHud extends Group{

    public NewGameScreen newGameScreen;

    public CardsTextureRepository cardsTexture;
    private Stage stage;



    private List<Card> playerCard;
    private List<String> strPlayerCard;

    public  void initCards (List<String> strLst,float screenWidth){


        this.clear();
        int i = strLst.size() * 40/2;
        int j = 0;
        Card2 crd = null;
        for(String str: strLst){
            crd = new Card2(str,cardsTexture.getCardTexture("back"),cardsTexture.getCardTexture(str),new Vector2(20 + j*20,0),new Vector2(screenWidth -i ,Constants.CARD_HAND_Y)) ;
            this.addActor(crd);
            i-=40;
            j++;
        }


    }


    public PlayerHud(CardsTextureRepository cardsTexture, NewGameScreen newGameScreen) {
        this.newGameScreen =  newGameScreen;
        this.cardsTexture = cardsTexture;
        this.stage = stage;
        playerCard = new ArrayList<>();

    }


    public void act(float delta){
        super.act(delta);
    }

    public Group getUpdatedHUD(){
        Group group = new Group();
        for(Card card: playerCard){
            System.out.println(card.isFlipped());
            group.addActor(card.getCardActor());
        }

        return group;

    }
    public List<Card> getPlayerCard() {
        return playerCard;
    }
    //adding bidding HUD;
}
