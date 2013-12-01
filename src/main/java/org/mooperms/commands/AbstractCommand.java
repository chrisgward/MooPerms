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

package org.mooperms.commands;

import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.mooperms.MooPerms;

import static org.mooperms.I18n._;

public abstract class AbstractCommand {
	protected MooPerms instance;

	public abstract void command(CommandSender sender, Command cmd, String cmdLabel, String[] args) throws Exception;

	public final void onCommand(CommandSender sender, Command cmd, String cmdLabel, String[] args) throws Exception {
		if(sender instanceof Player) {
			if(sender.hasPermission("mooperms." + cmd.getName()) || (sender.isOp() && instance.getConfiguration().getConfig().isOpOverride())) {
			  	command(sender, cmd, cmdLabel, args);
			} else {
				throw new Exception(_("noPermission"));
			}
		} else {
			if(!(sender instanceof BlockCommandSender) || instance.getConfiguration().getConfig().isAllowCommandBlocks())
				command(sender, cmd, cmdLabel, args);
			else
				throw new Exception(_("noCommandBlocks"));
		}
	}
}
