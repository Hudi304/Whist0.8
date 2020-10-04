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
import com.mygdx.game.presentationLayer.renderObjects.PlayerHud;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.abs;
import static java.lang.Math.min;
import static java.lang.Math.sqrt;

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

    PlayerHud playerHUD;

    public boolean canChooseCard;


    ScreenViewport viewport;
    public Stage stage;
    Skin skin;





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
        List<String> str = new ArrayList<>();
        str.add("h-12");
        str.add("h-4");
        str.add("h-6");

         //crd = new Card2("h-12",playerHUD.cardsTexture.getCardTexture("back"),playerHUD.cardsTexture.getCardTexture("h-12"),new Vector2(200,200),new Vector2(100,100));

        playerHUD.initCards(str,screenWidth);
        stage.addActor(playerHUD);

        flipBtn = new TextButton("Flip",skin);
        flipBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                //crd.setFlipped(!crd.isFlipped());
                System.out.println(stage.getActors());
            }
        });

        screenWidth = Gdx.graphics.getWidth();
        screenHeight = Gdx.graphics.getHeight();


        System.out.println("[NewGameScreen] Show()");
        System.out.println("screeWidth=" + screenWidth + "|" + "screenHeight=" + screenHeight);
        Vector2 spPos = new Vector2(screenWidth/2,-100);
        Vector2 tgPos = new Vector2(screenWidth/2,screenHeight/2);



        //group = playerHUD.getUpdatedHUD();
        //stage.addActor(playerHUD.getUpdatedHUD());
        stage.addActor(flipBtn);
        //stage.addActor(flipBtn);
        //crd.setTouchable(Touchable.enabled);
        //stage.addActor(crd);

        System.out.println(stage.getActors());

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
        this.screenWidth = width;
        this.screenHeight = height;
        resizeHUD(playerHUD.getPlayerCard());
        viewport.update(width, height, true);

    }

    //asta pune cartile pe cerc la fiecare resize
    public void resizeHUD(List<Card> hand){
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


        for (Card crd :hand) {
            y = (float) (sqrt(abs(R*R - (x-Cx)*(x-Cx))) + Cy);
            crd.getCardActor().setWidth(width);
            crd.getCardActor().setHeight(height);
            crd.getCardActor().setOriginX(width/2);
            crd.rePosition(x -crd.getCardActor().getWidth()/4,y);
            crd.setRot(rot);
            x += xOffset;
            rot -= rotOffset;
        }

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
