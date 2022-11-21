package ma.ensam.petkeeper.entities.relations;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

import ma.ensam.petkeeper.entities.Profile;

public class ProfileWithReviewsOnIt {
    @Embedded
    public Profile profile;

    @Relation(
            parentColumn = "id",
            entityColumn = "reviewerProfileId"
    )

    public List<Profile> reviewers;
}
