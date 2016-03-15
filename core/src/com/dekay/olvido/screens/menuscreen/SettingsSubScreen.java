package com.dekay.olvido.screens.menuscreen;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Array;
import com.dekay.olvido.Constants;
import com.dekay.olvido.objects.Ball;
import com.dekay.olvido.tools.Assets;


/**
 * Created by Крава on 03.02.2016.
 */
public class SettingsSubScreen extends Actor {
    private Sprite background;

    public SettingsSubScreen() {
        background = new Sprite(Assets.getInstance().background.settingsBackground);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        background.setBounds(getX(), getY(), getWidth(), getHeight());
        background.draw(batch);
    }

}
