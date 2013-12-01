/*
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
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.mooperms.commands.moo;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.mooperms.commands.AbstractCommand;
import org.mooperms.context.User;

public class Check extends AbstractCommand {
	@Override
	public void command(CommandSender sender, Command cmd, String cmdLabel, String[] args) throws Exception {
		if(args.length != 2) {
			sender.sendMessage("Usage: /moo check <player> <permission>");
			return;
		}

		Player player = instance.getServer().getPlayer(args[0]);
		if(player == null) {
			throw new Exception("Player not found");
		}

		User user = instance.getWorld(player.getWorld().getName()).getUser(player.getName());
		String[] perms = user.getEffectivePermissions();

		sender.sendMessage("SuperPerms reports that " + player.getName() + " " + (player.hasPermission(args[1].toLowerCase()) ? "has" : "does not have") + args[1].toLowerCase());
		for(String perm : perms) {
			if(perm.equalsIgnoreCase(args[1]) || (perm.endsWith("*") && args[1].startsWith(perm.substring(1, perm.length() - 1)))) {
				sender.sendMessage("MooPerms reports that the player inherits the permission from " + perm);
				return;
			}
		}
		sender.sendMessage("MooPerms reports that the player does not inherit this permission.");
	}
}
