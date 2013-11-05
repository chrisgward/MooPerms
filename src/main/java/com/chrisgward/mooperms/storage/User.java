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
import com.chrisgward.mooperms.api.storage.IGroup;
import com.chrisgward.mooperms.api.storage.IUser;
import com.chrisgward.mooperms.configuration.users.World;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.permissions.PermissionAttachmentInfo;
import sun.org.mozilla.javascript.commonjs.module.Require;

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

	public IGroup getGroup() {
		return instance.getGroup(user.getGroup());
	}

	public IGroup getGroup(String worldName) {
		Map<String, World> worlds = user.getWorlds();
		for(Map.Entry<String, World> world : worlds.entrySet()) {
			if(world.getKey().equalsIgnoreCase(worldName)) {
				String group = world.getValue().getGroup();
				if(group != null) {
					return instance.getWorld(world.getKey()).getGroup(group);
				}
			}
		}
		return null;
	}

	public IGroup[] getSubgroups() {
		return new IGroup[0];
	}

	public IGroup[] getSubgroups(String world) {
		return new IGroup[0];
	}

	public String[] getPermissions() {
		return new String[0];  //To change body of implemented methods use File | Settings | File Templates.
	}

	public String[] getPermissions(String world) {
		return new String[0];  //To change body of implemented methods use File | Settings | File Templates.
	}

	public String[] getAllPermissions() {
		Set<String> perms = new LinkedHashSet<>();
		perms.addAll(Arrays.asList(getPermissions()));
		perms.addAll(getGroup().getAllPermissions());
		for (IGroup subgroup : getSubgroups()) {
			perms.addAll(subgroup.getAllPermissions());
		}

		return perms.toArray(new String[perms.size()]);
	}

	public String[] getAllPermissions(String world) {
		return new String[0];  //To change body of implemented methods use File | Settings | File Templates.
	}

	public void updatePermissions() {
		Player player = instance.getServer().getPlayer(getName());
		if(player == null) {
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
		return new com.chrisgward.mooperms.context.User(getName(), (com.chrisgward.mooperms.storage.World)instance.getWorld(world), this);
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
}
