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
import com.mygdx.game.dataLayer.generics.Card2;
import com.mygdx.game.dataLayer.repositories.CardsTextureRepository;
import com.mygdx.game.presentationLayer.renderObjects.OpponentHud;
import com.mygdx.game.presentationLayer.renderObjects.PlayerHud;
import com.mygdx.game.presentationLayer.renderObjects.TableHud;

import java.util.ArrayList;
import java.util.List;
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
    Group opponentHuds = new Group();
    OpponentHud opponent1;
    OpponentHud opponent2;
    OpponentHud opponent3;
    OpponentHud opponent4;
    OpponentHud opponent5;

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
        tableHUD = new TableHud(cardsTextureRepository,this);
        System.out.println(screenWidth + " " + screenHeight );
        screenHeight = Gdx.graphics.getHeight();
        screenWidth = Gdx.graphics.getWidth();
        Vector2 castPoz1 =  new Vector2(screenWidth*0.15f , screenHeight*0.25f);
        Vector2 castPoz2 =  new Vector2(screenWidth*0.25f , screenHeight*0.55f);
        Vector2 castPoz3 =  new Vector2(screenWidth*0.5f , screenHeight*0.7f);
        Vector2 castPoz4 =  new Vector2(screenWidth*0.75f , screenHeight*0.55f);
        Vector2 castPoz5 =  new Vector2(screenWidth*0.85f , screenHeight*0.25f);
        opponent1 =  new OpponentHud(cardsTextureRepository,this,castPoz1,-90);
        opponent2 =  new OpponentHud(cardsTextureRepository,this,castPoz2,-135);
        opponent3 =  new OpponentHud(cardsTextureRepository,this,castPoz3,180);
        opponent4 =  new OpponentHud(cardsTextureRepository,this,castPoz4,135);
        opponent5 =  new OpponentHud(cardsTextureRepository,this,castPoz5,90);
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
                opponent1.putCastCard("h-8");
                opponent2.putCastCard("h-8");
                opponent3.putCastCard("h-8");
                opponent4.putCastCard("h-8");
                opponent5.putCastCard("h-8");

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


        opponentHuds.addActor(opponent1);
        opponentHuds.addActor(opponent2);
        opponentHuds.addActor(opponent3);
        opponentHuds.addActor(opponent4);
        opponentHuds.addActor(opponent5);


        opponent1.initCards();
        opponent2.initCards();
        opponent3.initCards();
        opponent4.initCards();
        opponent5.initCards();



        playerHUD.initCards(str,screenWidth);
        playerHUD.createBidHUD();
        tableHUD.initTable();



        stage.addActor(tableHUD);
        stage.addActor(opponentHuds);
        stage.addActor(playerHUD);

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

       tableHUD.resizeTable();
        playerHUD.resizeHUD();
        // todo pentu un oarecare motiv exista un delay cand merge prima data si le pune putin intr-o parte ...
        resizeOpponents(width,height);
        viewport.update(width, height, true);
    }

    public void resizeOpponents(float width, float height){
        float offset = width / 50;

        opponent1.setCastCardPosition(new Vector2(screenWidth*0.15f , screenHeight*0.25f));
        opponent2.setCastCardPosition(new Vector2(screenWidth*0.25f , screenHeight*0.55f));
        opponent3.setCastCardPosition(new Vector2(screenWidth*0.5f  , screenHeight*0.7f ));
        opponent4.setCastCardPosition(new Vector2(screenWidth*0.75f , screenHeight*0.55f));
        opponent5.setCastCardPosition(new Vector2(screenWidth*0.85f , screenHeight*0.25f));

         Vector2 plPos = new Vector2(width / 4, height / 2.9f);
         Vector2 centerPos = new Vector2(-width , height / 6);
         opponent1.positionCardsVert( width,  height, plPos, centerPos,offset* 0.6f, width, -90, false);

        plPos = new Vector2(width *0.2f ,height / 1.35f);
        centerPos =  new Vector2(width* 0.05f, height * 1.72f );
        opponent2.positionCardsHor(width, height, plPos, centerPos, offset* 0.6f, height,-155,true);

         plPos = new Vector2(width / 2 ,height / 2);
         centerPos =  new Vector2(width / 2, height * 1.88f );
         opponent3.positionCardsHor(width, height, plPos, centerPos, offset*0.7f, height,180,true);

        plPos = new Vector2(width * 0.8f, height / 1.35f);
        centerPos = new Vector2(width *0.95f , height * 1.7f);
        opponent4.positionCardsHor(width, height, plPos, centerPos, offset*0.6f, height,155,true);


        plPos = new Vector2(width *7 / 8, height / 2.9f);
         centerPos = new Vector2(width * 1.94f , height / 6);
         opponent5.positionCardsVert( width,  height, plPos, centerPos,offset* 0.6f, width, 90, true);



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

    public void updateCardsForPlayer(List<String> cards){
        playerHUD.initCards(cards,screenWidth);
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
