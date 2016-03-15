package com.dekay.olvido.tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetErrorListener;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2D;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Disposable;
import com.dekay.olvido.Constants;

/**
 * Created by Крава on 05.02.2016.
 */
public class Assets implements Disposable, AssetErrorListener {
    private static Assets instance;
    public Background background;
    public Indicator indicator;
    public Ball ball;
    public Button button;
    public Skin UIskin;
    private AssetManager assetManager;
    private BodyEditorLoader editorLoader;

    private Assets() {
        long time = System.currentTimeMillis();
        init();
        Gdx.app.log("TIME", String.format("%d", System.currentTimeMillis() - time));
    }

    public static Assets getInstance() {
        if (instance == null) {
            instance = new Assets();
        }
        return instance;
    }

    public void init() {
        assetManager = new AssetManager();

        Box2D.init();
        editorLoader = new BodyEditorLoader(Gdx.files.internal("background.json"));

        assetManager.setErrorListener(this);
        assetManager.load(Constants.TEXTURE_ATLAS, TextureAtlas.class);
        assetManager.finishLoading();
        TextureAtlas atlas = assetManager.get(Constants.TEXTURE_ATLAS);
        UIskin = new Skin(Gdx.files.internal(Constants.SKIN), atlas);

        background = new Background(atlas);
        indicator = new Indicator(atlas);
        ball = new Ball(atlas);
        button = new Button(atlas);
    }


    @Override
    public void error(AssetDescriptor asset, Throwable throwable) {
        Gdx.app.error("Assets", "Couldn't load asset '"
                + asset.fileName + "'", throwable);
    }

    @Override
    public void dispose() {
        System.out.println("dispose " + this.getClass().getName());
        assetManager.dispose();
        background = null;
        indicator = null;
        editorLoader = null;
        instance = null;
        UIskin.dispose();
    }


    public class Background {
        public final TextureAtlas.AtlasRegion gameBackground;
        public final TextureAtlas.AtlasRegion menuBackground;
        public final TextureAtlas.AtlasRegion settingsBackground;
        public final TextureAtlas.AtlasRegion shopBackground;
        public final TextureAtlas.AtlasRegion highscoreBackground;
        public final TextureAtlas.AtlasRegion pauseBackground;

        public Background(TextureAtlas atlas) {
            gameBackground = atlas.findRegion("game_background");
            menuBackground = atlas.findRegion("menu_background");
            settingsBackground = atlas.findRegion("menu_settings");
            shopBackground = atlas.findRegion("menu_shop");
            highscoreBackground = atlas.findRegion("menu_highscore");
            pauseBackground = atlas.findRegion("pause_background");
        }

        public void attachFixture(Body body, FixtureDef fDef, float scale) {
            editorLoader.attachFixture(body, "background", fDef, scale);
        }
    }

    public class Indicator {
        public final TextureAtlas.AtlasRegion indicator;
        public final TextureAtlas.AtlasRegion menuCircle;

        public Indicator(TextureAtlas atlas) {
            indicator = atlas.findRegion("indicator_ball");
            menuCircle = atlas.findRegion("menu_circle");
        }
    }

    public class Ball {
        public final TextureAtlas.AtlasRegion active;
        public final TextureAtlas.AtlasRegion blink;

        public Ball(TextureAtlas atlas) {
            active = atlas.findRegion("ball_texture");
            blink = atlas.findRegion("ball_texture_blink");
        }
    }

    public class Button {
        public final TextureAtlas.AtlasRegion pauseButtonBack;
        public final TextureAtlas.AtlasRegion pauseButtonRestart;
        public final TextureAtlas.AtlasRegion pauseButtonMenu;
        public final TextureAtlas.AtlasRegion prePauseButton;

        public Button(TextureAtlas atlas) {
            pauseButtonBack = atlas.findRegion("pause_button_back");
            pauseButtonRestart = atlas.findRegion("pause_button_restart");
            pauseButtonMenu = atlas.findRegion("pause_button_menu");
            prePauseButton = atlas.findRegion("pause_circle");
        }
    }

}
