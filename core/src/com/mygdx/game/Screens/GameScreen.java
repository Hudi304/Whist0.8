package com.mygdx.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.mygdx.game.Client;
import com.mygdx.game.Constants;
import com.mygdx.game.generics.Card;
import com.mygdx.game.generics.Player;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Queue;

import static java.lang.Math.abs;
import static java.lang.Math.min;
import static java.lang.Math.sqrt;

public class GameScreen implements Screen {

    float screenWidth = 0;
    float screenHeight = 0;
    ScreenViewport viewport;
    Stage stage;
    Skin skin;
    Client mainController;
    private TextureRegion[][] regions;
    Texture cardSprite = new Texture("cardSprite.gif");
    public boolean canChooseCard = true;
    Card aux;
    public List<String> cardsStrList =  new ArrayList<>();
    public List<Card> hand = new ArrayList<>();
    public List<Card> putDownCards = new ArrayList<>();
    public Queue<Player> players = new ArrayDeque<>();
    public List<Player> opponents = new ArrayList<>();
    int nrOfCards = 8;
    Slider bidSlider;
    Label bidVal;
    TextButton bidButton;

    SpriteBatch batch;
    BitmapFont font;

    BitmapFont font12 ;


    public void init() {

    }

    void initBidHuUD(){

        //todo fa asta sa se vada calumea

        bidSlider = new Slider(0f,8f,1, false, skin);
        bidVal = new Label( "[" + (int)bidSlider.getValue() + "/" + nrOfCards + "]",skin);
        bidButton =  new TextButton("Bid",skin);

        bidVal.setPosition(screenWidth/2 + bidSlider.getWidth()/2,screenHeight/2);
        bidSlider.setPosition(screenWidth/2 - bidSlider.getWidth()/2,screenHeight/2);
        bidButton.setPosition(screenWidth/2 ,screenHeight/2 - bidSlider.getHeight()*2);
        bidButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
//                System.out.println("[Game] Bid btn pressed");
//                try {
//                    mainController.sendBidRP((int)bidSlider.getValue());
//                    System.out.println((int)bidSlider.getValue());
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
                //mainController.bidRP((int)bidSlider.getValue());
            }
        });
    }

    public void initGraphics(){
        //cardBack = new TextureRegion(cardSprite,648,0,587,81);
        regions = TextureRegion.split(cardSprite, 81, 117);
    }

    public GameScreen(Client mainController){
        System.out.println("[GameScreen] : Constructor");
        batch = new SpriteBatch();
        initGraphics();
        viewport = new ScreenViewport();
        stage = new Stage(viewport);
        this.mainController=mainController;
    }

    @Override
    public void show() {
        //todo de adaugat un label cu numarul de playeri sus in dreapta
        hand.clear();
        System.out.println("[GameScreen] : Show");

        skin = new Skin(Gdx.files.internal(Constants.skinJsonString));
        Gdx.input.setInputProcessor(stage);

        //putDownCards.add(new Card("h-12",regions,screenWidth/2 ,screenHeight/2,this));

        cardsStrList.add("h-2");
        cardsStrList.add("d-2");
        cardsStrList.add("c-2");
        cardsStrList.add("s-2");
        cardsStrList.add("h-3");
        cardsStrList.add("d-3");
        cardsStrList.add("c-3");
        cardsStrList.add("s-3");
        Player hudi =  new Player("Hudy",8);
        Player odr  =  new Player("Odrin",8);
        Player hudi2  =  new Player("Hudy1",8);
        Player hudi3  =  new Player("Hudy2",8);
        Player hudi4  =  new Player("Hudy3",8);
        Player hudi5  =  new Player("Hudy4",8);
        players.add(hudi);
        players.add(odr);
        players.add(hudi2);
        players.add(hudi3);
        players.add(hudi4);
        players.add(hudi5);

        for (Player pl : players) {
            if(!pl.getNickName().equals(mainController.nickName)){
                opponents.add(pl);
            }
        }
        System.out.println("Opponents = " + opponents);

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("JosefinSans-SemiBold.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();

      parameter.size = 34;
       font12 = generator.generateFont(parameter);
        font12.setColor(Color.BLACK);

        initBidHuUD();
        initOpponetCards(players);

//        hand = initCards(cardsStrList);

        stage.addActor(bidSlider);
        stage.addActor(bidVal);
        stage.addActor(bidButton);

        //todo vezi care e faza cu shaderele ca pare tare smecher
        font = new BitmapFont();
        font.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        font.setColor(Color.BLACK);
        addCardsToScene();
    }

    public void addCardsToScene(){

        hand = initCards(cardsStrList);
        //System.out.println("Add cards to scene |hand| = " + hand);
        addCardsToScene(hand,stage);
        addCardsToScene(putDownCards,stage);

    }

    public void addCardsToScene(List<Card> cards, Stage stage){
        for (Card crd:cards) {
            stage.addActor(crd.getCardActor());
            //System.out.println(crd);
        }
    }

     public List<Card> initCards(List<String> cards){
        int i = cards.size()*40/2;
        List<Card> ret = new ArrayList<>();
        Card card;
        Collections.sort(cards);
        for (String str:cards) {
            card =  new Card(str,regions,screenWidth - i, Constants.CARD_HAND_Y,this);
            ret.add(card);
            i -= 40;
        }
        return ret;
    }

    void initOpponetCards(Queue<Player> players){
        System.out.println("initOpponetCards players.size()=" + players.size());
        Card card;

        Player thisPlayer = new Player("",0);
        for (Player pl:players){
            if(pl.getNickName().equals(mainController.nickName)){
                thisPlayer = pl;
                break;
            }
        }
        //todo vezi cum e initializat lista players ca e cu unu mai mult
        players.remove(thisPlayer);

        for (Player pl : players) {
            for(int i = 0; i< nrOfCards; i++){
                card =  new Card("b-2",regions,screenWidth/2,screenHeight*9/10,this);
                card.getCardActor().setOriginX(card.getCardActor().getWidth()/2);
                pl.cards.add(card);
                //todo vezi ce faci cu asta
                card.getCardActor().setTouchable(Touchable.disabled);
                stage.addActor(card.getCardActor());
            }
            //System.out.println(pl.cards);
        }
    }

    void updateOpponents(float delta){
        for (Player pl : players) {
            for (Card crd : pl.cards) {
                crd.update(delta,viewport);
            }
        }
    }

    //TODO lista de Stringuri pe care le transforma in Cards
    //itereaza prin hand
    public void renderHUD(float delta, List<Card> cards){
        for (Card crd:cards) {
            crd.update(delta,viewport);
            if(crd.originalPosition.y >= screenHeight/2){
                if(!crd.choosed){
                    crd.choosed = true;
                    //todo de pus asta false
                    canChooseCard = true;
                    aux = crd;
                    putDownCard(aux);
                    System.out.println("[GameScreen] : Card choosed = " + crd.toString() + " " + crd.originalPosition.y );
                }
            }
        }
        hand.remove(aux);
    }

    public void renderPutDownCards(float delta, List<Card> putDownCards){
            for (Card crd:putDownCards) {
                crd.update(delta,viewport);
            }
    }

    void putDownCard(Card card){
        card.getCardActor().setOriginX(card.getCardActor().getWidth()/2);
        card.rePosition(screenWidth /2, screenHeight /2);
        card.setRot((putDownCards.size() - 1)*50);
        putDownCards.add(card);
    }

    @Override
    public void render(float delta) {
        viewport.apply();
        Gdx.gl.glClearColor(0.8f, 0.8f, 0.8f, 1.0f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //todo nu se regleaza in functie de cat de lung e nicknameul
        batch.begin();
            renderNicknamesBatch();
            font12.draw(batch,"dasdas",200,200);
        batch.end();

        updateBidText();

        renderPutDownCards(delta,putDownCards);
        renderHUD(delta,hand);
        updateOpponents(delta);

        stage.act(delta);
        stage.draw();
    }

    private void renderNicknamesBatch(){
        if (opponents.size() == 1){
            font.draw(batch, opponents.get(0).getNickName(), screenWidth/2,screenHeight*0.74f);
        }
        if (opponents.size() == 2){
            font.draw(batch, opponents.get(0).getNickName(), screenWidth*0.2f,screenHeight*0.74f);
            font.draw(batch, opponents.get(1).getNickName(), screenWidth*0.8f,screenHeight*0.74f);
        }
        if (opponents.size() == 3){
            font.draw(batch, opponents.get(0).getNickName(), screenWidth*0.14f,screenHeight/2);
            font.draw(batch, opponents.get(1).getNickName(), screenWidth/2,screenHeight*0.74f);
            font.draw(batch, opponents.get(1).getNickName(), screenWidth*0.82f,screenHeight/2);
        }
        if (opponents.size() == 4){
            font.draw(batch, opponents.get(0).getNickName(), screenWidth*0.14f,screenHeight/2);
            font.draw(batch, opponents.get(1).getNickName(), screenWidth*0.25f,screenHeight*0.7f);
            font.draw(batch, opponents.get(2).getNickName(), screenWidth*0.75f,screenHeight*0.7f);
            font.draw(batch, opponents.get(3).getNickName(), screenWidth*0.82f,screenHeight/2);
        }
        if (opponents.size() == 5){
            font.draw(batch, opponents.get(0).getNickName(), screenWidth*0.14f,screenHeight/2);
            font.draw(batch, opponents.get(1).getNickName(), screenWidth*0.2f,screenHeight*0.74f);
            font.draw(batch, opponents.get(2).getNickName(), screenWidth/2,screenHeight*0.74f);
            font.draw(batch, opponents.get(3).getNickName(), screenWidth*0.8f,screenHeight*0.74f);
            font.draw(batch, opponents.get(4).getNickName(), screenWidth*0.82f,screenHeight/2);
        }
    }

    void updateBidText(){
        bidVal.setText("[" + (int)bidSlider.getValue() + "/" + nrOfCards + "]");
    }

//todo resize si put down cards
    public void resizeHUD(){
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

        for (Card crd :putDownCards) {
            crd.getCardActor().setWidth(width);
            crd.getCardActor().setHeight(height);
            crd.getCardActor().setOriginX(width/2);
            crd.rePosition(screenWidth/2,screenHeight/2);
        }

        resizeBidHUD();

        //bidVal.getStyle().font.
    }

    void resizeBidHUD(){
        bidSlider.setWidth(screenWidth/4);
        bidSlider.setDebug(true);
        bidSlider.setHeight(screenHeight/15);
        bidSlider.getStyle().knob.setMinHeight(screenHeight/15);
        bidSlider.getStyle().knob.setMinWidth(screenHeight/16);
        bidSlider.getStyle().background.setMinHeight(bidSlider.getHeight()/2);

        bidVal.setSize(screenHeight/15,screenHeight/15);
        bidVal.setPosition( screenWidth/2 + bidSlider.getWidth()/2,screenHeight/2);

        bidButton.setPosition(screenWidth/2 ,screenHeight/2 - bidSlider.getHeight()*2);
    }

    public void enableBidHUD(){
        for (Actor act:stage.getActors()) {
            if(act == bidButton || act == bidSlider || act == bidVal){
                act.setVisible(true);
            }
        }
    }

    public void disableBidHUD(){
        for (Actor act:stage.getActors()) {
            if(act == bidButton || act == bidSlider || act == bidVal){
                act.setVisible(false);
            }
        }
    }

//behemoth ... nu dedschideti
    void resizeOpponents(){
        //todo de adaugat nicknameul undedva pe Ecran (dupa ce invat partea de fonturi)
        Vector2 plPos;
        Vector2 centerPos;
        System.out.println("Players Size = " + players.size());
            if(players.size() == 1){
                //CENTER UP
                 Player pl = players.remove();
                     plPos = new Vector2(screenWidth/2,screenHeight/2);
                     centerPos =  new Vector2(screenWidth/2, screenHeight *14.4f/10);
                        pl.positionCardsHor((int)screenWidth, (int)screenHeight, plPos,centerPos,screenHeight/2,180,true);
                        System.out.println(pl.getNickName());
                     players.add(pl);
            }

            if(players.size() == 2){
                //UP left
                Player pl = players.remove();
                         plPos = new Vector2(screenWidth/8,screenHeight/2);
                         centerPos =  new Vector2(0, screenHeight *1.35f);
                             pl.positionCardsHor((int)screenWidth, (int)screenHeight, plPos,centerPos,screenHeight/2,210,true);
                    players.add(pl);
                //UP right
                    pl = players.remove();
                        plPos = new Vector2(screenWidth*7/8,screenHeight/2);
                        centerPos =  new Vector2(screenWidth, screenHeight *1.35f);
                            pl.positionCardsHor((int)screenWidth, (int)screenHeight, plPos,centerPos,screenHeight/2,-210,true);
                    players.add(pl);
            }


            if(players.size() == 3) {
                Player pl = players.remove();
                    plPos = new Vector2(screenWidth /8,screenHeight/2);
                    centerPos =  new Vector2(-screenWidth/2.5f, screenHeight /2);
                        pl.positionCardsVert((int)screenWidth, (int)screenHeight, plPos,centerPos,screenWidth/2.5f,-90,false);
                players.add(pl);

                pl = players.remove();
                    plPos = new Vector2(screenWidth/2,screenHeight/2);
                    centerPos =  new Vector2(screenWidth/2, screenHeight *14.4f/10);
                        pl.positionCardsHor((int)screenWidth, (int)screenHeight, plPos,centerPos,screenHeight/2,180,true);
                players.add(pl);

                pl = players.remove();
                    plPos = new Vector2(screenWidth/2,screenHeight/2);
                    centerPos =  new Vector2(screenWidth + screenWidth/3 , screenHeight / 2);
                        pl.positionCardsVert((int)screenWidth, (int)screenHeight, plPos,centerPos,screenWidth/2.5f,90,true);
                players.add(pl);
            }

        if(players.size() == 4) {
            Player pl = players.remove();
                plPos = new Vector2(screenWidth /8,screenHeight/2);
                centerPos =  new Vector2(-screenWidth/2.5f, screenHeight /2);
                    pl.positionCardsVert((int)screenWidth, (int)screenHeight, plPos,centerPos,screenWidth/2.5f,-90,false);
            players.add(pl);

            pl = players.remove();
                plPos = new Vector2(screenWidth/4,screenHeight/2);
                centerPos =  new Vector2(screenWidth*2/8, screenHeight *14f/10);
                    pl.positionCardsHor((int)screenWidth, (int)screenHeight, plPos,centerPos,screenHeight/2,185,true);
            players.add(pl);

            pl = players.remove();
                plPos = new Vector2(screenWidth*3/4,screenHeight/2);
                centerPos =  new Vector2(screenWidth*6/8, screenHeight *14f/10);
                    pl.positionCardsHor((int)screenWidth, (int)screenHeight, plPos,centerPos,screenHeight/2,-185,true);
            players.add(pl);

            pl = players.remove();
                plPos = new Vector2(screenWidth/2,screenHeight/2);
                centerPos =  new Vector2(screenWidth + screenWidth/3 , screenHeight / 2);
                    pl.positionCardsVert((int)screenWidth, (int)screenHeight, plPos,centerPos,screenWidth/2.5f,90,true);
            players.add(pl);
        }

        if(players.size() == 5) {
            Player pl = players.remove();
            plPos = new Vector2(screenWidth / 8, screenHeight / 2.9f);
            centerPos = new Vector2(-screenWidth / 2.5f, screenHeight / 4);
            pl.positionCardsVert((int) screenWidth, (int) screenHeight, plPos, centerPos, screenWidth / 2.5f, -68, false);
            players.add(pl);

            //UP left
            pl = players.remove();
            plPos = new Vector2(screenWidth / 8, screenHeight / 2);
            centerPos = new Vector2(0, screenHeight * 1.35f);
            pl.positionCardsHor((int) screenWidth, (int) screenHeight, plPos, centerPos, screenHeight / 2, 210, true);
            players.add(pl);

            pl = players.remove();
            plPos = new Vector2(screenWidth / 2, screenHeight / 2);
            centerPos = new Vector2(screenWidth / 2, screenHeight * 14.4f / 10);
            pl.positionCardsHor((int) screenWidth, (int) screenHeight, plPos, centerPos, screenHeight / 2, 180, true);
            players.add(pl);

            //UP right
            pl = players.remove();
            plPos = new Vector2(screenWidth * 7 / 8, screenHeight / 2);
            centerPos = new Vector2(screenWidth, screenHeight * 1.35f);
            pl.positionCardsHor((int) screenWidth, (int) screenHeight, plPos, centerPos, screenHeight / 2, -210, true);
            players.add(pl);

            pl = players.remove();
            plPos = new Vector2(screenWidth / 2, screenHeight / 2.9f);
            centerPos = new Vector2(screenWidth + screenWidth / 3, screenHeight / 4);
            pl.positionCardsVert((int) screenWidth, (int) screenHeight, plPos, centerPos, screenWidth / 2.5f, 68, true);
            players.add(pl);
            //TODO and this you lazy fuck

        }
    }

    @Override
    public void resize(int width, int height) {
       // System.out.println("[GameScreen] : resize");
        screenHeight = height;
        screenWidth = width;
        //System.out.println("[GameScreen] resize : width=" + screenWidth + "|" + " height=" + screenHeight);

        bidVal.setPosition(screenWidth/2 + bidSlider.getWidth()/2,screenHeight/2);
        bidSlider.setPosition(screenWidth/2 - bidSlider.getWidth()/2,screenHeight/2);
        bidButton.setPosition(screenWidth/2 ,screenHeight/2 - bidSlider.getHeight()*2);
        resizeHUD();
        resizeOpponents();
        font.getData().setScale(Math.min(width, height) / Constants.HUD_FONT_REFERENCE_SCREEN_SIZE);

        viewport.update(width, height, true);
        init();
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

    //todo de facut functiile de dispose

    @Override
    public void dispose() {
        System.out.println("[GameScreen] : dispose");
        hand.clear();
        stage.dispose();

    }

    public void renderCardBack(int x, int y, float rot){
        batch.draw(regions[4][0],x,y,0.0f,0.0f,regions[4][0].getRegionWidth(),regions[4][0].getRegionHeight(),1.0f,1.0f,rot);
    }

    public float getScreenWidth() {
        return screenWidth;
    }

    public float getScreenHeight() {
        return screenHeight;
    }
}
