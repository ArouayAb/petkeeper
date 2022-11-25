package ma.ensam.petkeeper.entities.relations;

import androidx.room.Embedded;
import androidx.room.Relation;

import ma.ensam.petkeeper.entities.Offer;
import ma.ensam.petkeeper.entities.Profile;

public class OfferWithProfile {
    @Embedded
    public Profile profile;

    @Relation(
            entity = Offer.class,
            parentColumn = "id",
            entityColumn = "profileCreatorId"
    )

    public Offer offer;
}
