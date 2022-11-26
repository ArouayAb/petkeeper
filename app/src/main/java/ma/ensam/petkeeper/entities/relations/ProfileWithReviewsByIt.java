package ma.ensam.petkeeper.entities.relations;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

import ma.ensam.petkeeper.entities.Profile;
import ma.ensam.petkeeper.entities.Review;

public class ProfileWithReviewsByIt {

    @Embedded public Profile profile;

    @Relation(
            parentColumn = "profileId",
            entityColumn = "revieweeProfileId"
    )

    public List<Review> reviews;
}
