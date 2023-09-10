package main;

public class EnumContainer {


    public enum AllPlayerStates {

        IDLE_UP, IDLE_DOWN, IDLE_LEFT, IDLE_RIGHT,
        IDLE_UP_LEFT, IDLE_UP_RIGHT, IDLE_DOWN_LEFT, IDLE_DOWN_RIGHT,
        MOVING_UP, MOVING_DOWN, MOVING_LEFT, MOVING_RIGHT,
        MOVING_UP_LEFT, MOVING_UP_RIGHT, MOVING_DOWN_LEFT, MOVING_DOWN_RIGHT

    }

    public enum AllPlayableChampions {

        DON_OHL,
        BIG_HAIRY_SWEATY_DUDE

    }




    public enum AllScenes {

        PLAYING,
        MENU,
        PAUSE,
        MAP_EDITOR;

        public static AllScenes Current_Scene = MENU;

    }

}
