# PlaceLimiter [![Travis master](https://img.shields.io/travis/Poeschl/PlaceLimiter/master.svg?maxAge=3600)](https://travis-ci.org/Poeschl/PlaceLimiter) [![Coveralls](https://img.shields.io/coveralls/Poeschl/PlaceLimiter/master.svg?maxAge=3600)](https://coveralls.io/github/Poeschl/PlaceLimiter?branch=master) [![Codacy grade](https://img.shields.io/codacy/grade/ca84deb8060d4fdb906cdcf415bb268a/master.svg?maxAge=3600)](https://www.codacy.com/app/poeschl/PlaceLimiter/dashboard)
The PlaceLimiter is a little bukkit plugin that allows the restriction for placing a specified amount of blocks.
For more explanation a explained sample configuration and the available permission nodes are shown below.

##Sample Configuration

```yml
noPermissionMessage: '&4You have no permission to use that command.'
limitPlaceReachedMessage: '&4You reached the limit of placing down %s.'
notPlacedFromThisPlayer: '&4You havent placed this %s.'
placeRules:
  - STONE: 2
  - 'DIRT:0': 1
```

The settings ``noPermissionMessage``, ``limitPlaceReachedMessage``, ``notPlacedFromThisPlayer`` are the messages the player gets displayed for the following reasons:
+ No permission for a command
+ He/She reached the placement limit
+ He/She tries to destroy a block which was not build by him-/herself

``placeRules`` contains the placement rules with following syntax: ``<MaterialName>[:<data>]:<allowed placement count>``
In the sample configuration above, a player is allowed to place 2 Stone blocks and 1 Dirtblock with the data-id 0. And all other blocks are unlimited.

##Commands

``/plreload`` - Will reload the settings file.

##Permission Nodes

+ ``placelimiter.reload`` - Permission for the ``reload`` command
+ ``placelimiter.override`` - Exception for the placing limit

---

[![GitHub license](https://img.shields.io/badge/license-GPL3-blue.svg)](https://raw.githubusercontent.com/Poeschl/PlaceLimiter/master/LICENSE)