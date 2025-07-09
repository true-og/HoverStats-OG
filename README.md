# HoverStats-OG

HoverStats-OG is a fork of [HoverStats](https://github.com/Brand0n1/HoverStats-Test) updated and maintained for 1.19.4 by [TrueOG Network](https://trueog.net).

HoverStats is a server-side chat plugin that allows for more user interaction with chat. This is done by adding a hoverable effect to the player's name and tags. All of these are defined inside the config.yml and can be very easily changed. There is also a customizable command that will be activated when the player clicks on the target player's name.

--- 
# Disclaimer
This plugin supports most chat plugins, however, some work needs to be done to make it compatible. If at first, the plugin doesn't work then please change the following entry inside the config
```yml
Chat Event Priority: HIGH # Defaults to HIGH
```
to 
```yml
Chat Event Priority: HIGHEST # Defaults to HIGH
```
This fix may not work for every instance, if this doesn't work please join our ![support discord](https://www.discord.com/invite/eYW6tTuCKz) and ask us for help. :D
<br />
<br />

<details>
<summary>Commands</summary>

| Command | Description |
| --- | --- |
| `hoverstats` | Shows the help message for the plugin |
| `hoverstats reload` | Reloads the plugin |
| `hoverstats version` | Shows the current version of the plugin and config file |
</details>

<details>
<summary>Permissions</summary>
<br />

<details open>
<summary>Permission Nodes</summary>
  
| Pemission | Description |
| --- | --- |
| `hoverstats.help` | Shows the help message for the plugin (Given by Default) |
| `hoverstats.reload` | Gives the player the ability to reload the plugin |
| `hoverstats.version` | Shows the current version of the plugin and config file |
| `hoverstats.update` | Will show the player if there is an update when they join the server |
| `hoverstats.chat.view` | Makes it so that the player can view hoverable messages (can only be used when **"Chat Formatting.Require Permissions"** is set to true |
| `hoverstats.magic.*` | his will give the player access to all "magic" color codes |
| `hoverstats.colors.hex` | This will give the player access to all hex colors |
| `hoverstats.colors.*` | This will give the player access to all chat colors |
| `hoverstats.colors.[color-name]` | This will give the player access to a specific chat color in chat. This has to be the color name (ex. dark_aqua) |
| `hoverstats.join-formatting` | This will show the player the join format message specified in the config.yml (Given by Default) |
| `hoverstats.leave-formatting` | This will show the player the quit format message specified in the config.yml (Given by Default) |
</details>

<br />
<details open>
<summary>Permission Packs</summary>

| Pemission | Description |
| --- | --- |
| `hoverstats.*` | Gives access to all of the permissions listed above |
| `hoverstats.admin` | Gives access to all of the permissions listed above |
</details>

</details>
