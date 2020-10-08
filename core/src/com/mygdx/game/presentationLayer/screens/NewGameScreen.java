package com.mygdx.game.presentationLayer.screens;




import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
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

    SpriteBatch batch;
    Client client;
    public float screenWidth;
    public float screenHeight;
    public Texture cardSprite = new Texture("cardSprite.gif");
    public TextureRegion[][] regions;

//-------HUDs------------
    PlayerHud playerHUD;
    TableHud tableHUD;
    Group opponentHuds = new Group();
    Group deck = new Group();
    OpponentHud opponent1;
    OpponentHud opponent2;
    OpponentHud opponent3;
    OpponentHud opponent4;
    OpponentHud opponent5;

    //-------FLAGURI--------------
    public boolean canChooseCard;
    //-------STAGE|STUFF----------
    ScreenViewport viewport;
    TextButton flipBtn;//p
    public Stage stage;
    public Skin skin;
    CardsTextureRepository cardsTextureRepository;

    public List<String> opponentsNames = new ArrayList<>();
    BitmapFont font12;//p
    public Vector2 deckPos;



    public NewGameScreen(Client client,CardsTextureRepository cardsTextureRepository){
        System.out.println("[NewGameScreen] : Constructor");
        //--------------GameScreenInit---------------------------

        this.cardsTextureRepository = cardsTextureRepository;
        this.client =  client;
            regions = TextureRegion.split(cardSprite, 81, 117);
            viewport = new ScreenViewport();
            stage = new Stage(viewport);
            skin = new Skin(Gdx.files.internal(Constants.skinJsonString));
            screenHeight = Gdx.graphics.getHeight();
            screenWidth = Gdx.graphics.getWidth();
            batch = new SpriteBatch();
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("JosefinSans-SemiBold.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
            parameter.size = 20;
            font12 = generator.generateFont(parameter);
            font12.setColor(Color.BLACK);
            deckPos = new Vector2(screenWidth/4,screenHeight/4);


        //--------------GameScreenHUDs---------------------------

        playerHUD = new PlayerHud(cardsTextureRepository,this);
        tableHUD = new TableHud(cardsTextureRepository,this);
        System.out.println("[NewGameScreen] : resolution " + screenWidth + "/" + screenHeight );

    }

    public void addTableToScene(){
        stage.addActor(tableHUD);
    }


    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
        stage.addActor(tableHUD);
        flipBtn = new TextButton("Flip",skin);
        flipBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                playerHUD.setFlipped();
                //playerHUD.bidHUDVisibility();
               // distributeCards(8);
            }
        });
        screenWidth = Gdx.graphics.getWidth();
        screenHeight = Gdx.graphics.getHeight();

        //initDeck(8);
        playerHUD.createBidHUD();
        tableHUD.initTable();
        stage.addActor(playerHUD);
        System.out.println("[GameScreen] deck = " + deck);
        stage.addActor(deck);
        stage.addActor(flipBtn);
    }
    // this need to be called
    public void initDeck(int nrCardsPerPlayer){
        for (int i = 0 ; i < (opponentsNames.size() ) *nrCardsPerPlayer ; i++ ){
            Card2 crd = new Card2("h-4",cardsTextureRepository.getCardTexture("back"),cardsTextureRepository.getCardTexture("back"), deckPos,new Vector2(screenWidth/2 , 100),this);
            //crd.se
            crd.setTouchable(Touchable.enabled);
            deck.addActor(crd);
        }
    }

    public void sendCard(String card){
        this.client.networkService.sendCardResponse(card);
    }

    public void distributeCards(int nrCardsPerPlayer) {
        int i = 0;

        for (Actor crd : deck.getChildren()) {
            if (crd instanceof Card2) {
                for (Actor opp : opponentHuds.getChildren()) {
                    if (opp instanceof OpponentHud) {
                        ((OpponentHud) opp).addActor(crd);
                        deck.removeActor(crd);
                    }
                }
            }
        }

        resizeOpponents(Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
    }

    public void initOpponentsHUD(List<String> opponets){
        opponent1 = null;
        opponent2 = null;
        opponent3 = null;
        opponent4 = null;
        opponent5 = null;

        Vector2 castPoz1 =  new Vector2(screenWidth*0.15f , screenHeight*0.25f);
        Vector2 castPoz2 =  new Vector2(screenWidth*0.25f , screenHeight*0.55f);
        Vector2 castPoz3 =  new Vector2(screenWidth*0.5f , screenHeight*0.7f);
        Vector2 castPoz4 =  new Vector2(screenWidth*0.75f , screenHeight*0.55f);
        Vector2 castPoz5 =  new Vector2(screenWidth*0.85f , screenHeight*0.25f);

        System.out.println("opp.size() = " + opponets.size());
        switch (opponets.size()) {
            case 1:
                opponent3 = new OpponentHud(opponets.get(0),cardsTextureRepository,this,castPoz3,180);
                opponent3.initCards();
                opponentHuds.addActor(opponent3);
                break;
            case 2:
                opponent2 = new OpponentHud(opponets.get(0), cardsTextureRepository,this,castPoz2,-135);
                opponent4 =  new OpponentHud(opponets.get(1), cardsTextureRepository,this,castPoz4,135);
                opponent2.initCards();
                opponent4.initCards();
                opponentHuds.addActor(opponent2);
                opponentHuds.addActor(opponent4);

                break;
            case 3:
                opponent1 =  new OpponentHud(opponets.get(0),cardsTextureRepository,this,castPoz1,-90);
                opponent3 =  new OpponentHud(opponets.get(1),cardsTextureRepository,this,castPoz3,180);
                opponent5 =  new OpponentHud(opponets.get(2),cardsTextureRepository,this,castPoz5,90);
                opponent1.initCards();
                opponent3.initCards();
                opponent5.initCards();
                opponentHuds.addActor(opponent1);
                opponentHuds.addActor(opponent3);
                opponentHuds.addActor(opponent5);
                break;
            case 4:
                opponent1 =  new OpponentHud(opponets.get(0),cardsTextureRepository,this,castPoz1,-90);
                opponent2 =  new OpponentHud(opponets.get(1),cardsTextureRepository,this,castPoz2,-135);
                opponent4 =  new OpponentHud(opponets.get(2),cardsTextureRepository,this,castPoz4,135);
                opponent5 =  new OpponentHud(opponets.get(3),cardsTextureRepository,this,castPoz5,90);
                opponent1.initCards();
                opponent2.initCards();
                opponent4.initCards();
                opponent5.initCards();
                opponentHuds.addActor(opponent1);
                opponentHuds.addActor(opponent2);
                opponentHuds.addActor(opponent4);
                opponentHuds.addActor(opponent5);
                break;
            case 5:
                opponent1 =  new OpponentHud(opponets.get(0),cardsTextureRepository,this,castPoz1,-90);
                opponent2 =  new OpponentHud(opponets.get(1),cardsTextureRepository,this,castPoz2,-135);
                opponent3 =  new OpponentHud(opponets.get(2),cardsTextureRepository,this,castPoz3,180);
                opponent4 =  new OpponentHud(opponets.get(3),cardsTextureRepository,this,castPoz4,135);
                opponent5 =  new OpponentHud(opponets.get(4),cardsTextureRepository,this,castPoz5,90);
                opponent1.initCards();
                opponent2.initCards();
                opponent3.initCards();
                opponent4.initCards();
                opponent5.initCards();
                opponentHuds.addActor(opponent1);
                opponentHuds.addActor(opponent2);
                opponentHuds.addActor(opponent3);
                opponentHuds.addActor(opponent4);
                opponentHuds.addActor(opponent5);
                break;
        }
        stage.addActor(opponentHuds);
        resizeOpponents(Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
        System.out.println(stage.getActors());
    }

    @Override
    public void render(float delta) {
        viewport.apply();
        Gdx.gl.glClearColor(0.8f, 0.8f, 0.8f, 1.0f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
            renderNicknamesBatch();
        batch.end();

        opponentHuds.act(delta);
        playerHUD.act(delta);

        stage.act(delta);
        stage.draw();
    }

    private void renderNicknamesBatch(){
        if (opponentsNames.size() == 1){
             font12.draw(batch, opponentsNames.get(0) , screenWidth/2,screenHeight*0.74f);
        }
        if (opponentsNames.size() == 2){
             font12.draw(batch, opponentsNames.get(0) , screenWidth*0.2f,screenHeight*0.74f);
             font12.draw(batch, opponentsNames.get(1) , screenWidth*0.8f,screenHeight*0.74f);
        }
        if (opponentsNames.size() == 3){
             font12.draw(batch, opponentsNames.get(0) , screenWidth*0.14f,screenHeight/2);
             font12.draw(batch, opponentsNames.get(1) , screenWidth/2,screenHeight*0.74f);
             font12.draw(batch, opponentsNames.get(1) , screenWidth*0.82f,screenHeight/2);
        }
        if (opponentsNames.size() == 4){
             font12.draw(batch, opponentsNames.get(0) , screenWidth*0.14f,screenHeight/2);
             font12.draw(batch, opponentsNames.get(1) , screenWidth*0.25f,screenHeight*0.7f);
             font12.draw(batch, opponentsNames.get(2) , screenWidth*0.75f,screenHeight*0.7f);
             font12.draw(batch, opponentsNames.get(3) , screenWidth*0.82f,screenHeight/2);
        }
        if (opponentsNames.size() == 5){
             font12.draw(batch, opponentsNames.get(0) , screenWidth*0.14f,screenHeight/2);
             font12.draw(batch, opponentsNames.get(1) , screenWidth*0.2f,screenHeight*0.74f);
             font12.draw(batch, opponentsNames.get(2) , screenWidth/2,screenHeight*0.74f);
             font12.draw(batch, opponentsNames.get(3) , screenWidth*0.8f,screenHeight*0.74f);
             font12.draw(batch, opponentsNames.get(4) , screenWidth*0.82f,screenHeight/2);
        }
    }

    @Override
    public void resize(int width, int height) {
        System.out.println("[NeGameScreen] : resize()");
        this.screenWidth = width;
        this.screenHeight = height;

        tableHUD.resizeTable();
        playerHUD.resizeHUD();

        resizeOpponents(width,height);
        viewport.update(width, height, true);
    }

    public void resizeOpponents(float width, float height){
        float offset = width / 40;

        Vector2 plPos;
        Vector2 centerPos;

        if(opponent1 != null){
            System.out.println("resize opp 1");
            opponent1.setCastCardPosition(new Vector2(screenWidth*0.15f , screenHeight*0.25f));
            plPos = new Vector2(width / 4, height / 2.9f);
            centerPos = new Vector2(-width , height / 6);
            opponent1.positionCardsVert( width,  height, plPos, centerPos,offset* 0.6f, width, -90, false);
        }
        if(opponent2 != null){
            opponent2.setCastCardPosition(new Vector2(screenWidth*0.25f , screenHeight*0.55f));
            plPos = new Vector2(width *0.2f ,height / 1.35f);
            centerPos =  new Vector2(width* 0.05f, height * 1.72f );
            opponent2.positionCardsHor(width, height, plPos, centerPos, offset* 0.6f, height,-155,true);
        }
        if(opponent3 != null){
            opponent3.setCastCardPosition(new Vector2(screenWidth*0.5f  , screenHeight*0.7f ));
            plPos = new Vector2(width / 2 ,height / 2);
            centerPos =  new Vector2(width / 2, height * 1.88f );
            opponent3.positionCardsHor(width, height, plPos, centerPos, offset*0.4f, height,180,true);
        }
        if(opponent4 != null){
            opponent4.setCastCardPosition(new Vector2(screenWidth*0.75f , screenHeight*0.55f));
            plPos = new Vector2(width * 0.8f, height / 1.35f);
            centerPos = new Vector2(width *0.95f , height * 1.7f);
            opponent4.positionCardsHor(width, height, plPos, centerPos, offset*0.6f, height,155,true);
        }
        if(opponent5 != null){
            opponent5.setCastCardPosition(new Vector2(screenWidth*0.85f , screenHeight*0.25f));
            plPos = new Vector2(width *7 / 8, height / 2.9f);
            centerPos = new Vector2(width * 1.94f , height / 6);
            opponent5.positionCardsVert( width,  height, plPos, centerPos,offset* 0.6f, width, 90, true);
        }

    }



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
