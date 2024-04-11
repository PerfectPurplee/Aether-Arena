package entities.spells.basicspells;

import datatransferobjects.Spell01DTO;
import entities.playercharacters.LocalPlayer;
import entities.playercharacters.OnlinePlayer;
import main.AssetLoader;

import java.awt.image.BufferedImage;
import java.util.Optional;

public class ESpell extends QSpell {
    public ESpell(LocalPlayer playerCastingThisSpell) {
        super(playerCastingThisSpell);


    }

    public ESpell(Spell01DTO spell01DTO) {
        super(spell01DTO);
    }

    @Override
    protected void getSpellSprites(LocalPlayer localPlayer) {
        super.getSpellSprites(localPlayer);
    }

    @Override
    protected void getSpellSprites(Optional<OnlinePlayer> onlinePlayer) {

        if (onlinePlayer.isPresent()) {
            System.out.println(onlinePlayer.get().onlinePlayerChampion.name());
            switch (onlinePlayer.get().onlinePlayerChampion) {
                case BLUE_HAIR_DUDE -> {
                    spellSprites_FLYING = AssetLoader.ESpellWaterGeyserCastEnd;
                }
                case PINK_HAIR_GIRL -> {
                }
                case BLOND_MOHAWK_DUDE -> {
                }
                case CAPE_BALDY_DUDE -> {
                }

            }

        }
    }
}
