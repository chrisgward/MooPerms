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

import org.mooperms.MooPerms;
import lombok.RequiredArgsConstructor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;

import java.util.HashMap;
import java.util.Map;

import static org.mooperms.I18n._;

@RequiredArgsConstructor
public class MooPermsCommandExecutor implements CommandExecutor {
	private final MooPerms instance;

	Map<String, AbstractCommand> executorMap = new HashMap<>();

	@Override
	public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {

		if (commandSender instanceof ConsoleCommandSender || commandSender.isOp() || commandSender.hasPermission("mooperms." + command.getName())) {
			AbstractCommand executor;
			if (executorMap.containsKey(command.getName())) {
				executor = executorMap.get(command.getName());
			} else {
				try {
					executor = (AbstractCommand) Class.forName("com.chrisgward.mooperms.commands.Command" + command.getName()).newInstance();
					executor.instance = instance;
					executorMap.put(command.getName(), executor);
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			}

			try {
				return executor.onCommand(commandSender, command, s, args);
			} catch (Exception e) {
				commandSender.sendMessage(_("commandError", e.getMessage()));
				instance.showError(e, true);
				return true;
			}
		} else {
			commandSender.sendMessage(_("noCommandPermission"));
			return true;
		}
	}
}
