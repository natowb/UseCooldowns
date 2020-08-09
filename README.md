
## UseCooldowns

### Current Features
- set cooldowns for potions
- Splash and normal potions are considered equal IE: a drinking a jump potion will add a cooldown to the splash as well
- set cooldowns for generic items as well
### Config Example
```YML
# #########################
# Created by NatoWB
# https://github.com/natowb
# #########################
# Prefix: sets the display of the plugin when it messages players
# itemDelays: is the list of items to give delays to
# Examples:
# birch_sapling:100
# horse_spawn_egg:100
# Examples:
# jump:100 -- Gives delay of 5 seconds to potions of type jump
# fire_resistance:100 -- Gives delay of 5 seconds to potions of type fire resistance
prefix: '[&aUse Cooldowns&r]'
itemCooldowns:
- birch_sapling:100
potionCooldowns:
- jump:100
- fire_resistance:100

```
### Permissions
```YML
usedelay.bypass # allows players to ignore cooldown restrictions. Defaults to false

```
