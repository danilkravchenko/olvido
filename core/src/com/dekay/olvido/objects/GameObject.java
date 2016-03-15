package com.dekay.olvido.objects;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Disposable;
import com.dekay.olvido.screens.GameScreen;

/**
 * Created by Крава on 05.02.2016.
 */
public abstract class GameObject extends Sprite implements Disposable {
    protected GameScreen screen;
    protected World world;
    protected Vector2 velocity;
    protected Body body;
    protected float angle;

    public GameObject(GameScreen screen, float x, float y) {
        this.screen = screen;
        this.world = screen.getWorld();
        velocity = new Vector2();
        setPosition(x, y);
        init();
    }

    public abstract void init();

    public void update(float delta) {
        setPosition(body.getPosition().x - getWidth() / 2, body.getPosition().y - getHeight() / 2);
        body.setLinearVelocity(velocity);
    }

    ;

    public Body getBody() {
        return body;
    }

    @Override
    public void dispose() {
        System.out.println("dispose " + this.getClass().getName());
        velocity = null;
        body = null;
    }
}

