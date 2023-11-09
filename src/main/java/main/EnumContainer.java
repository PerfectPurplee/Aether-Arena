package main;

import entities.spells.basicspells.Spell01;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class EnumContainer {


    public enum AllPlayerStates {

        IDLE_LEFT, IDLE_RIGHT,
        MOVING_LEFT, MOVING_RIGHT,
        DASHING_LEFT, DASHING_RIGHT,
        DEATH_LEFT, DEATH_RIGHT


    }

    public enum AllPlayableChampions {

        BLUE_HAIR_DUDE,
        PINK_HAIR_GIRL,
        BLOND_MOHAWK_DUDE,
        CAPE_BALDY_DUDE

    }


    public enum AllScenes {

        MENU,
        CHAMPION_SELECT,
        PLAYING,
        PAUSE,
        SETTINGS,
        MAP_EDITOR;

        public static AllScenes Current_Scene = MENU;

    }

    // here are saved objects send/received to/from server and then further copied to their respective classes if needed
    public abstract static class ServerClientConnectionCopyObjects {

        public static AllPlayerStates Current_Player_State_Shared;

        public static AllPlayableChampions PLayer_Champion_Shared;

        public static Boolean[] ArrayOfPlayerCreateSpellRequests = new Boolean[4];

        public static Point currentMousePosition = new Point();

        public static List<Spell01> listOfAllActiveSpellsCopy = new ArrayList<>();


    }
}
