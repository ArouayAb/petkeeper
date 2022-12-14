package ma.ensam.petkeeper.entities.relations;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

import ma.ensam.petkeeper.entities.Profile;
import ma.ensam.petkeeper.entities.Review;

public class ProfileWithReviewsOnIt {
    @Embedded
    public Review review;

    @Relation(
            entity = Profile.class,
            parentColumn = "reviewerProfileId",
            entityColumn = "profileId"
    )

    public Profile profile;

}
