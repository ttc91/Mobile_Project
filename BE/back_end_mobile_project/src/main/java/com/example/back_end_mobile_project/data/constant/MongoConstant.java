package com.example.back_end_mobile_project.data.constant;

public interface MongoConstant {

    String DATABASE_NAME = "habit_tracker";

    String DATABASE_HOST = "mongodb://localhost:27017/";

    String DATABASE_CONNECT = DATABASE_HOST + DATABASE_HOST;

    String BASE_PACKAGE = "com.example.back_end_mobile_project.data.remote.repository";

    interface CollectionName{
        String HABIT_COLLECTION = "habit";
        String HABIT_IN_WEEK_COLLECTION = "habit_in_week";
        String HISTORY_COLLECTION = "history";
        String REMAINDER_COLLECTION = "remainder";
        String USER_COLLECTION = "user";
        String USER_REFERENCE_HABITS = "user_ref_habits";
    }

}
