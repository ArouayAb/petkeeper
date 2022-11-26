package ma.ensam.petkeeper.entities.relations;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

import ma.ensam.petkeeper.entities.Offer;
import ma.ensam.petkeeper.entities.Profile;

public class ProfileWithOffers {

    @Embedded
    public Profile profile;

    @Relation(
            entity = Offer.class,
            parentColumn = "profileId",
            entityColumn = "profileCreatorId"
    )
    public List<Offer> offers;
}
