package com.mygdx.game.presentationLayer.renderObjects;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.mygdx.game.businessLayer.others.Constants;
import com.mygdx.game.dataLayer.generics.Card;
import com.mygdx.game.dataLayer.generics.Card2;
import com.mygdx.game.dataLayer.repositories.CardsTextureRepository;
import com.mygdx.game.presentationLayer.screens.NewGameScreen;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static java.lang.Math.abs;
import static java.lang.Math.min;
import static java.lang.Math.sqrt;

public class PlayerHud extends Group{

    public NewGameScreen newGameScreen;

    public CardsTextureRepository cardsTexture;
    private Stage stage;

    Slider bidSlider;//p
    Label bidVal;//p
    TextButton bidButton;//p
    Group bidHUD = new Group();

    private List<Card2> playerCard;
    private List<String> strPlayerCard;
    int nrOfCards = 0;

    public  void initCards (List<String> strLst,float screenWidth){
        getChildren().clear();
        int i = strLst.size() * 40/2;
        Card2 crd = null;
        for(String str: strLst){
            crd = new Card2(str,cardsTexture.getCardTexture("back"),cardsTexture.getCardTexture(str),new Vector2(0,0),new Vector2(screenWidth -i ,Constants.CARD_HAND_Y),newGameScreen) ;
            this.addActor(crd);
            i-=40;
            nrOfCards++;
        }

        this.resizeCards();

    }

    public void createBidHUD(){
        bidSlider = new Slider(0f,8f,1, false, newGameScreen.skin);
        bidVal = new Label( "[" + (int)bidSlider.getValue() + "/" + nrOfCards + "]",newGameScreen.skin);
        bidButton =  new TextButton("Bid",newGameScreen.skin);

        bidVal.setPosition(newGameScreen.screenWidth/2 + bidSlider.getWidth()/2,newGameScreen.screenHeight/2);
        bidSlider.setPosition(newGameScreen.screenWidth/2 - bidSlider.getWidth()/2,newGameScreen.screenHeight/2);
        bidButton.setPosition(newGameScreen.screenWidth/2 ,newGameScreen.screenHeight/2 - bidSlider.getHeight()*2);
        bidButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                //System.out.println("[Game] Bid btn pressed");

//                //todo vezi ce faci cu Sliderul
//                if(forbidenBet == (int)bidSlider.getValue()){
//                    System.out.println("Esti prost");
//                }
//                else {
//                    mainController.sendBid((int)bidSlider.getValue());
//                }
            }
        });
        bidHUD.addActor(bidSlider);
        bidHUD.addActor(bidVal);
        bidHUD.addActor(bidButton);
        bidHUD.setName("bidHUD");
        bidHUD.setVisible(false);
        this.addActor(bidHUD);
    }

    public void bidHUDVisibility(boolean visibility){
        bidHUD.setVisible(visibility); //NOT WORKING
//        for(Actor child: this.getChildren()){
//            if(child.getName().equals("bidHUD"))
//            {
//                System.out.println("Making BID HUD visible: " + visibility);
//                child.setVisible(visibility);
//                return;
//            }
//        }
//        System.out.println("BID HUD NOT FOUND!");
    }


    public PlayerHud(CardsTextureRepository cardsTexture, NewGameScreen newGameScreen) {
        this.newGameScreen =  newGameScreen;
        this.cardsTexture = cardsTexture;
        playerCard = new ArrayList<>();

    }

    public void resizeHUD(){
        resizeCards();
        resizeBidHUD();
    }

    //everything is set relative to the position of the bidBtn
    void resizeBidHUD(){
        bidButton.setPosition(newGameScreen.screenWidth/2 ,newGameScreen.screenHeight/2 - bidSlider.getHeight()*2);
        bidSlider.setWidth(newGameScreen.screenWidth/4);
        bidSlider.setDebug(true);
        bidSlider.setHeight(newGameScreen.screenHeight/15);
        bidSlider.getStyle().knob.setMinHeight(newGameScreen.screenHeight/15);
        bidSlider.getStyle().knob.setMinWidth(newGameScreen.screenHeight/16);
        bidSlider.getStyle().background.setMinHeight(bidSlider.getHeight()/2);
        bidSlider.setPosition(bidButton.getX() - bidVal.getWidth()/2 - bidSlider.getWidth()/2,bidButton.getY() + bidButton.getHeight());

        bidVal.setSize(newGameScreen.screenHeight/15,newGameScreen.screenHeight/15);
        bidVal.setPosition( bidSlider.getX() + bidSlider.getWidth() + newGameScreen.screenWidth/45 ,bidSlider.getY());
    }

    public void resizeCards(){
        float width = min(newGameScreen.screenHeight,newGameScreen.screenWidth) /4 * 0.6f * 0.7f;
        float height = min(newGameScreen.screenHeight,newGameScreen.screenWidth) /4 * 0.7f;
        float xOffset;
        float rotOffset = 3;
        if(nrOfCards == 0){
            System.out.println("return");
            return;
        }
        float rot =  nrOfCards/2 * rotOffset;

        if (newGameScreen.screenHeight > newGameScreen.screenWidth){
            xOffset = newGameScreen.screenWidth/12;
        }else{
            xOffset = newGameScreen.screenWidth/20;
        }

        float x = newGameScreen.screenWidth/2 - xOffset*nrOfCards/2 + xOffset/2;
        float y;
        float Cy = -newGameScreen.screenHeight *2.95f;
        float Cx = newGameScreen.screenWidth/2;
        float R =  newGameScreen.screenHeight*3;

        for (Actor crd :getChildren()) {
            if(crd instanceof Card2){
                Card2 localCard = (Card2) crd;
                y = (float) (sqrt(abs(R*R - (x-Cx)*(x-Cx))) + Cy) ;
                localCard.setWidth(width);
                localCard.setHeight(height);
                localCard.setOriginX(width/2);
                localCard.rePosition(x -crd.getWidth()/4,y);
                localCard.setRot(rot);
                x += xOffset;
                rot -= rotOffset;

            }
        }
    }


    public void act(float delta){
        super.act(delta);
    }


    public void setFlipped(){
        for (Actor act : getChildren()) {
            if(act instanceof Card2){
                Card2 localCard = (Card2) act;
                localCard.setFlipped(!localCard.isFlipped());
            }
        }

    }

}
