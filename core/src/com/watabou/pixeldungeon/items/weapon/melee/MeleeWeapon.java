/*
 * Pixel Dungeon
 * Copyright (C) 2012-2014  Oleg Dolya
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */
package com.watabou.pixeldungeon.items.weapon.melee;

import com.watabou.pixeldungeon.Dungeon;
import com.watabou.pixeldungeon.items.Item;
import com.watabou.pixeldungeon.items.weapon.Weapon;
import com.watabou.pixeldungeon.utils.Utils;
import com.watabou.utils.Random;

public class MeleeWeapon extends Weapon {

    private final int tier;

    public MeleeWeapon(int tier, float acu, float dly) {
        super();

        this.tier = tier;

        ACU = acu;
        DLY = dly;

        STR = typicalSTR();

        MIN = min();
        MAX = max();
    }

    private int min() {
        return tier;
    }

    private int max() {
        return (int) ((tier * tier - tier + 10) / ACU * DLY);
    }

    @Override
    public Item upgrade() {
        return upgrade(false);
    }

    public Item upgrade(boolean enchant) {
        STR--;
        MIN++;
        MAX += tier;

        return super.upgrade(enchant);
    }

    public Item safeUpgrade() {
        return upgrade(enchantment != null);
    }

    @Override
    public Item degrade() {
        STR++;
        MIN--;
        MAX -= tier;
        return super.degrade();
    }

    public int typicalSTR() {
        return 8 + tier * 2;
    }

    @Override
    public String info() {

        final String p = "\n\n";

        StringBuilder info = new StringBuilder(desc());

        String quality = levelKnown && level != 0 ? (level > 0 ? "upgraded" : "degraded") : "";
        info.append(p);
        info.append("This ").append(name).append(" is ").append(Utils.indefinite(quality));
        info.append(" tier-").append(tier).append(" melee weapon. ");

        if (levelKnown) {
            info.append("Its average damage is ").append(MIN + (MAX - MIN) / 2).append(" points per hit. ");
        } else {
            info.append("Its typical average damage is ").append(min() + (max() - min()) / 2).append(" points per hit ").append("and usually it requires ").append(typicalSTR()).append(" points of strength. ");
            if (typicalSTR() > Dungeon.getInstance().hero.STR()) {
                info.append("Probably this weapon is too heavy for you. ");
            }
        }

        if (DLY != 1f) {
            info.append("This is a rather ").append(DLY < 1f ? "fast" : "slow");
            if (ACU != 1f) {
                if ((ACU > 1f) == (DLY < 1f)) {
                    info.append(" and ");
                } else {
                    info.append(" but ");
                }
                info.append(ACU > 1f ? "accurate" : "inaccurate");
            }
            info.append(" weapon. ");
        } else if (ACU != 1f) {
            info.append("This is a rather ").append(ACU > 1f ? "accurate" : "inaccurate").append(" weapon. ");
        }

        if (enchantment != null) {
            info.append("It is enchanted.");
        }

        if (levelKnown && Dungeon.getInstance().hero.belongings.backpack.items.contains(this)) {
            if (STR > Dungeon.getInstance().hero.STR()) {
                info.append(p);
                info.append("Because of your inadequate strength the accuracy and speed " + "of your attack with this ").append(name).append(" is decreased.");
            }
            if (STR < Dungeon.getInstance().hero.STR()) {
                info.append(p);
                info.append("Because of your excess strength the damage " + "of your attack with this ").append(name).append(" is increased.");
            }
        }

        if (isEquipped(Dungeon.getInstance().hero)) {
            info.append(p);
            info.append("You hold the ").append(name).append(" at the ready").append(cursed ? ", and because it is cursed, you are powerless to let go." : ".");
        } else {
            if (cursedKnown && cursed) {
                info.append(p);
                info.append("You can feel a malevolent magic lurking within ").append(name).append(".");
            }
        }

        return info.toString();
    }

    @Override
    public int price() {
        int price = 20 * (1 << (tier - 1));
        if (enchantment != null) {
            price *= 1.5;
        }
        if (cursed && cursedKnown) {
            price /= 2;
        }
        if (levelKnown) {
            if (level > 0) {
                price *= (level + 1);
            } else if (level < 0) {
                price /= (1 - level);
            }
        }
        if (price < 1) {
            price = 1;
        }
        return price;
    }

    @Override
    public Item random() {
        super.random();

        if (Random.Int(10 + level) == 0) {
            enchant(Enchantment.random());
        }

        return this;
    }
}
