package com.dekay.olvido.screens.menuscreen;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;

/**
 * Created by Крава on 05.02.2016.
 */
public class SubScreensContainer extends Group {

    public static boolean movable;
    private boolean finishmoving;
    private float offset = 0;
    private float prevOffset = 0;
    private int transmission = 0;
    private float stopSection = 0;
    private float speed = 5000;
    private int currentSection = 1;
    // Скорость пиксель/секунда после которой считаем, что пользователь хочет перейти к следующей секции
    private float flingSpeed = 2000;
    private float overscrollDistance = 0;

    private float sectionWidth;
    private float sectionHeight;
    private Group sections;

    private Actor touchFocusedChild;
    private ActorGestureListener gestureListener;

    public SubScreensContainer(float x, float y) {
        sectionWidth = x;
        sectionHeight = y;

        sections = new Group();
        this.addActor(sections);

        gestureListener = new ActorGestureListener(1, 1, 0.2f, 1) {

            @Override
            public void fling(InputEvent event, float velocityX, float velocityY, int button) {
                if (movable) {
                    if (Math.abs(velocityX) > flingSpeed) {
                        finishmoving = false;
                        if (velocityX > 0) setStopSection(currentSection - 1);
                        else setStopSection(currentSection + 1);
                    }
                    cancelTouchFocusedChild();
                }
            }

            @Override
            public void pan(InputEvent event, float x, float y, float deltaX, float deltaY) {
                if(movable) {
                    if ((offset - deltaX) < -overscrollDistance) {
                        return;
                    }
                    if ((offset - deltaX) > (getSectionsCount() - 1) * sectionWidth + overscrollDistance)
                        return;
                    prevOffset = offset;
                    offset -= deltaX;
                    finishmoving = false;
                    cancelTouchFocusedChild();
                }
            }
        };
        this.addListener(gestureListener);
        movable = true;
    }

    public void addWidget(Actor widget) {
        widget.setX(getSectionsCount() * sectionWidth);
        widget.setY(0);
        widget.setWidth(sectionWidth);
        widget.setHeight(sectionHeight);
        sections.addActor(widget);
    }

    public int calculateCurrentSection() {
        // Текущая секция = (Текущее смещение / длинну секции) + 1, т.к наши секции нумеруются с 1
        int section = 1;
        if (prevOffset > offset) {
            section = Math.round((offset / sectionWidth) - 0.3f) + 1;
        } else {
            section = Math.round((offset / sectionWidth) + 0.3f) + 1;
        }
        //Проверяем адекватность полученного значения, вхождение в интервал [1, количество секций]
        if (section > getSectionsCount()) return getSectionsCount();
        if (section < 1) return 1;
        return section;
    }

    public int getSectionsCount() {
        return sections.getChildren().size;
    }

    // Устанавлием номер секции, к которой нам нужно сместить экран
    private void setStopSection(int stoplineSection) {

        // Проверяем вхождение секции в интервал [1, количество секций]
        if (stoplineSection < 1) stoplineSection = 1;
        if (stoplineSection > getSectionsCount()) stoplineSection = getSectionsCount();

        // Высчитываем смещение контейнера sections к которому нужно стремиться
        stopSection = (stoplineSection - 1) * sectionWidth;
        // Определяем направление движения
        // transmission ==  1 - вправо
        // transmission == -1 - влево
        if (offset < stopSection) {
            transmission = 1;
        } else {
            transmission = -1;
        }
    }

    private void move(float delta) {
        // Определяем направление смещения
        if (offset < stopSection) {
            // Двигаемся вправо
            // Если попали сюда, а при этом должны были двигаться влево
            // значит пора остановиться
           /* if (transmission == -1) {
                offset = stopSection;
                // Фиксируем номер текущей секции
                currentSection = calculateCurrentSection();
                return;
            }*/
            // Смещаем
            offset += speed * delta;
            if (offset > stopSection) {
                offset = stopSection;
                currentSection = calculateCurrentSection();
                return;
            }
        } else if (offset > stopSection) {
          /*  if (transmission == 1) {
                offset = stopSection;
                currentSection = calculateCurrentSection();
                return;
            }*/
            offset -= speed * delta;
            if (offset < stopSection) {
                offset = stopSection;
                currentSection = calculateCurrentSection();
                return;
            }
        } else
            finishmoving = true;
    }

    @Override
    public void act(float delta) {
        // Смещаем контейнер с секциями
        sections.setX(-offset);
        sections.act(delta);
        // Если водим пальцем по экрану
        if (gestureListener.getGestureDetector().isPanning()) {
            // Устанавливаем секцию, к которой будем анимировать движение
            // граница = номер предыдущей секции
            setStopSection(calculateCurrentSection());
        } else if (!finishmoving) {
            // Если палец далеко от экрана - анимируем движение в заданную точку
            move(delta);
        }
    }


    private void cancelTouchFocusedChild() {
        if (touchFocusedChild == null) return;
        try {
            this.getStage().cancelTouchFocus();
        } catch (Exception e) {

        }
        touchFocusedChild = null;
    }

    public void dispose() {
        touchFocusedChild = null;
        gestureListener = null;
        sections = null;
    }
}
