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

package org.mooperms.storage;

import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.mooperms.MooPerms;
import org.mooperms.configuration.users.World;

import java.util.*;

public class User {
	private final MooPerms instance;
	@Getter private final String name;
	private final org.mooperms.configuration.users.User user;

	public User(MooPerms instance, String name, org.mooperms.configuration.users.User user) {
		this.instance = instance;
		this.name = name;
		this.user = user;
	}

	public String getGroup() {
		return user.getGroup();
	}

	public String getGroup(String worldName) {
		Map<String, World> worlds = user.getWorlds();
		World world = worlds.get(worldName);
		if(world == null) {
			return null;
		}
		String group = world.getGroup();
		if(world.getGroup() == null) {
			return null;
		}
		return world.getGroup();
	}

	public String[] getSubgroups() {
		return user.getSubgroups().toArray(new String[user.getSubgroups().size()]);
	}

	public String[] getSubgroups(String worldName) {
		Map<String, World> worlds = user.getWorlds();
		World world = worlds.get(worldName);
		if(world == null) {
			return new String[0];
		}

		List<String> subgroups = world.getSubgroups();
		if(subgroups == null || subgroups.size() == 0) {
			return new String[0];
		}

		return subgroups.toArray(new String[subgroups.size()]);
	}

	public String[] getPermissions() {
		return user.getPermissions().toArray(new String[user.getPermissions().size()]);
	}

	public String[] getPermissions(String worldName) {
		Map<String, World> worlds = user.getWorlds();
		World world = worlds.get(worldName);
		if(world == null) {
			return new String[0];
		}

		List<String> permissions = world.getPermissions();
		if(permissions == null || permissions.size() == 0) {
			return new String[0];
		}

		return permissions.toArray(new String[permissions.size()]);
	}

	public String[] getAllPermissions(String world) {
		Set<String> perms = new LinkedHashSet<>();
		perms.addAll(Arrays.asList(getPermissions()));
		perms.addAll(Arrays.asList(getPermissions(world)));
		return perms.toArray(new String[perms.size()]);
	}

	public void updatePermissions() {
		instance.debug("Updating permissions for player " + getName());
		Player player = instance.getServer().getPlayer(getName());
		if (player == null) {
			return;
		}
		String[] permissions = getEffectivePermissions(player.getWorld().getName());

		Set<PermissionAttachmentInfo> effectivePerms = player.getEffectivePermissions();
		PermissionAttachment attachment = null;
		for (PermissionAttachmentInfo info : effectivePerms) {
			PermissionAttachment attach = info.getAttachment();
			if (attach != null && attach.getPlugin() instanceof MooPerms) {
				attachment = attach;
				break;
			}
		}

		if (attachment == null) {
			attachment = player.addAttachment(instance);
		}

		for (Map.Entry<String, Boolean> permission : attachment.getPermissions().entrySet()) {
			attachment.unsetPermission(permission.getKey());
		}

		for (String permission : permissions) {
			boolean value = true;
			if (permission.startsWith("-")) {
				value = false;
				permission = permission.substring(1);
			}

			attachment.setPermission(permission, value);
		}

		player.recalculatePermissions();
	}

	public org.mooperms.context.User getInContext(String world) {
		return new org.mooperms.context.User(instance, getName(), this, instance.getWorld(world));
	}

	public void addPermission(String permission) {
		if(permission.startsWith("-")) {
			removePermission(permission.substring(1));
			return;
		}

		List<String> permissions = user.getPermissions();

		if(permissions.contains("-" + permission)) {
			permissions.remove("-" + permission);
		} else {
			permissions.add(permission);
		}

		updatePermissions();
	}

	public void addPermission(String permission, String worldName) {
		if(permission.startsWith("-")) {
			removePermission(permission.substring(1), worldName);
			return;
		}
		World world = user.getWorlds().get(worldName);
		if(world == null) {
			world = new World();
			user.getWorlds().put(worldName, world);
		}

		if(world.getPermissions().contains("-" + permission)) {
			world.getPermissions().remove("-" + permission);
		} else {
			world.getPermissions().add(permission);
		}
		updatePermissions();
	}

	public void removePermission(String permission) {
		if(permission.startsWith("-")) {
			addPermission(permission.substring(1));
			return;
		}

		List<String> permissions = user.getPermissions();

		if(permissions.contains(permission)) {
			permissions.remove(permission);
		} else {
			permissions.add("-" + permission);
		}

		updatePermissions();
	}

	public void removePermission(String permission, String worldName) {
		if(permission.startsWith("-")) {
			addPermission(permission.substring(1), worldName);
			return;
		}
		World world = user.getWorlds().get(worldName);
		if(world == null) {
			world = new World();
			user.getWorlds().put(worldName, world);
		}

		List<String> permissions = world.getPermissions();

		if(permissions.contains(permission)) {
			permissions.remove(permission);
		} else {
			permissions.add("-" + permission);
		}

		updatePermissions();
	}

	public void setGroup(String group) {
		user.setGroup(group);

		updatePermissions();
	}

	public void setGroup(String group, String worldName) {
		World world = user.getWorlds().get(worldName);
		if(world == null) {
			if(group == null) {
				return;
			}
			world = new World();
			user.getWorlds().put(worldName, world);
		}
		world.setGroup(group);
		updatePermissions();
	}

	public void addSubgroup(String group) {
		user.getSubgroups().add(group);
		updatePermissions();
	}

	public void addSubgroup(String group, String worldName) {
		World world = user.getWorlds().get(worldName);
		if(world == null) {
			world = new World();
			user.getWorlds().put(worldName, world);
		}

		world.getSubgroups().add(group);

		updatePermissions();
	}

	public void removeSubgroup(String group) {
		user.getSubgroups().remove(group);
		updatePermissions();
	}

	public void removeSubgroup(String group, String worldName) {
		World world = user.getWorlds().get(worldName);
		if(world == null) {
			world = new World();
			user.getWorlds().put(worldName, world);
		}

		List<String> subgroups = world.getSubgroups();
		if(subgroups.contains(group)) {
			subgroups.remove(group);
		} else {
			subgroups.add("-" + group);
		}

		updatePermissions();
	}

	public String[] getAllSubgroups(String worldName) {
		Set<String> subgroups = new LinkedHashSet<>();
		subgroups.addAll(Arrays.asList(getSubgroups()));
		subgroups.addAll(Arrays.asList(getSubgroups(worldName)));
		return new String[0];
	}

	public String[] getEffectivePermissions() {
		Set<String> perms = new LinkedHashSet<>();
		perms.addAll(Arrays.asList(getPermissions()));
		perms.addAll(Arrays.asList(instance.getGroup(getGroup()).getEffectivePermissions()));

		for(String group : getSubgroups()) {
			perms.addAll(Arrays.asList(instance.getGroup(group).getEffectivePermissions()));
		}

		return perms.toArray(new String[perms.size()]);
	}

	public String[] getEffectivePermissions(String worldName) {
		Set<String> perms = new LinkedHashSet<>();
		perms.addAll(Arrays.asList(getAllPermissions(worldName)));
		perms.addAll(Arrays.asList(instance.getWorld(worldName).getGroup(getGroup()).getEffectivePermissions()));

		for(String group : getSubgroups(worldName)) {
			perms.addAll(Arrays.asList(instance.getWorld(group).getGroup(getGroup()).getEffectivePermissions()));
		}

		String[] permissions = perms.toArray(new String[perms.size()]);
		instance.debug("Listing " + getName() + "'s permissions");
		for(String s : permissions) {
			instance.debug(s);
		}
		instance.debug("Done.");
		return permissions;
	}
}
