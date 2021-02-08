# Project-2.1-Risk

This project has been collaboratively executed for project 2.1 of the BSc Data Science and Artificial Intelligence of Maastricht University. 

A modified version of the game of Risk has been implemented, with multiple bots that learn to play the game. More information on implementation can be found in "final_paper.pdf"

## Setting up the software

We used Gradle for the project, so setting it up to run the code shouldn’t be too much of a hassle. You can open the project as an existing Gradle project. For this, see https://www.jetbrains.com/help/idea/gradle.html “Open an existing Gradle project ”. Then,
Gradle should automatically start importing required modules like JavaFX. Then, you need tocreate the following run configuration (https://www.jetbrains.com/help/idea/run-debug-configuration.html):
After this, you can just execute the code using this configuration.

## Rules

The rules have been added to the “rules” section of the UI. They are also described in detail in our report under the ‘Abstractions and Simplifications’ section User manual The actions that will need to be performed are intuitively mentioned in the UI. Additionally, if
you want to leave a game, pause, or restart, you will need to press the escape button when in-game. This will pause the game and show you a pop-up menu. In order to run the game and test our bot(s), there is an option in the selection menu that is
displayed when you start a game to select which type of player will be playing. 

- Player: user(s) who will input the selections as described in the warning message at the bottom of the screen
- Easy Bot: Temporal Difference learning algorithm
- Hard Bot: Deep Q-learning algorithm

Select the appropriate players as well as a color that you wish them to have. If you do not want to fill all 6 players then it is sufficient to leave the player and the respective color field
empty. 

To play a game as a Player: Follow the instructions prompted on the bottom of the screen, which follows the current phase.

To play a game as a Bot: You will need to click on any SVG each time to trigger the action of a bot. (we recommend Madagascar to trigger actions as it is closest to the continue button
once a turn has finished.)
