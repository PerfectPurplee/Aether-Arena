package datatransferobjects;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Spell01DTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    public double spriteAngle;
    public float spellPosXWorld, spellPosYWorld;
    public float normalizedVectorX;
    public float normalizedVectorY;

    public int spellID;
    public final int spellCasterClientID;

    public Spell01DTO(float spellPosXWorld, float spellPosYWorld,
                      float normalizedVectorX, float normalizedVectorY,
                      int spellID, int spellCasterClientID, double spriteAngle) {
        this.spellCasterClientID = spellCasterClientID;
        this.spellPosXWorld = spellPosXWorld;
        this.spellPosYWorld = spellPosYWorld;
        this.normalizedVectorX = normalizedVectorX;
        this.normalizedVectorY = normalizedVectorY;
        this.spellID = spellID;
        this.spriteAngle = spriteAngle;

    }

}

