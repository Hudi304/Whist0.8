package com.mygdx.game.presentationLayer.renderObjects;



import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.mygdx.game.businessLayer.others.Constants;
import com.mygdx.game.dataLayer.generics.Card2;
import com.mygdx.game.dataLayer.repositories.CardsTextureRepository;
import com.mygdx.game.presentationLayer.screens.NewGameScreen;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.abs;
import static java.lang.Math.min;
import static java.lang.Math.sqrt;

public class OpponentHud extends Group {


    private NewGameScreen newGameScreen;
    private CardsTextureRepository cardsTexture;
    private Stage stage;
    private float offset ;

    public int nrOfCards = 8;
    public List<Card2> cards =  new ArrayList<>();

    //ADD HERE MORE DATA

    public OpponentHud(CardsTextureRepository cardsTexture, NewGameScreen newGameScreen){
        this.newGameScreen =  newGameScreen;
        this.cardsTexture = cardsTexture;
        this.offset = newGameScreen.screenWidth/50;
        initCards();
    }

    public  void initCards (float screenWidth){
        this.clear();
        Card2 crd = null;
        for(int i=0 ; i<nrOfCards; i++){
            crd = new Card2("h-4",cardsTexture.getCardTexture("back"),cardsTexture.getCardTexture("back"),new Vector2(0,0),new Vector2(screenWidth -i , Constants.CARD_HAND_Y)) ;
            this.addActor(crd);

        }
    }

    public void initCards(){
        this.clear();
        for(int i=0 ; i<nrOfCards; i++){
            Card2 crd = new Card2("h-4",cardsTexture.getCardTexture("back"),cardsTexture.getCardTexture("back"),new Vector2(0,0),new Vector2(100 , 100));
            this.addActor(crd);
        }
    }

    @Override
    public void act(float delta) {
        super.act(delta);
    }

    public void positionCardsHor(float width, float height, Vector2 plPos, Vector2 centerPos, float xOffset, float R, float rotation, boolean inverse){
        float rotOffset = 3;
        float rot;

        float cardWidth = min(height,width) /4 * 0.6f;
        float cardHeight = min(height,width) /4;

        float x = plPos.x - cards.size()*xOffset/2;
        float y ;
        rot =  cards.size()/2 * rotOffset;

        for (Actor act : getChildren()) {
            if(act instanceof Card2) {
                Card2 crd = (Card2) act;
                if(inverse){
                    y = (float) (-sqrt(abs(R*R - (x-centerPos.x)*(x-centerPos.x))) + centerPos.y);
                }else{
                    y = (float) (sqrt(abs(R*R - (x-centerPos.x)*(x-centerPos.x))) + centerPos.y);
                }
                //crd.debug();
                crd.setWidth(cardWidth*3/5);
                crd.setHeight(cardHeight*3/5);
                crd.setOriginX(cardWidth*3/5/2);
                crd.rePosition(x,y);
                crd.setRot( - rot + rotation );
                crd.setTouchable(Touchable.disabled);
                rot -= rotOffset;
                x += xOffset;
            }
        }
    }

    public void resizeOpponents(float width, float height){
        System.out.println("width = " + width + " height = " + height);
        System.out.println("screenWidth = " + newGameScreen.screenWidth + " screenHeight = " + newGameScreen.screenHeight);
        offset = width/50;

        Vector2 plPos = new Vector2(width/2 - nrOfCards/2*offset,height/2);
        Vector2 centerPos =  new Vector2(width/2, height *18.8f/10);
        positionCardsHor((int)newGameScreen.screenWidth, (int)newGameScreen.screenHeight, plPos,centerPos,offset,newGameScreen.screenHeight,170,true);

    }

    public void positionCardsVert(int width, int height, Vector2 plPos, Vector2 centerPos, float R, float rotation, boolean inverse){
        float xOffset =  width / 45;
        float rotOffset = 3;
        float rot;

        float cardWidth = min(height,width) /4 * 0.6f;
        float cardHeight = min(height,width) /4;

        float x ;
        float y = plPos.y - cards.size()*xOffset/2;
        rot =  cards.size()/2 * rotOffset;
        for (Card2 crd : cards) {

            if(inverse){
                x = (float) (-sqrt(abs(R*R - (y-centerPos.y)*(y-centerPos.y))) + centerPos.x);
                crd.setRot( rot + rotation );
            }else{
                x = (float) (sqrt(abs(R*R - (y-centerPos.y)*(y-centerPos.y))) + centerPos.x);
                crd.setRot( - rot + rotation );
            }
            crd.setWidth(cardWidth*3/4);
            crd.setHeight(cardHeight*3/4);
            crd.setOriginX(cardWidth*3/4/2);
            crd.rePosition(x,y);
            //crd.setTouchable(Touchable.disabled);
            rot -= rotOffset;
            y += xOffset;
        }
    }


}
