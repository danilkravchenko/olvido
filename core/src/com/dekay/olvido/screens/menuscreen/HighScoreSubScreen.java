package com.dekay.olvido.screens.menuscreen;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.dekay.olvido.tools.Assets;


/**
 * Created by Крава on 03.02.2016.
 */
public class HighScoreSubScreen extends Actor {
    private Sprite background;

    public HighScoreSubScreen() {
        background = new Sprite(Assets.getInstance().background.highscoreBackground);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        background.setBounds(getX(), getY(), getWidth(), getHeight());
        background.draw(batch);
    }

}
