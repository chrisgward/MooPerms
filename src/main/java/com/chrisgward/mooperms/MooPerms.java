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

package com.chrisgward.mooperms;

import com.chrisgward.mooperms.api.IMooPerms;
import com.chrisgward.mooperms.commands.MooPermsCommandExecutor;
import com.chrisgward.mooperms.configuration.Configuration;
import com.chrisgward.mooperms.storage.Group;
import com.chrisgward.mooperms.storage.User;
import com.chrisgward.mooperms.storage.World;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.mcstats.Metrics;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

import static com.chrisgward.mooperms.I18n._;

public class MooPerms extends JavaPlugin implements IMooPerms {

	@Getter private Configuration configuration;
	@Getter private boolean lock;
	@Getter @Setter private String defaultGroup;

	private Metrics metrics = null;

	static MooPerms instance;

	public void onEnable() {
		instance = this;

		I18n.load();

		configuration = new Configuration(this);
		try {
			configuration.loadConfiguration();
		} catch (Exception e) {
			configuration = null;
			showError(e, true);
			lock = true;
		}

		String locale = configuration.getConfig().getLocale();
		if (locale != null) {
			I18n.setLocale(locale);
		}

		if (metrics == null) {
			try {
				metrics = new Metrics(this);
				metrics.start();
			} catch (IOException e) {
				showError(e, false);
			}
		}

		for (Map.Entry<String, com.chrisgward.mooperms.configuration.groups.Group> group : getConfiguration().getGroups().getGroups().entrySet()) {
			if (group.getValue().isDefault())
				defaultGroup = group.getKey();
		}

		if (defaultGroup == null) {
			throw new RuntimeException("No default group.");
		}

		userMap = new HashMap<>();
	}

	public void onDisable() {
		try {
			configuration.saveConfiguration();
		} catch (Exception e) {
			lock = true;

			getLogger().log(Level.SEVERE, _("couldNotSave"), e);
		}

		configuration = null;
		userMap = null;
	}

	MooPermsCommandExecutor executor = new MooPermsCommandExecutor(this);
	public boolean onCommand(CommandSender sender, Command cmd, String cmdLabel, String[] args) {
		return executor.onCommand(sender, cmd, cmdLabel, args);
	}

	private Map<String, User> userMap = new HashMap<>();

	@Override
	public User getUser(String name) {
		if (userMap.containsKey(name))
			return userMap.get(name);

		com.chrisgward.mooperms.configuration.users.User config = getConfiguration().getUsers().getUsers().get(name);

		if (config == null) {
			config = new com.chrisgward.mooperms.configuration.users.User();
			config.setGroup(getDefaultGroup());
		}

		User user = new User(this, config);
		userMap.put(name, user);

		return user;
	}

	@Override
	public User getUser(Player player) {
		return getUser(player.getName());
	}

	public void disposeUser(String name) {
		userMap.remove(name);
	}

	public void disposeUser(Player player) {
		disposeUser(player.getName());
	}

	private Map<String, Group> groupMap = new HashMap<>();
	@Override
	public Group getGroup(String name) {
		return null;
	}

	public void debug(String text) {
		if (getConfiguration().getConfig().isDebug()) {
			getLogger().info(text);
		}
	}

	@Override
	public void showError(Exception exception, boolean isDebug) {

	}
}
