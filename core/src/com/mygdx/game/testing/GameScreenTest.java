package com.mygdx.game.testing;


import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.MoveByAction;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.actions.RotateByAction;
import com.badlogic.gdx.scenes.scene2d.actions.ScaleByAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.businessLayer.controllers.GameController;
import com.mygdx.game.businessLayer.others.Constants;
import com.mygdx.game.dataLayer.generics.Card;
import com.mygdx.game.dataLayer.repositories.CardsTextureRepository;

import java.util.ArrayList;
import java.util.List;

public class GameScreenTest implements Screen {

    Stage stage;
    Skin skin;
    CardsTextureRepository cardsTextureRepository;
    Viewport viewport;
    GameController controller;

    String[] cards = {"h-14", "h-13", "d-14", "d-12"};
    int i = 0;
    int nrOfCards = 8;

    PlayerHUDAct plHUD;
    OpponentHUD oppHUD1 = null;
    OpponentHUD oppHUD2 = null;
    OpponentHUD oppHUD3 = null;
    OpponentHUD oppHUD4 = null;
    OpponentHUD oppHUD5 = null;


    public GameScreenTest(CardsTextureRepository cardsTextureRepository, GameController controller) {
        viewport = new ScreenViewport();
        skin = new Skin(Gdx.files.internal(Constants.skinJsonString));
        stage = new Stage(viewport);
        this.controller = controller;

        this.cardsTextureRepository = cardsTextureRepository;
        plHUD = new PlayerHUDAct(cardsTextureRepository, viewport, this, controller);
        stage.addActor(plHUD);
    }

    @Override
    public void show() {


        Gdx.input.setInputProcessor(stage);


        TextButton flipButton = new TextButton("StartAction", skin);
        flipButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {

                oppHUD1.putCastCard("h-12");
                oppHUD2.putCastCard("h-12");
                oppHUD3.putCastCard("h-12");
                oppHUD4.putCastCard("h-12");
                oppHUD5.putCastCard("h-12");
                resizeOpponents(viewport.getScreenWidth(), viewport.getScreenHeight());
                plHUD.setBidHUDVisibility(!plHUD.bidHudVisibility);


            }
        });
        stage.addActor(flipButton);

    }


    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.8f, 0.8f, 0.8f, 1.0f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);


        stage.act(delta);

        stage.draw();
    }


    @Override
    public void resize(int width, int height) {
        float offset = viewport.getScreenWidth() / 50;
        plHUD.resize();

        resizeOpponents(viewport.getScreenWidth(), viewport.getScreenHeight());
    }

    public void resizeOpponents(float width, float height) {
        float offset = width / 40;

        Vector2 plPos;
        Vector2 centerPos;

        plPos = new Vector2(width / 4, height / 2.9f);
        centerPos = new Vector2(-width * 0.9f, height / 6);
        if (oppHUD1 != null)
            oppHUD1.positionCardsVert(width, height, plPos, centerPos, offset * 0.6f, width, -90, false);

        plPos = new Vector2(width * 0.2f, height / 1.35f);
        centerPos = new Vector2(width * 0.05f, height * 1.65f);
        if (oppHUD2 != null)
            oppHUD2.positionCardsHor(width, height, plPos, centerPos, offset * 0.6f, height, -155, true);

        plPos = new Vector2(width / 2, height / 2);
        centerPos = new Vector2(width / 2, height * 1.75f);
        if (oppHUD3 != null)
            oppHUD3.positionCardsHor(width, height, plPos, centerPos, offset * 0.4f, height, 180, true);

        plPos = new Vector2(width * 0.8f, height / 1.35f);
        centerPos = new Vector2(width * 0.95f, height * 1.65f);
        if (oppHUD4 != null)
            oppHUD4.positionCardsHor(width, height, plPos, centerPos, offset * 0.6f, height, 155, true);

        plPos = new Vector2(width * 7 / 8, height / 2.9f);
        centerPos = new Vector2(width * 1.94f * 0.95f, height / 6);
        if (oppHUD5 != null)
            oppHUD5.positionCardsVert(width, height, plPos, centerPos, offset * 0.6f, width, 90, true);


    }


    @Override
    public void pause() {

    }


    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }


    @Override
    public void dispose() {

    }

    public void initOpponents(List<String> opponents) {
        int i = 1;
        System.out.println("Opponents: " + opponents.size());
        for (String opp : opponents) {
            //todo vezi fa caluema pozitii alea de put down card p[entru opponenbnti
            switch (i) {
                case 1:
                    oppHUD1 = new OpponentHUD(opp, 0, cardsTextureRepository, viewport);
                    stage.addActor(oppHUD1);
                    oppHUD1.setCastCardPosition(new Vector2(viewport.getScreenWidth() / 2, viewport.getScreenHeight() / 2));
                    break;
                case 2:
                    oppHUD2 = new OpponentHUD(opp, 0, cardsTextureRepository, viewport);
                    stage.addActor(oppHUD2);
                    oppHUD2.setCastCardPosition(new Vector2(viewport.getScreenWidth() / 2, viewport.getScreenHeight() / 2));
                    break;
                case 3:
                    oppHUD3 = new OpponentHUD(opp, 0, cardsTextureRepository, viewport);
                    stage.addActor(oppHUD3);
                    oppHUD3.setCastCardPosition(new Vector2(viewport.getScreenWidth() / 2, viewport.getScreenHeight() / 2));
                    break;
                case 4:
                    oppHUD4 = new OpponentHUD(opp, 0, cardsTextureRepository, viewport);
                    stage.addActor(oppHUD4);
                    oppHUD4.setCastCardPosition(new Vector2(viewport.getScreenWidth() / 2, viewport.getScreenHeight() / 2));
                    break;
                case 5:
                    oppHUD5 = new OpponentHUD(opp, 0, cardsTextureRepository, viewport);
                    stage.addActor(oppHUD5);
                    oppHUD5.setCastCardPosition(new Vector2(viewport.getScreenWidth() / 2, viewport.getScreenHeight() / 2));
                    break;
            }
            i++;
        }
    }

    public void updateCardForOpp(String nickName, String card) {
        for (Actor opp : stage.getActors()) {
            if (opp instanceof OpponentHUD) {
                OpponentHUD oppHUD = (OpponentHUD) opp;
                if (oppHUD.nickname.equals(nickName)) {
                    oppHUD.putCastCard(card);
                    return;
                }
            }
        }
    }

    public void updateOpponentsCards(int numberOfCards) {
        if (oppHUD1 != null)
            oppHUD1.refreshOppCards(numberOfCards);

        if (oppHUD2 != null)
            oppHUD2.refreshOppCards(numberOfCards);

        if (oppHUD3 != null)
            oppHUD3.refreshOppCards(numberOfCards);

        if (oppHUD4 != null)
            oppHUD4.refreshOppCards(numberOfCards);

        if (oppHUD5 != null)
            oppHUD5.refreshOppCards(numberOfCards);

        resizeOpponents(viewport.getScreenWidth(), viewport.getScreenHeight());

    }

    public void updatePlayerCards(List<String> cards) {
        plHUD.refreshCards(cards);
        plHUD.resize();

    }


    public void setForbiddenValue(int value) {
        if (value != -1)
            plHUD.setForbiddenValue(value);
    }

    public void seteBidHudVisibile(boolean visibile) {
        plHUD.setBidHUDVisibility(visibile);
    }


    public void playerWonHand() {
        for (Actor act : plHUD.getChildren()) {
            if (act instanceof PlayerCard) {
                PlayerCard plCrd = (PlayerCard) act;
                if (plCrd.isPutDown()) {
                    MoveToAction moveToAction = new MoveToAction();
                    moveToAction.setPosition(100, 100);
                    moveToAction.setDuration(1f);
                    plCrd.addAction(moveToAction);
                }
            }
        }
        for (Actor act : stage.getActors()) {
            if (act instanceof OpponentHUD) {
                OpponentHUD opp = (OpponentHUD) act;
                for (Actor crd : opp.getChildren()) {
                    if (crd instanceof OpponentCard) {
                        OpponentCard plCrd = (OpponentCard) crd;
                        if (plCrd.isFlipped()) {
                            MoveToAction moveToAction = new MoveToAction();
                            moveToAction.setPosition(100, 100);
                            moveToAction.setDuration(1f);
                            plCrd.addAction(moveToAction);
                        }
                    }
                }
            }
        }
    }

    public void opponentWonHand(String winner) {
        for (Actor act : plHUD.getChildren()) {
            if (act instanceof PlayerCard) {
                PlayerCard plCrd = (PlayerCard) act;
                if (plCrd.isPutDown()) {
                    MoveToAction moveToAction = new MoveToAction();
                    moveToAction.setPosition(100, 100);
                    moveToAction.setDuration(1f);
                    plCrd.addAction(moveToAction);
                }
            }
        }
        for (Actor act : stage.getActors()) {
            if (act instanceof OpponentHUD) {
                OpponentHUD opp = (OpponentHUD) act;
                for (Actor crd : opp.getChildren()) {
                    if (crd instanceof OpponentCard) {
                        OpponentCard plCrd = (OpponentCard) crd;
                        if (plCrd.isFlipped()) {
                            MoveToAction moveToAction = new MoveToAction();
                            moveToAction.setPosition(100, 100);
                            moveToAction.setDuration(1f);
                            plCrd.addAction(moveToAction);
                        }
                    }
                }
            }
        }
    }
}
