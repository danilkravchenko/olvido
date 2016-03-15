package com.dekay.olvido.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.joints.MouseJoint;
import com.badlogic.gdx.physics.box2d.joints.MouseJointDef;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.dekay.olvido.Constants;
import com.dekay.olvido.objects.Ball;
import com.dekay.olvido.objects.Player;
import com.dekay.olvido.scenes.Hud;
import com.dekay.olvido.screens.pausescreen.PauseScreen;
import com.dekay.olvido.tools.Assets;
import com.dekay.olvido.tools.GameScreenManager;
import com.dekay.olvido.tools.WorldContactListener;

import org.lwjgl.LWJGLException;

/**
 * Created by Крава on 05.02.2016.
 */
public class GameScreen extends AbstractScreen implements InputProcessor {
    public static boolean isgameOver;
    private Vector3 position;
    private World world;
    private Box2DDebugRenderer renderer;
    private Sprite backgroundSprite;
    private Body body;
    private Hud hud;
    private Array<Ball> balls;
    private Player player;
    private MouseJointDef mouseJointDef;
    private MouseJoint mouseJoint;
    private FrameBuffer fb1, fb2;
    private ShaderProgram shaderProgram;
    private String VERT;
    private String FRAG;
    private Window gameOverWindow;
    private Button gameOverMenuButton;
    private Button gameOverRestartButton;
    private Stage gameOverStage;
    private Label scoreLabel;


    public GameScreen(GameScreenManager gsm) {
        super(gsm, Constants.VIEWPORT_WIDTH / Constants.PPM, Constants.VIEWPORT_HEIGHT / Constants.PPM);
        init();
    }

    public void init() {
        world = new World(new Vector2(0, 0), true);
        renderer = new Box2DDebugRenderer();
        position = new Vector3();
        isgameOver = false;
        world.setContactListener(new WorldContactListener());
        long time = System.currentTimeMillis();
        createBackgroung();
        createWorldObjects();
        createMouseJoint();
        createShaderUtils();
        createGameOverWindow();
        Gdx.app.log("TIME", String.format("%d", System.currentTimeMillis() - time));
    }

    private void createWorldObjects() {
        //creating Hud
        hud = new Hud(gsm.getBatch());
        //Creating balls
        balls = new Array<Ball>(Constants.BALLS_QUANTITY);
        for (int i = 0; i < Constants.BALLS_QUANTITY; i++) {
            balls.add(new Ball(this, viewport.getWorldWidth() / 2, viewport.getWorldHeight() / 2));
        }
        //Creating player
        player = new Player(this, viewport.getWorldWidth() / 2, viewport.getWorldHeight() / 2);
    }

    private void createMouseJoint() {
        //Creating mouse joint to let player follow inputs
        mouseJointDef = new MouseJointDef();
        mouseJointDef.maxForce = 10000;
        mouseJointDef.bodyA = body;
        mouseJointDef.bodyB = player.getBody();
        mouseJointDef.collideConnected = true;
    }

    private void createGameOverWindow() {
        //Creating gameOverWindow
        gameOverWindow = new Window("", Assets.getInstance().UIskin);
        gameOverWindow.setSize(gameOverWindow.getBackground().getMinWidth() / Constants.PPM,
                gameOverWindow.getBackground().getMinHeight() / Constants.PPM);
        gameOverWindow.setPosition(viewport.getWorldWidth() / 2 - gameOverWindow.getWidth() / 2,
                viewport.getWorldHeight() / 2 - gameOverWindow.getHeight() / 2);

        //creating menu button
        gameOverMenuButton = new Button(Assets.getInstance().UIskin, "menu_button");
        gameOverMenuButton.setSize(gameOverMenuButton.getBackground().getMinWidth() / Constants.PPM,
                gameOverMenuButton.getBackground().getMinHeight() / Constants.PPM);
        gameOverMenuButton.setPosition(gameOverWindow.getWidth() / 6 - gameOverMenuButton.getWidth() / 2,
                gameOverWindow.getHeight() / 3 - gameOverMenuButton.getHeight() / 2);
        gameOverMenuButton.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                gsm.toMenu();
                return true;
            }
        });
        //creating restart button
        gameOverRestartButton = new Button(Assets.getInstance().UIskin, "restart_button");
        gameOverRestartButton.setSize(gameOverRestartButton.getBackground().getMinWidth() / Constants.PPM,
                gameOverRestartButton.getBackground().getMinHeight() / Constants.PPM);
        gameOverRestartButton.setPosition(gameOverWindow.getWidth() - gameOverWindow.getWidth() / 6 - gameOverRestartButton.getWidth() / 2,
                gameOverWindow.getHeight() / 3 - gameOverRestartButton.getHeight() / 2);
        gameOverRestartButton.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                gsm.set(new GameScreen(gsm), true);
                return true;
            }
        });

      /*  scoreLabel = new Label(String.format("%03d", hud.getCurrentTime()),Assets.getInstance().UIskin);
        scoreLabel.setSize(gameOverRestartButton.getBackground().getMinWidth() / Constants.PPM,
                gameOverRestartButton.getBackground().getMinHeight() / Constants.PPM);
        scoreLabel.setPosition(gameOverWindow.getWidth() / 2 - scoreLabel.getWidth() / 2,
                gameOverWindow.getHeight() / 3 - scoreLabel.getHeight() / 2);
*/

        //Adding buttons to stage
        gameOverWindow.addActor(gameOverMenuButton);
        gameOverWindow.addActor(gameOverRestartButton);
       // gameOverWindow.addActor(scoreLabel);

        //Adding window to stage
        gameOverStage = new Stage(viewport);
        gameOverStage.addActor(gameOverWindow);
    }

    private void createShaderUtils() {
        //Creating framebuffers for shaders
        fb1 = new FrameBuffer(Pixmap.Format.RGBA8888, Constants.VIEWPORT_WIDTH, Constants.VIEWPORT_HEIGHT, true, true);
        fb2 = new FrameBuffer(Pixmap.Format.RGBA8888, Constants.VIEWPORT_WIDTH, Constants.VIEWPORT_HEIGHT, true, true);

        //our basic pass-through vertex shader
        VERT = Gdx.files.internal("vertex").readString();
        FRAG = Gdx.files.internal("fragment").readString();

        //initializing shaderprogram
        shaderProgram = new ShaderProgram(VERT, FRAG);
        if (shaderProgram.getLog().length() != 0)
            System.out.println(shaderProgram.getLog());
    }

    public void createBackgroung() {
        backgroundSprite = new Sprite(Assets.getInstance().background.gameBackground);
        backgroundSprite.setBounds(0, 0, viewport.getWorldWidth(), viewport.getWorldHeight());
        //Creating body bounds for background
        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(backgroundSprite.getX(), backgroundSprite.getY());
        bodyDef.type = BodyDef.BodyType.StaticBody;

        FixtureDef fDef = new FixtureDef();
        fDef.filter.categoryBits = Constants.OBJECT_BIT;
        fDef.filter.maskBits = Constants.BALL_BIT | Constants.PLAYER_BIT;

        body = world.createBody(bodyDef);
        Assets.getInstance().background.attachFixture(body, fDef, viewport.getWorldWidth());
        body.setUserData(this);
    }

    public World getWorld() {
        return world;
    }

    @Override
    public void update(float delta) {
        if (isgameOver) {
            if (Gdx.input.getInputProcessor() != gameOverStage) {
                Gdx.input.setInputProcessor(gameOverStage);
            }
            //scoreLabel.setText(String.format("%03d", hud.getCurrentTime()));
            mouseJoint = null;
            gameOverStage.act();
            for (Ball ball :
                    balls) {
                ball.resetVelosity(true);
            }
            player.update(delta);
        } else {
            hud.update(delta);
            for (Ball ball :
                    balls) {
                ball.update(delta);
            }
            player.update(delta);
            world.step(1 / 60f, 6, 2);
        }
        camera.update();
    }

    //////////////////////////////RENDERING METHODS////////////////////////////////////////
    //////////////THEY RENDER ALL THIS STUFF DEPENDS ON GAME STATE/////////////////

    @Override
    public void render(float delta) {
        update(delta);
        super.render(delta);
        if (!isgameOver)
            renderIfPlaying();
        else
            renderIfGameOver();
    }

    private void renderIfPlaying() {
        renderer.render(world, camera.combined);
        gsm.getBatch().begin();
        renderObjects();
        gsm.getBatch().end();
    }

    private void renderIfGameOver() {
        gsm.getBatch().begin();
        try {
            renderToFBO();
            horizontalBlur();
            verticalBlur();
        } catch (LWJGLException e) {
            e.printStackTrace();
        }
        gsm.getBatch().setProjectionMatrix(gameOverStage.getCamera().combined);
        gameOverStage.draw();
        gsm.getBatch().setShader(SpriteBatch.createDefaultShader());
        gsm.getBatch().end();
    }

    private void renderObjects() {
        gsm.getBatch().setProjectionMatrix(camera.combined);
        backgroundSprite.draw(gsm.getBatch());

        gsm.getBatch().setProjectionMatrix(hud.getStage().getCamera().combined);
        for (Actor actor :
                hud.getStage().getActors()) {
            actor.draw(gsm.getBatch(), 1f);
        }

        gsm.getBatch().setProjectionMatrix(camera.combined);
        player.draw(gsm.getBatch());
        for (Ball ball :
                balls) {
            ball.draw(gsm.getBatch());
        }
    }

    ////////////////////////RENDERING OUR STUFF TO FBOS TO MAKE FUCKING BLURR EFFECT USING SHADERS (FUCK THEM)//////////////////////////////////////
    void renderToFBO() throws LWJGLException {
        fb1.begin();
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        gsm.getBatch().setShader(SpriteBatch.createDefaultShader());
        renderObjects();
        gsm.getBatch().flush();
        fb1.end(viewport.getScreenX(), viewport.getScreenY(), viewport.getScreenWidth(), viewport.getScreenHeight());
    }

    void horizontalBlur() throws LWJGLException {
        gsm.getBatch().setShader(shaderProgram);
        shaderProgram.setUniformf("dir", new Vector2(1.0f, 0.0f));
        fb2.begin();
        gsm.getBatch().draw(fb1.getColorBufferTexture(), 0, 0, 0, 0, viewport.getWorldWidth(), viewport.getWorldHeight(), 1, 1, 0, 0, 0, fb1.getColorBufferTexture().getWidth(), fb1.getColorBufferTexture().getHeight(), false, true);
        gsm.getBatch().flush();
        fb2.end(viewport.getScreenX(), viewport.getScreenY(), viewport.getScreenWidth(), viewport.getScreenHeight());
    }

    void verticalBlur() throws LWJGLException {
        shaderProgram.setUniformf("dir", new Vector2(0.0f, 1.0f));
        gsm.getBatch().draw(fb2.getColorBufferTexture(), 0, 0, 0, 0, viewport.getWorldWidth(), viewport.getWorldHeight(), 1, 1, 0, 0, 0, fb2.getColorBufferTexture().getWidth(), fb2.getColorBufferTexture().getHeight(), false, true);
    }
    //////////////////////////////RENDERING METHODS ARE DONE////////////////////////////////////////

    @Override
    public void show() {
        super.show();
        mouseJointDef.target.set(player.getBody().getPosition().x, player.getBody().getPosition().y);
        mouseJoint = (MouseJoint) world.createJoint(mouseJointDef);
    }

    @Override
    public void pause() {

    }

    private void toPauseScreen(float x, float y) {
        Gdx.input.setInputProcessor(null);
        world.destroyJoint(mouseJoint);
        mouseJoint = null;
        gsm.push(new PauseScreen(gsm).setCirclePosition(new Vector2(x * Constants.PPM, y * Constants.PPM)));
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
        world.dispose();
        renderer.dispose();
        hud.dispose();
        position = null;
        backgroundSprite = null;
        balls = null;
        player.dispose();
        mouseJointDef = null;
        mouseJoint = null;
        body = null;
        shaderProgram.dispose();
        gameOverStage.dispose();
        gameOverMenuButton = null;
        gameOverRestartButton = null;
        gameOverWindow = null;
        fb1.dispose();
        fb2.dispose();
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        if (!isgameOver)
            toPauseScreen(player.getBody().getPosition().x, player.getBody().getPosition().y);
        return true;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        if (mouseJoint == null)
            return false;
        camera.unproject(position.set(screenX, screenY, 0));
        mouseJoint.setTarget(new Vector2(position.x, position.y));
        return true;
    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}

