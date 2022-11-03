# WallsSG
Mine within your space to upgrade your gear before the walls fall. The NPCs outside of each team cave will have special items for you to purchase! Last team standing wins. Also supports player voting for a custom MODIFIER gamemode that allows players to select a custom class to play, each with a special ability.


This project has officially been in an on/off development state since sometime mid-to-late 2021. Though this was the first minigame that I ever WANTED to develop and made a super bare bones shell of a couple years prior but never finished. The base game was officially completed within a few weeks, but many updates and improvements to the game have come out since then. Latest update was done in October 2022, adding to Walls SG the special modifier gamemode and all the custom classes, support for my HerobrinePVP-CORE's new leveling system, and various other improvements.

So yeah, if you see something looking wonky in the code, chances are I haven't touched that part for about a year+ and it will get refactored in the future.


Dependencies:

- [Mine] HerobrinePVP-CORE (Main core. It's the backbone for all my custom plugins within this setup.)
- [Mine] GameCore (Lots of minigame-related functions, configuration settings, and arena management. It's the backbone for all my minigame plugins within this setup.)
- [3rd Party] Citizens (NPCs - while used in this minigame for the shop, it's used all around the whole server setup.)
- [3rd Party] WorldEdit (Helps wtih world-related tasks, such as making the walls fall, replacing placeholder blocks, or placing custom objects via schematics.)
- [3rd Party] NoteBlockAPI (GameCore/HerobrinePVP-CORE dependency - plays custom NoteBlock music.)

Custom Gameplay Mechanics:

A video showcase of *some* of the below mechanics can be seen here: https://streamable.com/hkt2kg (Apologies for any recording glitches)

- Randomized Ore Generation - When the game starts, placeholder blocks are replaced with certain ore types at random. Some ores are guranteed, some are randomized! Different ore types are more common than others in different sections of each team's cave. Each team's cave has the same amount of resources.
  - Key (common->rarest): 
      - Sponge = Coal, Iron
      - Melon = Gold, Diamond
      - Bookshelf = Lapis, Obsidian, Emerald
![image](https://user-images.githubusercontent.com/74119793/198732055-afa7a80a-6720-4a22-bebc-2d2f3908b989.png)


- Custom Shop System - Automatically scales with the Shops and ShopItems enums!
![image](https://user-images.githubusercontent.com/74119793/198731127-6fbe864d-37fc-44f7-90ae-cf025caacfad.png)
![image](https://user-images.githubusercontent.com/74119793/198731166-00f233ec-bc9b-42a8-a601-4a2a08449b4c.png)

- Loot Chest Spawning - Once the Walls Fall, loot chests will spawn in the center of the map. Do you want to risk getting killed for better resources, rush the enemy teams, or stay back? 
![image](https://user-images.githubusercontent.com/74119793/198731426-5eee3c73-f9e9-4bbf-85e6-6ea76c249e6d.png)

- Custom Sudden Death Mechanics: The World Border closes in to the center of the map for the last couple minutes of the game. Watch out! (https://streamable.com/yni27r Footage of an older version of the game utilizing this mechanic)

![image](https://user-images.githubusercontent.com/74119793/198730927-618f6192-769d-40d8-a982-12393bbbf419.png)
- These custom in-game classes:
  - Juggernaut: Spawn with a special Diamond Axe that gains +1 level of Sharpness for every kill you get.
  - Emerus's Heir (aka ECONOMIST in the code): Spawns with some starting resources, and has a 25% discount on all shop items.
  ![image](https://user-images.githubusercontent.com/74119793/198731246-9ee6dca2-c6bb-4745-aecf-f16731874778.png)

  - Miner: 15% Chance to DOUBLE mined ore drops. Comes with an enchanted Diamond Pickaxe on spawn.
  - Engineer: Place a custom cannon object that can shoot enemy players within a certain range and deal damage to them. The cannon expires after 20s and is automatically removed. The player can remove the cannon at any time using the Callback Device in their inventory while a cannon is placed down. There's a 1.5s delay before you can actually call the cannon back once it is placed down to prevent accidental callbacks.
  
  ![image](https://user-images.githubusercontent.com/74119793/198731320-49fc391d-45f1-4ce1-ba2b-75afd746127d.png)

 
 
