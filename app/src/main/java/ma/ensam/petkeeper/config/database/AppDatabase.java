package ma.ensam.petkeeper.config.database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.Date;

import ma.ensam.petkeeper.daos.OfferDao;
import ma.ensam.petkeeper.daos.ProfileDao;
import ma.ensam.petkeeper.daos.ReviewDao;
import ma.ensam.petkeeper.daos.UserDao;
import ma.ensam.petkeeper.entities.Offer;
import ma.ensam.petkeeper.entities.Profile;
import ma.ensam.petkeeper.entities.Review;
import ma.ensam.petkeeper.entities.User;
import ma.ensam.petkeeper.entities.enums.OfferType;
import ma.ensam.petkeeper.entities.enums.PetSpecies;
import ma.ensam.petkeeper.utils.Converters;


@Database(entities = {User.class, Profile.class, Offer.class, Review.class}, version = 1)
@TypeConverters({Converters.class})
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase instance;

    public abstract UserDao userDao();

    public abstract ProfileDao profileDao();

    public abstract OfferDao offerDao();

    public abstract ReviewDao reviewDao();

    public static synchronized AppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context, AppDatabase.class, "petkeeper-db")
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallBack)
                    .build();
        }
        return instance;
    }

    private static final RoomDatabase.Callback roomCallBack = new Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);

            new Thread(() -> {
                instance.userDao().insertAll(
                        new User("user1@gmail.com", "password1"),
                        new User("user2@gmail.com", "password2")
                );

                instance.profileDao().insertAll(
                        new Profile("fullname1", "0610203040",
                                "user1 desc", "pic_url",
                                "marrakesh, morocco", 1),
                        new Profile("fullname2", "0611203040",
                                "user2 desc", "pic_url",
                                "marrakesh, morocco", 2)
                );

                instance.offerDao().insertAll(
                        new Offer(OfferType.KEEPER, PetSpecies.CAT, "cats",
                        "all cats welcome : )", "img_url",new Date(),
                                new Date(), new Date(), 1),
                        new Offer(OfferType.OWNER, PetSpecies.DOG, "d o g",
                                "big  m a l e  sleepy dog", "img_url",new Date(),
                                new Date(), new Date(), 1)
                );

                instance.reviewDao().insertAll(
                        new Review(1,2, 1, "very bad"),
                        new Review(2, 1, 1, "very bad too >:(")
                );

            }).start();
        }
    };
}
