package com.dekay.olvido.objects;

import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.dekay.olvido.Constants;
import com.dekay.olvido.screens.GameScreen;
import com.dekay.olvido.tools.Assets;

/**
 * Created by Крава on 05.02.2016.
 */
public class Player extends GameObject {

    public Player(GameScreen screen, float x, float y) {
        super(screen, x, y);
    }

    @Override
    public void init() {
        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(getX(), getY());
        bodyDef.type = BodyDef.BodyType.DynamicBody;

        CircleShape shape = new CircleShape();
        shape.setRadius(Constants.BALL_RADIUS / Constants.PPM);

        FixtureDef fDef = new FixtureDef();
        fDef.shape = shape;
        fDef.filter.categoryBits = Constants.PLAYER_BIT;
        fDef.filter.maskBits = Constants.OBJECT_BIT |
                Constants.BALL_BIT;

        body = world.createBody(bodyDef);
        body.createFixture(fDef).setUserData(this);
        body.setLinearVelocity(0, 0);

        setRegion(Assets.getInstance().ball.blink);
        setSize(Constants.BALL_RADIUS * 2 / Constants.PPM, Constants.BALL_RADIUS * 2 / Constants.PPM);
    }

    public void onBallHit() {
        GameScreen.isgameOver = true;
    }

    @Override
    public void dispose() {
        super.dispose();
    }
}
