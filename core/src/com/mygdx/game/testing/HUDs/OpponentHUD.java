package com.mygdx.game.testing.HUDs;


import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.DelayAction;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.actions.ParallelAction;
import com.badlogic.gdx.scenes.scene2d.actions.RotateToAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.dataLayer.repositories.CardsTextureRepository;
import com.mygdx.game.presentationLayer.screens.NewGameScreen;
import com.mygdx.game.testing.Atoms.OpponentCard;

import static java.lang.Math.abs;
import static java.lang.Math.min;
import static java.lang.Math.sqrt;

public class OpponentHUD extends Group {
    private Viewport viewport;
    public int nrOfCards;
    private NewGameScreen newGameScreen;
    BitmapFont font12;//p
    private Stage stage;
    private float offset;
    private float r;

    public String nickname;
    public Vector2 castCardPosition;
    public Vector2 centerPosition;
    public Vector2 takenHandPoz;
    public Vector2 deckPos;
    float castCardRot;


    float delay;
    CardsTextureRepository cardsTextureRepository;
    private int cardNr = 0;


    public OpponentHUD(String nickname, int nrOfCards, CardsTextureRepository cardsTextureRepository, Viewport viewport, Vector2 deckPosition) {
        this.nickname = nickname;
        this.viewport = viewport;
        this.nrOfCards = nrOfCards;
        this.deckPos = deckPosition;
        //this.castCardRot = castPoz;
        this.cardsTextureRepository = cardsTextureRepository;

        for (int i = 0; i < nrOfCards; i++) {
            OpponentCard card = new OpponentCard(cardsTextureRepository.getCardTexture("back"));
            card.setPosition(deckPosition.x, deckPosition.y);
            card.setOrigPos(deckPosition.x,deckPosition.y);
            this.addActor(card);
        }
    }

    public void positionCardsHor(float width, float height, Vector2 plPos, Vector2 centerPos, float xOffset, float R, float rotation, boolean inverse) {
        centerPosition = centerPos;
        this.r = R;
        float rotOffset = 3;
        float rot;

        float cardWidth = min(height, width) / 4 * 0.6f;
        float cardHeight = min(height, width) / 4;

        float x = plPos.x - nrOfCards * xOffset / 2;
        float y;
        rot = nrOfCards / 2 * rotOffset;

        float delay = this.delay;

        for (Actor act : getChildren()) {
            if (act instanceof OpponentCard) {
                OpponentCard crd = (OpponentCard) act;
                if (inverse) {
                    y = (float) (-sqrt(abs(R * R - (x - centerPos.x) * (x - centerPos.x))) + centerPos.y);
                } else {
                    y = (float) (sqrt(abs(R * R - (x - centerPos.x) * (x - centerPos.x))) + centerPos.y);
                }
                if (crd.isFlipped()) {

                } else {
                    crd.setPosition(deckPos.x,deckPos.y);
                    crd.setWidth(cardWidth * 3 / 5);
                    crd.setHeight(cardHeight * 3 / 5);
                    crd.setOriginX(cardWidth * 3 / 5 / 2);

                    DelayAction delayAction = new DelayAction();
                    delayAction.setDuration(delay);
                    delay = delay + 0.4f;

                    MoveToAction moveToAction = new MoveToAction();
                    moveToAction.setPosition(x, y);
                    moveToAction.setDuration(0.5f);

                    RotateToAction rotateByAction = new RotateToAction();
                    rotateByAction.setRotation(-rot + rotation);
                    rotateByAction.setDuration(0.5f);

                    ParallelAction moveAndRotate = new ParallelAction(moveToAction, rotateByAction);
                    SequenceAction sqAct = new SequenceAction(delayAction, moveAndRotate);
                    crd.addAction(sqAct);

                    crd.setTouchable(Touchable.disabled);
                    rot -= rotOffset;
                    x += xOffset;
                    //delay = delay + 0.4f;
                }
            }
        }
    }

    public void positionCardsVert(float width, float height, Vector2 plPos, Vector2 centerPos, float xOffset, float R, float rotation, boolean inverse) {
        centerPosition = centerPos;
        this.r = R;
        float rotOffset = 3;
        float rot;

        float cardWidth = min(height, width) / 4 * 0.6f;
        float cardHeight = min(height, width) / 4;

        float x;
        float y = plPos.y - nrOfCards * xOffset / 2;
        rot = nrOfCards / 2 * rotOffset;

        float delay = this.delay;
        //todo if != null
        for (Actor act : getChildren()) {
            if (act instanceof OpponentCard) {
                OpponentCard crd = (OpponentCard) act;
                if (inverse) {
                    x = (float) (-sqrt(abs(R * R - (y - centerPos.y) * (y - centerPos.y))) + centerPos.x);
                   // crd.setRotation(rot + rotation);
                } else {
                    x = (float) (sqrt(abs(R * R - (y - centerPos.y) * (y - centerPos.y))) + centerPos.x);
                    //crd.setRotation(-rot + rotation);
                }

                if (crd.isFlipped()) {

                } else {
                    crd.setWidth(cardWidth * 3 / 5);
                    crd.setHeight(cardHeight * 3 / 5);
                    crd.setOriginX(cardWidth * 3 / 5 / 2);
                    crd.setPosition(deckPos.x,deckPos.y);
                    crd.setWidth(cardWidth * 3 / 5);
                    crd.setHeight(cardHeight * 3 / 5);
                    crd.setOriginX(cardWidth * 3 / 5 / 2);

                    DelayAction delayAction = new DelayAction();
                    delayAction.setDuration(delay);
                    delay = delay + 0.4f;

                    MoveToAction moveToAction = new MoveToAction();
                    moveToAction.setPosition(x, y);
                    moveToAction.setDuration(0.5f);

                    RotateToAction rotateByAction = new RotateToAction();
                    if (!inverse) {
                        rotateByAction.setRotation(-rot + rotation);
                    }else {
                        rotateByAction.setRotation(+rot + rotation);
                    }
                    rotateByAction.setRotation(-rot + rotation);
                    rotateByAction.setDuration(0.5f);

                    ParallelAction moveAndRotate = new ParallelAction(moveToAction, rotateByAction);
                    SequenceAction sqAct = new SequenceAction(delayAction, moveAndRotate);
                    crd.addAction(sqAct);

                    //crd.setPosition(x, y);
//                    if (!inverse) {
//                        crd.setRotation(-rot + rotation);
//                    } else {
//                        crd.setRotation(+rot + rotation);
//                    }
                    crd.setTouchable(Touchable.disabled);
                    rot -= rotOffset;
                    y += xOffset;
                }
            }
        }
    }

    public void putCastCard(String str) {
        int max = 7;
        int min = 0;
        int range = max - min + 1;
        //int rand = (int)(Math.random() * range) + min;

        int i = 0;
        for (Actor act : getChildren()) {
            if (act instanceof OpponentCard && i == cardNr) {
                OpponentCard castCardLocal = (OpponentCard) act;
                // System.out.println("[OpponetHUD] putCastCard str = " + str);
                castCardLocal.setDrawable(new SpriteDrawable(new Sprite(cardsTextureRepository.getCardTexture(str))));
                //castCardLocal.debug();
                MoveToAction mvb = new MoveToAction();
                mvb.setPosition(castCardPosition.x, castCardPosition.y);
                mvb.setDuration(0.2f);
                mvb.setInterpolation(Interpolation.circleOut);
                castCardLocal.addAction(mvb);
                castCardLocal.setFlipped(true);
            }
            i++;
        }
        cardNr++;
        nrOfCards--;
    }

    public void refreshOppCards(int nrOfCards) {
        this.viewport = viewport;
        this.nrOfCards = nrOfCards;
        this.cardsTextureRepository = cardsTextureRepository;
        for (Actor act : getChildren()) {
            if (act instanceof OpponentCard) {
                this.removeActor(act);
            }
        }
        for (int i = 0; i < nrOfCards; i++) {
            OpponentCard card = new OpponentCard(cardsTextureRepository.getCardTexture("back"));
            this.addActor(card);
        }
    }

    public void setTakenHandPoz(Vector2 pos) {
        takenHandPoz = pos;
    }

    public void setTakenHandPoz(float x, float y) {
        takenHandPoz = new Vector2(x, y);
    }

    public Vector2 getTakenHandPoz() {
        return takenHandPoz;
    }

    public Vector2 getCastCardPosition() {
        return castCardPosition;
    }

    public void setCastCardPosition(Vector2 castCardPosition) {
        this.castCardPosition = castCardPosition;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public float getDelay() {
        return delay;
    }

    public void setDelay(float delay) {
        this.delay = delay;
    }

}