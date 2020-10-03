package com.mygdx.game.dataLayer.generics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.presentationLayer.screens.GameScreen;
import com.mygdx.game.presentationLayer.screens.NewGameScreen;


public class Card extends Actor implements Comparable<Card>{

    private static final float DRAG = 2.0f;
    private static final float MAX_SPEED = 2000.0f;
    private static final float FOLLOW_MULTIPLIER = 5.0f;

    public Vector2 currentPosition ;// pozitia cartii
    public Vector2 originalPosition;
    public Vector2 spownPosition;
    public float originalRot;



    boolean isFlipped = false; //false pe spate, true pe fata
    boolean flipping = false;
    boolean following = true;
    boolean goingBack = false;
    public boolean choosed = false;

    Vector2 targetPosition = new Vector2(0f,0f);// finger position
    Vector2 velocity;// vector viteza al cartii

    private String Symbol;
    private int intSymbol; // asta da ordinea cartilor in mana
    private int value;
    private Actor cardActor;

    private Image backTexture;
    private Image faceTexture;

    Stage stage;
    Screen gameScreen;
    Group group;

    //-----------------------------------------------------------------



    public Card(String val, TextureRegion[][] regions, float x, float y, GameScreen gameScreen){
        String[] cardVal = val.split("-");
        currentPosition = new Vector2(x,y);
        originalPosition = new Vector2(x,y);
        velocity = new Vector2(0,0);

        int cardSimb = 0;
        int cardNr = 0;

        if(cardVal[0].equals("h")){
            cardSimb = 0;
        }
        if(cardVal[0].equals("d")){
            cardSimb = 1;
        }
        if(cardVal[0].equals("c")){
            cardSimb = 2;
        }
        if(cardVal[0].equals("s")){
            cardSimb = 3;
        }
        if(cardVal[0].equals("b")){
            cardSimb = 4;
        }
        //System.out.println("AICI" + cardVal[1]);
        cardNr = Integer.parseInt( cardVal[1]) - 2;

        final Card thisCard = this;

        intSymbol = cardSimb;
        this.cardActor =  new Image(regions[cardSimb][cardNr]);
        cardActor.setPosition(originalPosition.x,originalPosition.y);
        cardActor.setTouchable(Touchable.enabled);
        cardActor.addListener(new DragListener() {
            //todo de resetat rotatia la touchUp/touchDown
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {

                targetPosition = new Vector2(cardActor.getX() + x, cardActor.getY() + y);
                following = true;
                goingBack = false;
                return true;
            }
            @Override
            public void touchDragged(InputEvent event, float x, float y, int pointer) {
                if (following ==  true){
                    targetPosition = new Vector2(cardActor.getX() + x,cardActor.getY() + y);
                   // System.out.println("touchDragged" + targetPosition.x + " " + targetPosition.y);
                }
                if(following == true && targetPosition.y > Gdx.graphics.getHeight()/2){
//                    if(gameScreen.canChooseCard && !thisCard.getSymbol().equals("b")){
//                        //System.out.println("[Card] : Gdx.height = " + Gdx.graphics.getHeight());
//                        originalPosition.y = Gdx.graphics.getHeight()/2;
//                        //ToDo ceva .emmit() pentru o singura carte
//                        //System.out.println("Am ales cartea!!!!!!!!!!!!!!!!11");
//                    }
                }
                goingBack = false;
            }
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                //super.touchUp(event, x, y, pointer, button);
                //System.out.println("[current poz]: " + position.toString());
                // System.out.println("[origin poz]: " + originalPosition.toString());
                targetPosition =  originalPosition;
                following = false;
                goingBack = true;
            }
        });
        cardActor.debug();
        this.Symbol = cardVal[0];
        this.value = cardNr;
        this.gameScreen = gameScreen;
    }

    public Card(String val, TextureRegion faceTexture, Vector2 spPos, Vector2 tgPos, NewGameScreen gameScreen, Group group){
        this.group = group;
        this.faceTexture = new Image(faceTexture);
        this.backTexture = new Image(gameScreen.regions[4][0]);
        velocity = new Vector2(0,0);

        currentPosition = spPos;
        originalPosition = tgPos;
        targetPosition = tgPos;


        final Card thisCard = this;
        cardActor = backTexture;
        cardActor.setPosition(originalPosition.x,originalPosition.y);
        cardActor.setTouchable(Touchable.enabled);
        cardActor.addListener(new DragListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {

                targetPosition = new Vector2(cardActor.getX() + x, cardActor.getY() + y);
                following = true;
                goingBack = false;
                return true;
            }
            @Override
            public void touchDragged(InputEvent event, float x, float y, int pointer) {
                if (following ==  true){
                    targetPosition = new Vector2(cardActor.getX() + x,cardActor.getY() + y);
                    // System.out.println("touchDragged" + targetPosition.x + " " + targetPosition.y);
                }
                if(following == true && targetPosition.y > Gdx.graphics.getHeight()/2){
//                    if(gameScreen.canChooseCard && !thisCard.getSymbol().equals("b")){
//                        //System.out.println("[Card] : Gdx.height = " + Gdx.graphics.getHeight());
//                        originalPosition.y = Gdx.graphics.getHeight()/2;
//                        //ToDo ceva .emmit() pentru o singura carte
//                        //System.out.println("Am ales cartea!!!!!!!!!!!!!!!!11");
//                    }
                }
                goingBack = false;
            }
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                targetPosition =  originalPosition;
                following = false;
                goingBack = true;
            }
        });
        cardActor.debug();
        this.gameScreen = gameScreen;
    }

    public void render(){
        cardActor.setPosition(currentPosition.x,currentPosition.y);
    }

    public void update(float delta, Viewport viewport){
        if(flipping){
            if(isFlipped){
                group.removeActor(cardActor);
                group.addActor(faceTexture);
            }else{
                group.removeActor(cardActor);
                group.addActor(backTexture);
            }
            flipping = false;
        }


        if (following) {
            Vector2 followVector = new Vector2(targetPosition.x - currentPosition.x, targetPosition.y - currentPosition.y);
            velocity.x = FOLLOW_MULTIPLIER * followVector.x;
            velocity.y = FOLLOW_MULTIPLIER * followVector.y;
        }
        //TODO vezi ce faci cu asta
        if (goingBack) {
            Vector2 followVector = new Vector2(targetPosition.x - currentPosition.x, targetPosition.y - currentPosition.y);
            velocity.x = FOLLOW_MULTIPLIER * followVector.x;
            velocity.y = FOLLOW_MULTIPLIER * followVector.y;
        }
        velocity.clamp(0, MAX_SPEED);
        velocity.x = velocity.x - delta *DRAG *velocity.x;
        velocity.y = velocity.y - delta *DRAG *velocity.y;

        currentPosition.x = currentPosition.x + delta * velocity.x;
        currentPosition.y = currentPosition.y + delta * velocity.y;
        //collideWithWalls(viewport.getWorldWidth(),viewport.getWorldHeight());

        cardActor.setPosition(currentPosition.x,currentPosition.y);
    }

    private void collideWithWalls( float viewportWidth, float viewportHeight) {
        if (currentPosition.x < 0) {
            currentPosition.x = 0;
            velocity.x = -velocity.x;
        }
        if (currentPosition.x + cardActor.getWidth() > viewportWidth ) {
            currentPosition.x = viewportWidth - cardActor.getWidth();
            velocity.x = -velocity.x;
        }
        if (currentPosition.y  < 0) {
            currentPosition.y = 0;
            velocity.y = -velocity.y;
        }
        if (currentPosition.y + cardActor.getHeight() > viewportHeight ) {
            currentPosition.y = viewportHeight - cardActor.getHeight();
            velocity.y = -velocity.y;
        }
    }

    public void scale(float x, float y){
        cardActor.setX(x);
        cardActor.setY(y);
    }


    // se face in sens trigonometric
    public void setRot(float rot){
        cardActor.setRotation(rot);
    }

    public void setPosition(float x, float y){
        cardActor.setX(x);
        cardActor.setY(y);
    }

    public void rePosition(float x, float y){
        this.originalPosition.x = x;
        this.originalPosition.y = y;
        this.currentPosition.x = x;
        this.currentPosition.y = y;
    }

    @Override
    //ToDo de revizitat asta (merge da' nu e perfecta)
    public int compareTo(Card card) {
        if(this.intSymbol < card.intSymbol){
            if(this.value < card.value){
                return 1;
            }
            else{
                return -1;
            }
        }else {
           return 1;

        }
    }

    @Override
    public String toString() {
        return "Card{" +
                "Symbol='" + Symbol + '\'' +
                ", value=" + value +
                '}';
    }

    public String getSymbol() {
        return Symbol;
    }

    public int getValue() {
        return value;
    }

    public Actor getCardActor() {
        return cardActor;
    }

    public void init() {
        currentPosition = new Vector2(0,0);
        velocity = new Vector2(0,0);
    }

    public boolean isFlipped() {
        return isFlipped;
    }

    public void setFlipped(boolean flipped) {
        System.out.println("isFlipped ==" + isFlipped);
        isFlipped = flipped;
        flipping = true;
    }
}
