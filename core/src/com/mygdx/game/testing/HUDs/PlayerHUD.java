package com.mygdx.game.testing.HUDs;


import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;

import com.badlogic.gdx.scenes.scene2d.actions.DelayAction;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.actions.ParallelAction;
import com.badlogic.gdx.scenes.scene2d.actions.RotateToAction;
import com.badlogic.gdx.scenes.scene2d.actions.ScaleToAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.businessLayer.controllers.GameController;

import com.mygdx.game.dataLayer.repositories.CardsTextureRepository;
import com.mygdx.game.testing.Atoms.Circle;
import com.mygdx.game.testing.GameScreenTest;
import com.mygdx.game.testing.Atoms.PlayerCard;


import java.util.List;

import static java.lang.Math.abs;
import static java.lang.Math.min;
import static java.lang.Math.sqrt;


public class PlayerHUD extends Group {

    public int nrOfCards;
    private Viewport viewport;
    public boolean bidHudVisibility  = false;
    private Vector2 deckPos;

    CardsTextureRepository cardsTextureRepository;
    GameController gameController;
    GameScreenTest gameScreenTest;



    public PlayerHUD(CardsTextureRepository cardsTextureRepository, Viewport viewport, GameScreenTest gameScreenTest, GameController controller, Vector2 deckPosition) {
        this.viewport = viewport;
        gameController = controller;
        this.gameScreenTest = gameScreenTest;
        this.cardsTextureRepository = cardsTextureRepository;
        this.deckPos = deckPosition;
        BidHUD bidHUD =  new BidHUD(gameScreenTest, viewport);
        this.addActor(bidHUD);
        Circle rCircle = new Circle(new Texture("redCircle.png"));
        Circle gCircle = new Circle(new Texture("greenCircle.png"));
            rCircle.setPosition(viewport.getScreenWidth()*0.8f,viewport.getScreenHeight() * 0.05f);
            rCircle.setWidth(viewport.getScreenWidth() /30);
            rCircle.setHeight(viewport.getScreenHeight() /30);
            rCircle.setName("redCircle");
        addActor(rCircle);
            gCircle.setPosition(viewport.getScreenWidth()*0.8f,viewport.getScreenHeight() * 0.05f);
            gCircle.setWidth(viewport.getScreenWidth() /30);
            gCircle.setHeight(viewport.getScreenHeight() /30);
            gCircle.setVisible(false);
            gCircle.setName("greenCircle");
         addActor(gCircle);

        setBidHUDVisibility(false);
    }



    public void refreshCards(List<String> crd){
        nrOfCards = crd.size();
        clearPlayerHUDofCards();

        for (String str:crd) {
            PlayerCard card = new PlayerCard(cardsTextureRepository.getCardTexture(str),cardsTextureRepository.getCardTexture("back"),viewport,gameScreenTest.tableHUD);
            card.setOrigPos(deckPos.x,deckPos.y);
            card.setPosition(deckPos.x,deckPos.y);
            card.setID(str);
            card.setName(str);
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

    public void changeCircle(boolean canChooseCard) {
        for (Actor act : getChildren()) {
            if (act instanceof Circle) {
                if (canChooseCard && act.getName().equals("greenCircle")) {
                    act.setVisible(true);
                }
                if (canChooseCard && act.getName().equals("redCircle")) {
                    act.setVisible(false);
                }
                if (!canChooseCard && act.getName().equals("greenCircle")) {
                    act.setVisible(false);
                }
                if (!canChooseCard && act.getName().equals("redCircle")) {
                    act.setVisible(true);
                }
            }
        }
    }

    public void resize () {


        //todo de bumarat cartile care nu au falgul true si facut resize pe ele
        System.out.println("[PlayerHUD] resize");
        float width = min(viewport.getScreenHeight(), viewport.getScreenWidth()) / 4 * 0.6f * 0.7f;
        float height = min(viewport.getScreenHeight(), viewport.getScreenWidth()) / 4 * 0.7f;
        float xOffset = viewport.getScreenWidth()/20;
        float rotOffset = 3;
        if (nrOfCards == 0) {
            //System.out.println("return");
            return;
        }
        float rot = nrOfCards / 2 * rotOffset;
        float x = viewport.getScreenWidth() / 2 - xOffset * nrOfCards / 2 + xOffset / 2;
        float y;
        float Cy = -viewport.getScreenHeight() * 2.95f;
        float Cx = viewport.getScreenWidth() / 2;
        float R = viewport.getScreenHeight() * 3;

        float delay = 0;

        for (Actor act : getChildren()) {
            if (act instanceof PlayerCard) {
                final PlayerCard localCard = (PlayerCard) act;
                if(localCard.isPutDown()){

                }else{
                y = (float) (sqrt(abs(R * R - (x - Cx) * (x - Cx))) + Cy);
                localCard.flip( false);
                localCard.setWidth(width);
                localCard.setHeight(height);
                localCard.setOrigPos(x - act.getWidth() / 4, y);

                    DelayAction delayAction = new DelayAction();
                    delayAction.setDuration(delay);
                    delay = delay + 0.4f;

                    MoveToAction moveToAction = new MoveToAction();
                    moveToAction.setPosition(x - act.getWidth() / 4, y);
                    moveToAction.setDuration(0.5f);

                    RotateToAction rotateByAction  = new RotateToAction();
                    rotateByAction.setRotation(rot);
                    rotateByAction.setDuration(0.5f);

                    ParallelAction moveAndRotate  = new ParallelAction(moveToAction,rotateByAction);
                    Action flip = new Action(){
                        public boolean act( float delta ) {
                            localCard.flip(true);
                            return true;
                        }
                    };

                    ScaleToAction scaleMIN  = new ScaleToAction();
                    scaleMIN.setScale(0.01f,1f);
                    scaleMIN.setDuration(0.25f);

                    ScaleToAction scaleMAX  = new ScaleToAction();
                    scaleMAX.setScale(1f,1f);
                    scaleMAX.setDuration(0.25f);

                    SequenceAction sqAct = new SequenceAction(delayAction,moveAndRotate,scaleMIN,flip,scaleMAX);
                    localCard.addAction(sqAct);

                x += xOffset;
                rot -= rotOffset;
                }
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

    private void clearPlayerHUDofCards(){
        for (Actor act : getChildren()) {
            if(act instanceof PlayerCard){
                this.removeActor(act);
            }
        }
    }


    public void sendCard(String crd){
        gameController.sendCard(crd);
    }

    public void sendBidValue(int bid ){
        gameController.sendBid(bid);
    }


    public boolean getCanChooseCard() {
        return gameController.getCanChooseCard();
    }



}
