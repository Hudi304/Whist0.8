package com.mygdx.game.testing;


import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;

import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.businessLayer.controllers.GameController;

import com.mygdx.game.dataLayer.repositories.CardsTextureRepository;


import java.util.List;

import static java.lang.Math.abs;
import static java.lang.Math.min;
import static java.lang.Math.sqrt;


public class PlayerHUDAct extends Group {

    public int nrOfCards;
    private Viewport viewport;
    public boolean bidHudVisibility  = false;
    CardsTextureRepository cardsTextureRepository;
    GameController gameController;


    public PlayerHUDAct(CardsTextureRepository cardsTextureRepository, Viewport viewport,GameScreenTest gameScreenTest,GameController controller) {
        this.viewport = viewport;
        gameController = controller;
        this.cardsTextureRepository = cardsTextureRepository;
//        for (String str:crd) {
//            PlayerCard card = new PlayerCard(cardsTextureRepository.getCardTexture(str));
//                    this.addActor(card);
//        }
        BidHUD bidHUD =  new BidHUD(gameScreenTest, viewport);
        this.addActor(bidHUD);
        setBidHUDVisibility(false);
    }

    public void refreshCards(List<String> crd){
        nrOfCards = crd.size();
        for (Actor act : getChildren()) {
           if(act instanceof PlayerCard){
               this.removeActor(act);
           }
        }
        for (String str:crd) {
            PlayerCard card = new PlayerCard(cardsTextureRepository.getCardTexture(str));
            this.addActor(card);
        }

        for(Actor act: getChildren()){
            if (act instanceof BidHUD) {
                BidHUD bd = (BidHUD) act;
                bd.setNumberOfCards(crd.size());
                return;
            }
        }

    }

    public void resize () {
        float width = min(viewport.getScreenHeight(), viewport.getScreenWidth()) / 4 * 0.6f * 0.7f;
        float height = min(viewport.getScreenHeight(), viewport.getScreenWidth()) / 4 * 0.7f;
        float xOffset = viewport.getScreenWidth()/20;
        float rotOffset = 3;
        if (nrOfCards == 0) {
            System.out.println("return");
            return;
        }
        float rot = nrOfCards / 2 * rotOffset;


        float x = viewport.getScreenWidth() / 2 - xOffset * nrOfCards / 2 + xOffset / 2;
        float y;
        float Cy = -viewport.getScreenHeight() * 2.95f;
        float Cx = viewport.getScreenWidth() / 2;
        float R = viewport.getScreenHeight() * 3;

        for (Actor act : getChildren()) {
            if (act instanceof PlayerCard) {
                PlayerCard localCard = (PlayerCard) act;
                y = (float) (sqrt(abs(R * R - (x - Cx) * (x - Cx))) + Cy);
                localCard.setWidth(width);
                localCard.setHeight(height);
                localCard.setOrigPos(x - act.getWidth() / 4, y);
                localCard.setPosition(x - act.getWidth() / 4, y);
                localCard.setRotation(rot);
                x += xOffset;
                rot -= rotOffset;
            }
            if(act instanceof BidHUD){
                BidHUD bdHUD = (BidHUD)act;
                bdHUD.resizeBidHUD(viewport.getScreenWidth(),viewport.getScreenHeight());
            }
        }

    }


    public void setBidHUDVisibility(boolean visibility) {
        bidHudVisibility = visibility;
        for (Actor act : getChildren()) {
            if (act instanceof BidHUD) {
                BidHUD bidHUD = (BidHUD) act;
                for (Actor bidAct : bidHUD.getChildren()) {
                    bidAct.setVisible(visibility);
                }
            }
        }
    }

    public void setForbiddenValue (int forb){
        for (Actor act : getChildren()) {
            if (act instanceof BidHUD) {
                BidHUD bdHUD = (BidHUD) act;
                bdHUD.setForbiddenValue(forb);
                return;
            }

        }
    }

    public void sendBidValue(int bid ){
        gameController.sendBid(bid);
    }



}
