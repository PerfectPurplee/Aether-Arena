package datatransferobjects;

import entities.spells.basicspells.Spell01;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Spell01DTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    public float spellPosXWorld, spellPosYWorld;
    public float normalizedVectorX;
    public float normalizedVectorY;

    public int spellID;
    public int spellCasterClientID;

    public static List<Spell01DTO> listOfAllSpell01DTO = new ArrayList<>();


    public Spell01DTO(float spellPosXWorld, float spellPosYWorld,
                      float normalizedVectorX, float normalizedVectorY,
                      int spellID, int spellCasterClientID) {
        this.spellPosXWorld = spellPosXWorld;
        this.spellPosYWorld = spellPosYWorld;
        this.normalizedVectorX = normalizedVectorX;
        this.normalizedVectorY = normalizedVectorY;
        this.spellID = spellID;
        this.spellCasterClientID = spellCasterClientID;

    }

    public Spell01DTO(Spell01 spell01) {


    }
}

