package entities.spells.basicspells;

import datatransferobjects.Spell01DTO;
import entities.playercharacters.LocalPlayer;
import entities.playercharacters.OnlinePlayer;
import main.AssetLoader;
import main.EnumContainer;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.Optional;

public class Ultimate extends QSpell {

    public static long ULTIMATESPELLCOOLDOWN = 3000;

    public Ultimate(LocalPlayer playerCastingThisSpell) {
        super(playerCastingThisSpell);



    }

    public Ultimate(Spell01DTO spell01DTO) {
        super(spell01DTO);
    }

    @Override
    protected void getSpellSprites(Optional<OnlinePlayer> onlinePlayer) {
        BufferedImage[] spellAssets;
        if (onlinePlayer.isPresent()) {
            System.out.println(onlinePlayer.get().onlinePlayerChampion.name());
            for (EnumContainer.AllQspellStates element : EnumContainer.AllQspellStates.values()) {
                switch (element) {
                    case Q_SPELL_START -> {
                        if (onlinePlayer.get().onlinePlayerChampion == EnumContainer.AllPlayableChampions.BLUE_HAIR_DUDE)
                            spellAssets = AssetLoader.ULTWaterSpellCastStart;
                        else if (onlinePlayer.get().onlinePlayerChampion == EnumContainer.AllPlayableChampions.PINK_HAIR_GIRL)
                            spellAssets = AssetLoader.ULTFireSpellCastStart;
                        else if (onlinePlayer.get().onlinePlayerChampion == EnumContainer.AllPlayableChampions.BLOND_MOHAWK_DUDE)
                            spellAssets = AssetLoader.ULTRockSpellCastStart;
                        else
                            spellAssets = AssetLoader.ULTWindSpellCastStart;
                    }
                    case Q_SPELL_FLYING -> {
                        if (onlinePlayer.get().onlinePlayerChampion == EnumContainer.AllPlayableChampions.BLUE_HAIR_DUDE)
                            spellAssets = AssetLoader.ULTWaterSpellCastFlying;
                        else if (onlinePlayer.get().onlinePlayerChampion == EnumContainer.AllPlayableChampions.PINK_HAIR_GIRL)
                            spellAssets = AssetLoader.ULTFireSpellCastFlying;
                        else if (onlinePlayer.get().onlinePlayerChampion == EnumContainer.AllPlayableChampions.BLOND_MOHAWK_DUDE)
                            spellAssets = AssetLoader.ULTRockSpellCastFlying;
                        else
                            spellAssets = AssetLoader.ULTWindSpellCastFlying;
                    }
                    case Q_SPELL_END -> {
                        if (onlinePlayer.get().onlinePlayerChampion == EnumContainer.AllPlayableChampions.BLUE_HAIR_DUDE)
                            spellAssets = AssetLoader.ULTWaterSpellCastEnd;
                        else if (onlinePlayer.get().onlinePlayerChampion == EnumContainer.AllPlayableChampions.PINK_HAIR_GIRL)
                            spellAssets = AssetLoader.ULTFireSpellCastEnd;
                        else if (onlinePlayer.get().onlinePlayerChampion == EnumContainer.AllPlayableChampions.BLOND_MOHAWK_DUDE)
                            spellAssets = AssetLoader.ULTRockSpellCastEnd;
                        else
                            spellAssets = AssetLoader.ULTWindSpellCastEnd;
                    }
                    default -> spellAssets = AssetLoader.QSpellViolet;
                }

                BufferedImage[] spellAssetsRotated = new BufferedImage[NUMBER_OF_SPRITES];
                Graphics2D g2d;
                for (int i = 0; i < spellAssets.length; i++) {
                    BufferedImage spellAsset = new BufferedImage(spellAssets[i].getWidth(), spellAssets[i].getHeight(), BufferedImage.TYPE_INT_ARGB);
                    g2d = spellAsset.createGraphics();

                    AffineTransform transform = new AffineTransform();
                    transform.rotate(spriteAngle, (double) spellAsset.getWidth() / 2, (double) spellAsset.getHeight() / 2);
                    g2d.setTransform(transform);
                    g2d.drawImage(spellAssets[i], 0, 0, null);
                    spellAssetsRotated[i] = spellAsset;
                }
                if (element == EnumContainer.AllQspellStates.Q_SPELL_START) spellSprites_START = spellAssetsRotated;
                else if (element == EnumContainer.AllQspellStates.Q_SPELL_FLYING)
                    spellSprites_FLYING = spellAssetsRotated;
                else spellSprites_END = spellAssetsRotated;
            }
        } else {
            System.out.println("Online player not present, cant get Sprites");

        }
    }

    @Override
    protected void getSpellSprites(LocalPlayer localPlayer) {
        BufferedImage[] spellAssets;
        for (EnumContainer.AllQspellStates element : EnumContainer.AllQspellStates.values()) {
            switch (element) {
                case Q_SPELL_START -> {
                    if (localPlayer.localPlayerChampion == EnumContainer.AllPlayableChampions.BLUE_HAIR_DUDE)
                        spellAssets = AssetLoader.ULTWaterSpellCastStart;
                    else if (localPlayer.localPlayerChampion == EnumContainer.AllPlayableChampions.PINK_HAIR_GIRL)
                        spellAssets = AssetLoader.ULTFireSpellCastStart;
                    else if (localPlayer.localPlayerChampion == EnumContainer.AllPlayableChampions.BLOND_MOHAWK_DUDE)
                        spellAssets = AssetLoader.ULTRockSpellCastStart;
                    else
                        spellAssets = AssetLoader.ULTWindSpellCastStart;
                }
                case Q_SPELL_FLYING -> {
                    if (localPlayer.localPlayerChampion == EnumContainer.AllPlayableChampions.BLUE_HAIR_DUDE)
                        spellAssets = AssetLoader.ULTWaterSpellCastFlying;
                    else if (localPlayer.localPlayerChampion == EnumContainer.AllPlayableChampions.PINK_HAIR_GIRL)
                        spellAssets = AssetLoader.ULTFireSpellCastFlying;
                    else if (localPlayer.localPlayerChampion == EnumContainer.AllPlayableChampions.BLOND_MOHAWK_DUDE)
                        spellAssets = AssetLoader.ULTRockSpellCastFlying;
                    else
                        spellAssets = AssetLoader.ULTWindSpellCastFlying;
                }
                case Q_SPELL_END -> {
                    if (localPlayer.localPlayerChampion == EnumContainer.AllPlayableChampions.BLUE_HAIR_DUDE)
                        spellAssets = AssetLoader.ULTWaterSpellCastEnd;
                    else if (localPlayer.localPlayerChampion == EnumContainer.AllPlayableChampions.PINK_HAIR_GIRL)
                        spellAssets = AssetLoader.ULTFireSpellCastEnd;
                    else if (localPlayer.localPlayerChampion == EnumContainer.AllPlayableChampions.BLOND_MOHAWK_DUDE)
                        spellAssets = AssetLoader.ULTRockSpellCastEnd;
                    else
                        spellAssets = AssetLoader.ULTWindSpellCastEnd;
                }
                default -> spellAssets = AssetLoader.QSpellViolet;
            }

            BufferedImage[] spellAssetsRotated = new BufferedImage[NUMBER_OF_SPRITES];
            spriteAngle = Math.atan2(normalizedVectorY, normalizedVectorX);
            Graphics2D g2d;
            for (int i = 0; i < spellAssets.length; i++) {
                BufferedImage spellAsset = new BufferedImage(spellAssets[i].getWidth(), spellAssets[i].getHeight(), BufferedImage.TYPE_INT_ARGB);
                g2d = spellAsset.createGraphics();

                AffineTransform transform = new AffineTransform();
                transform.rotate(spriteAngle, (double) spellAsset.getWidth() / 2, (double) spellAsset.getHeight() / 2);
                g2d.setTransform(transform);
                g2d.drawImage(spellAssets[i], 0, 0, null);
                spellAssetsRotated[i] = spellAsset;
            }
            if (element == EnumContainer.AllQspellStates.Q_SPELL_START) spellSprites_START = spellAssetsRotated;
            else if (element == EnumContainer.AllQspellStates.Q_SPELL_FLYING) spellSprites_FLYING = spellAssetsRotated;
            else spellSprites_END = spellAssetsRotated;
        }
    }


}
