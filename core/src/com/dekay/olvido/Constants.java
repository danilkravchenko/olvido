package com.dekay.olvido;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.Array;
import com.dekay.olvido.objects.Ball;

/**
 * Created by Крава on 05.02.2016.
 */
public final class Constants {
    public static final String TEXTURE_ATLAS = "atlases/textures.pack";
    public static final String SKIN = "skins/skin.json";

    public static int BALLS_QUANTITY = 5; //easy - 3, medium - 4, hard - 5 (посмотрим как будет с препятствиями)
    public static final float BALL_RADIUS = 50;   //размер шарика
    public static final int SMALL_BALL_RADIUS_FOR_BITMAP = 512;
    public static final float SMALL_BALL_SPEED = 7f;   //easy - 15, medium - 20, hard - 25

    public static final Color DARK = new Color(0.066f, 0.094f, 0.121f, 1);
    public static final Color LIGHT = new Color(0.95f, 0.95f, 0.95f, 1);
    public static final Color GRAY = new Color(0.87f, 0.87f, 0.87f, 1);

    public static final int VIEWPORT_WIDTH = 1080;
    public static final int VIEWPORT_HEIGHT = 1920;
    public static final float PPM = 100f;

    public static final int BALL_BIT = 1;
    public static final int OBJECT_BIT = 2;
    public static final int PLAYER_BIT = 4;
    public static final int SPAWNING_BALL_BIT = 8;
}