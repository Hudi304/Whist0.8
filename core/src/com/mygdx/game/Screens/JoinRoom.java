package com.mygdx.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.Client;
import com.mygdx.game.Constants;
import com.mygdx.game.ScreenController;
import com.mygdx.game.networking.dto.NetworkDTO;


import java.util.ArrayList;
import java.util.List;

public class JoinRoom implements Screen {

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
    TextButton createRoomBtn;


    public List<NetworkDTO.Room> rooms = new ArrayList<>();
    public Table table = new Table();

    public JoinRoom(Client mainController){
        viewport = new ScreenViewport();
        stage = new Stage(viewport);
        this.mainController = mainController;
        screenController =  mainController.screenController;
    }

    @Override
    public void show() {

        stage = new Stage(new ExtendViewport(Gdx.graphics.getWidth(),Gdx.graphics.getHeight()));
        skin = new Skin(Gdx.files.internal(Constants.skinJsonString));


        table.debug();

        table.center();

        table.defaults().expandX().fill().space(5f);

        /*
        NetworkDTO.Room rm1 = new NetworkDTO.Room("rm1", 8 , 0);
        Room rm2 = new Room("rm2", 6 , 1);
        Room rm3 = new Room("rm3", 3 , 1);
        Room rm4 = new Room("rm4", 2 , 1);

        rooms.add(rm1);
        rooms.add(rm2);
        rooms.add(rm3);
        rooms.add(rm4);
*/

        refreshTable(table,this.rooms);

        ScrollPane scrollPane = new ScrollPane(table,skin);
        scrollPane.setWidth(Gdx.graphics.getWidth()-200);
        scrollPane.setHeight(Gdx.graphics.getHeight()-100);
        scrollPane.setPosition(50,50);
        scrollPane.debug();

        TextButton backBtn = new TextButton("Back",skin);
        backBtn.setPosition(15,15);
        backBtn.setHeight(30);
        backBtn.setWidth(100);

        backBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
               mainController.goToMainMenu();
            }
        });
        // stage.addActor(table);
        stage.addActor(backBtn);
        stage.addActor(scrollPane);

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
        //todo schimba constantele cu rezolutia din resize
        //todo de facut la tot resize
        stage.getViewport().update(width, height,true);
    }

    @Override
    public void pause() {
        System.out.println("pause Join Room");
        refreshTable(table,this.rooms);

    }

    @Override
    public void resume() {
        System.out.println("resume Join Room");
        refreshTable(table,this.rooms);

    }

    @Override
    public void hide() {
        System.out.println("hide Join Room");
        refreshTable(table,this.rooms);
    }

    @Override
    public void dispose() {
        System.out.println("Dispose Join Room");
        refreshTable(table,this.rooms);
        stage.dispose();
        skin.dispose();
    }

    public void refreshTable(Table table, List<NetworkDTO.Room> rooms){
        table.clear();
        table.defaults().width(110);

        for (final NetworkDTO.Room rm:rooms) {
            System.out.println(rm);
            table.row().setActorHeight(20);
            System.out.println("rm.getROOMID() = " + rm.getRoomID());

            table.add(new Label(rm.getRoomID() + " ",skin)).width(rm.getRoomID().length()*20);//!! NETESTAT
            table.add(new Label("[" + rm.getPlayers()+ "/" + rm.getCapacity() +"]",skin)).width(50).expandX();
            TextButton joinBtn = new TextButton("Join",skin);
            joinBtn.setHeight(30);
            table.add(joinBtn).width(100).pad(3);
            joinBtn.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    mainController.goToScreen(ScreenState.CREDENTIALS);
                    // mainController.goToCredentialsScreen(rm.getRoomID());
                }
            });
            table.row();
        }
    }

    public List<NetworkDTO.Room> getRooms() {
        return rooms;
    }

    public void setRooms(List<NetworkDTO.Room> rooms) {
        this.rooms = rooms;

    }
}
