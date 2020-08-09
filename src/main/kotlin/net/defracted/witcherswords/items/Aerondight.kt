/*
 * MIT License
 *
 * Copyright (c) 2020 defracted
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 */

package net.defracted.witcherswords.items

import net.defracted.witcherswords.Main
import net.md_5.bungee.api.ChatColor
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType
import java.util.*
import kotlin.collections.ArrayList


class Aerondight(private val pl: Main) {
    val HIT_COUNTER_KEY = NamespacedKey(pl, "HIT_COUNTER")

    val PERSISTENT_KEY = NamespacedKey(pl, "CODE_NAME")
    val CODE_NAME = "Aerondight EP2"

    private val CHARGED_DAMAGE_MODE = AttributeModifier(UUID.randomUUID(), "generic.attackDamage", 100.0, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HAND)
    private val DEFAULT_ITEM_STACK = get()

    fun get(): ItemStack? {
        val sword = ItemStack(Material.IRON_SWORD)
        val swordMeta = sword.itemMeta

        swordMeta!!.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&6&lAerondight"))

        val damageModifier = AttributeModifier(UUID.randomUUID(), "generic.attackDamage", 12.0, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HAND)
        swordMeta.addAttributeModifier(Attribute.GENERIC_ATTACK_DAMAGE, damageModifier)

        val attackSpeedModifier = AttributeModifier(UUID.randomUUID(), "generic.attackSpeed", 2.0, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HAND)
        swordMeta.addAttributeModifier(Attribute.GENERIC_ATTACK_SPEED, attackSpeedModifier)

        swordMeta.addEnchant(Enchantment.OXYGEN, 1, true)
        swordMeta.addEnchant(Enchantment.DURABILITY, 5, true)
        swordMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS)

        swordMeta.persistentDataContainer.set(PERSISTENT_KEY, PersistentDataType.STRING, CODE_NAME)
        swordMeta.persistentDataContainer.set(HIT_COUNTER_KEY, PersistentDataType.INTEGER, 0)

        val lore = ArrayList<String>()
        lore.add(ChatColor.translateAlternateColorCodes('&', "&5&oLight and sharp as a razor."))
        swordMeta.lore = lore

        swordMeta.isUnbreakable = true
        sword.itemMeta = swordMeta

        return sword
    }

    /**
     * Updates sword's hit counter
     */
    fun addCounter(item: ItemStack) {
        val hitCounter = item.itemMeta?.persistentDataContainer?.get(HIT_COUNTER_KEY, PersistentDataType.INTEGER)

        if (hitCounter != null) {
            val counterAdd = hitCounter.plus(1)
            val newMeta = item.itemMeta?.clone()
            newMeta?.persistentDataContainer?.set(HIT_COUNTER_KEY, PersistentDataType.INTEGER, counterAdd)

            item.itemMeta = newMeta
        }
    }

    /**
     * Resets sword's hit counter
     */
    fun resetCounter(item: ItemStack) {
        if (item.itemMeta?.persistentDataContainer?.has(HIT_COUNTER_KEY, PersistentDataType.INTEGER) == true) {
            val newMeta = item.itemMeta?.clone()
            newMeta?.persistentDataContainer?.set(HIT_COUNTER_KEY, PersistentDataType.INTEGER, 0)

            item.itemMeta = newMeta
        }
    }

    /**
     * Returns current value of the sword's hit counter
     */
    fun getCounter(item: ItemStack): Int? {
        return item.itemMeta?.persistentDataContainer?.get(HIT_COUNTER_KEY, PersistentDataType.INTEGER)
    }

    /**
     * Handles the hit
     */
    fun handleHit(item: ItemStack) {
        addCounter(item)

        val counter = getCounter(item)
        // Bukkit.getServer().broadcastMessage("$counter")

        if (counter == 10) {
            val newMeta = item.itemMeta?.clone()

            newMeta?.removeAttributeModifier(Attribute.GENERIC_ATTACK_DAMAGE)
            newMeta?.addAttributeModifier(Attribute.GENERIC_ATTACK_DAMAGE, CHARGED_DAMAGE_MODE)

            item.itemMeta = newMeta
        } else if (counter != null) {
            if (counter >= 11) {
                item.itemMeta = DEFAULT_ITEM_STACK?.itemMeta?.clone()
            }
        }
    }
}