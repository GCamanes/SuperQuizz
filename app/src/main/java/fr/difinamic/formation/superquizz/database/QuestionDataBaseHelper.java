package fr.difinamic.formation.superquizz.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import fr.difinamic.formation.superquizz.api.APIClient;
import fr.difinamic.formation.superquizz.model.Question;
import fr.difinamic.formation.superquizz.model.TypeQuestion;

public class QuestionDataBaseHelper extends SQLiteOpenHelper {

    private static QuestionDataBaseHelper sInstance;

    // Database Info
    private static final String DATABASE_NAME = "questionDatabase";
    private static final int DATABASE_VERSION = 1;

    // Table Names
    private static final String TABLE_QCM = "qcm";

    // QCM Table Columns
    private static final String KEY_QCM_ID = "question_id";
    private static final String KEY_QCM_LABEL = "label";
    private static final String KEY_QCM_ANSWER1 = "answer1";
    private static final String KEY_QCM_ANSWER2 = "answer2";
    private static final String KEY_QCM_ANSWER3 = "answer3";
    private static final String KEY_QCM_ANSWER4 = "answer4";
    private static final String KEY_QCM_GOOD_ANSWER = "good_answer";
    private static final String KEY_QCM_USER_ANSWER = "user_answer";
    private static final String KEY_QCM_USER_ANSWER_TIME = "user_answer_time";

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Query of table creation
        String CREATE_QCM_TABLE = "CREATE TABLE " + TABLE_QCM +
                "("+
                    KEY_QCM_ID +" INTEGER PRIMARY KEY AUTOINCREMENT,"+
                    KEY_QCM_LABEL + " VARCHAR(100)," +
                    KEY_QCM_ANSWER1 +" VARCHAR(50) NOT NULL,"+
                    KEY_QCM_ANSWER2 +" VARCHAR(50) NOT NULL,"+
                    KEY_QCM_ANSWER3 +" VARCHAR(50) NOT NULL,"+
                    KEY_QCM_ANSWER4 +" VARCHAR(50) NOT NULL,"+
                    KEY_QCM_GOOD_ANSWER +" VARCHAR(50) NOT NULL,"+
                    KEY_QCM_USER_ANSWER +" VARCHAR(50),"+
                    KEY_QCM_USER_ANSWER_TIME + " INTEGER"+
                ")";
        // Execute the query of table creation
        db.execSQL(CREATE_QCM_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion != newVersion) {
            // Simplest implementation is to drop all old tables and recreate them
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_QCM);
            onCreate(db);
        }
    }

    public List<Question> getAllQuestions() {
        List<Question> listQuestions = new ArrayList<Question>();

        String QUESTIONS_SELECT_QUERY =
                String.format("SELECT * FROM %s", TABLE_QCM);

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(QUESTIONS_SELECT_QUERY, null);

        try {
            if (cursor.moveToFirst()) {
                do {
                    Question q = new Question(
                            cursor.getInt(cursor.getColumnIndex(KEY_QCM_ID)),
                            cursor.getString(cursor.getColumnIndex(KEY_QCM_LABEL)),
                            4, TypeQuestion.SIMPLE);

                    q.addProposition(cursor.getString(cursor.getColumnIndex(KEY_QCM_ANSWER1)));
                    q.addProposition(cursor.getString(cursor.getColumnIndex(KEY_QCM_ANSWER2)));
                    q.addProposition(cursor.getString(cursor.getColumnIndex(KEY_QCM_ANSWER3)));
                    q.addProposition(cursor.getString(cursor.getColumnIndex(KEY_QCM_ANSWER4)));

                    q.setBonneReponse(cursor.getString(cursor.getColumnIndex(KEY_QCM_GOOD_ANSWER)));

                    listQuestions.add(q);
                } while(cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e("ERROR SQL SELECT "+TABLE_QCM, "Error while trying to get questions from database");
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return listQuestions;
    }

    public void synchroniseDatabaseQuestions(List<Question> serverQuestions) {

        List<Question> databaseQuestions = getAllQuestions();

        // Here we will choose if we need to add or to update the question return by the server
        for (Question serverQuestion : serverQuestions) {
            boolean found = false;
            for (Question dataBaseQuestion : databaseQuestions) {
                if (serverQuestion.getId() == dataBaseQuestion.getId()) {
                    found = true;
                    break;
                }
            }

            if (found) {
                updateQuestion(serverQuestion);
               // updateUserAnswer(serverQuestion, null);
            } else {
                addQuestion(serverQuestion);
            }
        }

        // Now we want to delete the question if thy are not on the server anymore
        for (Question dataBaseQuestion : databaseQuestions) {
            boolean found = false;
            for (Question serverQuestion : serverQuestions) {
                if (serverQuestion.getId() == dataBaseQuestion.getId()) {
                    found = true;
                    break;
                }
            }

            if (!found) {
                deleteQuestion(dataBaseQuestion);
            }
        }
    }

    public int updateQuestion(Question q) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_QCM_LABEL, q.getIntitule());
        values.put(KEY_QCM_ANSWER1, q.getPropositions().get(0));
        values.put(KEY_QCM_ANSWER2, q.getPropositions().get(1));
        values.put(KEY_QCM_ANSWER3, q.getPropositions().get(2));
        values.put(KEY_QCM_ANSWER4, q.getPropositions().get(3));
        values.put( KEY_QCM_GOOD_ANSWER, q.getBonneReponse());

        // Updating profile picture url for user with that userName
        return db.update(TABLE_QCM, values, KEY_QCM_ID + " = ?",
                new String[] { String.valueOf(q.getId()) });
    }

    public void addQuestion(Question q) {
        // Create and/or open the database for writing
        SQLiteDatabase db = getWritableDatabase();

        db.beginTransaction();
        try {

            ContentValues values = new ContentValues();
            values.put(KEY_QCM_LABEL, q.getIntitule());
            values.put(KEY_QCM_ANSWER1, q.getPropositions().get(0));
            values.put(KEY_QCM_ANSWER2, q.getPropositions().get(1));
            values.put(KEY_QCM_ANSWER3, q.getPropositions().get(2));
            values.put(KEY_QCM_ANSWER4, q.getPropositions().get(3));
            values.put( KEY_QCM_GOOD_ANSWER, q.getBonneReponse());

            if (q.getId() != -1) {
                values.put(KEY_QCM_ID, q.getId());
            }

            db.insertOrThrow(TABLE_QCM, null, values);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.e("ERROR SQL CREATION "+TABLE_QCM, "Error while trying to add question to database");
        } finally {
            db.endTransaction();
        }
    }

    public void addOrUpdateQuestion(Question q) {
        // Create and/or open the database for writing
        SQLiteDatabase db = getWritableDatabase();

        db.beginTransaction();

        try {
            ContentValues values = new ContentValues();
            values.put(KEY_QCM_ID, q.getId());

            String QUESTIONS_SELECT_QUERY =
                    String.format("SELECT * FROM %s WHERE %s = ?", TABLE_QCM, KEY_QCM_ID);

            Cursor cursor = db.rawQuery(QUESTIONS_SELECT_QUERY, new String[] { String.valueOf(q.getId()) });

            if (cursor.moveToFirst()) {
                updateQuestion(q);
            } else {
                addQuestion(q);
            }
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.e("ERROR SQL CREATION "+TABLE_QCM, "Error while trying to add or update question to database");
        } finally {
            db.endTransaction();
        }
    }

    public void deleteQuestion(Question q) {
        // Create and/or open the database for writing
        SQLiteDatabase db = getWritableDatabase();

        db.beginTransaction();
        try {
            db.delete(TABLE_QCM, KEY_QCM_ID + " = ?", new String[] { String.valueOf(q.getId()) });
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.e("ERROR SQL CREATION "+TABLE_QCM, "Error while trying to delete question from database");
        } finally {
            db.endTransaction();
        }
    }

    public void updateUserAnswer(Question q, String userAnswer, int elapsedTime) {
        // Create and/or open the database for writing
        SQLiteDatabase db = getWritableDatabase();

        db.beginTransaction();

        try {
            ContentValues values = new ContentValues();
            values.put(KEY_QCM_ID, q.getId());

            String QUESTIONS_SELECT_QUERY =
                    String.format("SELECT * FROM %s WHERE %s = ?", TABLE_QCM, KEY_QCM_ID);

            Cursor cursor = db.rawQuery(QUESTIONS_SELECT_QUERY, new String[] { String.valueOf(q.getId()) });

            if (cursor.moveToFirst()) {
                values.put( KEY_QCM_USER_ANSWER, userAnswer);
                values.put( KEY_QCM_USER_ANSWER_TIME, elapsedTime);

                db.update(TABLE_QCM, values, KEY_QCM_ID + " = ?",
                        new String[] { String.valueOf(q.getId()) });
            }
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.e("ERROR SQL CREATION "+TABLE_QCM, "Error while trying to save user answer to database");
        } finally {
            db.endTransaction();
        }
    }

    public String getUserAnswer(Question q) {
        boolean result = false;
        String userAnswer = "...........";

        SQLiteDatabase db = getReadableDatabase();

        db.beginTransaction();

        try {
            ContentValues values = new ContentValues();
            values.put(KEY_QCM_ID, q.getId());

            String QUESTIONS_SELECT_QUERY =
                    String.format("SELECT * FROM %s WHERE %s = ?", TABLE_QCM, KEY_QCM_ID);

            Cursor cursor = db.rawQuery(QUESTIONS_SELECT_QUERY, new String[] { String.valueOf(q.getId()) });

            if (cursor.moveToFirst()) {
                userAnswer = cursor.getString(cursor.getColumnIndex(KEY_QCM_USER_ANSWER));
            }
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.e("ERROR SQL CREATION "+TABLE_QCM, "Error while trying to save user answer to database");
        } finally {
            db.endTransaction();
        }
        return userAnswer;
    }

    public int getUserAnswerTime(Question q) {
        boolean result = false;
        int userAnswerTime = 0;

        SQLiteDatabase db = getReadableDatabase();

        db.beginTransaction();

        try {
            ContentValues values = new ContentValues();
            values.put(KEY_QCM_ID, q.getId());

            String QUESTIONS_SELECT_QUERY =
                    String.format("SELECT * FROM %s WHERE %s = ?", TABLE_QCM, KEY_QCM_ID);

            Cursor cursor = db.rawQuery(QUESTIONS_SELECT_QUERY, new String[] { String.valueOf(q.getId()) });

            if (cursor.moveToFirst()) {
                userAnswerTime = cursor.getInt(cursor.getColumnIndex(KEY_QCM_USER_ANSWER_TIME));
            }
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.e("ERROR SQL CREATION "+TABLE_QCM, "Error while trying to save user answer to database");
        } finally {
            db.endTransaction();
        }
        return userAnswerTime;
    }

    public void resetUserAnswers() {
        List<Question> questions = getAllQuestions();

        for (Question q: questions) {
            updateUserAnswer(q, null, 0);
        }
    }

    public static synchronized QuestionDataBaseHelper getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new QuestionDataBaseHelper(context);
        }

        return sInstance;
    }

    private QuestionDataBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
}
