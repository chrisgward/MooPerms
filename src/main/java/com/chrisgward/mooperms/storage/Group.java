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

import java.util.List;

public class Group implements IGroup {

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

	public void addPermission(String permission) {

	}

	public void removePermission(String permission) {

	}
}
