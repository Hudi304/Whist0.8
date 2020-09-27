package com.mygdx.game.Screens;

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
import com.mygdx.game.Constants;
import com.mygdx.game.ScreenController;

public class Credentials implements Screen {

    Stage stage;
    Skin skin;
    Viewport viewport;
    float width;
    float height;

    Client mainController;
    ScreenController screenController;

    TextField userNameTF;
    TextField roomNameTF;

    //Buttons
    TextButton registerRoomBtn;

    public Credentials(Client mainController){
        viewport = new ScreenViewport();
        stage = new Stage(viewport);
        this.mainController = mainController;
        this.screenController = mainController.screenController;
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

        registerRoomBtn = new TextButton("Register",skin);
        registerRoomBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                System.out.println( "Register Pressed");
                mainController.nickName = userNameTF.getText();
                System.out.println("player nickname =" + mainController.nickName);
                screenController.goToLobbyScreen();
            }
        });

        stage.addActor(registerRoomBtn);
        stage.addActor(userNameTF);
        //stage.addActor(roomNameTF);

        Gdx.input.setInputProcessor(stage);
    }

    private void update(float width,float height){
        //todo vezi ca textul nu se scaleaza

        userNameTF.setHeight(height/10);
        userNameTF.setWidth(width/3);
        userNameTF.setPosition((int)(width/2),(int)(height/2) , Align.center);
        userNameTF.debug();

//        roomNameTF.setHeight(height/10);
//        roomNameTF.setWidth(width/3);
//        roomNameTF.setPosition((int)(width/2),(int)(height/2), Align.center);
//        roomNameTF.debug();

        registerRoomBtn.setHeight(height/10);
        registerRoomBtn.setWidth(width/5 - 20);
        registerRoomBtn.setPosition((int)(width/2),(int)(height/2 - registerRoomBtn.getHeight()*1.1f), Align.center);
        registerRoomBtn.debug();
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
