package com.mygdx.game.testing;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.MoveByAction;
import com.badlogic.gdx.scenes.scene2d.actions.RotateByAction;
import com.badlogic.gdx.scenes.scene2d.actions.ScaleByAction;
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
        Gdx.input.setInputProcessor(stage);
        MyActor actor = new MyActor(cardsTextureRepository.getCardTexture("h-14"),0);
        MyActor actor1 = new MyActor(cardsTextureRepository.getCardTexture("h-14"),20);
        Group group = new Group();
        group.setWidth(200);
        group.setHeight(200);
        group.setX(200);
        group.setY(200);


        group.setTransform(true);
        group.addActor(actor);
        group.addActor(actor1);

        stage.addActor(group);

        TextButton flipButton = new TextButton("StartAction",skin);

        flipButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                System.out.println("Pressed");
                RotateByAction action1 = new RotateByAction();
                if(i > 180)
                    i = 0;
                System.out.println("Radius: " + i);
                action1.setAmount(i++);

                action1.setDuration(1f);
//                MoveByAction action2 = new MoveByAction();
//                action2.setAmount(200,200);
//                action2.setDuration(1f);

                ScaleByAction action3 = new ScaleByAction();
                action3.setAmount(0.1f);
                stage.getActors().first().addAction(action1);
                //stage.getActors().first().addAction(action2);
                stage.getActors().first().addAction(action3);


            }
        });


        stage.addActor(flipButton);

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
