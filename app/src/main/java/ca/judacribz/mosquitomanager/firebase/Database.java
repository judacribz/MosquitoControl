package ca.judacribz.mosquitomanager.firebase;

import android.app.Activity;

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
import ca.judacribz.mosquitomanager.models.User;

public class Database {

    // Constants
    // --------------------------------------------------------------------------------------------
    private final static String EMAIL = "email";
    private final static String USERNAME = "username";
    private final static String CATCH_BASIN = "catch_basin";
    private final static String SETS = "sets";

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
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });
        }
    }

    /* Gets workouts from firebase db under 'default_workouts/' and adds it to
     * 'users/<uid>/workouts/' */
    public static void copyDefaultWorkoutsFirebase() {
        DatabaseReference defaultRef = FirebaseDatabase.getInstance().getReference(
                DEFAULT_WORKOUTS_PATH
        );

        defaultRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                getCatchBasinRef().setValue(dataSnapshot.getValue());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

//
//    /* Gets all workouts from firebase db from 'users/<uid>/workouts/' and if the local db is
//     * missing any workouts, those ones are added */
//    public static void getWorkoutsFirebase(final Activity act) {
//
//        getCatchBasinRef().addChildEventListener(new ChildEventListener() {
//            WorkoutHelper workoutHelper = new WorkoutHelper(act);
//
//            ArrayList<String> workoutNames = workoutHelper.getAllWorkoutNames();
//            ArrayList<Exercise> exercises;
//            ArrayList<Set> sets;
//
//            Exercise exercise;
//            Set set;
//            String workoutName;
//
//            @Override
//            public void onChildAdded(DataSnapshot workoutShot, String s) {
//
//                workoutName = workoutShot.getKey();
//                if (!workoutNames.contains(workoutName)) {
//
//                    exercises = new ArrayList<>();
//                    for (DataSnapshot exerciseShot : workoutShot.getChildren()) {
//
//                        // Add set to sets list
//                        sets = new ArrayList<>();
//                        for (DataSnapshot setShot : exerciseShot.child(SETS).getChildren()) {
//                            set = setShot.getValue(Set.class);
//
//                            if (set != null) {
//                                set.setSetNumber(Integer.valueOf(setShot.getKey()));
//
//                                sets.add(set);
//                            }
//                        }
//
//                        // Adds sets to exercise object, and add exercise to exercises list
//                        exercise = exerciseShot.getValue(Exercise.class);
//                        if (exercise != null) {
//                            exercise.setName(exerciseShot.getKey());
//                            exercise.setSets(sets);
//
//                            exercises.add(exercise);
//                        }
//                    }
//
//                    Workout workout = new Workout(workoutShot.getKey(), exercises);
//
//                    // Add workout name and exercises list to workouts list
//                    workoutHelper.addWorkout(workout);
//                }
//            }
//
//            @Override
//            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
//            }
//
//            @Override
//            public void onChildRemoved(DataSnapshot dataSnapshot) {
//            }
//
//            @Override
//            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//            }
//        });
//    }
//
//    /* Adds a workout under "users/<uid>/workouts/" */
    public static void addCBFirebase(CB cb) {
        getCatchBasinRef().push().setValue(cb.toMap());
    }
}