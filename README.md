<p align="center">
    <a href="https://github.com/rusthero/BiomeCompass">
        <img src="https://user-images.githubusercontent.com/120267985/208212764-59068903-f51e-4631-b98c-e0bf4faa82cc.png" align="center" width="300">
    </a>
    <br>
    <a href="https://github.com/rusthero/BiomeCompass/releases">
        <img src="https://img.shields.io/github/v/release/rusthero/BiomeCompass?style=flat&labelColor=1C2C2E&color=D0A384&logo=GitHub&logoColor=white">
    </a>
    <a href="https://github.com/rusthero/BiomeCompass/actions?query=workflow%3A%22Java CI%22">
        <img src="https://img.shields.io/github/actions/workflow/status/rusthero/BiomeCompass/build.yml?branch=main&style=flat&labelColor=1C2C2E&color=BEC5C9&logo=GitHub%20Actions&logoColor=BEC5C9"/>
    </a>
    <a href="https://www.codefactor.io/repository/github/rusthero/biomecompass">
        <img src="https://www.codefactor.io/repository/github/rusthero/biomecompass/badge" alt="CodeFactor"/>
    </a>
    <a href="https://discord.gg/5z6cqzQGjS">
        <img src="https://img.shields.io/discord/1051165269709557813.svg?style=flat&color=7289DA&logo=Discord" alt="Discord"/>
    </a>
</p>

## About
"Biome Compass" is a Spigot plugin that adds a new item to the game – a compass that helps players navigate to specific biomes in any dimension. Players can use a GUI to select the desired biome and the compass will locate it in a short time.

![preview](https://user-images.githubusercontent.com/120267985/208215600-4306468b-675a-42d9-9ed0-394f1b8cb064.png)

## Installation
To install the "Biome Compass" plugin, you will need to follow these steps:
1. Make sure you have the latest version of [Java](https://adoptium.net/temurin/releases/) installed on your machine. (Up to Java 14 can be installed for 1.16.2)
2. Install Spigot on your server. You can find instructions on how to install it [here](https://www.spigotmc.org/wiki/spigot-installation/).
3. Download the latest release of the "Biome Compass" plugin from the [releases](https://github.com/rusthero/BiomeCompass/releases) page.
4. Copy the downloaded JAR file to the `plugins` folder of your Spigot server.
5. Start the server and wait for the plugin to be loaded.
6. You can now use the "Biome Compass" plugin in your game.

Alternatively, you can use Gradle to build the plugin from source:
1. Clone the repository by running `git clone https://github.com/rusthero/BiomeCompass.git` in your terminal.
2. Navigate to the root directory of the repository.
3. Run `gradle build` to build the plugin. This will create a JAR file in the build/libs directory.
4. Copy the JAR file to the `plugins` folder of your Spigot server and start the server.

## License
GNU General Public License ([v3.0](https://www.gnu.org/licenses/gpl.txt))
