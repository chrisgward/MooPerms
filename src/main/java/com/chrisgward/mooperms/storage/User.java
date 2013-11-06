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

package com.chrisgward.mooperms.storage;

import com.chrisgward.mooperms.MooPerms;
import com.chrisgward.mooperms.api.IGroup;
import com.chrisgward.mooperms.api.IUser;
import com.chrisgward.mooperms.configuration.users.World;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.permissions.PermissionAttachmentInfo;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

public class User {
	private final MooPerms instance;
	@Getter private final String name;
	private final com.chrisgward.mooperms.configuration.users.User user;

	public User(MooPerms instance, String name, com.chrisgward.mooperms.configuration.users.User user) {
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
		return new String[0];
	}

	public String[] getSubgroups(String world) {
		return new String[0];
	}

	public String[] getPermissions() {
		return new String[0];
	}

	public String[] getPermissions(String world) {
		return new String[0];
	}

	public String[] getAllPermissions() {
		Set<String> perms = new LinkedHashSet<>();
		perms.addAll(Arrays.asList(getPermissions()));
		perms.addAll(Arrays.asList(instance.getGroup(getGroup()).getEffectivePermissions()));
		for (String subgroup : getSubgroups()) {
			perms.addAll(Arrays.asList(instance.getGroup(subgroup).getAllPermissions()));
		}

		return perms.toArray(new String[perms.size()]);
	}

	public String[] getAllPermissions(String world) {
		return new String[0];
	}

	public void updatePermissions() {
		Player player = instance.getServer().getPlayer(getName());
		if (player == null) {
			return;
		}
		String[] permissions = getAllPermissions();

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

	public IUser getInContext(String world) {
		return new com.chrisgward.mooperms.context.User(instance, getName(), this, (com.chrisgward.mooperms.storage.World) instance.getWorld(world));
	}

	public void addPermission(String permission) {

	}

	public void addPermission(String permission, com.chrisgward.mooperms.storage.World world) {

	}

	public void removePermission(String permission) {

	}

	public void removePermission(String permission, com.chrisgward.mooperms.storage.World world) {

	}

	public void setGroup(String group) {

	}

	public void setGroup(String group, com.chrisgward.mooperms.storage.World world) {

	}

	public void addSubgroup(String group) {

	}

	public void removeSubgroup(String group) {

	}

	public void addSubgroup(String group, com.chrisgward.mooperms.storage.World world) {

	}

	public void removeSubgroup(String group, com.chrisgward.mooperms.storage.World world) {

	}

	public String[] getAllSubgroups(String name) {
		return new String[0];
	}

	public String[] getEffectivePermissions() {
		return new String[0];
	}

	public String[] getEffectivePermissions(String name) {
		return new String[0];
	}
}
