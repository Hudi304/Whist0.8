package com.mygdx.game.presentationLayer.screens;



import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.mygdx.game.Client;
import com.mygdx.game.businessLayer.others.Constants;
import com.mygdx.game.dataLayer.generics.Card;
import com.badlogic.gdx.graphics.GL20;

import java.util.ArrayList;
import java.util.List;

public class NewGameScreen implements Screen {


    float screenWidth;
    float screenHeight;
    Client client;

    //opponets
            // position
            //cards
            //score
            //nickname
    //Plyer
    public List<Card> hand = new ArrayList<>();
    public Texture cardSprite = new Texture("cardSprite.gif");
    public TextureRegion[][] regions;
           // cardHUD
           // bidHUD
    //Cards
        //image
        //value
        //symbol

    //tableStatus
    //score


//adauga in scene un grup de carti si in stage
    TextButton flipBtn;//p
    Group group;

    ScreenViewport viewport;
    public Stage stage;
    Card crd;
    Skin skin;

    public NewGameScreen(Client client){
        this.client =  client;
        System.out.println("[NewGameScreen] : Constructor");
        regions = TextureRegion.split(cardSprite, 81, 117);
        viewport = new ScreenViewport();
        stage = new Stage(viewport);
        skin = new Skin(Gdx.files.internal(Constants.skinJsonString));
        this.client =  client;
    }



    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);

        flipBtn = new TextButton("Flip",skin);
        flipBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
              crd.setFlipped(!crd.isFlipped());
            }
        });

        screenWidth = Gdx.graphics.getWidth();
        screenHeight = Gdx.graphics.getHeight();


        System.out.println("[NewGameScreen] Show()");
        System.out.println("screeWidth=" + screenWidth + "|" + "screenHeight=" + screenHeight);
        Vector2 spPos = new Vector2(screenWidth/2,-100);
        Vector2 tgPos = new Vector2(screenWidth/2,screenHeight/2);


        crd =  new Card("h-8",regions[0][0],spPos,tgPos ,this,group);
        //crd.getCardActor().setOriginX(crd.getCardActor().getWidth()/2);

        stage.addActor(crd.getCardActor());
        stage.addActor(flipBtn);

    }

    @Override
    public void render(float delta) {

        viewport.apply();
        Gdx.gl.glClearColor(0.8f, 0.8f, 0.8f, 1.0f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        crd.update(delta,viewport);



        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        this.screenWidth = width;
        this.screenHeight = height;
        viewport.update(width, height, true);

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
