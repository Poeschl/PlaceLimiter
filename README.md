# PlaceLimiter
The PlaceLimiter is a little bukkit plugin that allows the restriction for placing a specified amount of blocks.
For more explanation a explained sample configuration and the available permission nodes are shown below.

##Sample Configuration##

```yml
noPermissionMessage: '&4You have no permission to use that command.'
limitPlaceReachedMessage: '&4You reached the limit of placing down %s.'
notPlacedFromThisPlayer: '&4You havent placed this %s.'
placeRules:
  - STONE: 2
  - "DIRT:0": 1
```

The settings ``noPermissionMessage``, ``limitPlaceReachedMessage``, ``notPlacedFromThisPlayer`` are the messages the player gets displayed for the following reasons:
+ No permission for a command
+ He/She reached the placement limit
+ He/She tries to destroy a block which was not build by him-/herself

``placeRules`` contains the placement rules with following syntax: ``<MaterialName>[:<data>]:<allowed placement count>``
In the sample configuration above, a player is allowed to place 2 Stone blocks and 1 Dirtblock with the dataid 0. And all other blocks are unlimited.

##Commands##

``/plreload`` - Will reload the settings file.

##Permission Nodes##

+ ``placelimiter.reload`` - Permission for the ``reload`` command
+ ``placelimiter.override`` - Exception for the placing limit
