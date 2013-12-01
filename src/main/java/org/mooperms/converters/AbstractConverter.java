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

package org.mooperms.converters;

import lombok.Getter;
import org.mooperms.MooPerms;
import org.mooperms.configuration.config.Config;
import org.mooperms.configuration.groups.Groups;
import org.mooperms.configuration.users.Users;
import org.reflections.Reflections;

import java.io.File;
import java.util.Set;

public abstract class AbstractConverter {
	public AbstractConverter(String folder) {
		this.folder = folder;
	}

	public boolean canConvert() {
		if (configDirectory != null && configDirectory.exists() && configDirectory.isDirectory()) {
			if (!lock.exists()) {
				return true;
			}
		}
		return false;
	}

	public abstract void doConversion();

	@Getter protected Config config;
	@Getter protected Groups groups;
	@Getter protected Users users;
	protected MooPerms instance;
	@Getter protected File configDirectory;

	protected abstract String getName();

	private String folder;
	private File lock;

	public static boolean convert(MooPerms instance) {
		Reflections reflections = new Reflections("org.mooperms");
		Set<Class<? extends AbstractConverter>> classes = reflections.getSubTypesOf(AbstractConverter.class);

		for (Class<? extends AbstractConverter> clazz : classes) {
			try {
				AbstractConverter converter = clazz.newInstance();
				converter.instance = instance;
				converter.configDirectory = new File(instance.getDataFolder().getParentFile(), converter.folder);
				converter.lock = new File(converter.configDirectory, ".mooperms");
				if (converter.canConvert()) {
					converter.lock.createNewFile();
					instance.getLogger().info("Importing permissions from " + converter.getName());
					converter.doConversion();
					instance.getConfiguration().setUsers(converter.getUsers());
					instance.getConfiguration().setGroups(converter.getGroups());
					instance.getConfiguration().setConfig(converter.getConfig());
					instance.getConfiguration().saveConfiguration();
					instance.reloadConfig();
					instance.getLogger().info("Appears to have worked!");
					converter.lock.delete();
					return true;
				}
			} catch (Exception e) {
				instance.showError(e, false);
			}
		}

		return false;
	}
}
