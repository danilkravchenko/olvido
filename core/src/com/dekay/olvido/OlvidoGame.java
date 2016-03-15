package com.dekay.olvido;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.tools.texturepacker.TexturePacker;
import com.dekay.olvido.screens.menuscreen.Menu;
import com.dekay.olvido.tools.Assets;
import com.dekay.olvido.tools.GameScreenManager;

public class OlvidoGame extends Game {
    public SpriteBatch batch;
    private GameScreenManager manager;

    @Override
    public void create() {
        if (Gdx.app.getType() == Application.ApplicationType.Desktop) {
            TexturePacker.Settings settings = new TexturePacker.Settings();
            settings.maxWidth = 2000;
            settings.maxHeight = 2000;
            settings.minWidth = 2000;
            settings.minHeight = 2000;
            settings.pot = false;
            settings.filterMag = Texture.TextureFilter.Linear;
            settings.filterMin = Texture.TextureFilter.Linear;
            settings.atlasExtension = "";

            TexturePacker.processIfModified(settings, "textures", "atlases", "textures.pack");
        }
        batch = new SpriteBatch();
        manager = new GameScreenManager(this);
        manager.addMainMenu(new Menu(manager));
        manager.toMenu();
    }

    @Override
    public void render() {
        super.render();
    }

    @Override
    public void dispose() {
        System.out.println("dispose " + this.getClass().getName());
        super.dispose();
        batch.dispose();
        manager.dispose();
        Assets.getInstance().dispose();
    }
}
