package com.mygdx.game.testing.HUDs;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.actions.AlphaAction;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.testing.GameScreenTest;


public class BidHUD extends Group {

    GameScreenTest gameScreenTest;
    Viewport viewport;
    int numberOfCards = 0;
    int forbidden = -1;

    Slider bidSlider;
    Label bidVal;
    TextButton bidButton;

    public BidHUD(GameScreenTest gameScreenTest, Viewport viewport){
        this.gameScreenTest = gameScreenTest;
        this.viewport = viewport;

         bidSlider = new Slider(0f,numberOfCards,1, false, gameScreenTest.skin);
         bidVal = new Label( "[" + (int)bidSlider.getValue() + "/" + numberOfCards + "]",gameScreenTest.skin);
         bidButton =  new TextButton("Bid",gameScreenTest.skin);

        bidVal.setPosition(viewport.getScreenWidth()/2 + bidSlider.getWidth()/2 ,viewport.getScreenHeight()/2);
        bidSlider.setPosition(viewport.getScreenWidth()/2 - bidSlider.getWidth()/2,viewport.getScreenHeight()/2);
        bidButton.setPosition(viewport.getScreenWidth()/2 ,viewport.getScreenHeight()/2 - bidSlider.getHeight()*2);
        bidButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                System.out.println("[Game] Bid btn pressed");
                PlayerHUD plHUD  =  (PlayerHUD) getParent();
                int value = getSliderValue();
                if(value == forbidden)
                    System.out.println("MUIEEE");
                else
                    plHUD.sendBidValue(getSliderValue());

            }
        });
        bidSlider.addListener(new InputListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
               super.touchUp(event,x,y,pointer,button);

                System.out.println("Slider works ");
                // Set volume to slider.getValue();
            }
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                System.out.println("Slider works ");

                return true;
            };
        });

        addActor(bidButton);
        addActor(bidSlider);
        addActor(bidVal);

        setName("bidHUD");
        setVisible(true);
    }


    private void changeBidValText(){
        bidVal.setText("[" + (int)bidSlider.getValue() + "/" + numberOfCards + "]");
    }

    void resizeBidHUD(float width, float height) {
        for (Actor act : getChildren()) {
            if (act instanceof Slider) {
                Slider bidSlider = (Slider) act;
                bidSlider.setWidth(width / 4);
               // bidSlider.setDebug(true);
                bidSlider.setHeight(height / 15);
                bidSlider.getStyle().knob.setMinHeight(height / 15);
                bidSlider.getStyle().knob.setMinWidth(height / 16);
                bidSlider.getStyle().background.setMinHeight(bidSlider.getHeight() / 2);
                bidSlider.setPosition(width / 2 - bidSlider.getWidth()*0.6f, height * 0.6f);

            }
            if (act instanceof TextButton) {
                TextButton bidButton = (TextButton) act;
                bidButton.setPosition(width / 2, height / 2);

            }
            if (act instanceof Label) {
                Label bidVal = (Label) act;
                bidVal.setSize(width / 15, height / 15);
                bidVal.setPosition(width * 0.6f, height * 0.6f);
            }
        }
    }

    public void setVisibility(boolean visibility){
        for (Actor act : getChildren()) {
            act.setVisible(visibility);
        }
    }

    public void setForbiddenValue(int value){
        this.forbidden = value;
    }

    public void setNumberOfCards(int numberOfCards){
        this.numberOfCards = numberOfCards;
        for(Actor act: getChildren()){
            if(act instanceof Slider || act instanceof Label)
            {
                removeActor(act);
            }

        }
        Slider bidSlider = new Slider(0f,numberOfCards,1, false, gameScreenTest.skin);
        Label bidVal = new Label( "[" + (int)bidSlider.getValue() + "/" + numberOfCards + "]",gameScreenTest.skin);
        addActor(bidSlider);
        addActor(bidVal);

    }

    public int getSliderValue(){
        for (Actor act:getChildren()) {
            if (act instanceof Slider){
                return (int) ((Slider)act).getValue();
            }
        }
        return 0;
    }

}
