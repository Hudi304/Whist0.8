package com.mygdx.game.testing;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.Viewport;

public class BidHUD extends Group {

    GameScreenTest gameScreenTest;
    Viewport viewport;

    public BidHUD(GameScreenTest gameScreenTest, Viewport viewport){
        this.gameScreenTest = gameScreenTest;
        this.viewport = viewport;

        Slider bidSlider = new Slider(0f,8f,1, false, gameScreenTest.skin);
        Label bidVal = new Label( "[" + (int)bidSlider.getValue() + "/" + gameScreenTest.nrOfCards + "]",gameScreenTest.skin);
        TextButton bidButton =  new TextButton("Bid",gameScreenTest.skin);

        bidVal.setPosition(viewport.getScreenWidth()/2 + bidSlider.getWidth()/2 ,viewport.getScreenHeight()/2);
        bidSlider.setPosition(viewport.getScreenWidth()/2 - bidSlider.getWidth()/2,viewport.getScreenHeight()/2);
        bidButton.setPosition(viewport.getScreenWidth()/2 ,viewport.getScreenHeight()/2 - bidSlider.getHeight()*2);
        bidButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                System.out.println("[Game] Bid btn pressed");
//                    mainController.sendBid((int)bidSlider.getValue());
//                }
            }
        });
        addActor(bidButton);
        addActor(bidSlider);
        addActor(bidVal);

        setName("bidHUD");
        setVisible(true);
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

}
