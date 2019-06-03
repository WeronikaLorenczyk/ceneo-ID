package controllers;

import sceneManagers.FXMLScene;
import sceneManagers.SceneController;
import sceneManagers.SceneHandler;

public class PriceCompScene extends FXMLScene {

    static int id;

    PriceCompScene(SceneController prev, SceneHandler sh, String fxmlpath, int id) {
        super(prev, sh, fxmlpath);
        PriceCompScene.id=id;
    }

}