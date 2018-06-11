package ca.judacribz.mosquitomanager.models;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DataHelper extends SQLiteOpenHelper {

    // Constants
    // ============================================================================================
    private static final int    DATABASE_VERSION = 1;
    private static final String TABLE_CATCH_BASIN = "catch_basin";

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
                    "PRIMARY KEY (" + EMAIL + ", " + SAMPLING_DATE + ")" +
            ")";

    public DataHelper(Context context) {
        super(context, "data_helper.sqlite3", null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
