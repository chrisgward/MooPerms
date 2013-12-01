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

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.HashMap;
import java.util.Map;

import static org.mooperms.I18n._;

public class Moo extends AbstractCommand {

	Map<String, AbstractCommand> commands = new HashMap<>();
	Map<String, String> aliasMap = new HashMap<>();

	public Moo() {
		aliasMap.put("?", "Help");
	}


	@Override
	public void command(CommandSender sender, Command cmd, String cmdLabel, String[] args) throws Exception {
		if (args.length == 0) {
			sender.sendMessage(new String[]{
					_("mooPermsVersion", "?"),
					_("forHelp"),
					_("forSupport")
			});
			return;
		}

		String commandName = args[0].substring(0, 1).toUpperCase() + args[0].substring(1).toLowerCase();

		if(aliasMap.containsKey(commandName))
			commandName = aliasMap.get(commandName);

		AbstractCommand command = commands.get(commandName);
		if(command == null) {
			try {
				Class clazz = Class.forName("org.mooperms.commands.moo." + commandName);

				command = (AbstractCommand)clazz.newInstance();
				command.instance = instance;
			} catch (Exception e) {
				sender.sendMessage(_("forHelp"));
				return;
			}
		}

		String[] newargs = new String[args.length - 1];
		System.arraycopy(args, 1, newargs, 0, args.length - 1);

		command.onCommand(sender, cmd, cmdLabel, newargs);
	}
}
