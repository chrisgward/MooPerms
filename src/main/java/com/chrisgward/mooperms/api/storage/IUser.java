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

package com.chrisgward.mooperms.api.storage;

public interface IUser {
	/**
	 * Returns the player's name
	 *
	 * @return Player name
	 */
	String getName();

	/**
	 * Returns the player's primary group
	 *
	 * @return Group
	 */
	IGroup getGroup();

	/**
	 * Sets the player's primary group
	 *
	 * @param group Group
	 */
	void setGroup(String group);

	/**
	 * Gets the player's non-primary(sub) groups
	 *
	 * @return Subgroups
	 */
	IGroup[] getSubgroups();

	/**
	 * Adds a non-primary(sub) group to a player
	 *
	 * @param group Group to add
	 * @throws NullPointerException Thrown if the group does not exist
	 */
	void addSubgroup(String group);

	/**
	 * Removes a non-primary(sub) group to a player
	 *
	 * @param group Group to remove
	 *              * @throws NullPointerException Thrown if the group does not exist or if the player is not in the group.
	 */
	void removeSubgroup(String group);

	/**
	 * Gets the permissions applied directly to the player in the world context.
	 * Does not include permissions applied directly to the player unless in the global context.
	 * Does not include permissions applied to the groups/subgroups the player inherits.
	 *
	 * @return List of permissions
	 */
	String[] getPermissions();

	/**
	 * Gets the permissions applied to the player, groups and subgroups in the world context.
	 * Includes permissions applied to the player in the global context, as well as permissions applied to the groups/subgroups in both the global and world context.
	 *
	 * @return List of permissions
	 */
	String[] getAllPermissions();

	/**
	 * Adds a permission to a player in the world context
	 *
	 * @param permission Permission to add
	 */
	public void addPermission(String permission);

	/**
	 * Removes a permission to a player in the world context
	 * Note: The permission will be added as a negation should the player not have the permission
	 * Removing node.* while a player has a node.anything will remove the matching permissions (TODO)
	 * Removing node.* while a player has no matching nodes will add a negation node of node.*
	 *
	 * @param permission Permission to remove
	 */
	public void removePermission(String permission);
}
