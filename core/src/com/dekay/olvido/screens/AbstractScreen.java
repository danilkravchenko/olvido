package com.dekay.olvido.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.dekay.olvido.tools.GameScreenManager;

/**
 * Created by Крава on 05.02.2016.
 */
public abstract class AbstractScreen extends InputAdapter implements Screen {
    protected GameScreenManager gsm;
    protected Viewport viewport;
    protected OrthographicCamera camera;

    public AbstractScreen(GameScreenManager gsm, float viewportWidth, float viewportHeight) {
        this.gsm = gsm;
        camera = new OrthographicCamera();
        viewport = new FitViewport(viewportWidth, viewportHeight, camera);
        camera.position.set(viewport.getWorldWidth() / 2, viewport.getWorldHeight() / 2, 0);
    }

    public abstract void update(float delta);

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
        camera.update();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 1, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(this);
    }

    public GameScreenManager getGsm() {
        return gsm;
    }

    @Override
    public void dispose() {
        System.out.println("dispose abstract screeen");
        gsm = null;
        viewport = null;
        camera = null;
    }
}
