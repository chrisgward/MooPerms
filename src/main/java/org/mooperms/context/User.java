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

package org.mooperms.context;

import org.mooperms.MooPerms;
import org.mooperms.api.IUser;
import org.mooperms.storage.World;
import lombok.Getter;

public class User implements IUser {

	@Getter private final String name;
	private final World world;
	@Getter private final org.mooperms.storage.User user;
	private final MooPerms instance;

	public User(MooPerms instance, String name, org.mooperms.storage.User user, World world) {
		this.name = name;
		this.world = world;
		this.user = user;
		this.instance = instance;
	}

	@Override
	public String getGroup() {
		if (world == null) {
			return user.getGroup();
		}
		return user.getGroup(world.getName());
	}

	@Override
	public String[] getSubgroups() {
		if (world == null) {
			return user.getSubgroups();
		}
		return user.getSubgroups(world.getName());
	}

	@Override
	public String[] getAllSubgroups() {
		if (world == null) {
			return getSubgroups();
		} else {
			return user.getAllSubgroups(world.getName());
		}
	}

	@Override
	public void addSubgroup(String group) {
		if (world == null) {
			user.addSubgroup(group);
		} else {
			user.addSubgroup(group, world);
		}
		instance.updatePermissions(user);
	}

	@Override
	public void removeSubgroup(String group) {
		if (world == null) {
			user.removeSubgroup(group);
		} else {
			user.removeSubgroup(group, world);
		}
		instance.updatePermissions(user);
	}

	@Override
	public String[] getPermissions() {
		if (world == null) {
			return user.getPermissions();
		}
		return user.getPermissions(world.getName());
	}

	@Override
	public String[] getAllPermissions() {
		if (world == null) {
			return user.getPermissions();
		}
		return user.getAllPermissions(world.getName());
	}

	@Override
	public String[] getEffectivePermissions() {
		if (world == null) {
			return user.getEffectivePermissions();
		}
		return user.getEffectivePermissions(world.getName());
	}

	@Override
	public void addPermission(String permission) {
		if (world == null) {
			user.addPermission(permission);
		} else {
			user.addPermission(permission, world);
		}
		instance.updatePermissions(user);
	}

	@Override
	public void removePermission(String permission) {
		if (world == null) {
			user.removePermission(permission);
		} else {
			user.removePermission(permission, world);
		}
		instance.updatePermissions(user);
	}

	@Override
	public void setGroup(String group) {
		if (world == null) {
			user.setGroup(group);
		} else {
			user.setGroup(group, world);
		}
		instance.updatePermissions(user);
	}
}
