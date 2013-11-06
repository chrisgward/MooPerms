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

package com.chrisgward.mooperms.api;

import com.chrisgward.mooperms.api.storage.IGroup;
import com.chrisgward.mooperms.api.storage.IUser;
import com.chrisgward.mooperms.api.storage.IWorld;

public interface IMooPerms {
	/**
	 * Gets a user by name, any methods applied to this method will apply to the global context
	 *
	 * @param name Name of user
	 * @return User object, null if user does not exist
	 */
	IUser getUser(String name);

	/**
	 * Gets a group by name, any methods applied to this method will apply to the global context
	 *
	 * @param name Name of group
	 * @return Group object, null if group does not exist.
	 */
	IGroup getGroup(String name);

	/**
	 * Gets a world by name, any methods applied to this method will apply to the world parameter's context.
	 *
	 * @param world
	 * @return
	 */
	IWorld getWorld(String world);

	/**
	 * Creates a group
	 *
	 * @param name
	 * @return
	 */
	void createGroup(String name);
}
