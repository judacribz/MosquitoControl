package ca.judacribz.mosquitomanager.firebase;

import android.app.Activity;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import ca.judacribz.mosquitomanager.models.CB;
import ca.judacribz.mosquitomanager.models.DataHelper;
import ca.judacribz.mosquitomanager.models.User;

public class Database {

    // Constants
    // --------------------------------------------------------------------------------------------
    private final static String EMAIL = "email";
    private final static String CATCH_BASIN = "catch_basin";

    private final static String DEFAULT_WORKOUTS_PATH = "default_workouts";
    private static final String USER_PATH = "users/%s";

    // --------------------------------------------------------------------------------------------


    /* Gets firebase db reference for 'users/<uid>/' */
    private static DatabaseReference getUserRef() {
        DatabaseReference userRef = null;

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            userRef = FirebaseDatabase.getInstance().getReference(
                    String.format(USER_PATH, user.getUid())
            );
        }

        return userRef;
    }

    /* Gets firebase db reference for 'users/<uid>/workouts/' */
    private static DatabaseReference getCatchBasinRef() {
        return getUserRef().child(CATCH_BASIN);
    }


    /* Sets the user email in firebase db under 'users/<uid>/email' */
    public static void setUserInfo(final Activity act) {
        final FirebaseUser fbUser = FirebaseAuth.getInstance().getCurrentUser();

        // FIREBASE: add user data
        if (fbUser != null) {
            final DatabaseReference userRef = getUserRef();

            userRef.addListenerForSingleValueEvent(new ValueEventListener() {

                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String email = fbUser.getEmail();
                    String uid = fbUser.getUid();

                    // Sets singleton User Instance variables
                    User user = User.getInstance();
                    user.setEmail(email);
                    user.setUid(uid);

                    // If newly added user
                    if (!dataSnapshot.hasChildren()) {
                        userRef
                            .child(EMAIL)
                            .setValue(email);

                    }

                    getFirebaseData(act);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });
        }
    }


    /* Gets all workouts from firebase db from 'users/<uid>/workouts/' and if the local db is
     * missing any workouts, those ones are added */
    public static void getFirebaseData(final Activity act) {

        getCatchBasinRef().addChildEventListener(new ChildEventListener() {
            DataHelper dataHelper = new DataHelper(act);

            ArrayList<CB> cbs = new ArrayList<>();

            @Override
            public void onChildAdded(DataSnapshot cbShot, String s) {
                for (DataSnapshot cbEntry : cbShot.getChildren()) {
                    dataHelper.addCatchBasin(cbEntry.getValue(CB.class));
//                    Toast.makeText(act, "" + cbEntry.getValue(), Toast.LENGTH_SHORT).show();
                }


            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
//
//    /* Adds a workout under "users/<uid>/workouts/" */
    public static void addCBFirebase(CB cb, long id) {
        getCatchBasinRef().child(cb.getSamplingDate()).child(String.valueOf(id)).setValue(cb.toMap());
    }
}