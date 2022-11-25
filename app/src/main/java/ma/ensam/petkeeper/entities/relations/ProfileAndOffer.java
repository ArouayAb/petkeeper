package ma.ensam.petkeeper.entities.relations;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

import ma.ensam.petkeeper.entities.Offer;
import ma.ensam.petkeeper.entities.Profile;

public class ProfileAndOffer {
    @Embedded
    public Profile profile;

    @Relation(
            entity = Offer.class,
            parentColumn = "id",
            entityColumn = "profileCreatorId"
    )
    public Offer offer;
}
