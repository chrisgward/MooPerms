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
	 * Use getUser(name) or getGroup(name) to get/set attributes in the global context.
	 *
	 * @param world World context to get
	 * @return
	 */
	IWorld getWorld(String world);

	/**
	 * Creates a group
	 *
	 * @param name Name of the group to create
     * @throws IllegalArgumentException If the group already exists
	 */
	void createGroup(String name);

	/**
	 * Deletes a group
	 * Any players in this group will be moved to the default group (TODO)
	 *
	 * @param name Group to delete
	 */
	void removeGroup(String name);

	/**
	 * Get the names of all registered groups on the server
	 * @return Group names
	 */
	String[] getGroups();
}
