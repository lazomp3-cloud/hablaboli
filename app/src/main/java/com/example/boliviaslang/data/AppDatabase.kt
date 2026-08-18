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
        ),
        Word(
            term = "Wistupiku",
            meaning = "Apodo que significa 'boca o pico chueco/torcido' en quechua. Hoy también es el nombre de una reconocida tradición familiar de salteñas y empanadas en Cochabamba.",
            region = "Cochabamba",
            category = "Apodo / Comida",
            exampleSentence = "Vamos a comprar salteñas donde el Wistupiku, son las mejores de la ciudad.",
            exampleTranslation = "Vamos a comprar salteñas al local del Wistupiku, son las mejores de la ciudad.",
            funFact = "El apodo nació con don José Solíz en 1939, quien empezó esta tradición junto a su esposa Elisa Lazarte."
        ),
        Word(
            term = "Colla",
            meaning = "Persona originaria o que vive en la región occidental de Bolivia (altiplano y valles andinos: La Paz, Oruro, Potosí, Cochabamba, Chuquisaca).",
            region = "Todo el país (más usado en el oriente para referirse a los del occidente)",
            category = "Expresión / Identidad regional",
            exampleSentence = "Mi papá es colla, nació en Oruro, y mi mamá es camba, de Santa Cruz.",
            exampleTranslation = "Mi papá es del occidente boliviano, nació en Oruro, y mi mamá es del oriente, de Santa Cruz.",
            funFact = "El término puede sentirse como un insulto o como un orgullo de identidad, según el contexto y quién lo use."
        ),
        Word(
            term = "Camote",
            meaning = "Estar enamorado o profundamente encaprichado con alguien.",
            region = "Todo el país",
            category = "Expresión / Amor",
            exampleSentence = "Está azul de camote por esa chica, no deja de hablar de ella.",
            exampleTranslation = "Está perdidamente enamorado de esa chica, no deja de hablar de ella.",
            funFact = "No tiene relación con el tubérculo camote; aquí es puramente una expresión de amor o enamoramiento."
        ),
        Word(
            term = "Trucho",
            meaning = "Algo falso, de mala calidad o una imitación de lo original.",
            region = "Todo el país",
            category = "Expresión / Cotidiano",
            exampleSentence = "¡Pucha! Me compré un reloj trucho en el mercado.",
            exampleTranslation = "¡Qué mal! Me compré un reloj falso en el mercado.",
            funFact = "Es una palabra prestada del lunfardo argentino que se volvió muy común en el habla boliviana diaria."
        ),
        Word(
            term = "Alalay",
            meaning = "Interjección para expresar que se tiene mucho frío.",
            region = "Todo el país (especialmente el Altiplano)",
            category = "Expresión / Clima",
            exampleSentence = "¡Alalay! No traje chompa y está haciendo un frío tremendo.",
            exampleTranslation = "¡Qué frío! No traje chompa y está haciendo un frío tremendo.",
            funFact = "Es de origen aimara/quechua y se usa igual que diríamos '¡brrr!' en otros países."
        ),
        Word(
            term = "Sarna",
            meaning = "Usado en broma entre amigos como insulto cariñoso para decirle a alguien que es pesado, molesto o fastidioso, sin intención de ofender de verdad.",
            region = "Todo el país",
            category = "Expresión / Insulto de broma",
            exampleSentence = "No seas sarna, deja de molestarme con eso.",
            exampleTranslation = "No seas pesado, deja de molestarme con eso.",
            funFact = "Literalmente 'sarna' es una enfermedad de la piel, pero aquí se usa solo como insulto juguetón entre amigos."
        ),
        Word(
            term = "Ispi",
            meaning = "Apodo cariñoso que se le da a una persona de estatura muy baja o contextura pequeña.",
            region = "La Paz / Altiplano",
            category = "Apodo",
            exampleSentence = "Ahí viene el Ispi, siempre el más bajito del equipo pero el más rápido.",
            exampleTranslation = "Ahí viene el chiquitín, siempre el más bajito del equipo pero el más rápido.",
            funFact = "El ispi es en realidad un pescadito diminuto del Lago Titicaca, muy popular frito con papa y chuño."
        ),
        Word(
            term = "Yesca",
            meaning = "Estar sin dinero, en la pobreza o muy escaso de plata.",
            region = "Santa Cruz / Oriente",
            category = "Expresión / Dinero",
            exampleSentence = "No puedo salir este fin de semana, ando en la yesca total.",
            exampleTranslation = "No puedo salir este fin de semana, estoy sin nada de plata.",
            funFact = "Es un regionalismo típico del oriente boliviano, distinto a su uso en otros países como material para encender fuego."
        ),
        Word(
            term = "Tojpi",
            meaning = "Persona loca, alocada o que actúa de forma extraña.",
            region = "Cochabamba / Valles",
            category = "Expresión / Personalidad",
            exampleSentence = "Ese tojpi se puso a bailar solo en medio de la plaza.",
            exampleTranslation = "Ese loquito se puso a bailar solo en medio de la plaza.",
            funFact = "Forma parte de un grupo de apodos y palabras coloquiales típicas de Cochabamba, como 'wist'u picu' o 'p'ajla'."
        )
    )
}
