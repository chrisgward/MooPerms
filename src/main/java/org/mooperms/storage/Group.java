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
import org.mooperms.MooPerms;

public class Group {
	private final MooPerms instance;
	@Getter private final String name;
	private final org.mooperms.configuration.groups.Group group;

	public Group(MooPerms instance, String name, org.mooperms.configuration.groups.Group group) {
		this.instance = instance;
		this.name = name;
		this.group = group;
	}


	public String[] getInheritance() {
		return group.getInheritance().toArray(new String[group.getInheritance().size()]);
	}

	public String[] getInheritance(String world) {
		// TODO
		return null;
	}

	public String[] getPermissions() {
		return group.getPermissions().toArray(new String[group.getPermissions().size()]);
	}

	public String[] getUsers() {
		// TODO
		return null;
	}

	public String[] getUsers(String world) {
		// TODO
		return null;
	}

	public void addPermission(String permission) {
		// TODO
	}

	public void addPermission(String permission, World world) {
		// TODO
	}

	public void removePermission(String permission) {
		// TODO
	}

	public void removePermission(String permission, World world) {
		// TODO
	}

	public String[] getPermissions(String world) {
		// TODO
		return null;
	}

	public String[] getAllPermissions(String world) {
		// TODO
		return new String[0];
	}

	public String[] getEffectivePermissions() {
		// TODO
		return new String[0];
	}

	public String[] getEffectivePermissions(String world) {
		// TODO
		return new String[0];
	}

	public String[] getAllInheritance(World world) {
		// TODO
		return new String[0];
	}

	public org.mooperms.context.Group getInContext(String world) {
		return new org.mooperms.context.Group(instance, getName(), this, instance.getWorld(world));
	}
}
