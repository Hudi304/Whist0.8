package com.mygdx.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.Client;
import com.mygdx.game.Constants;
import com.mygdx.game.ScreenController;

public class MainMenu implements Screen {

        Stage stage;
        Skin skin;
        Viewport viewport;
        float width;
        float height;

        Client mainController;
        ScreenController screenController;

        //Buttons
        TextButton createRoomBtn;
        TextButton joinBtn;
        TextButton optionsBtn;
        TextButton exitBtn;

       public MainMenu(Client mainController){
           viewport = new ScreenViewport();
           stage = new Stage(viewport);
           this.mainController = mainController;
           this.screenController = mainController.screenController;
       }


        @Override
        public void show() {
            skin = new Skin(Gdx.files.internal(Constants.skinJsonString));
            Gdx.input.setInputProcessor(stage);

            createRoomBtn = new TextButton("Create Room",skin);
            createRoomBtn.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    System.out.println( "Create Room Button Pressed");
                    mainController.goToScreen(ScreenState.CREDENTIALS);
                }
            });
            joinBtn = new TextButton("JoinRoom",skin);
            joinBtn.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    System.out.println( "Join Button Pressed");
                    mainController.getRooms();
                    //screenController.goToJoinRoomScreen();
                }
            });
            optionsBtn = new TextButton("Options",skin);
            optionsBtn.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    System.out.println( "Options Button Pressed");
                }
            });
            exitBtn = new TextButton("Exit",skin);
            exitBtn.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    System.out.println( "Exit Button Pressed");
                    mainController.disconnect();
                    //mainController.goToScreen(ScreenState.MAIN_MENU);
                }
            });

            stage.addActor(createRoomBtn);
            stage.addActor(joinBtn);
            stage.addActor(optionsBtn);
            stage.addActor(exitBtn);
        }

        private void updateButtons(float width,float height){
           //todo vezi ca textul nu se scaleaza
            joinBtn.setHeight(height/10);
            joinBtn.setWidth(width/5);
            joinBtn.setPosition((int)(width/2 - joinBtn.getWidth()/2),(int)(height/2 - joinBtn.getHeight()/2));
            //joinBtn.getLabel().setSize(joinBtn.getWidth() - 10,joinBtn.getHeight() - 10);
            createRoomBtn.setHeight(height/10);
            createRoomBtn.setWidth(width/5);
            createRoomBtn.setPosition((int)(width/2 - joinBtn.getWidth()/2),(int)(height/2 - joinBtn.getHeight()/2) + joinBtn.getHeight()*1.1f);

            optionsBtn.setHeight(height/10);
            optionsBtn.setWidth(width/5);
            optionsBtn.setPosition((int)(width/2 - joinBtn.getWidth()/2),(int)(height/2 - joinBtn.getHeight()/2) - joinBtn.getHeight()*1.1f);

            exitBtn.setHeight(height/10);
            exitBtn.setWidth(width/5);
            exitBtn.setPosition((int)(width/2 - joinBtn.getWidth()/2),(int)(height/2 - joinBtn.getHeight()/2) - joinBtn.getHeight()*2.2f);

            //System.out.println((int)(width/2 - joinBtn.getWidth()/2) + "   " + (int)(height/2 - joinBtn.getHeight()/2));
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
            updateButtons(width,height);
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
