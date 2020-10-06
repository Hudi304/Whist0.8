package com.mygdx.game.testing;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.mygdx.game.businessLayer.others.Constants;
import com.mygdx.game.dataLayer.generics.Card;
import com.mygdx.game.dataLayer.repositories.CardsTextureRepository;

public class GameScreenTest implements Screen {

    Stage stage;
    Skin skin;
    CardsTextureRepository cardsTextureRepository;

    String[] cards = {"h-14","h-13","d-14","d-12"};
    int i = 0;

    public GameScreenTest(CardsTextureRepository cardsTextureRepository) {
        this.cardsTextureRepository = cardsTextureRepository;
    }

    @Override
    public void show() {
        skin = new Skin(Gdx.files.internal(Constants.skinJsonString));
        stage = new Stage();





        CardTest1 c = new CardTest1(cardsTextureRepository.getCardTexture("back"));
        c.addListener(new ClickListener());

        stage.addActor(c);


        TextButton flipButton = new TextButton("Flip",skin);


        flipButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {

                for (Actor a: stage.getActors()){
                    if(a instanceof CardTest1){
                        CardTest1 localCard = (CardTest1) a;
                        localCard.setBackImage(cardsTextureRepository.getCardTexture(cards[i]));
                        i++;
                        if(i == cards.length)
                            i = 0;
                    }
                }
            }
        });


        stage.addActor(flipButton);
        Gdx.input.setInputProcessor(stage);
    }


    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.8f,0.8f, 0.8f, 1.0f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);


        stage.act(delta);
        stage.draw();
    }


    @Override
    public void resize(int width, int height) {

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
