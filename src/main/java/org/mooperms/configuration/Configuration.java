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

package org.mooperms.configuration;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.mooperms.MooPerms;
import org.mooperms.configuration.config.Config;
import org.mooperms.configuration.groups.Groups;
import org.mooperms.configuration.users.Users;
import org.yaml.snakeyaml.Yaml;

import java.io.*;

import static org.mooperms.I18n._;

@SuppressWarnings({"DuplicateStringLiteralInspection", "HardCodedStringLiteral", "StringConcatenation"})
public class Configuration {
	private final MooPerms instance;
	private final File folder;
	private final Yaml yaml = new Yaml();

	public Configuration(MooPerms instance) {
		this.instance = instance;
		folder = instance.getDataFolder();
		configLocation = new File(folder, "config.yml");
		groupsLocation = new File(folder, "groups.yml");
		usersLocation = new File(folder, "users.yml");
	}

	@Getter @Setter private Config config;
	@Getter @Setter private Groups groups;
	@Getter @Setter private Users users;
	private final File configLocation;
	private long configLoadTime;
	private final File groupsLocation;
	private long groupsLoadTime;
	private final File usersLocation;
	private long usersLoadTime;

	public void loadConfiguration() throws IOException {
		boolean failed = false;
		try {
			loadConfig();
		} catch (Exception e) {
			instance.showError(e, false);
			failed = true;
		}
		try {
			loadGroups();
		} catch (Exception e) {
			instance.showError(e, false);
			failed = true;
		}
		try {
			loadUsers();
		} catch (Exception e) {
			instance.showError(e, false);
			failed = true;
		}
		if (failed) {
			throw new IOException("Could not load configuration!");
		}
	}

	public void loadConfig() throws IOException {
		copyDefaults("config.yml");
		setConfig(loadFile(Config.class, configLocation));
		configLoadTime = System.currentTimeMillis();
	}

	public void loadGroups() throws IOException {
		copyDefaults("groups.yml");
		setGroups(loadFile(Groups.class, groupsLocation));
		groupsLoadTime = System.currentTimeMillis();
	}

	public void loadUsers() throws IOException {
		copyDefaults("users.yml");
		setUsers(loadFile(Users.class, usersLocation));
		usersLoadTime = System.currentTimeMillis();
	}

	public <T> T loadFile(Class<T> clazz, File file) throws IOException {
		return yaml.loadAs(new InputStreamReader(new FileInputStream(file)), clazz);
	}

	public void saveConfiguration() throws IOException {
		saveConfig();
		saveGroups();
		saveUsers();
	}

	public void saveConfig() throws IOException {
		if (configLocation.lastModified() > configLoadTime) {
			fileNewer("config.yml");
			return;
		}
		saveFile(getConfig(), configLocation);
	}

	public void saveGroups() throws IOException {
		if (groupsLocation.lastModified() > groupsLoadTime) {
			fileNewer("groups.yml");
			return;
		}
		saveFile(getGroups(), groupsLocation);
	}

	public void saveUsers() throws IOException {
		if (usersLocation.lastModified() > usersLoadTime) {
			fileNewer("users.yml");
			return;
		}
		saveFile(getUsers(), usersLocation);
	}

	public void saveFile(Object obj, File file) throws IOException {
		yaml.dump(obj, new OutputStreamWriter(new FileOutputStream(file)));
	}

	@SuppressWarnings("ResultOfMethodCallIgnored")
	public void mkdir() throws IOException {
		if (!folder.exists()) {
			folder.mkdirs();
		}
	}

	public void copyDefaults(String location) throws IOException {
		mkdir();
		File file = new File(folder, location);

		if (!file.exists()) {
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file)));
			BufferedReader reader = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("/" + location)));

			while (reader.ready()) {
				writer.write(reader.read());
			}
			reader.close();
			writer.close();
		}
	}

	public void fileNewer(String file) {
		for (Player player : instance.getServer().getOnlinePlayers()) {
			if (player.hasPermission("mooperms.warnings")) {
				player.sendMessage(_("fileNewer", file));
			}
		}

		instance.getLogger().severe(_("fileNewerConsole"));
		instance.getLogger().severe(_("fileNewerCommands"));
	}
}
