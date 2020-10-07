package com.mygdx.game.presentationLayer.renderObjects;



import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
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
    private float r;
    BitmapFont font12;//p
    Vector2 castCardPosition;


    float castCardRot;

    public int nrOfCards = 8;

    FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("JosefinSans-SemiBold.ttf"));
    FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();

    public Vector2 castCatrdPosition = new Vector2();
    public Vector2 centerPosition;
   // public List<Card2> cards =  new ArrayList<>();

    //ADD HERE MORE DATA

    public OpponentHud(CardsTextureRepository cardsTexture, NewGameScreen newGameScreen,Vector2  castPos, float castRot){
        this.newGameScreen =  newGameScreen;
        this.cardsTexture = cardsTexture;
        this.offset = newGameScreen.screenWidth/50;
        parameter.size = 34;
        font12 = generator.generateFont(parameter);
        font12.setColor(Color.BLACK);
        System.out.println(castPos);
        castCardPosition = castPos;
        castCardRot = castRot;
        //initCards();
    }

    public void initCards(){
        this.clear();
        for(int i=0 ; i<nrOfCards; i++){
            Card2 crd = new Card2("h-4",cardsTexture.getCardTexture("back"),cardsTexture.getCardTexture("back"),new Vector2(0,0),new Vector2(100 , 100));
            crd.setFlipped(false);
            this.addActor(crd);
            System.out.println(i);
        }
    }


    public void putCastCard(String str){
        int max = 7;
        int min = 0;
        int range = max - min + 1;
        int rand = (int)(Math.random() * range) + min;

        int i = 0;
        for (Actor act:getChildren()) {
            if(act instanceof  Card2 && i == rand){
                Card2 castCardLocal = (Card2)act;
                castCardLocal.setFrontImage(cardsTexture.getCardTexture(str));
                castCardLocal.debug();
               // System.out.println(castCardPosition.x + " " +castCatrdPosition.y );
               castCardLocal.rePosition(castCardPosition.x,castCardPosition.y);
                castCardLocal.setRot(castCardRot);
                castCardLocal.setFlipped(true);
            }
            i++;
        }
    }


    @Override
    public void act(float delta) {
        super.act(delta);
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

        System.out.println("childer = "  + getChildren().size);

        for (Actor act : getChildren()) {
            if(act instanceof Card2) {
                Card2 crd = (Card2) act;
                if(inverse){
                    y = (float) (-sqrt(abs(R*R - (x-centerPos.x)*(x-centerPos.x))) + centerPos.y);
                }else{
                    y = (float) (sqrt(abs(R*R - (x-centerPos.x)*(x-centerPos.x))) + centerPos.y);
                }
                //crd.debug();
                if(crd.isFlipped()){
                    crd.rePosition(castCardPosition.x,castCardPosition.y);
                }else{
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
            if(act instanceof Card2) {
                Card2 crd = (Card2) act;
                if (inverse) {
                    x = (float) (-sqrt(abs(R * R - (y - centerPos.y) * (y - centerPos.y))) + centerPos.x);
                    crd.setRot(rot + rotation);
                } else {
                    x = (float) (sqrt(abs(R * R - (y - centerPos.y) * (y - centerPos.y))) + centerPos.x);
                    crd.setRot(-rot + rotation);
                }

                if(crd.isFlipped()){
                    crd.rePosition(castCardPosition.x,castCardPosition.y);
                }else {
                    crd.setWidth(cardWidth * 3 / 5);
                    crd.setHeight(cardHeight * 3 / 5);
                    crd.setOriginX(cardWidth * 3 / 5 / 2);
                    crd.rePosition(x, y);
                    if(!inverse){
                        crd.setRot(-rot + rotation);
                    }else{
                        crd.setRot(+rot + rotation);
                    }

                    //crd.setTouchable(Touchable.disabled);
                    rot -= rotOffset;
                    y += xOffset;
                }
            }
        }
    }


    public Vector2 getCastCardPosition() {
        return castCardPosition;
    }

    public void setCastCardPosition(Vector2 castCardPosition) {
        this.castCardPosition = castCardPosition;
    }



}
