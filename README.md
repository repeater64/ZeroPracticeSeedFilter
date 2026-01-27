# ZeroPracticeSeedFilter

## About

This is an MCSR practice tool, designed generally for use with MPK (https://github.com/Knawk/mc-MiniPracticeKit). MPK is a wonderful tool for practicing zeroes which many people use, but people generally use it with totally random seeds. This tool lets you customise which zeroes you do or don't get! 

## How it works

This isn't a mod, just a simple jar file that you run in the background (like Jingle or Ninjabrainbot). Download it from the "releases" tab on the right. You use the menus to customise your settings (see below), then the tool automatically copies Minecraft world seeds to your clipboard, giving you a new one each time it detects you've created a new world. So all you need to do is when creating a new creative world to use MPK, just hit "More World Options" and Ctrl+V into the seed bar. The tool will supply you a new seed which matches your settings every time.

## The Settings

With the basic mode, this tool lets you use MPK kind of like the zero practice map, choosing what zero(s) to practice:

<img width="407" height="572" alt="BasicView" src="https://github.com/user-attachments/assets/1f3988b9-c8eb-45c9-b93c-d76845b73d21" />

Or with Advanced mode, you can get full control over which tower+spawn combinations you will and won't get. This would be perfect for making it so you only get zeroes which you think you can hit, or to filter out the free zeroes and give yourself a personalised challenge whilst practicing:

<img width="641" height="641" alt="AdvancedView" src="https://github.com/user-attachments/assets/b1ac3263-23c1-41bb-9758-274cfb414313" />

Full details about all the settings can be seen by hovering the info icons in the application itself.

## Issues / questions / feedback

If you have any problems, questions, or feedback (or even just found the tool useful and wanted to let me know) feel free to message me on discord, my username is **repeater64**. This is the first time I've made a tool for the community and I'm hoping it will be useful to people, I'll certainly be using it!

## Autohotkey script for fast MPK resetting

For myself, I made an autohotkey script that triggers when I press ctrl+K and automatically does the following actions:
 - Quit the current world I'm in, if I'm in one (works from the You Died screen and normal gameplay)
 - Click singleplayer, create new world
 - Double click gamemode to set to creative
 - Switch to more world options, paste in seed from clipboard (provided by the ZeroPracticeSeedFilter tool)
 - Click create world

It does all that in like a second which is pretty useful and satisfying. So I'm sharing the section of the autohotkey script here. If you already use/know how to use autohotkey (or are willing to look into setting it up) then feel free to use this snippet:

```
^k::  ; Ctrl + K hotkey. Change the k to something else if you want, or change the ^ if you wanna use a modifier other than Ctrl (look up the autohotkey documentation)
{
    ; Handles exiting from a world, works from death screen or normal gameplay (also works from the ranked queuing screen because it clicks the queue in background button for you!! Very nice for practicing quickly whilst in queue)
    Send "{Tab}"
    Sleep 20
    Send "{Space}"
    Sleep 800
    Send "{Esc}"
    Sleep 20
    Click 2450, 1400 ; Clicks the "Quit to title" button in the bottom right to skip the saving screen

    ; If you often find that when using the shortcut whilst in a world, you get stuck at the main menu screen and have to press the shortcut again, try increasing this time (300 = 0.3 seconds)
    Sleep 300

    ; click singleplayer
    Send "{Tab}"
    Sleep 20
    Send "{Space}"
    Sleep 20

    ; Click create new world
    Click 1518, 1295
    Sleep 20
    ; Double click gamemode button
    Click 1055, 330
    Sleep 20
    Click 1055, 330
    Sleep 20

    ; Click more world options and paste seed
    Click 1355, 570
    Sleep 20
    Click 1355, 230
    Sleep 20
    Send("{Ctrl down}")
    Send("v")
    Send("{Ctrl up}")
    Sleep 20

    ; Click create button
    Click 1055, 1369
}
```

Note that it uses a bunch of screen coordinates to click on. This works for me on a 2560x1440 monitor with GUI scale 3. If you have a different monitor size and/or GUI scale you'll need to change the numbers - I'd recommend using Windows+PrintScreen to take a screenshot of the world creation screen, then pasting this screenshot into an image editor that lets you see pixel coordinates so you can work out the coords of the buttons and change it in the code. You'll also need to change the coords of the "Quit to title" button in the bottom right, but the coords for this will just be the resolution of your screen minus a little bit.
