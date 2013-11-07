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

package com.chrisgward.mooperms.commands;

import com.chrisgward.mooperms.converters.AbstractConverter;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import static com.chrisgward.mooperms.I18n._;

public class Commandmoo extends AbstractCommand {
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String cmdLabel, String[] args) {
		if(args.length == 0) {
			sender.sendMessage(new String[] {
					_("mooPermsVersion", "?"),
					_("forHelp"),
					_("forSupport")
			});
			return true;
		}

		switch (args[0].toLowerCase()) {
			case "help":

				break;
			case "import":
				if(AbstractConverter.convert(instance)) {
					sender.sendMessage("Conversion completed successfully");
				} else {
					sender.sendMessage("Conversion failed. See console for more information.");
				}
				break;
			default:
				sender.sendMessage(new String[] {
						_("unrecognizedCommand", args[0]),
						_("forHelp")});
				break;
		}
		return true;
	}
}
