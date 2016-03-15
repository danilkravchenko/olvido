package com.dekay.olvido.screens.menuscreen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.dekay.olvido.Constants;
import com.dekay.olvido.screens.AbstractScreen;
import com.dekay.olvido.tools.Assets;
import com.dekay.olvido.tools.GameScreenManager;

/**
 * Created by Крава on 05.02.2016.
 */
public class Menu extends AbstractScreen {
    private Sprite indicatorBall;

    private SubScreensContainer container;
    private Stage stage;

    public Menu(GameScreenManager gsm) {
        super(gsm, Constants.VIEWPORT_WIDTH, Constants.VIEWPORT_HEIGHT);
        indicatorBall = new Sprite(Assets.getInstance().indicator.indicator);
        container = new SubScreensContainer(viewport.getWorldWidth(), viewport.getWorldHeight());
        container.addWidget(new MenuSubScreen(gsm));
        container.addWidget(new SettingsSubScreen());
        container.addWidget(new HighScoreSubScreen());
        container.addWidget(new ShopSubScreen());

        stage = new Stage(viewport, gsm.getBatch());
        stage.addActor(container);
    }

    @Override
    public void update(float delta) {
        stage.act(delta);
        camera.update();
    }

    @Override
    public void render(float delta) {
        update(delta);
        gsm.getBatch().setColor(1, 1, 1, 1);
        super.render(delta);
        stage.draw();

        gsm.getBatch().setProjectionMatrix(camera.combined);
        gsm.getBatch().begin();
        for (int i = 1; i <= container.getSectionsCount(); i++) {
            if (i == container.calculateCurrentSection()) {
                indicatorBall.setPosition(viewport.getWorldWidth() / 2 - container.getSectionsCount() * indicatorBall.getWidth() + (i - 1) * indicatorBall.getWidth() * 2 + indicatorBall.getWidth() / 2,
                        viewport.getWorldHeight() / 8);
                indicatorBall.setScale(1.3f);
                indicatorBall.draw(gsm.getBatch());
                indicatorBall.setScale(1);
            } else {
                indicatorBall.setPosition(viewport.getWorldWidth() / 2 - container.getSectionsCount() * indicatorBall.getWidth() + (i - 1) * indicatorBall.getWidth() * 2 + indicatorBall.getWidth() / 2,
                        viewport.getWorldHeight() / 8);
                indicatorBall.draw(gsm.getBatch());
            }
        }
        gsm.getBatch().end();
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
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
        super.dispose();
        stage.dispose();
        container.dispose();
    }
}
