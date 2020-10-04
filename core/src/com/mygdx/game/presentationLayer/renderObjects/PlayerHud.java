package com.mygdx.game.presentationLayer.renderObjects;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.mygdx.game.businessLayer.others.Constants;
import com.mygdx.game.dataLayer.generics.Card;
import com.mygdx.game.dataLayer.repositories.CardsTextureRepository;
import com.mygdx.game.presentationLayer.screens.NewGameScreen;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PlayerHud {

    public NewGameScreen newGameScreen;

    public CardsTextureRepository cardsTexture;
    private Stage stage;



    private List<Card> playerCard;
    private List<String> strPlayerCard;

    public  void initCards (List<String> strLst,float screenWidth){
//        Card crd = new Card("h-12",cardsTexture.getCardTexture("h-12"),cardsTexture.getCardTexture("back"),new Vector2(0,0),new Vector2(0,0));
//        playerCard.add(crd);
        int i = strLst.size()*40/2;
        List<Card> ret = new ArrayList<>();
        Card crd;
        Collections.sort(strLst);
        for (String str:strLst) {
            crd = new Card(str,cardsTexture.getCardTexture(str),cardsTexture.getCardTexture("back"),new Vector2(0,0),new Vector2(screenWidth - i,Constants.CARD_HAND_Y),newGameScreen );
            playerCard.add(crd);
            i -= 40;
        }
    }


    public PlayerHud(CardsTextureRepository cardsTexture, NewGameScreen newGameScreen) {
        this.newGameScreen =  newGameScreen;
        this.cardsTexture = cardsTexture;
        this.stage = stage;
        playerCard = new ArrayList<>();

    }


    public void act(float delta){
        for (Card crd :playerCard) {
            crd.update(delta);
        }
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
