package ma.ensam.petkeeper.entities.relations;

import androidx.room.Relation;

import java.util.List;

import ma.ensam.petkeeper.entities.Offer;
import ma.ensam.petkeeper.entities.Profile;

public class ProfileWithOffers {

    public Profile profile;

    @Relation(
            parentColumn = "id",
            entityColumn = "profileCreatorId"
    )

    public List<Offer> offers;
}
