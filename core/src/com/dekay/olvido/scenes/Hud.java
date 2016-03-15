package com.dekay.olvido.scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.dekay.olvido.Constants;

/**
 * Created by Крава on 05.02.2016.
 */
public class Hud {
    private Viewport viewport;
    private SpriteBatch batch;
    private Stage stage;
    private Table table;

    private Integer currentTime;
    private float stateTimer;

    private Label timeLabel;

    public Hud(SpriteBatch batch) {
        this.batch = batch;
        viewport = new FitViewport(Constants.VIEWPORT_WIDTH, Constants.VIEWPORT_HEIGHT, new OrthographicCamera());

        stage = new Stage(viewport, batch);

        table = new Table();
        table.setFillParent(true);
        table.center();

        currentTime = 0;
        stateTimer = 0;

        BitmapFont font = new BitmapFont(Gdx.files.internal("fonts/f100.fnt"), false);
        font.setColor(Constants.GRAY);
        font.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        timeLabel = new Label(String.format("%03d", currentTime), new Label.LabelStyle(font, font.getColor()));
        table.add(timeLabel).expandX();

        stage.addActor(table);
    }

    public void update(float delta) {
        stateTimer += delta;
        if (stateTimer >= 1) {
            currentTime += 1;
            timeLabel.setText(String.format("%03d", currentTime));
            stateTimer = 0;
        }
    }

    public Stage getStage() {
        return stage;
    }

    public void dispose() {
        batch = null;
        table = null;
        currentTime = null;
        viewport = null;
        stage.dispose();
    }

    public Integer getCurrentTime() {
        return currentTime;
    }
}

