//package com.mygdx.game.dataLayer.generics;
//
//import com.badlogic.gdx.Gdx;
//import com.badlogic.gdx.Screen;
//import com.badlogic.gdx.graphics.g2d.TextureRegion;
//import com.badlogic.gdx.math.Vector2;
//import com.badlogic.gdx.scenes.scene2d.Actor;
//import com.badlogic.gdx.scenes.scene2d.InputEvent;
//import com.badlogic.gdx.scenes.scene2d.Stage;
//import com.badlogic.gdx.scenes.scene2d.Touchable;
//import com.badlogic.gdx.scenes.scene2d.ui.Image;
//import com.badlogic.gdx.scenes.scene2d.utils.DragListener;
//
//public class CardActor extends Actor {
//
//    private static final float MAX_SPEED = 2000.0f;
//    private static final float FOLLOW_MULTIPLIER = 5.0f;
//
//    public Vector2 currentPosition ;// pozitia cartii
//    public Vector2 originalPosition;
//    public Vector2 spownPosition;
//    public float originalRot;
//
//
//
//    boolean isFlipped = false; //false pe spate, true pe fata
//    boolean flipping = false;
//    boolean following = true;
//    boolean goingBack = false;
//    public boolean choosed = false;
//
//    Vector2 targetPosition = new Vector2(0f,0f);// finger position
//    Vector2 velocity;// vector viteza al cartii
//
//    private String Symbol;
//    private int intSymbol; // asta da ordinea cartilor in mana
//    private int value;
//
//    private String cardID;
//    private Actor cardActor;
//
//    private Image backTexture;
//    private Image faceTexture;
//
//    Stage stage;
//    Screen gameScreen;
//
//
//        public CardActor(String val, TextureRegion faceTexture,TextureRegion backTexture, Vector2 spPos, Vector2 tgPos){
//            cardID = val;
//
//            this.faceTexture = new Image(faceTexture);
//            this.backTexture = new Image(backTexture);
//            velocity = new Vector2(0,0);
//
//            currentPosition = spPos;
//            originalPosition = tgPos;
//            targetPosition = tgPos;
//
//
//
//            this = new Image(backTexture);
//            cardActor.setPosition(originalPosition.x,originalPosition.y);
//            cardActor.setTouchable(Touchable.enabled);
//            cardActor.addListener(new DragListener() {
//                @Override
//                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
//
//                    targetPosition = new Vector2(cardActor.getX() + x, cardActor.getY() + y);
//                    following = true;
//                    goingBack = false;
//                    return true;
//                }
//                @Override
//                public void touchDragged(InputEvent event, float x, float y, int pointer) {
//                    if (following ==  true){
//                        targetPosition = new Vector2(cardActor.getX() + x,cardActor.getY() + y);
//                        // System.out.println("touchDragged" + targetPosition.x + " " + targetPosition.y);
//                    }
//                    if(following == true && targetPosition.y > Gdx.graphics.getHeight()/2){
////                    if(gameScreen.canChooseCard && !thisCard.getSymbol().equals("b")){
////                        //System.out.println("[Card] : Gdx.height = " + Gdx.graphics.getHeight());
////                        originalPosition.y = Gdx.graphics.getHeight()/2;
////                        //ToDo ceva .emmit() pentru o singura carte
////                        //System.out.println("Am ales cartea!!!!!!!!!!!!!!!!11");
////                    }
//                    }
//                    goingBack = false;
//                }
//                @Override
//                public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
//                    targetPosition =  originalPosition;
//                    following = false;
//                    goingBack = true;
//                }
//            });
//            //cardActor.debug();
//            this.gameScreen = gameScreen;
//        }
//
//
//}
