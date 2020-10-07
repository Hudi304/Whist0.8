package com.mygdx.game.presentationLayer.screens;



import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.mygdx.game.Client;
import com.mygdx.game.businessLayer.others.Constants;
import com.mygdx.game.dataLayer.generics.Card;
import com.badlogic.gdx.graphics.GL20;
import com.mygdx.game.dataLayer.generics.Card2;
import com.mygdx.game.dataLayer.generics.CardTest;
import com.mygdx.game.dataLayer.repositories.CardsTextureRepository;
import com.mygdx.game.presentationLayer.renderObjects.OpponentHud;
import com.mygdx.game.presentationLayer.renderObjects.PlayerHud;
import com.mygdx.game.presentationLayer.renderObjects.TableHud;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.abs;
import static java.lang.Math.min;
import static java.lang.Math.sqrt;

public class NewGameScreen implements Screen {


    Client client;
    public float screenWidth;
    public float screenHeight;

    public List<Card> hand = new ArrayList<>();
    public Texture cardSprite = new Texture("cardSprite.gif");
    public TextureRegion[][] regions;


    //adauga in scene un grup de carti si in stage
//-------HUDs------------
    PlayerHud playerHUD;
    TableHud tableHUD;
    List<OpponentHud> opponentHuds =  new ArrayList<>();
    OpponentHud opponentHud;
    //-------FLAGURI---------------
    public boolean canChooseCard;
    //-------STAGE|STUFF----------
    ScreenViewport viewport;
    TextButton flipBtn;//p
    public Stage stage;
    public Skin skin;



    public NewGameScreen(Client client,CardsTextureRepository cardsTextureRepository){
        this.client =  client;
        System.out.println("[NewGameScreen] : Constructor");
        regions = TextureRegion.split(cardSprite, 81, 117);
        viewport = new ScreenViewport();
        stage = new Stage(viewport);
        skin = new Skin(Gdx.files.internal(Constants.skinJsonString));
        playerHUD = new PlayerHud(cardsTextureRepository,this);
        this.client =  client;
    }




    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);

        flipBtn = new TextButton("Flip",skin);
        flipBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                playerHUD.setFlipped();
                playerHUD.bidHUDVisibility();
//                opponentHud.nrOfCards--;
//                opponentHud.resizeOpponents(screenWidth,screenHeight);
                System.out.println(stage.getActors());
            }
        });


        screenWidth = Gdx.graphics.getWidth();
        screenHeight = Gdx.graphics.getHeight();
        List<String> str = new ArrayList<>();
        str.add("h-12");
        str.add("h-4");
        str.add("h-6");
        str.add("h-12");
        str.add("h-4");
        str.add("h-6");
        str.add("h-4");
        str.add("h-6");

        //opponentHud.initCards();
        Vector2 plPos = new Vector2(screenWidth/2,screenHeight/2);
        Vector2 centerPos =  new Vector2(screenWidth/2, screenHeight *14.4f/10);
        // opponentHud.positionCardsHor((int)screenWidth, (int)screenHeight, plPos,centerPos,screenHeight/2,180,true);
        //playerHUD.initCards(str,screenWidth);
        playerHUD.createBidHUD();
//        tableHUD.initTable();




        //stage.addActor(tableHUD);
        stage.addActor(playerHUD);
       // stage.addActor(opponentHud);



        stage.addActor(flipBtn);

    }

    @Override
    public void render(float delta) {


        viewport.apply();
        Gdx.gl.glClearColor(0.8f, 0.8f, 0.8f, 1.0f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
       // stage.clear();

        //crd.updatePosition(delta);
        playerHUD.act(delta);

        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        System.out.println("[NeGameScreen] : resize()");
        this.screenWidth = width;
        this.screenHeight = height;

//        tableHUD.resizeTable();
        playerHUD.resizeHUD();
        // todo pentu un oarecare motiv exista un delay cand merge prima data si le pune putin intr-o parte ...
//        opponentHud.resizeOpponents(width,height);
   //     opponentHud.resizeOpponents(width,height);
        viewport.update(width, height, true);
    }

    //asta pune cartile pe cerc la fiecare resize
    public void resizeHUD(List<Card2> hand){
        boolean portrait = false;
        boolean landscape = false;
        float width = min(screenHeight,screenWidth) /4 * 0.6f;
        float height = min(screenHeight,screenWidth) /4;
        float xOffset;
        float rotOffset = 3;
        float rot =  hand.size()/2 * rotOffset;

        if (screenHeight > screenWidth){
            xOffset = screenWidth/12;
        }else{
            xOffset = screenWidth/20;
        }

        float x = screenWidth/2 - xOffset*hand.size()/2 + xOffset/2;
        float y;
        //cerc
        float Cy = -screenHeight *90/100;
        float Cx = screenWidth/2;
        float R =  screenHeight;


        for (Card2 crd :hand) {
            y = (float) (sqrt(abs(R*R - (x-Cx)*(x-Cx))) + Cy);
            crd.setWidth(width);
            crd.setHeight(height);
            crd.setOriginX(width/2);
            crd.rePosition(x -crd.getWidth()/4,y);
            crd.setRot(rot);
            x += xOffset;
            rot -= rotOffset;
        }

    }



    /*
    * Functions for gameScreen
     */


    public void updateCardsForPlayer(List<String> cards){
        this.playerHUD.initCards(cards,screenWidth);

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
