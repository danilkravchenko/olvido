package com.dekay.olvido.tools;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Disposable;
import com.dekay.olvido.OlvidoGame;
import com.dekay.olvido.screens.AbstractScreen;

import java.util.Stack;

/**
 * Created by Крава on 05.02.2016.
 */
public class GameScreenManager implements Disposable {
    private Stack<AbstractScreen> screens;
    private OlvidoGame game;
    private AbstractScreen mainScreen;

    public GameScreenManager(OlvidoGame game) {
        this.game = game;
        screens = new Stack<AbstractScreen>();
        push(mainScreen);
    }

    public void addMainMenu(AbstractScreen screen) {
        this.mainScreen = screen;
    }

    public SpriteBatch getBatch() {
        return game.batch;
    }

    public void push(AbstractScreen screen) {
        screens.push(screen);
        game.setScreen(screen);
    }

    public void pop() {
        screens.peek().dispose();
        screens.pop();
        game.setScreen(screens.peek());
    }

    private void set(AbstractScreen screen) {
        screens.peek().dispose();
        screens.pop();
        screens.push(screen);
        game.setScreen(screen);
    }

    public void set(AbstractScreen screen, boolean fromMenu) {
        if (fromMenu) {
            for (int i = 1; i < screens.size(); i++) {
                screens.get(i).dispose();
            }
            screens.clear();
            screens.push(mainScreen);
            push(screen);
        } else {
            set(screen);
        }
    }

    public void toMenu() {
        screens.clear();
        push(mainScreen);
    }


    @Override
    public void dispose() {
        System.out.println("dispose " + this.getClass().getName());
        screens = null;
    }
}

