package br.edu.ufam.pedro.sportgo.model.banco

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import br.edu.ufam.pedro.sportgo.controller.interfac.DadosDao
import br.edu.ufam.pedro.sportgo.model.entidade.DadosLogin

@Database(entities = [DadosLogin::class], version = 1)
abstract class AppDatabase : RoomDatabase(){
    abstract fun userDao(): DadosDao
    companion object {
        fun instancia(context: Context): AppDatabase {
            return Room.databaseBuilder(
                context,
                AppDatabase::class.java,
                "libras.db"
            ).allowMainThreadQueries()
                .build()
        }
    }
}