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
//        Card crd = new Card("h-12",cardsTexture.getCardTexture("h-12"),cardsTexture.getCardTexture("back"),new Vector2(0,0),new Vector2(0,0));
//        playerCard.add(crd);
//        int i = strLst.size()*40/2;
//        List<Card> ret = new ArrayList<>();
//        Card crd;
//        Collections.sort(strLst);
//        for (String str:strLst) {
//            crd = new Card(str,cardsTexture.getCardTexture(str),cardsTexture.getCardTexture("back"),new Vector2(0,0),new Vector2(screenWidth - i,Constants.CARD_HAND_Y),newGameScreen );
//            playerCard.add(crd);
//            i -= 40;
//        }
        this.clear();
        Card2 c1 = new Card2("h-12",cardsTexture.getCardTexture("back"),cardsTexture.getCardTexture("h-12"),new Vector2(20,0),new Vector2(screenWidth,Constants.CARD_HAND_Y)) ;
        Card2 c2 = new Card2("h-13",cardsTexture.getCardTexture("back"),cardsTexture.getCardTexture("h-13"),new Vector2(30,0),new Vector2(screenWidth,Constants.CARD_HAND_Y));
        this.addActor(c1);
        this.addActor(c2);
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
