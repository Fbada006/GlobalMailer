package com.droidafricana.globalmail.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.droidafricana.globalmail.database.business.BusinessArticleDatabaseDao
import com.droidafricana.globalmail.database.business.BusinessDatabaseArticle
import com.droidafricana.globalmail.database.entertainment.EntDatabaseArticle
import com.droidafricana.globalmail.database.entertainment.EntertainmentArticleDatabaseDao
import com.droidafricana.globalmail.database.favs.FavsArticleDatabaseDao
import com.droidafricana.globalmail.database.favs.FavsDatabaseArticle
import com.droidafricana.globalmail.database.general.GeneralArticleDatabaseDao
import com.droidafricana.globalmail.database.general.GeneralDatabaseArticle
import com.droidafricana.globalmail.database.health.HealthArticleDatabaseDao
import com.droidafricana.globalmail.database.health.HealthDatabaseArticle
import com.droidafricana.globalmail.database.science.ScienceArticleDatabaseDao
import com.droidafricana.globalmail.database.science.ScienceDatabaseArticle
import com.droidafricana.globalmail.database.sports.SportsArticleDatabaseDao
import com.droidafricana.globalmail.database.sports.SportsDatabaseArticle
import com.droidafricana.globalmail.database.technology.TechnologyArticleDatabaseDao
import com.droidafricana.globalmail.database.technology.TechnologyDatabaseArticle

@Database(entities = [GeneralDatabaseArticle::class, SportsDatabaseArticle::class,
    EntDatabaseArticle::class, TechnologyDatabaseArticle::class,
    BusinessDatabaseArticle::class, HealthDatabaseArticle::class, ScienceDatabaseArticle::class,
    FavsDatabaseArticle::class],
        version = 1, exportSchema = false)
abstract class ArticleDatabase : RoomDatabase() {

    //DAOs for accessing the data in the respective tables
    abstract val sportsArticleDao: SportsArticleDatabaseDao
    abstract val entArticleDao: EntertainmentArticleDatabaseDao
    abstract val technologyArticleDao: TechnologyArticleDatabaseDao
    abstract val busArticleDao: BusinessArticleDatabaseDao
    abstract val healthArticleDao: HealthArticleDatabaseDao
    abstract val scienceArticleDao: ScienceArticleDatabaseDao
    abstract val generalArticleDao: GeneralArticleDatabaseDao
    abstract val favsArticleDao: FavsArticleDatabaseDao

    companion object {
        @Volatile
        private var INSTANCE: ArticleDatabase? = null

        //Singleton for the DB
        fun getDatabaseInstance(context: Context): ArticleDatabase {
            // Multiple threads can ask for the database at the same time, ensure we only initialize
            // it once by using synchronized. Only one thread may enter a synchronized block at a
            // time.
            synchronized(this) {
                // Copy the current value of INSTANCE to a local variable so Kotlin can smart cast.
                // Smart cast is only available to local variables.
                var instance = INSTANCE
                // If instance is `null` make a new database instance.
                if (instance == null) {
                    instance = Room.databaseBuilder(
                            context.applicationContext,
                            ArticleDatabase::class.java,
                            "articles_database")
                            .fallbackToDestructiveMigration()
                            .build()
                    // Assign INSTANCE to the newly created database.
                    INSTANCE = instance
                }
                // Return instance; smart cast to be non-null.
                return instance
            }
        }
    }
}