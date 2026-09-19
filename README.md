# ⚡ Hardcore Reset

[![Minecraft](https://img.shields.io/badge/Minecraft-26.2-brightgreen.svg)](https://fabricmc.net/)
[![Fabric](https://img.shields.io/badge/Modloader-Fabric-blue.svg)](https://fabricmc.net/)
[![Release](https://img.shields.io/badge/Release-v1.0.0_Stable-green.svg)](https://github.com/Aosiika/fabric-hardcore-reset/releases)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)

[English](#english) | [Español](#español)

---

<a name="english"></a>
## 🇬🇧 English

Have you ever died in a Minecraft Hardcore world, only to face the tedious routine of clicking *Title Screen*, manually deleting the world from your saves list, clicking *Create New World*, retyping settings, and waiting all over again?

**Hardcore Reset** completely solves this. It's a lightweight, seamless mod designed exclusively for **Singleplayer** that automates the entire restart process the second you die—without you needing to click a single button.

> ⚠️ **Note:** This mod is currently built and available for **Minecraft 26.2** running on the **Fabric** modloader. It is intended **strictly for Singleplayer** worlds.

### 🌟 What does it do?

* 🔄 **Zero-Click Instant Reset**: When you take lethal damage, the mod bypasses the vanilla death screen. It halts the local server cleanly, deletes the old dead world from your `/saves/` folder in the background (no file-lock errors), and immediately launches a brand-new Hardcore world.
* 🕊️ **10-Second Spectator Limbo**: The moment you die, you are turned invulnerable and put into Spectator mode for 10 seconds. You can fly around freely to see what killed you while a smooth on-screen countdown ticks down (`10s... 9s... 1s`).
* ⏱️ **Live In-Game Scoreboard (HUD)**:
  * **Attempt Counter**: Automatically tracks your runs (`#1`, `#2`, `#3`...). This counter persists even if you exit or restart Minecraft.
  * **Live Stopwatch**: Ticks up from `00:00` while you play. Upon death, it freezes at your exact run duration.
  * **Reset Countdown**: Shows a live countdown when you die (`Resetting: 10s`).
  * **Last Run**: Displays how long your previous run lasted.
* 🏷️ **Smart World Naming**: New worlds are automatically named `Hardcore - Attempt #X`.
* 🌐 **Built-in Languages**: English (`en_us`) and Spanish (`es_es`). If your client language isn't supported, it smoothly falls back to English.
* 🛠️ **Simple In-Game Command**:
  * `/hardcore reset` — Resets your attempt counter and run timer back to Attempt #1. Works directly in Hardcore mode without requiring cheats, creative mode, or opening to LAN.

### 📦 Installation

1. Download and install the [Fabric Loader](https://fabricmc.net/use/installer/).
2. Make sure you have [Fabric API](https://modrinth.com/mod/fabric-api) in your `.minecraft/mods` folder.
3. Drop `hardcore_reset-1.0.0.jar` into your `.minecraft/mods` folder.
4. Launch the game, create a Hardcore world, and enjoy endless attempts!

### 🐛 Found a Bug or Have a Suggestion?

If you run into any issues, unexpected crashes, or have an idea to make the mod even better, please feel free to [open an issue on GitHub](https://github.com/Aosiika/fabric-hardcore-reset/issues). All feedback and bug reports are warmly welcomed!

### ⭐ Support the Project

If this mod saved you time or made your Hardcore runs more fun, please consider **leaving a star ⭐ on this repository**! It helps others discover the project and motivates future updates.

---

<a name="español"></a>
## 🇪🇸 Español

¿Alguna vez has muerto en un mundo Hardcore y has tenido que pasar por el tedioso proceso de salir al menú principal, buscar el mundo en la lista, borrarlo a mano, darle a *Crear nuevo mundo*, volver a configurar todo y esperar?

**Hardcore Reset** elimina esa frustración. Es un mod ligero y fluido diseñado exclusivamente para **Singleplayer (Un Jugador)** que automatiza todo el proceso de reinicio en cuanto mueres, ¡sin que tengas que tocar absolutamente ningún botón!

> ⚠️ **Nota:** El mod está disponible por el momento para **Minecraft 26.2** bajo el cargador de mods **Fabric**. Está diseñado **únicamente para partidas en solitario (Singleplayer)**.

### 🌟 ¿Qué funciones incluye?

* 🔄 **Reinicio Automático con 0 Clics**: Al recibir daño letal, el mod cancela la pantalla roja de muerte vanilla. Desconecta de forma limpia el servidor interno, borra la carpeta del mundo muerto en `/saves/` en segundo plano (sin bloqueos de archivos en Windows) y genera de inmediato un nuevo mundo Hardcore.
* 🕊️ **Limbo de 10 Segundos en Espectador**: Al morir, te vuelves invulnerable y pasas automáticamente a modo Espectador durante 10 segundos. Puedes volar libremente por la zona para ver qué monstruo o trampa acabó contigo mientras una cuenta regresiva en pantalla descuenta segundo a segundo (`10s... 9s... 1s`).
* ⏱️ **Scoreboard en Pantalla (HUD en tiempo real)**:
  * **Contador de Intentos**: Lleva la cuenta de tus runs (`#1`, `#2`, `#3`...). Este número se guarda de forma persistente aunque cierres el juego.
  * **Cronómetro en Vivo**: Cuenta hacia adelante desde `00:00`. Al morir, se congela mostrando exactamente cuánto duró tu aventura.
  * **Cuenta atrás de Reinicio**: Muestra los segundos restantes antes del cambio de mundo (`Reiniciando: 10s`).
  * **Última Run**: Muestra la duración que tuvo tu intento anterior.
* 🏷️ **Nombres Automáticos**: Cada nuevo mundo generado se titula automáticamente como `Hardcore - Intento #X`.
* 🌐 **Soporte de Idiomas**: Español (`es_es`) e Inglés (`en_us`). Si juegas en otro idioma, usará el inglés por defecto.
* 🛠️ **Comando Cómodo**:
  * `/hardcore reset` — Reinicia el contador de intentos y el cronómetro al Intento #1. Funciona directamente en Hardcore sin necesidad de trucos, creativo ni abrir a LAN.

### 📦 Instalación

1. Descarga e instala [Fabric Loader](https://fabricmc.net/use/installer/).
2. Asegúrate de tener [Fabric API](https://modrinth.com/mod/fabric-api) en tu carpeta `.minecraft/mods`.
3. Pega el archivo `hardcore_reset-1.0.0.jar` en la carpeta `.minecraft/mods`.
4. ¡Inicia Minecraft, crea un mundo Hardcore y a jugar!

### 🐛 ¿Has encontrado un fallo o problema?

Si encuentras algún error, mal funcionamiento o tienes alguna sugerencia para mejorar el mod, por favor [abre una incidencia (Issue) en GitHub](https://github.com/Aosiika/fabric-hardcore-reset/issues). ¡Cualquier reporte o comentario constructivo es bienvenido y ayuda muchísimo!

### ⭐ Apoya el Proyecto

Si el mod te resulta útil para tus retos o series de Hardcore, agradecería un montón que **dejes una estrella ⭐ en este repositorio**. Ayuda a que más gente lo conozca y me anima a seguir mejorándolo.

---

## 📄 Licencia / License

This project is licensed under the [MIT License](LICENSE).
