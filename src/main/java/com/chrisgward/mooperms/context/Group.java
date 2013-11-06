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

package com.chrisgward.mooperms.context;

import com.chrisgward.mooperms.MooPerms;
import com.chrisgward.mooperms.api.storage.IGroup;
import com.chrisgward.mooperms.storage.World;
import lombok.Getter;

public class Group implements IGroup {

	@Getter private final String name;
	private final World world;
	private final com.chrisgward.mooperms.storage.Group group;
	private final MooPerms instance;

	public Group(MooPerms instance, String name, com.chrisgward.mooperms.storage.Group group, World world) {
		this.name = name;
		this.world = world;
		this.group = group;
		this.instance = instance;
	}


	@Override
	public String[] getInheritance() {
		if (world == null) {
			return group.getInheritance();
		} else {
			return group.getInheritance(world);
		}
	}

	@Override
	public String[] getPermissions() {
		if (world == null) {
			return group.getPermissions();
		} else {
			return group.getPermissions(world);
		}
	}

	@Override
	public String[] getAllPermissions() {
		if (world == null) {
			return group.getAllPermissions();
		} else {
			return group.getAllPermissions(world);
		}
	}

	@Override
	public String[] getUsers() {
		if (world == null) {
			return group.getUsers();
		} else {
			return group.getUsers(world);
		}
	}

	@Override
	public void addPermission(String permission) {
		if (world == null) {
			group.addPermission(permission);
		} else {
			group.addPermission(permission, world);
		}
		instance.updatePermissions(getUsers());
	}

	@Override
	public void removePermission(String permission) {
		if (world == null) {
			group.removePermission(permission);
		} else {
			group.removePermission(permission, world);
		}
		instance.updatePermissions(getUsers());
	}
}
