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
import org.mooperms.commands.AbstractCommand;
import org.mooperms.converters.AbstractConverter;

public class Import extends AbstractCommand {
	@Override
	public void command(CommandSender sender, Command cmd, String cmdLabel, String[] args) {
		if (AbstractConverter.convert(instance)) {
			sender.sendMessage("Conversion completed successfully");
		} else {
			sender.sendMessage("Conversion failed. See console for more information.");
		}
	}
}
