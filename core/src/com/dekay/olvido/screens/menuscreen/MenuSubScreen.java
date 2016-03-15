package com.dekay.olvido.screens.menuscreen;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.utils.Disposable;
import com.dekay.olvido.screens.GameScreen;
import com.dekay.olvido.tools.Assets;
import com.dekay.olvido.tools.GameScreenManager;

/**
 * Created by Крава on 05.02.2016.
 */
public class MenuSubScreen extends Group implements Disposable {
    private GameScreenManager gsm;
    private Sprite background;
    private Sprite circle;
    private Actor actor;

    private boolean needToDownScale;
    private boolean needToUpScale;
    private boolean scaled;
    private float startScale;
    private Vector2 targetScale;
    private float scaleUpDuration;
    private float scaleDownDuration;
    private float scaleDurationTotal;


    public MenuSubScreen(GameScreenManager gsm) {
        this.gsm = gsm;
        background = new Sprite(Assets.getInstance().background.menuBackground);
        circle = new Sprite(Assets.getInstance().indicator.menuCircle);
        circle.setOrigin(circle.getWidth() / 2, circle.getHeight() / 2);
        actor = new Actor();
        actor.setName("menu_circle");

        actor.addListener(new ActorGestureListener(1, 1, 0.08f, 0.5f) {
            @Override
            public boolean longPress(Actor actor, float x, float y) {
                SubScreensContainer.movable = false;
                startScale = circle.getScaleX();
                scaleUpDuration = 0;
                needToDownScale = false;
                needToUpScale = true;
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                SubScreensContainer.movable = true;
                startScale = circle.getScaleX();
                scaleDownDuration = 0;
                needToUpScale = false;
                needToDownScale = true;
            }

        });
        this.addActor(actor);

        needToDownScale = false;
        needToUpScale = false;
        scaled = false;
        targetScale = new Vector2(10, 1);
        scaleUpDuration = 0;
        scaleDownDuration = 0;
        scaleDurationTotal = 1;
    }


    @Override
    public void act(float delta) {
        circle.setPosition(getWidth() / 2 - circle.getWidth() / 2, getHeight() / 2 - circle.getHeight() / 2);
        actor.setBounds(getWidth() / 2 - circle.getWidth() / 2, getHeight() / 2 - circle.getHeight() / 2, circle.getWidth(), circle.getHeight());
        if (!needToUpScale && !scaled && !needToDownScale)
            return;
        else if (needToUpScale) {
            scaleUpDuration += delta;
            if (scaleUpDuration >= scaleDurationTotal) {
                needToUpScale = false;
                scaled = true;
                circle.setScale(targetScale.x);
            } else {
                float percentComplete = scaleUpDuration / scaleDurationTotal;
                float scale = startScale + percentComplete * (targetScale.x - startScale);
                circle.setScale(scale);
            }
        } else if (needToDownScale) {
            scaleDownDuration += delta;
            if (scaleDownDuration >= scaleUpDuration) {
                needToDownScale = false;
                circle.setScale(targetScale.y);
            } else {
                float percentComplete = scaleDownDuration / scaleUpDuration;
                float scale = startScale + percentComplete * (targetScale.y - startScale);
                circle.setScale(scale);
            }
        }
        if (scaled) {
            scaled = false;
            circle.setScale(targetScale.y);
            scaleUpDuration = 0;
            scaleDownDuration = 0;
            gsm.push(new GameScreen(gsm));
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        background.setBounds(getX(), getY(), getWidth(), getHeight());
        background.draw(batch);
        circle.draw(batch);
    }

    @Override
    public void dispose() {
        background = null;
        circle = null;
        actor = null;
        targetScale = null;
    }
}
