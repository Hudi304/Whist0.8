package com.mygdx.game.testing.HUDs;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.testing.Atoms.ChatDragBar;

public class Chat extends Group {

    Table table;
    ScrollPane scrollPane;
    Viewport viewport;
    Skin skin;
    TextField textField;
    TextButton sendButton;
    ChatDragBar chatDragBar;


    public Chat(Skin skin, Viewport viewport, SpriteDrawable dragBarImage){
        this.viewport = viewport;
        this.skin = skin;
        table = new Table();
        table.align(Align.bottomLeft);
        scrollPane =  new ScrollPane(table,skin);
        textField =  new TextField("",skin);
        sendButton = new TextButton("Send",skin);
        chatDragBar = new ChatDragBar(viewport,scrollPane,sendButton,textField,dragBarImage);

        chatDragBar.setHeight(viewport.getScreenWidth()/4);
        chatDragBar.setWidth(viewport.getScreenHeight()/40);
        chatDragBar.setPosition(viewport.getScreenWidth()*2,viewport.getScreenHeight()/2 - chatDragBar.getHeight()/2);


        scrollPane.debugAll();
        scrollPane.setPosition(viewport.getScreenWidth(),viewport.getScreenHeight() * 0.1f);
        scrollPane.setHeight( viewport.getScreenHeight() * 0.9f);
        scrollPane.setWidth( viewport.getScreenWidth() /3);

        table.setWidth(scrollPane.getWidth());

        sendButton.setHeight( viewport.getScreenHeight() * 0.1f);

        sendButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                addChatMessageHost(textField.getText());
            }
        });




        textField.setHeight( viewport.getScreenHeight() * 0.1f);
        textField.setWidth( viewport.getScreenWidth() - sendButton.getWidth());


        this.addActor(scrollPane);
        this.addActor(textField);
        this.addActor(sendButton);
        this.addActor(chatDragBar);

    }


    public void addChatMessage(String mesage){
        Label msg = new Label(mesage,skin);
        msg.setAlignment(Align.left);
        msg.setFillParent(true);
        table.add(msg).maxWidth(scrollPane.getWidth());
        table.row();
        //todo sedn this to server
    }

    public void addChatMessageHost(String mesage){
        Label msg = new Label(mesage,skin);
        msg.setAlignment(Align.right);
        //msg.setFillParent(true);
        table.add(msg).expandX();
        table.row();
        //todo sedn this to server
    }

    public ScrollPane getScrollPane() {
        return scrollPane;
    }

    public void setScrollPane(ScrollPane scrollPane) {
        this.scrollPane = scrollPane;
    }



}
