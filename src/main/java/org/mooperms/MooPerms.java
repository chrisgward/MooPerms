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

import lombok.Getter;
import lombok.Setter;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.mcstats.Metrics;
import org.mooperms.api.IMooPerms;
import org.mooperms.commands.MooPermsCommandExecutor;
import org.mooperms.configuration.Configuration;
import org.mooperms.listener.PlayerListener;
import org.mooperms.storage.Group;
import org.mooperms.storage.User;
import org.mooperms.storage.World;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import static org.mooperms.I18n._;

public class MooPerms extends JavaPlugin implements IMooPerms {

	static MooPerms instance;

	@Getter private Configuration configuration;
	@Getter private boolean lock;
	@Getter @Setter private String defaultGroup;

	private Metrics metrics = null;

	@Getter private Map<String, User> userMap;
	@Getter private Map<String, Group> groupMap;
	private Map<String, World> worldMap;

	public void onEnable() {
		instance = this;

		try {
			configuration = new Configuration(this);
			configuration.loadConfiguration();

			groupMap = new HashMap<>();
			userMap = new HashMap<>();
			worldMap = new HashMap<>();

			for(Map.Entry<String, org.mooperms.configuration.groups.Group> entry : configuration.getGroups().getGroups().entrySet()) {
				debug("Loading group " + entry.getKey());
				groupMap.put(entry.getKey(), new Group(this, entry.getKey(), entry.getValue()));
			}

			for(Map.Entry<String, org.mooperms.configuration.users.User> entry : configuration.getUsers().getUsers().entrySet()) {
				debug("Loading user " + entry.getKey());
				userMap.put(entry.getKey(), new User(this, entry.getKey(), entry.getValue()));
			}
		} catch (Exception e) {
			getLogger().log(Level.SEVERE, e.getMessage(), e);
			lock = true;
		}
		try {

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
		} catch (Exception e) {
			lock = true;
			throw new RuntimeException(e);
		}

		getServer().getPluginManager().registerEvents(new PlayerListener(this), this);
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


	@Override
	public org.mooperms.context.User getUser(String name) {
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

	public void showError(Throwable exception, boolean isDebug) {
		if (!isDebug || getConfiguration().getConfig().isDebug()) {
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
					getUser(player).getUser().updatePermissions();
				}
			}
		});
	}


	@Override
	public org.mooperms.context.Group getGroup(String name) {
		Group group = groupMap.get(name);
		if(group == null) {
			debug("Group " + name + " not found");
			debug("Loaded groups:");
			for(String gr : groupMap.keySet()) {
				debug(gr);
			}
			debug("Done.");
			return null;
		} else {
			return group.getInContext(null);
		}
	}

	@Override
	public World getWorld(String worldName) {
		String mirror = getMirror(worldName);
		if(worldMap.containsKey(mirror)) {
			return worldMap.get(mirror);
		}
		else {
			World world = new World(this, mirror);
			worldMap.put(mirror, world);
			return world;
		}
	}

	@Override
	public void createGroup(String name) {
		Map<String, org.mooperms.configuration.groups.Group> groups = getConfiguration().getGroups().getGroups();

		if(groups.containsKey(name)) {
			throw new RuntimeException("Group already exists");
		}

		groups.put(name, new org.mooperms.configuration.groups.Group());
	}

	@Override
	public void removeGroup(String name) {
		// TODO
	}

	@Override
	public String[] getGroups() {
		// TODO
		return new String[0];
	}

	private String getMirror(String world) {
		for(Map.Entry<String, List<String>> parent : getConfiguration().getConfig().getMirrors().entrySet()) {
			if(parent.getValue().contains(world))
				return parent.getKey();
		}
		return world;
	}
}
