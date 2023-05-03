package com.example.back_end_mobile_project.data.constant;

public interface MessageConstant {

    interface UserMessage{

        String DATA_CREATE_USER_COMPLETE = "insert user complete !";

        String DATA_CREATE_USER_FAILURE = "insert user failure !";

        String DATA_GET_ALL_USER_COMPLETE = "get user list complete !";

        String DATA_GET_ALL_USER_FAILURE = "get user list failure !";

        String DATA_GET_ONE_USER_COMPLETE = "get user complete !";

        String DATA_GET_ONE_USER_FAILURE = "get user failure !";

        String DATA_SIGN_IN_COMPLETE = "sign in complete !";

        String DATA_SIGN_IN_FAILURE = "sign in failure";

        String DATA_REFRESH_TOKEN_COMPLETE = "sign in complete !";

        String DATA_REFRESH_TOKEN_FAILURE = "sign in failure";

    }

    interface HabitMessage{

        String DATA_GET_HABIT_LIST_BY_USERNAME_COMPLETE = "get habit list by username complete !";

        String DATA_GET_HABIT_LIST_BY_USERNAME_FAILURE = "get habit list by username failure !";

        String DATA_SYNCHRONIZE_HABIT_COMPLETE = "synchronize habit complete !";

        String DATA_SYNCHRONIZE_HABIT_FAILURE = "synchronize habit failure !";

    }

    interface HabitInWeekMessage{

        String DATA_GET_HABIT_IN_WEEK_LIST_BY_USERNAME_COMPLETE = "get habit in week list by username complete !";

        String DATA_GET_HABIT_IN_WEEK_LIST_BY_USERNAME_FAILURE = "get habit in week list by username failure !";

        String DATA_SYNCHRONIZE_HABIT_IN_WEEK_COMPLETE = "synchronize habit in week complete !";

        String DATA_SYNCHRONIZE_HABIT_IN_WEEK_FAILURE = "synchronize habit in week failure !";

    }

    interface HistoryMessage{

        String DATA_GET_HISTORY_BY_USERNAME_COMPLETE = "get habit histories by username complete !";

        String DATA_GET_HISTORY_BY_USERNAME_FAILURE = "get histories by username failure !";

        String DATA_SYNCHRONIZE_HISTORY_COMPLETE = "synchronize histories complete !";

        String DATA_SYNCHRONIZE_HISTORY_FAILURE = "synchronize histories failure !";

    }

    interface RemainderMessage{

        String DATA_GET_REMAINDER_BY_USERNAME_COMPLETE = "get habit remainders by username complete !";

        String DATA_GET_REMAINDER_BY_USERNAME_FAILURE = "get remainders by username failure !";

        String DATA_SYNCHRONIZE_REMAINDER_COMPLETE = "synchronize remainders complete !";

        String DATA_SYNCHRONIZE_REMAINDER_FAILURE = "synchronize remainders failure !";

    }

}
