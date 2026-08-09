package com.example.boliviaslang.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [Word::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun wordDao(): WordDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "bolivia_slang_database"
                )
                    // Precarga los datos de ejemplo la primera vez que se crea la BD.
                    .addCallback(object : RoomDatabase.Callback() {
                        override fun onCreate(db: androidx.sqlite.db.SupportSQLiteDatabase) {
                            super.onCreate(db)
                            scope.launch(Dispatchers.IO) {
                                getDatabase(context, scope).wordDao().insertAll(SampleData.words)
                            }
                        }
                    })
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}

/**
 * Modelo de datos de ejemplo solicitado: Chala, Jenecherú, Yapa y Cambita.
 * Estos son los datos "semilla" que se insertan la primera vez que arranca la app.
 */
object SampleData {
    val words = listOf(
        Word(
            term = "Chala",
            meaning = "Sandalia u ojota, calzado abierto e informal.",
            region = "Todo el país (más común en el Altiplano)",
            category = "Objeto / Ropa",
            exampleSentence = "Ponte tus chalas que hace calor, vamos a la plaza.",
            exampleTranslation = "Ponte tus sandalias porque hace calor, vamos a la plaza.",
            funFact = "La palabra viene del quechua/aimara y es de uso cotidiano en toda Bolivia."
        ),
        Word(
            term = "Jenecherú",
            meaning = "Apodo cariñoso y tradicional para referirse a Santa Cruz de la Sierra.",
            region = "Santa Cruz",
            category = "Expresión / Identidad regional",
            exampleSentence = "Volví a Jenecherú después de tres años y todo cambió un montón.",
            exampleTranslation = "Volví a Santa Cruz después de tres años y todo cambió mucho.",
            funFact = "Jenechero significa 'gente venida de lejos' en un idioma indígena local."
        ),
        Word(
            term = "Yapa",
            meaning = "Un extra o adición gratuita que el vendedor da al comprador, como agradecimiento.",
            region = "Todo el país",
            category = "Costumbre / Comercio",
            exampleSentence = "Cómprame un kilo de tomate y no te olvides de pedir la yapa.",
            exampleTranslation = "Cómprame un kilo de tomate y no te olvides de pedir el regalito extra.",
            funFact = "Es una costumbre andina muy arraigada en los mercados populares."
        ),
        Word(
            term = "Cambita",
            meaning = "Forma cariñosa y coloquial de llamar a una persona de Santa Cruz (cambo/camba).",
            region = "Santa Cruz",
            category = "Expresión / Gentilicio",
            exampleSentence = "Mi vecino es bien cambita, siempre habla con ese acento tan alegre.",
            exampleTranslation = "Mi vecino es bien cruceño, siempre habla con ese acento tan alegre.",
            funFact = "'Camba' es el gentilicio informal para las personas del oriente boliviano."
        )
    )
}
