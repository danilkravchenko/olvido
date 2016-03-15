package com.dekay.olvido.objects;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.utils.Array;
import com.dekay.olvido.Constants;
import com.dekay.olvido.screens.GameScreen;
import com.dekay.olvido.tools.Assets;

/**
 * Created by Крава on 05.02.2016.
 */
public class Ball extends GameObject {
    private TextureRegion defaultTexture;
    private TextureRegion blinkTexture;
    private STATE prevState;
    private STATE currState;
    private float stateTimer;
    private float spawningTime;
    private boolean spawned;
    private Animation animation;

    public Ball(GameScreen screen, float x, float y) {
        super(screen, x, y);
    }

    @Override
    public void init() {
        defaultTexture = Assets.getInstance().ball.active;
        blinkTexture = Assets.getInstance().ball.blink;

        Array<TextureRegion> frames = new Array<TextureRegion>();
        frames.add(defaultTexture);
        frames.add(new TextureRegion(blinkTexture));
        animation = new Animation(0.3f, frames);


        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(getX(), getY());
        bodyDef.type = BodyDef.BodyType.DynamicBody;

        CircleShape shape = new CircleShape();
        shape.setRadius(Constants.BALL_RADIUS / Constants.PPM);

        FixtureDef fDef = new FixtureDef();
        fDef.shape = shape;
        fDef.filter.categoryBits = Constants.BALL_BIT;
        fDef.filter.maskBits = Constants.OBJECT_BIT |
                Constants.PLAYER_BIT;

        body = world.createBody(bodyDef);
        body.createFixture(fDef).setUserData(this);

        angle = MathUtils.random(0, 2 * MathUtils.PI);
        velocity.set(Constants.SMALL_BALL_SPEED * MathUtils.cos(angle),
                Constants.SMALL_BALL_SPEED * MathUtils.sin(angle));
        body.setLinearVelocity(velocity);

        setSize(Constants.BALL_RADIUS * 2 / Constants.PPM, Constants.BALL_RADIUS * 2 / Constants.PPM);

        prevState = STATE.DEFAULT;
        currState = STATE.DEFAULT;
        spawned = false;
        spawningTime = 3;
        stateTimer = 0;
    }

    public void resetVelosity(boolean isNull) {
        if (isNull)
            velocity.set(0.0f, 0.0f);
        else
            velocity.set(Constants.SMALL_BALL_SPEED * MathUtils.cos(angle),
                    Constants.SMALL_BALL_SPEED * MathUtils.sin(angle));
    }

    public void onObjectHit(Contact contact) {
        if ((contact.getWorldManifold().getNormal().x == 1 && contact.getWorldManifold().getNormal().y < 0) ||
                (contact.getWorldManifold().getNormal().x == -1 && contact.getWorldManifold().getNormal().y > 0))
            velocity.x = -velocity.x;
        if ((contact.getWorldManifold().getNormal().x > 0 && contact.getWorldManifold().getNormal().y == 1) ||
                (contact.getWorldManifold().getNormal().x > 0 && contact.getWorldManifold().getNormal().y == -1))
            velocity.y = -velocity.y;
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        setRegion(getFrame(delta));
    }

    private TextureRegion getFrame(float delta) {
        currState = getState(delta);
        Filter filter = new Filter();
        TextureRegion region;
        switch (currState) {
            case SPAWNING:
                if (prevState != currState) {
                    filter.maskBits = Constants.OBJECT_BIT;
                    for (Fixture fixture :
                            body.getFixtureList()) {
                        filter.categoryBits = fixture.getFilterData().categoryBits;
                        fixture.setFilterData(filter);
                    }
                }
                region = animation.getKeyFrame(stateTimer, true);
                break;
            case SPAWNED:
            case DEFAULT:
            default:
                if (prevState != currState) {
                    filter.maskBits = Constants.PLAYER_BIT | Constants.OBJECT_BIT;
                    for (Fixture fixture :
                            body.getFixtureList()) {
                        filter.categoryBits = fixture.getFilterData().categoryBits;
                        fixture.setFilterData(filter);
                    }
                }
                region = defaultTexture;
                break;
        }
        prevState = currState;
        return region;
    }

    private STATE getState(float delta) {
        if (spawned)
            return STATE.SPAWNED;
        stateTimer += delta;
        if (stateTimer <= spawningTime) {
            return STATE.SPAWNING;
        } else {
            spawned = true;
            return STATE.SPAWNED;
        }
    }

    @Override
    public void dispose() {
        super.dispose();
    }

    public enum STATE {DEFAULT, SPAWNING, SPAWNED}
}
