package ca.judacribz.mosquitomanager.models;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DataHelper extends SQLiteOpenHelper {

    // Constants
    // ============================================================================================
    private static final int    DATABASE_VERSION = 1;
    private static final String TABLE_CATCH_BASIN = "catch_basin";

    // Global Vars
    // ============================================================================================
    private String email;
    private Context context;

    // Column names
    private static final String EMAIL                = "email";
    private static final String SAMPLING_DATE        = "sampling_date";
    private static final String COMMUNITY            = "community";
    private static final String CB_ADDRESS           = "cb_address";
    private static final String NUMBER_OF_LARVAE     = "num_of_larvae";
    private static final String STAGE_OF_DEVELOPMENT = "stage_of_development";

    // Table create statement
    private static final String CREATE_STATEMENT =
            "CREATE TABLE " + TABLE_CATCH_BASIN + " (" +
                    EMAIL                + " varchar(100) not null," +
                    SAMPLING_DATE        + " text not null," +
                    COMMUNITY            + " text not null," +
                    CB_ADDRESS           + " varchar(100) not null," +
                    NUMBER_OF_LARVAE     + " int not null," +
                    STAGE_OF_DEVELOPMENT + " text," +
                    "PRIMARY KEY (" + EMAIL + ", " + CB_ADDRESS + ", " + SAMPLING_DATE + ")" +
            ")";

    private static final String DROP_STATEMENT = "DROP TABLE" + TABLE_CATCH_BASIN;

    public DataHelper(Context context) {
        super(context, "data_helper.sqlite3", null, DATABASE_VERSION);

        this.context = context;
        this.email = User.getInstance().getEmail();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // create the database, using CREATE SQL statement
        db.execSQL(CREATE_STATEMENT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        // delete the old database
        db.execSQL(DROP_STATEMENT);

        // re-create the database
        db.execSQL(CREATE_STATEMENT);
    }

    // CRUD functions
    // ============================================================================================
    // CREATE
    // --------------------------------------------------------------------------------------------
    /* Creates a db entry for the workout
     */
    public long addCatchBasin(CB cb) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues newValues = new ContentValues();

        email = User.getInstance().getEmail();

        newValues.put(EMAIL,                email);
        newValues.put(SAMPLING_DATE,        cb.getSamplingDate());
        newValues.put(COMMUNITY,            cb.getCommunity());
        newValues.put(CB_ADDRESS,           cb.getCbAddress());
        newValues.put(NUMBER_OF_LARVAE,     cb.getNumberOfLarvae());
        newValues.put(STAGE_OF_DEVELOPMENT, cb.getStageOfDevelopment());

        return db.insert(TABLE_CATCH_BASIN, null, newValues);
    }

    /* Gets all workouts in the db and returns a list of Workout objects
     */
    public ArrayList<CB> getAllCatchBasins() {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<CB> cbs = new ArrayList<>();

        String[] column      = new String[] {"rowid", SAMPLING_DATE, COMMUNITY, CB_ADDRESS, NUMBER_OF_LARVAE, STAGE_OF_DEVELOPMENT};
        String where         = EMAIL + " = ?";
        String[] whereArgs   = new String[] {email};

        Cursor cursor = db.query(TABLE_CATCH_BASIN, column, where, whereArgs, "rowid", null, null, null);


        if (cursor.moveToFirst()) {
            CB cb;
            do {
                cb = new CB(cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getInt(4), cursor.getString(5));
                cb.setId(cursor.getLong(0));
                cbs.add(cb);
            } while (cursor.moveToNext());
        }
        cursor.close();

        return cbs;
    }

}
