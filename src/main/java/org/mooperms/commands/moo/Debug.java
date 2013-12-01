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

import static org.mooperms.I18n._;

public class Debug extends AbstractCommand {
	@Override
	public void command(CommandSender sender, Command cmd, String cmdLabel, String[] args) throws Exception {
		boolean isDebug = !instance.getConfiguration().getConfig().isDebug();

		instance.getConfiguration().getConfig().setDebug(isDebug);
		sender.sendMessage(_("debugStatus", isDebug ? _("enabled") : _("disabled")));
	}
}
