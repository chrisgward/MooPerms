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
import com.chrisgward.mooperms.api.storage.IUser;
import com.chrisgward.mooperms.configuration.users.World;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.permissions.PermissionAttachmentInfo;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

public class User implements IUser {
	private final MooPerms instance;
	private com.chrisgward.mooperms.configuration.users.User user;

	public User(MooPerms instance, com.chrisgward.mooperms.configuration.users.User config) {
		this.instance = instance;
		this.user = user;
	}

	@Override
	public Group getGroup() {
		return instance.getGroup(user.getGroup());
	}

	@Override
	public Group getGroup(Player player) {
		Map<String, World> worlds = user.getWorlds();
		String worldname = player.getWorld().getName().toLowerCase();
		if (worlds != null && worlds.containsKey(worldname)) {
			World world = worlds.get(worldname);

			if (world.getGroup() != null)
				return instance.getGroup(world.getGroup());
		}
		return getGroup();
	}

	@Override
	public Group[] getSubgroups() {
		return new Group[0];
	}

	@Override
	public String[] getPermissions() {
		return new String[0];  //To change body of implemented methods use File | Settings | File Templates.
	}

	@Override
	public String[] getAllPermissions() {
		Set<String> perms = new LinkedHashSet<>();
		perms.addAll(Arrays.asList(getPermissions()));
		perms.addAll(getGroup().getAllPermissions());
		for (Group subgroup : getSubgroups()) {
			perms.addAll(subgroup.getAllPermissions());
		}

		return perms.toArray(new String[perms.size()]);
	}


	public void updatePermissions(Player player) {
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
}
