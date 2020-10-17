package com.mygdx.game.testing;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.SnapshotArray;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.businessLayer.controllers.GameController;
import com.mygdx.game.businessLayer.networking.dto.NetworkDTO;
import com.mygdx.game.businessLayer.others.Constants;
import com.mygdx.game.dataLayer.repositories.CardsTextureRepository;
import com.mygdx.game.testing.Atoms.ChatDragBar;
import com.mygdx.game.testing.Atoms.DragBar;
import com.mygdx.game.testing.Atoms.OpponentCard;
import com.mygdx.game.testing.Atoms.PlayerCard;
import com.mygdx.game.testing.HUDs.Chat;
import com.mygdx.game.testing.HUDs.PlayerHUD;
import com.mygdx.game.testing.HUDs.OpponentHUD;
import com.mygdx.game.testing.HUDs.TableHUD;

import java.util.ArrayList;
import java.util.List;

public class GameScreenTest implements Screen {

    //-------------UI-----------------
    public GameController controller;

    public Stage stage;
    public CardsTextureRepository cardsTextureRepository;
    public Viewport viewport;

    public BitmapFont font12;
    public Skin skin;
    public SpriteBatch batch;

    public Texture dragBarImange = new Texture("dragBar.png");
    public Texture chatDragBarImange = new Texture("chatDragBar.png");
    private DragBar tableDragBar;
    private Chat chat;
    Table table ;
    //ScrollPane scrollPane = new ScrollPane(scoreTable,skin);
    ChatDragBar chatDragBar;

    //-----------DATA-----------------
    public String nickname;
    public List<String> opponentsNames = new ArrayList<>();
    public boolean canCastCard = false;
    int nrOfCards = 8;
    private Vector2 deckPos;

    public List<NetworkDTO.Score> scoreList = new ArrayList<>();



    private List<NetworkDTO.Bids.Bid> bids =  new ArrayList<>();

   //------------HUDs----------------

    public PlayerHUD plHUD;
    OpponentHUD oppHUD1 = null;
    OpponentHUD oppHUD2 = null;
    OpponentHUD oppHUD3 = null;
    OpponentHUD oppHUD4 = null;
    OpponentHUD oppHUD5 = null;

    public TableHUD tableHUD = null;




    public GameScreenTest(CardsTextureRepository cardsTextureRepository, GameController controller) {
        viewport = new ScreenViewport();
        skin = new Skin(Gdx.files.internal(Constants.skinJsonString));
        stage = new Stage(viewport);
        batch = new SpriteBatch();
        this.controller = controller;
        this.cardsTextureRepository = cardsTextureRepository;
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("JosefinSans-SemiBold.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
            parameter.size = 20;
            font12 = generator.generateFont(parameter);
            font12.setColor(Color.BLACK);
        deckPos = new Vector2(Gdx.graphics.getWidth() * 0.8f, Gdx.graphics.getHeight() * 0.05f);
        plHUD = new PlayerHUD(cardsTextureRepository, viewport, this, controller,deckPos);
        tableHUD = new TableHUD();

        table =  new Table();
        tableDragBar = new DragBar(viewport,table);
        tableDragBar.setDrawable(new SpriteDrawable(new Sprite(dragBarImange)));

        //chatDragBar = new ChatDragBar(viewport,new SpriteDrawable(new Sprite(chatDragBarImange)));

        chat = new Chat(skin,viewport,new SpriteDrawable(new Sprite(chatDragBarImange)));

        stage.addActor(plHUD);
        stage.addActor(tableHUD);
        //stage.addActor(chatDragBar);
        stage.addActor(chat);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);


        tableDragBar.setWidth(viewport.getScreenWidth()/4);
        tableDragBar.setHeight(viewport.getScreenHeight()/40);
        tableDragBar.setOriginX(tableDragBar.getWidth()/2);

        tableDragBar.setPosition(viewport.getScreenWidth()/2 - tableDragBar.getWidth()/2,viewport.getScreenHeight());


        TextButton flipButton = new TextButton("StartAction", skin);
        flipButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {

               // resizeOpponents(viewport.getScreenWidth(), viewport.getScreenHeight());
                //plHUD.setBidHUDVisibility(!plHUD.bidHudVisibility);
                //plHUD.changeCircle(true);
                //refreshTable();
                //destroyAllCards();
                chat.addChatMessage("PULA");
                //returnCardsToDeck();

            }
        });



        stage.addActor(flipButton);
        stage.addActor(tableDragBar);
        //stage.addActor(scoreTable);

    }

    public void initTable(){
        //table = new Table();
        table.setPosition(viewport.getScreenWidth()/2,viewport.getScreenHeight()/2);
        table.setTouchable(Touchable.disabled);
        table.setPosition(viewport.getScreenWidth()/2,100);
        table.defaults().width(110);
        table.setSkin(skin);
        table.debugAll();

        Label plName = new Label(nickname,skin);
        plName.setAlignment(Align.center);
        table.add(plName);

        for (String opp:opponentsNames) {
            Label oppName = new Label(opp,skin);
            oppName.setAlignment(Align.center);
            table.add(oppName).center();
        }
        table.row();

        Label score = new Label("0",skin);
        score.setAlignment(Align.center);
        table.add(score).center();
        for (String opp:opponentsNames) {
            score = new Label("0",skin);
            score.setAlignment(Align.center);
            table.add(score).center();
        }
        table.row();
        stage.addActor(table);

    }

    //todo nu le pune bine
    public void addScoreToTable(List<NetworkDTO.Score> scores){
        for (NetworkDTO.Score scr:scores) {
            Label oppName = new Label(scr.getTotal() + "",skin);
            oppName.setAlignment(Align.center);
            table.add(oppName).center();
        }
        table.row();
    }

    public void refreshTable(){
        //scoreTable.invalidateHierarchy();
        //scoreTable.setTransform(true);
        //scoreTable.clear();


        table.setPosition(viewport.getScreenWidth()/2,viewport.getScreenHeight()/2);
        table.setTouchable(Touchable.disabled);
        table.setPosition(viewport.getScreenWidth()/2,100);
        table.defaults().width(110);
        table.setSkin(skin);
        table.debugAll();
       // scoreTable.center();
        //scoreTable.align(Align.center);
        //float charWidth = viewport.getScreenWidth() / 50;

        //    | Player[1]   | Playerr [2]
        //    |    0        |     0
        //  1 |   -2        |     +5

        //scoreTable.add();

        Label plName = new Label(nickname,skin);
        plName.setAlignment(Align.center);
        table.add(plName);

        for (String opp:opponentsNames) {
            Label oppName = new Label(opp,skin);
            oppName.setAlignment(Align.center);
            table.add(oppName).center();
        }

       table.row();


//        scoreTable.row().setActorHeight(viewport.getScreenHeight()/20);
//        String str = "Player [1]";
//        Label playerName = new Label(str,skin);
//        playerName.setWidth(str.length() * charWidth);
//        scoreTable.add(playerName).center();
//        str = "Player [2]";
//        playerName = new Label(str,skin);
//        playerName.setWidth(str.length() * charWidth);
//        scoreTable.add(playerName).center();
//        str = "Player [3]";
//        playerName = new Label(str,skin);
//        playerName.setWidth(str.length() * charWidth);
//        scoreTable.add(playerName).center();
//        str = "Player [4]";
//        playerName = new Label(str,skin);
//        playerName.setWidth(str.length() * charWidth);
//        scoreTable.add(playerName).center();
//        str = "Player [5]";
//        playerName = new Label(str,skin);
//        playerName.setWidth(str.length() * charWidth);
//        scoreTable.add(playerName).center();

        //Label handsWon = new Label("[" + 3 + "/" + 6 +"]",skin);

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.4f, 0.4f, 0.4f, 1.0f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act(delta);
        stage.draw();
        renderNicknamesBatch();
    }

    private void renderNicknamesBatch(){
        float screenWidth = viewport.getScreenWidth();
        float screenHeight = viewport.getScreenHeight();
        batch.begin();
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
        batch.end();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width,height);
        deckPos = new Vector2(width * 0.8f, height * 0.05f);
        float offset = viewport.getScreenWidth() / 50;
        if (plHUD.hasChildren()){
          //  plHUD.resize();
        }

        resizeOpponents(viewport.getScreenWidth(), viewport.getScreenHeight());
    }

    public void resizeOpponents(float width, float height) {
        float offset = width / 40;

        Vector2 plPos;
        Vector2 centerPos;

        plPos = new Vector2(width / 4, height / 2.9f);
        centerPos = new Vector2(-width * 0.9f, height / 6);
        if (oppHUD1 != null){
            oppHUD1.positionCardsVert(width, height, plPos, centerPos, offset * 0.6f, width, -90, false);
        }

        plPos = new Vector2(width * 0.2f, height / 1.35f);
        centerPos = new Vector2(width * 0.05f, height * 1.65f);
        if (oppHUD2 != null){
            oppHUD2.positionCardsHor(width, height, plPos, centerPos, offset * 0.6f, height, -155, true);
        }

        plPos = new Vector2(width / 2, height / 2);
        centerPos = new Vector2(width / 2, height * 1.75f);
        if (oppHUD3 != null){
            oppHUD3.positionCardsHor(width, height, plPos, centerPos, offset * 0.4f, height, 180, true);
        }

        plPos = new Vector2(width * 0.8f, height / 1.35f);
        centerPos = new Vector2(width * 0.95f, height * 1.65f);
        if (oppHUD4 != null){
            oppHUD4.positionCardsHor(width, height, plPos, centerPos, offset * 0.6f, height, 155, true);
        }

        plPos = new Vector2(width * 7 / 8, height / 2.9f);
        centerPos = new Vector2(width * 1.94f * 0.95f, height / 6);
        if (oppHUD5 != null){
            oppHUD5.positionCardsVert(width, height, plPos, centerPos, offset * 0.6f, width, 90, true);
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
        stage.dispose();
    }

    public void updatePlayerCards(List<String> cards) {
        plHUD.refreshCards(cards);
        plHUD.resize();
    }

    public void returnCardsToDeck() {
        for (Actor act : stage.getActors()) {
            if (act instanceof PlayerHUD) {
                PlayerHUD plhud = (PlayerHUD) act;
                for (Actor card : plhud.getChildren()) {
                    if (card instanceof PlayerCard) {
                        MoveToAction moveTo = new MoveToAction();
                        moveTo.setPosition(card.getX(), -200);
                        moveTo.setDuration(1);
                        card.addAction(moveTo);
                    }
                }
            }
            if (act instanceof OpponentHUD) {
                OpponentHUD oppHUD = (OpponentHUD) act;
                for (Actor card : oppHUD.getChildren()) {
                    if (card instanceof OpponentCard) {
                        MoveToAction moveTo = new MoveToAction();
                        if (card.getY() < viewport.getScreenHeight() * 0.7f) {
                            if (card.getX() < viewport.getScreenWidth() / 2) {
                                moveTo.setPosition(-200, card.getY());
                            } else {
                                moveTo.setPosition(viewport.getScreenWidth() + 200, card.getY());
                            }
                        } else {
                            moveTo.setPosition(card.getX(), viewport.getScreenHeight() + 200);
                        }
                        moveTo.setDuration(1);
                        card.addAction(moveTo);
                    }
                }
            }
        }
    }

    public  void destroyAllCards(){
        for (Actor act : stage.getActors()) {
            if (act instanceof PlayerHUD) {
                PlayerHUD plhud = (PlayerHUD) act;
                SnapshotArray<Actor> arr =  plhud.getChildren();
                Object[] items = arr.begin();
                for (int i = 0, n = arr.size; i < n; i++) {
                    Actor card = (Actor)items[i];
                    card.remove();
                }
                arr.end();
            }
            if (act instanceof OpponentHUD) {
                OpponentHUD oppHUD = (OpponentHUD) act;
                oppHUD.clear();
            }
        }
    }

    public void setForbiddenValue(int value) {
        if (value != -1)
            plHUD.setForbiddenValue(value);
    }

    public void seteBidHudVisibile(boolean visibile) {
        plHUD.setBidHUDVisibility(visibile);
    }

    public void playerWonHand() {
        //iterates through all the cards in PlayerHUD and moves them at predefined coordinates
        for (Actor act : plHUD.getChildren()) {
            if (act instanceof com.mygdx.game.testing.Atoms.PlayerCard) {
                com.mygdx.game.testing.Atoms.PlayerCard plCrd = (com.mygdx.game.testing.Atoms.PlayerCard) act;
                if (plCrd.isPutDown() && plCrd.isFlipped()) {
                    MoveToAction moveToAction = new MoveToAction();
                    moveToAction.setPosition(viewport.getScreenWidth() * 0.2f, viewport.getScreenHeight() * 0.05f);
                    moveToAction.setDuration(1f);
                    plCrd.addAction(moveToAction);
                    plCrd.setDrawable( new SpriteDrawable(new Sprite(cardsTextureRepository.getCardTexture("back"))));
                    plCrd.setRotation(0);
                    plCrd.setTouchable(Touchable.disabled);
                    plCrd.setFlipped(false);
                    plCrd.setPutDown(true);
                }
            }
        }
        //iterates through all the cards in OpponentsHUD and moves them at predefined coordinates
        for (Actor act : stage.getActors()) {
            if (act instanceof OpponentHUD) {
                OpponentHUD opp = (OpponentHUD) act;
                for (Actor crd : opp.getChildren()) {
                    if (crd instanceof com.mygdx.game.testing.Atoms.OpponentCard) {
                        com.mygdx.game.testing.Atoms.OpponentCard plCrd = (com.mygdx.game.testing.Atoms.OpponentCard) crd;
                        if (plCrd.isFlipped()) {
                                MoveToAction moveToAction = new MoveToAction();
                                moveToAction.setPosition(viewport.getScreenWidth() * 0.2f, viewport.getScreenHeight() * 0.05f);
                                moveToAction.setDuration(1f);
                            plCrd.addAction(moveToAction);
                            plCrd.setDrawable( new SpriteDrawable(new Sprite(cardsTextureRepository.getCardTexture("back"))));
                            plCrd.setRotation(0);
                            plCrd.setTouchable(Touchable.disabled);
                            plCrd.setFlipped(false);
                        }
                    }
                }
            }
        }
    }

    public void opponentWonHand(String winner) {
        Vector2 wonHandPos = new Vector2();

        if(!oppHUD1.equals(null) && oppHUD1.getNickname().equals(winner)){
            wonHandPos = oppHUD1.getTakenHandPoz();
        }
        if(!oppHUD2.equals(null) && oppHUD2.getNickname().equals(winner)){
            wonHandPos = oppHUD2.getTakenHandPoz();
        }
        if(!oppHUD3.equals(null) && oppHUD3.getNickname().equals(winner)){
            wonHandPos = oppHUD3.getTakenHandPoz();
        }
        if(!oppHUD4.equals(null) && oppHUD4.getNickname().equals(winner)){
            wonHandPos = oppHUD4.getTakenHandPoz();
        }
        if(!oppHUD5.equals(null) && oppHUD5.getNickname().equals(winner)){
            wonHandPos = oppHUD5.getTakenHandPoz();
        }

       // System.out.println("[opponentWonHand] wonHandPos = " + wonHandPos);

        for (Actor act : plHUD.getChildren()) {
            if (act instanceof com.mygdx.game.testing.Atoms.PlayerCard) {
                com.mygdx.game.testing.Atoms.PlayerCard plCrd = (PlayerCard) act;
                if (plCrd.isPutDown() && plCrd.isFlipped()) {
                    MoveToAction moveToAction = new MoveToAction();
                    moveToAction.setPosition(wonHandPos.x, wonHandPos.y);
                    moveToAction.setDuration(1f);
                    plCrd.addAction(moveToAction);
                    plCrd.setDrawable( new SpriteDrawable(new Sprite(cardsTextureRepository.getCardTexture("back"))));
                    plCrd.setRotation(0);
                    plCrd.setTouchable(Touchable.disabled);
                    plCrd.setFlipped(false);
                    plCrd.setPutDown(true);
                }
            }
        }
        for (Actor act : stage.getActors()) {
            if (act instanceof OpponentHUD) {
                OpponentHUD opp = (OpponentHUD) act;
                for (Actor crd : opp.getChildren()) {
                    if (crd instanceof com.mygdx.game.testing.Atoms.OpponentCard) {
                        com.mygdx.game.testing.Atoms.OpponentCard plCrd = (OpponentCard) crd;
                        if (plCrd.isFlipped()) {
                            MoveToAction moveToAction = new MoveToAction();
                            moveToAction.setPosition(wonHandPos.x, wonHandPos.y);
                            moveToAction.setDuration(1f);
                            plCrd.addAction(moveToAction);
                            plCrd.setDrawable( new SpriteDrawable(new Sprite(cardsTextureRepository.getCardTexture("back"))));
                            plCrd.setRotation(0);
                            plCrd.setTouchable(Touchable.disabled);
                            plCrd.setFlipped(false);
                        }
                    }
                }
            }
        }
    }

    public void initOpponents(List<String> opponents) {
        opponentsNames = opponents;
        int nrOpp = opponents.size();

        float width  =  viewport.getScreenWidth();
        float height = viewport.getScreenHeight();

        Vector2 takenHandPosPl1 = new Vector2(width * 0.2f, height * 0.4f);
        Vector2 takenHandPosPl2 = new Vector2(width * 0.3f, height * 0.7f);
        Vector2 takenHandPosPl3 = new Vector2(width * 0.45f, height * 0.7f);
        Vector2 takenHandPosPl4 = new Vector2(width * 0.7f, height * 0.7f);
        Vector2 takenHandPosPl5 = new Vector2(width * 0.8f, height * 0.4f);

        oppHUD1 = new OpponentHUD("", 0, cardsTextureRepository, viewport,deckPos);
        oppHUD2 = new OpponentHUD("", 0, cardsTextureRepository, viewport,deckPos);
        oppHUD3 = new OpponentHUD("", 0, cardsTextureRepository, viewport,deckPos);
        oppHUD4 = new OpponentHUD("", 0, cardsTextureRepository, viewport,deckPos);
        oppHUD5 = new OpponentHUD("", 0, cardsTextureRepository, viewport,deckPos);


        deckPos = new Vector2(width * 0.8f, height * 0.05f);
        System.out.println("--------------" + deckPos);
        // System.out.println("Opponents: " + opponents.size());
        switch (nrOpp) {
            case 1:
                oppHUD3 = new OpponentHUD(opponents.get(0), 0, cardsTextureRepository, viewport,deckPos);
                oppHUD3.setDelay(0.1f);
                stage.addActor(oppHUD3);
                oppHUD3.setCastCardPosition(new Vector2(viewport.getScreenWidth() / 2, viewport.getScreenHeight() / 2));
                oppHUD3.setTakenHandPoz(takenHandPosPl3);

                break;
            case 2:
                oppHUD2 = new OpponentHUD(opponents.get(0), 0, cardsTextureRepository, viewport,deckPos);
                oppHUD2.setDelay(0.1f);
                stage.addActor(oppHUD2);
                oppHUD2.setCastCardPosition(new Vector2(viewport.getScreenWidth() / 2, viewport.getScreenHeight() / 2));
                oppHUD2.setTakenHandPoz(takenHandPosPl2);


                oppHUD4 = new OpponentHUD(opponents.get(1), 0, cardsTextureRepository, viewport,deckPos);
                oppHUD4.setDelay(0.2f);
                stage.addActor(oppHUD4);
                oppHUD4.setCastCardPosition(new Vector2(viewport.getScreenWidth() / 2, viewport.getScreenHeight() / 2));
                oppHUD4.setTakenHandPoz(takenHandPosPl4);

                break;
            case 3:
                oppHUD1 = new OpponentHUD(opponents.get(0), 0, cardsTextureRepository, viewport,deckPos);
                oppHUD1.setDelay(0.1f);
                stage.addActor(oppHUD1);
                oppHUD1.setCastCardPosition(new Vector2(viewport.getScreenWidth() / 2, viewport.getScreenHeight() / 2));
                oppHUD1.setTakenHandPoz(takenHandPosPl1);


                oppHUD3 = new OpponentHUD(opponents.get(1), 0, cardsTextureRepository, viewport,deckPos);
                oppHUD3.setDelay(0.2f);
                stage.addActor(oppHUD3);
                oppHUD3.setCastCardPosition(new Vector2(viewport.getScreenWidth() / 2, viewport.getScreenHeight() / 2));
                oppHUD3.setTakenHandPoz(takenHandPosPl3);


                oppHUD5 = new OpponentHUD(opponents.get(2), 0, cardsTextureRepository, viewport,deckPos);
                oppHUD5.setDelay(0.3f);
                stage.addActor(oppHUD5);
                oppHUD5.setCastCardPosition(new Vector2(viewport.getScreenWidth() / 2, viewport.getScreenHeight() / 2));
                oppHUD5.setTakenHandPoz(takenHandPosPl5);

                break;
            case 4:
                oppHUD1 = new OpponentHUD(opponents.get(0), 0, cardsTextureRepository, viewport,deckPos);
                oppHUD1.setDelay(0.1f);
                stage.addActor(oppHUD1);
                oppHUD1.setCastCardPosition(new Vector2(viewport.getScreenWidth() / 2, viewport.getScreenHeight() / 2));
                oppHUD1.setTakenHandPoz(takenHandPosPl1);

                oppHUD2 = new OpponentHUD(opponents.get(1), 0, cardsTextureRepository, viewport,deckPos);
                oppHUD2.setDelay(0.2f);
                stage.addActor(oppHUD2);
                oppHUD2.setCastCardPosition(new Vector2(viewport.getScreenWidth() / 2, viewport.getScreenHeight() / 2));
                oppHUD2.setTakenHandPoz(takenHandPosPl2);


                oppHUD4 = new OpponentHUD(opponents.get(2), 0, cardsTextureRepository, viewport,deckPos);
                oppHUD4.setDelay(0.3f);
                stage.addActor(oppHUD4);
                oppHUD4.setCastCardPosition(new Vector2(viewport.getScreenWidth() / 2, viewport.getScreenHeight() / 2));
                oppHUD4.setTakenHandPoz(takenHandPosPl4);


                oppHUD5 = new OpponentHUD(opponents.get(3), 0, cardsTextureRepository, viewport,deckPos);
                oppHUD5.setDelay(0.4f);
                stage.addActor(oppHUD5);
                oppHUD5.setCastCardPosition(new Vector2(viewport.getScreenWidth() / 2, viewport.getScreenHeight() / 2));
                oppHUD5.setTakenHandPoz(takenHandPosPl5);

                break;
            case 5:
                oppHUD1 = new OpponentHUD(opponents.get(0), 0, cardsTextureRepository, viewport,deckPos);
                oppHUD1.setDelay(0.1f);
                stage.addActor(oppHUD1);
                oppHUD1.setCastCardPosition(new Vector2(viewport.getScreenWidth() / 2, viewport.getScreenHeight() / 2));
                oppHUD1.setTakenHandPoz(takenHandPosPl1);


                oppHUD2 = new OpponentHUD(opponents.get(1), 0, cardsTextureRepository, viewport,deckPos);
                oppHUD2.setDelay(0.2f);
                stage.addActor(oppHUD2);
                oppHUD2.setCastCardPosition(new Vector2(viewport.getScreenWidth() / 2, viewport.getScreenHeight() / 2));
                oppHUD2.setTakenHandPoz(takenHandPosPl2);


                oppHUD3 = new OpponentHUD(opponents.get(2), 0, cardsTextureRepository, viewport,deckPos);
                oppHUD3.setDelay(0.3f);
                stage.addActor(oppHUD3);
                oppHUD3.setCastCardPosition(new Vector2(viewport.getScreenWidth() / 2, viewport.getScreenHeight() / 2));
                oppHUD3.setTakenHandPoz(takenHandPosPl3);


                oppHUD4 = new OpponentHUD(opponents.get(3), 0, cardsTextureRepository, viewport,deckPos);
                oppHUD4.setDelay(0.4f);
                stage.addActor(oppHUD4);
                oppHUD4.setCastCardPosition(new Vector2(viewport.getScreenWidth() / 2, viewport.getScreenHeight() / 2));
                oppHUD4.setTakenHandPoz(takenHandPosPl4);


                oppHUD5 = new OpponentHUD(opponents.get(4), 0, cardsTextureRepository, viewport,deckPos);
                oppHUD5.setDelay(0.5f);
                stage.addActor(oppHUD5);
                oppHUD5.setCastCardPosition(new Vector2(viewport.getScreenWidth() / 2, viewport.getScreenHeight() / 2));
                oppHUD5.setTakenHandPoz(takenHandPosPl5);
                break;
        }
    }

    public List<NetworkDTO.Bids.Bid> getBids() {
        return bids;
    }

    public void setBids(List<NetworkDTO.Bids.Bid> bids) {
        this.bids = bids;
    }


    public void addScore(List<NetworkDTO.Score> scores){
        System.out.println("[GameScreen] " + scores);
        //scoreList.add(score);
        addScoreToTable(scores);
    }

    public void deleteAllScores(){
        scoreList.clear();
    }

}
