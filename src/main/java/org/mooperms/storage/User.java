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

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

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
		for (Map.Entry<String, World> world : worlds.entrySet()) {
			if (world.getKey().equalsIgnoreCase(worldName)) {
				String group = world.getValue().getGroup();
				if (group != null) {
					return group;
				}
			}
		}
		return null;
	}

	public String[] getSubgroups() {
		return user.getSubgroups().toArray(new String[user.getSubgroups().size()]);
	}

	public String[] getSubgroups(String world) {
		return new String[0];
	}

	public String[] getPermissions() {
		return user.getPermissions().toArray(new String[user.getPermissions().size()]);
	}

	public String[] getPermissions(String world) {
		return user.getWorlds().get(world).getPermissions().toArray(new String[user.getWorlds().get(world).getPermissions().size()]);
	}

	public String[] getAllPermissions() {
		return getPermissions();
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
		// TODO
		updatePermissions();
	}

	public void addPermission(String permission, org.mooperms.storage.World world) {
		// TODO
		updatePermissions();
	}

	public void removePermission(String permission) {
		// TODO
		updatePermissions();
	}

	public void removePermission(String permission, org.mooperms.storage.World world) {
		// TODO
		updatePermissions();
	}

	public void setGroup(String group) {
		// TODO
		updatePermissions();
	}

	public void setGroup(String group, org.mooperms.storage.World world) {
		// TODO
		updatePermissions();
	}

	public void addSubgroup(String group) {
		// TODO
		updatePermissions();
	}

	public void removeSubgroup(String group) {
		// TODO
		updatePermissions();
	}

	public void addSubgroup(String group, org.mooperms.storage.World world) {
		// TODO
		updatePermissions();
	}

	public void removeSubgroup(String group, org.mooperms.storage.World world) {
		// TODO
		updatePermissions();
	}

	public String[] getAllSubgroups(String name) {
		// TODO
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

	public String[] getEffectivePermissions(String name) {
		Set<String> perms = new LinkedHashSet<>();
		perms.addAll(Arrays.asList(getAllPermissions(name)));
		perms.addAll(Arrays.asList(instance.getWorld(name).getGroup(getGroup()).getEffectivePermissions()));

		for(String group : getSubgroups()) {
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
