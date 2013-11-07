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

package org.mooperms;

import org.mooperms.api.IMooPerms;
import org.mooperms.api.IGroup;
import org.mooperms.api.IUser;
import org.mooperms.api.IWorld;
import org.mooperms.commands.MooPermsCommandExecutor;
import org.mooperms.configuration.Configuration;
import org.mooperms.storage.Group;
import org.mooperms.storage.User;
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

import static org.mooperms.I18n._;

public class MooPerms extends JavaPlugin implements IMooPerms {

	@Getter private Configuration configuration;
	@Getter private boolean lock;
	@Getter @Setter private String defaultGroup;

	private Metrics metrics = null;

	static MooPerms instance;

	public void onEnable() {
		try {
			instance = this;

			configuration = new Configuration(this);
			configuration.loadConfiguration();

			I18n.load();

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

			for (Map.Entry<String, org.mooperms.configuration.groups.Group> group : getConfiguration().getGroups().getGroups().entrySet()) {
				if (group.getValue().isDefault()) {
					if (defaultGroup == null) {
						defaultGroup = group.getKey();
					} else {
						throw new RuntimeException("More than one default group listed!");
					}
				}
			}

			if (defaultGroup == null) {
				throw new RuntimeException("No default group.");
			}

			userMap = new HashMap<>();
			groupMap = new HashMap<>();
		} catch (Exception e) {
			lock = true;
			throw new RuntimeException(e);
		}
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
	public IUser getUser(String name) {
		if (userMap.containsKey(name)) {
			return userMap.get(name).getInContext(null);
		}

		org.mooperms.configuration.users.User config = getConfiguration().getUsers().getUsers().get(name);

		if (config == null) {
			config = new org.mooperms.configuration.users.User();
			config.setGroup(getDefaultGroup());
		}

		User user = new User(this, name, config);
		userMap.put(name, user);

		return user.getInContext(null);
	}

	public void disposeUser(String name) {
		userMap.remove(name);
	}

	public void disposeUser(Player player) {
		disposeUser(player.getName());
	}


	public void debug(String text) {
		if (getConfiguration().getConfig().isDebug()) {
			getLogger().info(text);
		}
	}

	public void showError(Exception exception, boolean isDebug) {
		if (getConfiguration().getConfig().isDebug() || !isDebug) {
			getLogger().log(Level.SEVERE, exception.getMessage(), exception);
		}
	}

	public void updatePermissions(final User user) {
		getServer().getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
			@Override
			public void run() {
				user.updatePermissions();
			}
		});
	}

	public void updatePermissions(final String[] users) {
		getServer().getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
			@Override
			public void run() {
				for (String player : users) {
					((User) getUser(player)).updatePermissions();
				}
			}
		});
	}

	private Map<String, Group> groupMap = new HashMap<>();

	@Override
	public IGroup getGroup(String name) {
		return new org.mooperms.context.Group(this, name, groupMap.get(name), null);
	}

	@Override
	public IWorld getWorld(String world) {
		return null;
	}

	@Override
	public void createGroup(String name) {

	}

	@Override
	public void removeGroup(String name) {

	}

	@Override
	public String[] getGroups() {
		return new String[0];
	}
}
