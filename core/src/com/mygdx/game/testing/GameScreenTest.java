package com.mygdx.game.testing;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.MoveByAction;
import com.badlogic.gdx.scenes.scene2d.actions.RotateByAction;
import com.badlogic.gdx.scenes.scene2d.actions.ScaleByAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
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

    String[] cards = {"h-14","h-13","d-14","d-12"};
    int i = 0;
    int nrOfCards = 8;

    PlayerHUDAct plHUD;
    OpponentHUD oppHUD1;
    OpponentHUD oppHUD2;
    OpponentHUD oppHUD3;
    OpponentHUD oppHUD4;
    OpponentHUD oppHUD5;

    public GameScreenTest(CardsTextureRepository cardsTextureRepository) {
        viewport = new ScreenViewport();
        stage = new Stage(viewport);

        this.cardsTextureRepository = cardsTextureRepository;
    }

    @Override
    public void show() {
        skin = new Skin(Gdx.files.internal(Constants.skinJsonString));
        Gdx.input.setInputProcessor(stage);
        List<String> str = new ArrayList<>();
        str.add("h-12");
        str.add("h-11");
        str.add("h-10");
        str.add("h-9");
        str.add("h-8");
        str.add("h-7");
        str.add("h-6");
        str.add("h-5");

         plHUD = new PlayerHUDAct(cardsTextureRepository,viewport,this);
         plHUD.refreshCards(str);
        oppHUD1 = new OpponentHUD(8,cardsTextureRepository,viewport);
        oppHUD2 = new OpponentHUD(8,cardsTextureRepository,viewport);
        oppHUD3 = new OpponentHUD(8,cardsTextureRepository,viewport);
        oppHUD4 = new OpponentHUD(8,cardsTextureRepository,viewport);
        oppHUD5 = new OpponentHUD(8,cardsTextureRepository,viewport);
        oppHUD1.setCastCardPosition(new Vector2(viewport.getScreenWidth()*0.2f , viewport.getScreenHeight()*0.25f));
        oppHUD2.setCastCardPosition(new Vector2(viewport.getScreenWidth()*0.25f , viewport.getScreenHeight()*0.55f));
        oppHUD3.setCastCardPosition(new Vector2(viewport.getScreenWidth()*0.5f  , viewport.getScreenHeight()*0.6f ));
        oppHUD4.setCastCardPosition(new Vector2(viewport.getScreenWidth()*0.75f , viewport.getScreenHeight()*0.55f));
        oppHUD5.setCastCardPosition(new Vector2(viewport.getScreenWidth()*0.75f , viewport.getScreenHeight()*0.25f));
        stage.addActor(oppHUD1);
        stage.addActor(oppHUD2);
        stage.addActor(oppHUD3);
        stage.addActor(oppHUD4);
        stage.addActor(oppHUD5);
        stage.addActor(plHUD);


        TextButton flipButton = new TextButton("StartAction",skin);
        flipButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
//                System.out.println("Pressed");
//                RotateByAction action1 = new RotateByAction();
//                if(i > 180)
//                    i = 0;
//                System.out.println("Radius: " + i);
//                action1.setAmount(i++);
//                action1.setDuration(1f);
//                ScaleByAction action3 = new ScaleByAction();
//                action3.setAmount(0.1f);
//                stage.getActors().first().addAction(action1);
//                stage.getActors().first().addAction(action3);
                oppHUD1.putCastCard("h-12");
                oppHUD2.putCastCard("h-12");
                oppHUD3.putCastCard("h-12");
                oppHUD4.putCastCard("h-12");
                oppHUD5.putCastCard("h-12");
                resizeOpponents(viewport.getScreenWidth(),viewport.getScreenHeight());
                plHUD.setBidHUDVisibility(!plHUD.bidHudVisibility);
            }
        });
        stage.addActor(flipButton);
    }


    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.8f,0.8f, 0.8f, 1.0f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);


        stage.act(delta);
        stage.draw();
    }


    @Override
    public void resize(int width, int height) {
        float offset = viewport.getScreenWidth()/50;
        plHUD.resize();

        resizeOpponents(viewport.getScreenWidth(),viewport.getScreenHeight());
    }

    public void resizeOpponents(float width, float height){
        float offset = width / 40;

        Vector2 plPos;
        Vector2 centerPos;

            plPos = new Vector2(width / 4, height / 2.9f);
            centerPos = new Vector2(-width*0.9f , height / 6);
            oppHUD1.positionCardsVert( width,  height, plPos, centerPos,offset* 0.6f, width, -90, false);

            plPos = new Vector2(width *0.2f ,height / 1.35f);
            centerPos =  new Vector2(width* 0.05f, height * 1.65f );
            oppHUD2.positionCardsHor(width, height, plPos, centerPos, offset* 0.6f, height,-155,true);

            plPos = new Vector2(width / 2 ,height / 2);
            centerPos =  new Vector2(width / 2, height * 1.75f );
            oppHUD3.positionCardsHor(width, height, plPos, centerPos, offset*0.4f, height,180,true);

            plPos = new Vector2(width * 0.8f, height / 1.35f);
            centerPos = new Vector2(width *0.95f , height * 1.65f);
            oppHUD4.positionCardsHor(width, height, plPos, centerPos, offset*0.6f, height,155,true);

            plPos = new Vector2(width *7 / 8, height / 2.9f);
            centerPos = new Vector2(width * 1.94f *0.95f , height / 6);
            oppHUD5.positionCardsVert( width,  height, plPos, centerPos,offset* 0.6f, width, 90, true);


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
}
