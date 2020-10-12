package com.mygdx.game.testing;


import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.MoveByAction;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.dataLayer.generics.Card2;
import com.mygdx.game.dataLayer.repositories.CardsTextureRepository;
import com.mygdx.game.presentationLayer.renderObjects.OpponentHud;
import com.mygdx.game.presentationLayer.screens.NewGameScreen;

import static java.lang.Math.abs;
import static java.lang.Math.min;
import static java.lang.Math.sqrt;

public class OpponentHUD extends Group {
    private Viewport viewport;
    public int nrOfCards;
    private NewGameScreen newGameScreen;
    private CardsTextureRepository cardsTexture;
    private Stage stage;
    private float offset ;
    private float r;
    BitmapFont font12;//p

    String nickname;

    Vector2 castCardPosition;
    public Vector2 deckPos;
    float castCardRot;
    CardsTextureRepository cardsTextureRepository;
    private int cardNr = 0;


    public Vector2 centerPosition;


    public OpponentHUD(String nickname,int nrOfCards, CardsTextureRepository cardsTextureRepository, Viewport viewport){
        this.nickname = nickname;
        this.viewport = viewport;
        this.nrOfCards = nrOfCards;
        this.cardsTextureRepository = cardsTextureRepository;

        for(int i = 0; i < nrOfCards ; i++){
            OpponentCard card = new OpponentCard(cardsTextureRepository.getCardTexture("back"));
            this.addActor(card);
        }

    }

    public void positionCardsHor(float width, float height, Vector2 plPos, Vector2 centerPos, float xOffset, float R, float rotation, boolean inverse){
        centerPosition = centerPos;
        this.r = R;
        float rotOffset = 3;
        float rot;

        float cardWidth = min(height,width) /4 * 0.6f;
        float cardHeight = min(height,width) /4;

        float x = plPos.x - nrOfCards *xOffset/2;
        float y ;
        rot =  nrOfCards/2 * rotOffset;

        for (Actor act : getChildren()) {
            if(act instanceof OpponentCard) {
                OpponentCard crd = (OpponentCard) act;
                if(inverse){
                    y = (float) (-sqrt(abs(R*R - (x-centerPos.x)*(x-centerPos.x))) + centerPos.y);
                }else{
                    y = (float) (sqrt(abs(R*R - (x-centerPos.x)*(x-centerPos.x))) + centerPos.y);
                }
                if(crd.isFlipped()){

                }else{
                    crd.setWidth(cardWidth*3/5);
                    crd.setHeight(cardHeight*3/5);
                    crd.setOriginX(cardWidth*3/5/2);
                    crd.setPosition(x,y);
                    crd.setRotation( - rot + rotation );
                    crd.setTouchable(Touchable.disabled);
                    rot -= rotOffset;
                    x += xOffset;
                }
            }
        }
    }

    public void positionCardsVert(float width, float height, Vector2 plPos, Vector2 centerPos,float xOffset, float R, float rotation, boolean inverse){
        centerPosition = centerPos;
        this.r = R;
        float rotOffset = 3;
        float rot;

        float cardWidth = min(height,width) /4 * 0.6f;
        float cardHeight = min(height,width) /4;

        float x ;
        float y = plPos.y - nrOfCards*xOffset/2;
        rot =  nrOfCards/2 * rotOffset;
        for (Actor act : getChildren()) {
            if(act instanceof OpponentCard) {
                OpponentCard crd = (OpponentCard) act;
                if (inverse) {
                    x = (float) (-sqrt(abs(R * R - (y - centerPos.y) * (y - centerPos.y))) + centerPos.x);
                    crd.setRotation(rot + rotation);
                } else {
                    x = (float) (sqrt(abs(R * R - (y - centerPos.y) * (y - centerPos.y))) + centerPos.x);
                    crd.setRotation(-rot + rotation);
                }

                if(crd.isFlipped()){

                }else {
                    crd.setWidth(cardWidth * 3 / 5);
                    crd.setHeight(cardHeight * 3 / 5);
                    crd.setOriginX(cardWidth * 3 / 5 / 2);
                    crd.setPosition(x, y);
                    if(!inverse){
                        crd.setRotation(-rot + rotation);
                    }else{
                        crd.setRotation(+rot + rotation);
                    }
                    crd.setTouchable(Touchable.disabled);
                    rot -= rotOffset;
                    y += xOffset;
                }
            }
        }
    }

    public void putCastCard(String str){
        int max = 7;
        int min = 0;
        int range = max - min + 1;
        //int rand = (int)(Math.random() * range) + min;

        int i = 0;
        for (Actor act:getChildren()) {
            if(act instanceof  OpponentCard && i == cardNr){
                OpponentCard castCardLocal = (OpponentCard)act;
                System.out.println("[OpponetHUD] putCastCard str = " + str);
                castCardLocal.setDrawable( new SpriteDrawable(new Sprite(cardsTextureRepository.getCardTexture(str))));
                castCardLocal.debug();
                MoveToAction mvb = new MoveToAction();
                mvb.setPosition(castCardPosition.x,castCardPosition.y);
                mvb.setDuration(0.2f);
                mvb.setInterpolation(Interpolation.circleOut);
                castCardLocal.addAction(mvb);
                castCardLocal.setFlipped(true);
            }
            i++;
        }
        cardNr++;
        nrOfCards--;
    }

    public void refreshOppCards(int nrOfCards){
        this.viewport = viewport;
        this.nrOfCards = nrOfCards;
        this.cardsTextureRepository = cardsTextureRepository;
        for (Actor act:getChildren()) {
            if(act instanceof OpponentCard){
                this.removeActor(act);
            }
        }
        for(int i = 0; i < nrOfCards ; i++){
            OpponentCard card = new OpponentCard(cardsTextureRepository.getCardTexture("back"));
            this.addActor(card);
        }
    }

    public Vector2 getCastCardPosition() {
        return castCardPosition;
    }

    public void setCastCardPosition(Vector2 castCardPosition) {
        this.castCardPosition = castCardPosition;
    }

    public String getNickname() {
        return nickname;
    }
}
