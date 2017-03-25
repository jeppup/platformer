package com.example.jesper.platformer.engine;

import com.example.jesper.platformer.Viewport;
import com.example.jesper.platformer.gameobjects.GameObject;

import java.util.ArrayList;

/**
 * Created by Jesper on 2017-03-22.
 */

public interface IGameView {

    void setGameObjects(ArrayList<GameObject> gameObjects);
    void render();
    Viewport createViewPort(float metersToShowX, float metersToShowY, float scaleFactor);
}
