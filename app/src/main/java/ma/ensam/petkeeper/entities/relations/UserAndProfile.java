package ma.ensam.petkeeper.entities.relations;

import androidx.room.Embedded;
import androidx.room.Relation;

import ma.ensam.petkeeper.entities.Profile;
import ma.ensam.petkeeper.entities.User;

public class UserAndProfile {

    @Embedded
    public Profile profile;

    @Relation(
            entity = User.class,
            parentColumn = "userId",
            entityColumn = "id"
    )
    public User user;

}