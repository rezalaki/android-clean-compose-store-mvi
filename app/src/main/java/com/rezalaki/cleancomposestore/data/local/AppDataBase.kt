package com.rezalaki.cleancomposestore.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.rezalaki.cleancomposestore.data.model.db.MyComment


/**
 * At the time/date of writing this comment, we only store user comments
 * for each product, into our local database.
 *
 * So, now we have only one table named `my_comments`
 *
* */
@Database(
    entities = [MyComment::class],
    version = 1,
    exportSchema = false
)
abstract class AppDataBase : RoomDatabase() {
    abstract fun myCommentsDao(): MyCommentsDao
}