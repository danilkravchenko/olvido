package com.dekay.olvido.screens.pausescreen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Disposable;
import com.dekay.olvido.Constants;
import com.dekay.olvido.screens.AbstractScreen;
import com.dekay.olvido.screens.GameScreen;
import com.dekay.olvido.tools.Assets;
import com.dekay.olvido.tools.GameScreenManager;

/**
 * Created by Крава on 05.02.2016.
 */
public class PauseScreen extends AbstractScreen {
    private Sprite background;
    private PrePauseButton prePauseButton;
    private PauseButtonBack pauseButtonBack;
    private PauseButtonRestart pauseButtonRestart;
    private PauseButtonMenu pauseButtonMenu;
    private Stage stage;

    public PauseScreen(GameScreenManager gsm) {
        super(gsm, Constants.VIEWPORT_WIDTH, Constants.VIEWPORT_HEIGHT);
        stage = new Stage(viewport, gsm.getBatch());

        initButtons();


        Gdx.input.setInputProcessor(stage);
        //Background for Tests
        showPrePause();
    }

    private void initButtons() {
        prePauseButton = new PrePauseButton();
        prePauseButton.addListener(new ActorGestureListener(1, 1, 0.4f, 1) {
            @Override
            public void tap(InputEvent event, float x, float y, int count, int button) {
                showPause();
            }

            @Override
            public boolean longPress(Actor actor, float x, float y) {
                gsm.pop();
                return true;
            }
        });
        stage.addActor(prePauseButton);

        pauseButtonBack = new PauseButtonBack();

        pauseButtonBack.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                showPrePause();
                return true;
            }
        });

        pauseButtonMenu = new PauseButtonMenu();
        pauseButtonMenu.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                gsm.toMenu();
                return true;
            }
        });
        pauseButtonRestart = new PauseButtonRestart();
        pauseButtonRestart.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                gsm.set(new GameScreen(gsm), true);
                return true;
            }
        });
    }

    private void showPause() {
        background = new Sprite(Assets.getInstance().background.pauseBackground);
        background.setSize(viewport.getWorldWidth(), viewport.getWorldHeight());
        Table table = new Table();
        table.setFillParent(true);
        stage.clear();
        table.add(pauseButtonMenu).expandX();
        table.add(pauseButtonRestart).expandX();
        table.add(pauseButtonBack).expandX();
        stage.addActor(table);
    }

    private void showPrePause() {
        stage.clear();
        stage.addActor(prePauseButton);
        Pixmap pixmap = new Pixmap((int) viewport.getWorldWidth(), (int) viewport.getWorldHeight(), Pixmap.Format.RGBA8888);
        pixmap.setColor(Constants.DARK);
        pixmap.fill();
        background = new Sprite(new Texture(pixmap));
    }

    public PauseScreen setCirclePosition(Vector2 circlePosition) {
        prePauseButton.setPosition(circlePosition);
        return this;
    }

    @Override
    public void update(float delta) {
        stage.act();
        camera.update();
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        update(delta);

        gsm.getBatch().setProjectionMatrix(camera.combined);
        gsm.getBatch().begin();
        background.draw(gsm.getBatch());
        gsm.getBatch().end();
        stage.draw();
    }

    @Override
    public void show() {

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
        System.out.println("dispose " + this.getClass().getName());
        super.dispose();
        background = null;
        prePauseButton.dispose();
        stage.dispose();
    }

    private class PrePauseButton extends Actor implements Disposable {
        private Sprite texture;

        public PrePauseButton() {
            texture = new Sprite(Assets.getInstance().button.prePauseButton);
            this.setSize(texture.getWidth(), texture.getHeight());
        }

        public void setPosition(Vector2 position) {
            texture.setPosition(position.x - texture.getWidth() / 2, position.y - texture.getHeight() / 2);
            this.setPosition(texture.getX(), texture.getY());
        }

        @Override
        public void draw(Batch batch, float parentAlpha) {
            texture.draw(batch);
        }

        @Override
        public void dispose() {
            System.out.println("dispose " + this.getClass().getName());
            texture = null;
        }
    }

    private class PauseButtonBack extends Actor implements Disposable {
        private Sprite texture;

        public PauseButtonBack() {
            texture = new Sprite(Assets.getInstance().button.pauseButtonBack);
            this.setSize(texture.getWidth(), texture.getHeight());
        }

        @Override
        public void draw(Batch batch, float parentAlpha) {
            texture.setPosition(getX(), getY());
            texture.draw(batch);
        }

        @Override
        public void dispose() {
            System.out.println("dispose " + this.getClass().getName());
            texture = null;
        }
    }

    private class PauseButtonMenu extends Actor implements Disposable {
        private Sprite texture;

        public PauseButtonMenu() {
            texture = new Sprite(Assets.getInstance().button.pauseButtonMenu);
            this.setSize(texture.getWidth(), texture.getHeight());
        }

        @Override
        public void draw(Batch batch, float parentAlpha) {
            texture.setPosition(getX(), getY());
            texture.draw(batch);
        }

        @Override
        public void dispose() {
            System.out.println("dispose " + this.getClass().getName());
            texture = null;
        }
    }

    private class PauseButtonRestart extends Actor implements Disposable {
        private Sprite texture;

        public PauseButtonRestart() {
            texture = new Sprite(Assets.getInstance().button.pauseButtonRestart);
            this.setSize(texture.getWidth(), texture.getHeight());
        }

        @Override
        public void draw(Batch batch, float parentAlpha) {
            texture.setPosition(getX(), getY());
            texture.draw(batch);
        }

        @Override
        public void dispose() {
            System.out.println("dispose " + this.getClass().getName());
            texture = null;
        }
    }

}
