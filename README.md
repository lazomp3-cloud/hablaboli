# Habla Boliviano 🇧🇴

App Android nativa (Kotlin + Jetpack Compose) para enseñar a extranjeros palabras,
expresiones y modismos típicos de Bolivia: Chala, Jenecherú, Yapa, Cambita, y las que quieras agregar.

## Stack técnico

| Capa | Tecnología |
|---|---|
| UI | Jetpack Compose + Material 3 (soporta Material You / dynamic color en Android 12+) |
| Navegación | Navigation Compose, con animaciones de transición entre pantallas |
| Base de datos local | Room (SQLite) |
| Preferencias (tema) | DataStore Preferences |
| Audio de ejemplos | TextToSpeech nativo de Android (no requiere archivos .mp3) |
| Arquitectura | MVVM (ViewModel + Repository + StateFlow) |
| min/target SDK | minSdk 31 (Android 12) · targetSdk 34 |

## 1. Requisitos previos para compilar

1. **Android Studio** (recomendado: la versión más reciente, ej. "Koala" o superior) — incluye JDK 17 y el SDK de Android.
   Descarga: https://developer.android.com/studio
2. **JDK 17** (Android Studio ya lo trae embebido; si compilas por línea de comandos necesitas instalarlo aparte).
3. Conexión a internet la primera vez (Gradle descarga las dependencias listadas en `app/build.gradle.kts`).

No necesitas instalar Flutter ni nada adicional: el proyecto es 100% Android nativo con Gradle.

## 2. Abrir el proyecto

1. Abre **Android Studio**.
2. `File > Open...` y selecciona la carpeta raíz `BoliviaSlangApp/` (la que contiene `settings.gradle.kts`).
3. Espera a que Android Studio sincronice Gradle automáticamente (puede tardar unos minutos la primera vez
   mientras descarga dependencias). Si te pide generar el `gradle-wrapper.jar`, acepta (Android Studio lo
   genera solo) — o ejecuta `gradle wrapper` una vez si usas Gradle instalado localmente.

## 3. Ejecutar en un emulador o celular (modo prueba)

1. Conecta un celular Android 12+ por USB con "Depuración USB" activada, o crea un emulador
   (`Device Manager > Create Device`, imagen API 31+).
2. Click en el botón ▶ (Run) en Android Studio, o `Shift + F10`.

## 4. Generar el archivo APK

### Opción A — Desde Android Studio (recomendada)
1. Menú `Build > Build Bundle(s) / APK(s) > Build APK(s)`.
2. Cuando termine, aparece un aviso "APK(s) generated successfully" — click en **locate** para encontrarlo.
3. El archivo estará en: `app/build/outputs/apk/debug/app-debug.apk`

### Opción B — Por línea de comandos (Terminal)
Desde la carpeta raíz del proyecto:

```bash
# En Mac/Linux
./gradlew assembleDebug

# En Windows
gradlew.bat assembleDebug
```

El APK de depuración quedará en:
```
app/build/outputs/apk/debug/app-debug.apk
```

### Generar un APK de producción (firmado, optimizado)

```bash
./gradlew assembleRelease
```

Esto genera `app/build/outputs/apk/release/app-release-unsigned.apk`. Para publicarlo en Play Store
o distribuirlo, debes **firmarlo** con tu propia keystore:

```bash
keytool -genkey -v -keystore mi-release-key.jks -keyalg RSA -keysize 2048 -validity 10000 -alias habla-boliviano
```

Luego, en Android Studio: `Build > Generate Signed Bundle / APK`, elige APK, selecciona tu keystore
y sigue el asistente. Esto produce el APK final listo para instalar en cualquier Android 12+.

## 5. Instalar el APK en un celular

1. Copia el `.apk` generado al celular (USB, Drive, etc.).
2. En el celular, habilita "Instalar apps de orígenes desconocidos" para la app que uses para abrir el archivo.
3. Toca el archivo `.apk` e instálalo.

## 6. Estructura del proyecto

```
BoliviaSlangApp/
├── app/
│   ├── build.gradle.kts          # Dependencias y configuración del módulo (minSdk 31, Compose, Room...)
│   ├── proguard-rules.pro
│   └── src/main/
│       ├── AndroidManifest.xml
│       ├── java/com/example/boliviaslang/
│       │   ├── MainActivity.kt              # Punto de entrada
│       │   ├── data/
│       │   │   ├── Word.kt                  # Entidad Room
│       │   │   ├── WordDao.kt               # Consultas (incluye búsqueda predictiva)
│       │   │   ├── AppDatabase.kt           # Base de datos + datos de ejemplo (Chala, Yapa, etc.)
│       │   │   ├── WordRepository.kt
│       │   │   └── ThemePreferences.kt      # Persistencia claro/oscuro con DataStore
│       │   ├── ui/theme/                    # Colores, tipografía y Theme.kt (claro/oscuro + Material You)
│       │   ├── ui/components/
│       │   │   ├── WordCard.kt
│       │   │   ├── FlashcardComponent.kt    # Tarjeta con animación de volteo 3D
│       │   │   └── SpeechHelper.kt          # Text-to-Speech para "escuchar ejemplo"
│       │   ├── ui/screens/
│       │   │   ├── HomeScreen.kt
│       │   │   ├── SearchScreen.kt          # Buscador predictivo (debounce + Room LIKE query)
│       │   │   ├── FlashcardScreen.kt
│       │   │   ├── WordDetailScreen.kt
│       │   │   └── SettingsScreen.kt        # Selector manual de tema claro/oscuro/sistema
│       │   ├── navigation/NavGraph.kt       # Navegación con animaciones y bottom bar
│       │   └── viewmodel/                   # WordViewModel, ThemeViewModel, Factory
│       └── res/
│           ├── values/ (themes.xml claro, strings.xml)
│           ├── values-night/ (themes.xml oscuro)
│           ├── drawable/ (ícono adaptativo)
│           └── mipmap-anydpi-v26/
├── build.gradle.kts               # Config a nivel proyecto
├── settings.gradle.kts
└── gradle.properties
```

## 7. Cómo agregar más palabras

Edita `SampleData.kt` (dentro de `AppDatabase.kt`) y agrega más objetos `Word(...)` a la lista.
También podrías construir después una pantalla de "Agregar palabra" reutilizando el mismo `WordRepository`.

## 8. Notas de rendimiento (Android 12+)

- Se usa `LazyColumn` en vez de listas no virtualizadas, para bajo consumo de memoria con listas largas.
- Room ejecuta las consultas en `Dispatchers.IO` mediante `Flow`, sin bloquear el hilo principal.
- `isMinifyEnabled = true` y `isShrinkResources = true` en el build de release reducen el tamaño del APK
  y eliminan código muerto.
- Se usa Material You (`dynamicColorScheme`) disponible nativamente desde Android 12 (API 31),
  por lo que la app puede adaptar sus colores al fondo de pantalla del usuario.
