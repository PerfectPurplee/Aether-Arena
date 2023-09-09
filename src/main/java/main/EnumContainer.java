package main;

public class EnumContainer {


    public enum PlayerState {
        IDLE_UP, IDLE_DOWN, IDLE_LEFT, IDLE_RIGHT,
        MOVING_UP, MOVING_DOWN, MOVING_LEFT, MOVING_RIGHT

    }

    public enum AllScenes {

        PLAYING,
        MENU,
        PAUSE,
        MAP_EDITOR;

       public static AllScenes Current_Scene = MENU;

    }
}
