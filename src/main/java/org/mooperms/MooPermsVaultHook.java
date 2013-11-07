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

package org.mooperms;

import lombok.Getter;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.plugin.Plugin;
import org.mooperms.api.IMooPerms;

import java.util.Arrays;

public class MooPermsVaultHook extends Permission {

	@Getter private final String name = "MooPerms";
	@Getter private final boolean enabled = true;

	private final IMooPerms mooperms;

	public MooPermsVaultHook(Plugin plugin) {
		this.plugin = plugin;
		this.mooperms = (IMooPerms) plugin.getServer().getPluginManager().getPlugin("MooPerms");
	}

	@Override
	public boolean hasSuperPermsCompat() {
		return true;
	}

	@Override
	public boolean hasGroupSupport() {
		return true;
	}

	@Override
	public boolean playerHas(String world, String player, String permission) {
		return Arrays.asList(mooperms.getWorld(world).getUser(player).getEffectivePermissions()).contains(permission);
	}

	@Override
	public boolean playerAdd(String world, String player, String permission) {
		try {
			mooperms.getWorld(world).getUser(player).addPermission(permission);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	@Override
	public boolean playerRemove(String world, String player, String permission) {
		try {
			mooperms.getWorld(world).getUser(player).removePermission(permission);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	@Override
	public boolean groupHas(String world, String group, String permission) {
		return Arrays.asList(mooperms.getWorld(world).getGroup(group).getEffectivePermissions()).contains(permission);
	}

	@Override
	public boolean groupAdd(String world, String group, String permission) {
		try {
			mooperms.getWorld(world).getGroup(group).addPermission(permission);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	@Override
	public boolean groupRemove(String world, String group, String permission) {
		try {
			mooperms.getWorld(world).getGroup(group).removePermission(permission);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	@Override
	public boolean playerInGroup(String world, String player, String group) {
		return Arrays.asList(mooperms.getWorld(world).getGroup(group).getUsers()).contains(player);
	}

	@Override
	public boolean playerAddGroup(String world, String player, String group) {
		try {
			mooperms.getWorld(world).getUser(player).addSubgroup(group);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	@Override
	public boolean playerRemoveGroup(String world, String player, String group) {
		try {
			mooperms.getWorld(world).getUser(player).removeSubgroup(group);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	@Override
	public String[] getPlayerGroups(String world, String player) {
		return mooperms.getWorld(world).getUser(player).getAllSubgroups();
	}

	@Override
	public String getPrimaryGroup(String world, String player) {
		return mooperms.getWorld(world).getUser(player).getGroup();
	}

	@Override
	public String[] getGroups() {
		return mooperms.getGroups();
	}
}
