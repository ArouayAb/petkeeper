package ma.ensam.petkeeper.config.database;

import android.content.Context;
import android.os.Environment;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.Date;

import ma.ensam.petkeeper.daos.OfferDao;
import ma.ensam.petkeeper.daos.ProfileDao;
import ma.ensam.petkeeper.daos.RelationDao;
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

    public abstract RelationDao relationDao();

    public static synchronized AppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context, AppDatabase.class,
                            "petkeeper-db")
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
                        new User("user2@gmail.com", "password2"),
                        new User("user3@gmail.com", "password3"),
                        new User("user4@gmail.com", "password4"),
                        new User("user5@gmail.com", "password5")
                );

                instance.profileDao().insertAll(
                        new Profile("fullname1", "0610203040",
                                "user1 desc", Environment.getExternalStorageDirectory().getPath() + "/Download/LnRrYf6e_400x400.jpg",
                                "morocco", "marrakech", 1),
                        new Profile("fullname2", "0611203040",
                                "user2 desc", Environment.getExternalStorageDirectory().getPath() + "/Download/LnRrYf6e_400x400.jpg",
                                "morocco", "marrakech", 2),
                        new Profile("fullname2", "0611203041",
                                "user2 desc", Environment.getExternalStorageDirectory().getPath() + "/Download/LnRrYf6e_400x400.jpg",
                                "morocco", "marrakech", 3),
                        new Profile("fullname2", "0611203042",
                                "user2 desc", Environment.getExternalStorageDirectory().getPath() + "/Download/LnRrYf6e_400x400.jpg",
                                "morocco", "marrakech", 4),
                        new Profile("fullname2", "0611203043",
                                "user2 desc", Environment.getExternalStorageDirectory().getPath() + "/Download/LnRrYf6e_400x400.jpg",
                                "morocco", "marrakech", 5)
                );

                instance.offerDao().insertAll(
                        new Offer(OfferType.KEEPER, PetSpecies.CAT, "cats",
                        "All cats welcome : )", "img_url",new Date(),
                                new Date(), new Date(), 1),
                        new Offer(OfferType.KEEPER, PetSpecies.BIRD, "2 birds",
                                "Any one can hold my 2 couple birds : )", "img_url",new Date(),
                                new Date(), new Date(), 1),
                        new Offer(OfferType.KEEPER, PetSpecies.DOG, "All dogs welcome",
                                "I have a special place for dogs )", "img_url",new Date(),
                                new Date(), new Date(), 1),
                        new Offer(OfferType.OWNER, PetSpecies.DOG, "d o g",
                                "big  male  sleepy dog", "img_url",new Date(1669419983723L),
                                new Date(1669429383723L), new Date(), 1),
                        new Offer(OfferType.OWNER, PetSpecies.DOG, "d o g",
                                "big  male  sleepy dog", "img_url",new Date(1669419383723L),
                                new Date(1669419983723L), new Date(), 1),
                        new Offer(OfferType.OWNER, PetSpecies.TURTLE, "Turtle kind",
                                "Asian turtle", "img_url",new Date(),
                                new Date(), new Date(), 1)

                );

                instance.reviewDao().insertAll(
                        new Review(1,2, 1, "very bad"),
                        new Review(3,1, 2, "mildly bad"),
                        new Review(4,1, 3, "mediocre"),
                        new Review(5,1, 4, "good"),
                        new Review(2, 1, 1, "very bad too >:(")
                );

            }).start();
        }
    };
}

