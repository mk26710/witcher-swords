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

package net.defracted.witcherswords.commands

import net.defracted.witcherswords.Main
import net.md_5.bungee.api.ChatColor
import org.bukkit.Sound
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class GetAerondight(private val pl: Main) : CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (command.name.equals("getaerondight", ignoreCase = true)) {
            if (sender is Player) {
                val issuer = sender
                val inventory = issuer.inventory
                val location = issuer.location
                val world = issuer.world

                if (inventory.firstEmpty() == -1) {
                    pl.AERONDIGHT.get()?.let { world.dropItemNaturally(location, it) }
                    issuer.sendMessage(ChatColor.translateAlternateColorCodes('&',
                            "&8&oAerondight is somewhere around you."))
                } else {
                    inventory.addItem(pl.AERONDIGHT.get())
                    issuer.sendMessage(ChatColor.translateAlternateColorCodes('&',
                            "&8&oAerondight was obtained."))
                }

                issuer.playSound(location, Sound.ITEM_TOTEM_USE, 1f, 1f)


                return true
            }
            return false
        }
        return false
    }
}