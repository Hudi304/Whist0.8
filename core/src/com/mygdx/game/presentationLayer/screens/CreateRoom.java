package com.mygdx.game.presentationLayer.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.Client;
import com.mygdx.game.businessLayer.others.Constants;


public class CreateRoom implements Screen {

    Stage stage;
    Skin skin;
    Viewport viewport;
    float width;
    float height;

    Client mainClient;


    TextField userNameTF;
    TextField roomNameTF;

    //Buttons
    TextButton createRoomBtn;

    public CreateRoom(Client mainController){
        viewport = new ScreenViewport();
        stage = new Stage(viewport);
        this.mainClient = mainController;

    }

    @Override
    public void show() {
        skin = new Skin(Gdx.files.internal(Constants.skinJsonString));

        userNameTF = new TextField(" ", skin);
        userNameTF.setAlignment(Align.center);
        roomNameTF = new TextField(" ", skin);
        roomNameTF.setAlignment(Align.center);

        userNameTF.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                roomNameTF.setText(userNameTF.getText()+ "'s room");
            }
        });

        createRoomBtn = new TextButton("Register",skin);
        createRoomBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                System.out.println( "Register Room Button Pressed");
                mainClient.createGame(roomNameTF.getText(),userNameTF.getText());
            }
        });

        stage.addActor(createRoomBtn);
        stage.addActor(userNameTF);
        stage.addActor(roomNameTF);

        Gdx.input.setInputProcessor(stage);
    }

    private void update(float width,float height){
        //todo vezi ca textul nu se scaleaza

        userNameTF.setHeight(height/10);
        userNameTF.setWidth(width/3);
        userNameTF.setPosition((int)(width/2),(int)(height/2 + createRoomBtn.getHeight()*1.1f) , Align.center);
        userNameTF.debug();

        roomNameTF.setHeight(height/10);
        roomNameTF.setWidth(width/3);
        roomNameTF.setPosition((int)(width/2),(int)(height/2 ), Align.center);
        roomNameTF.debug();

        createRoomBtn.setHeight(height/10);
        createRoomBtn.setWidth(width/5 - 20);
        createRoomBtn.setPosition((int)(width/2),(int)(height/2 - createRoomBtn.getHeight()*1.1f), Align.center);
        createRoomBtn.debug();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1.0f, 1.0f, 1.0f, 0.8f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        this.width = width;
        this.height =  height;
        System.out.println("width=" + width + "  height=" + height);
        update(width,height);
        stage.getViewport().update(width, height,true);

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
    //TODO asta nu se apeleaza automat! (din DOC)
    public void dispose() {
        stage.dispose();
        skin.dispose();
    }


}
