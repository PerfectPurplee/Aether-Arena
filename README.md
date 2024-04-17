# Aether-Arena

Aether-Arena is a multiplayer top-down game where players engage in magical battles. The main goal is to defeat as many players as you can to be on top of the scoreboard as it is a deathmatch mode game. Its all about casting spells, dodging attacks, and strategizing to secure victory.

## GamePlay Screenshots

![Alt Text](previewGif.gif)

## Technologies Used
- Project doesnt really use any frameworks or libraries other than what is available in the Java standard library.
  With that being said:
- build tool: **Maven:** 
- for drawing the game interface: **Java Swing:**

## Setup
You can run this chain of commands to open a game directly from you terminal:
```
git clone git@github.com:PerfectPurplee/Aether-Arena.git && 
cd Aether-Arena && mvn clean package && 
java -jar target/AetherArena.jar

```
Or otherwise if you dont have git/maven/java in your system's path you can use IDE of your choice to compile and run the project.

In order to host a game you need to host a server wich is a separate application. more info here: https://github.com/PerfectPurplee/Aether-ArenaServer

## Gameplay instructions

- Dodge attacks: Use `Shift` to dodge incoming spells.
- Cast spells: Cast spells using `Q`, `W`, `E`, and `R`.
- Move character: Navigate your character using the mouse.

Feel free to join the magical battles and may the best mage triumph in the Aether-Arena!

---
