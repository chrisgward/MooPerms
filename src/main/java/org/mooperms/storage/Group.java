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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
		org.mooperms.configuration.groups.World w = group.getWorlds().get(world);
		if(w == null) {
			return new String[0];
		}
		List<String> permissions = w.getPermissions();
		if(permissions == null || permissions.size() == 0) {
			return new String[0];
		}
		return permissions.toArray(new String[permissions.size()]);
	}

	public String[] getAllInheritance(String world) {
		List<String> inheritance = new ArrayList<String>();
		inheritance.addAll(Arrays.asList(getInheritance()));
		inheritance.addAll(Arrays.asList(getInheritance(world)));
		return inheritance.toArray(new String[inheritance.size()]);
	}

	public String[] getPermissions() {
		return group.getPermissions().toArray(new String[group.getPermissions().size()]);
	}


	public String[] getPermissions(String world) {
		org.mooperms.configuration.groups.World w = group.getWorlds().get(world);
		if(w == null) {
			return new String[0];
		}
		List<String> permissions = w.getPermissions();
		if(permissions == null || permissions.size() == 0) {
			return new String[0];
		}
		return permissions.toArray(new String[permissions.size()]);
	}

	public String[] getAllPermissions(String world) {
		List<String> perms = new ArrayList<>();
		perms.addAll(Arrays.asList(getPermissions()));
		perms.addAll(Arrays.asList(getPermissions(world)));
		return perms.toArray(new String[perms.size()]);
	}

	public String[] getEffectivePermissions() {
		List<String> perms = new ArrayList<>();
		perms.addAll(Arrays.asList(getPermissions()));
		for(String group : getInheritance()) {
			perms.addAll(Arrays.asList(instance.getGroup(group).getEffectivePermissions()));
		}

		return perms.toArray(new String[perms.size()]);
	}

	public String[] getEffectivePermissions(String world) {
		List<String> perms = new ArrayList<>();
		perms.addAll(Arrays.asList(getEffectivePermissions()));
		for(String group : getInheritance(world)) {
			perms.addAll(Arrays.asList(instance.getWorld(world).getGroup(group).getEffectivePermissions()));
		}

		return perms.toArray(new String[perms.size()]);
	}

	public String[] getUsers() {
		// TODO
		return new String[0];
	}

	public String[] getUsers(String world) {
		// TODO
		return new String[0];
	}

	public void addPermission(String permission) {
		if(permission.startsWith("-")) {
			removePermission(permission.substring(1));
			return;
		}

		List<String> permissions = group.getPermissions();

		if(permissions.contains(permission)) {
			throw new IllegalArgumentException("Group already has this permission.");
		} else {
			if(permissions.contains("-" + permission)) {
				permissions.remove("-" + permission);
			} else {
				permissions.add(permission);
			}

			instance.updatePermissions();
		}
	}

	public void addPermission(String permission, String world) {
		if(permission.startsWith("-")) {
			removePermission(permission.substring(1), world);
			return;
		}

		org.mooperms.configuration.groups.World w = group.getWorlds().get(world);
		if(w == null) {
			w = new org.mooperms.configuration.groups.World();
			group.getWorlds().put(world.toLowerCase(), w);
		}

		List<String> permissions = w.getPermissions();
		if(permissions.contains(permission)) {
			throw new IllegalArgumentException("Group already has this permission.");
		} else {
			if(permissions.contains("-" + permission)) {
				permissions.remove("-" + permission);
			} else {
				permissions.add(permission);
			}
			instance.updatePermissions();
		}
	}

	public void removePermission(String permission) {
		if(permission.startsWith("-")) {
			addPermission(permission.substring(1));
			return;
		}

		List<String> permissions = group.getPermissions();

		instance.updatePermissions();
	}

	public void removePermission(String permission, String world) {
		if(permission.startsWith("-")) {
			addPermission(permission.substring(1), world);
			return;
		}

		org.mooperms.configuration.groups.World w = group.getWorlds().get(world);
		if(w == null) {
			w = new org.mooperms.configuration.groups.World();
			group.getWorlds().put(world.toLowerCase(), w);
		}

		w.getPermissions().add(permission);

		instance.updatePermissions();
	}

	public org.mooperms.context.Group getInContext(String world) {
		return new org.mooperms.context.Group(instance, getName(), this, instance.getWorld(world));
	}
}
