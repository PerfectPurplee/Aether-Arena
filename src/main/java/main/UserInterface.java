package main;

import entities.playercharacters.LocalPlayer;

import java.awt.image.BufferedImage;

public class UserInterface {

    public BufferedImage currentQSpellICON;
    public BufferedImage currentWSpellICON;
    public BufferedImage currentESpellICON;
    public BufferedImage currentRSpellICON;
    //    There is only one dash icon for all characters
    public BufferedImage dashICON;

    EnumContainer.AllPlayableChampions currentlyPlayedChampion;

    public UserInterface(LocalPlayer localPlayer) {
        setCurrentlyPlayedChampionForUI(localPlayer.localPlayerChampion);
        setCurrentIconsForDrawingInterface(currentlyPlayedChampion);
    }


    public void setCurrentIconsForDrawingInterface(EnumContainer.AllPlayableChampions currentlyPlayedChampion) {
        currentQSpellICON = AssetLoader.QSpellICON[currentlyPlayedChampion.ordinal()];
        currentWSpellICON = AssetLoader.QSpellICON[currentlyPlayedChampion.ordinal()];
        currentESpellICON = AssetLoader.ESpellICON[currentlyPlayedChampion.ordinal()];
        currentRSpellICON = AssetLoader.UltimateSpellICON[currentlyPlayedChampion.ordinal()];
        dashICON = AssetLoader.DashSpellICON;
    }

    public void setCurrentlyPlayedChampionForUI(EnumContainer.AllPlayableChampions currentlyPlayedChampion) {
        this.currentlyPlayedChampion = currentlyPlayedChampion;
    }


}
