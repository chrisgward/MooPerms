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

import lombok.RequiredArgsConstructor;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.mooperms.MooPerms;

import java.util.HashMap;
import java.util.Map;

import static org.mooperms.I18n._;

@RequiredArgsConstructor
public class MooPermsCommandExecutor implements CommandExecutor {
	private final MooPerms instance;

	Map<String, AbstractCommand> executorMap = new HashMap<>();

	@Override
	public boolean onCommand(CommandSender sender, Command command, String cmdLabel, String[] args) {
		if(sender instanceof Player) {
			if (!sender.hasPermission("mooperms." + command.getName().toLowerCase()) && (!sender.isOp() || !instance.getConfiguration().getConfig().isOpOverride())) {
				sender.sendMessage(_("noPermission"));
				return true;
			}
		} else if ((sender instanceof BlockCommandSender) && !instance.getConfiguration().getConfig().isAllowCommandBlocks()) {
			sender.sendMessage(_("noCommandBlocks"));
			return true;
		}

		try {
			AbstractCommand executor;
			if (executorMap.containsKey(command.getName())) {
				executor = executorMap.get(command.getName());
			} else {
				try {
					String name = command.getName();
					name = name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase();
					executor = (AbstractCommand) Class.forName("org.mooperms.commands." + name).newInstance();
					executor.instance = instance;
					executorMap.put(command.getName(), executor);
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			}
			if(executor == null) {
				throw new NullPointerException("Command improperly loaded");
			}
			executor.command(sender, command, cmdLabel, args);
		} catch (IllegalArgumentException e) {
			instance.showError(e, true);
			return false;
		} catch(RuntimeException e) {
			instance.showError(e, false);
			sender.sendMessage(_("cmdError", e.getMessage()));
		} catch (Throwable e) {
			instance.showError(e, true);
			sender.sendMessage(_("cmdError", e.getMessage()));
		}

		return true;
	}
}
