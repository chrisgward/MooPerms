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

import com.chrisgward.mooperms.api.storage.IGroup;
import com.chrisgward.mooperms.api.storage.IUser;
import org.bukkit.*;
import org.bukkit.World;

import java.util.List;

public class Group {

	@SuppressWarnings("unchecked")
	public List<Group> getInheritance() {
		return null;
	}

	public List<String> getPermissions() {
		return null;
	}

	public List<String> getAllPermissions() {
		return null;
	}

	public List<IUser> getUsers() {
		return null;  //To change body of implemented methods use File | Settings | File Templates.
	}

	public List<IUser> getUsers(World world) {
		return null;  //To change body of implemented methods use File | Settings | File Templates.
	}

	public void addPermission(String permission) {

	}

	public void addPermission(String permission, World world) {
		//To change body of implemented methods use File | Settings | File Templates.
	}

	public void removePermission(String permission) {

	}

	public void removePermission(String permission, World world) {
		//To change body of implemented methods use File | Settings | File Templates.
	}
}
