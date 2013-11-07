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

package org.mooperms.api;

public interface IGroup {
	/**
	 * Gets the name of the group
	 *
	 * @return Group name
	 */
	String getName();

	/**
	 * Gets a list of groups this group inherits in the specified context.
	 *
	 * @return Groups inherited
	 */
	String[] getInheritance();

	/**
	 * Gets a list of groups this group inherits in the specified context and in the global context. Does not include their inheritances, however.
	 *
	 * @return Groups inherited
	 */
	String[] getAllInheritance();

	/**
	 * Gets a list of groups this group inherits in the specified context and in the global context. Includes their inheritances and continues until the top of the tree (usually the default rank).
	 *
	 */
	String[] getEffectiveInheritance();

	/**
	 * Gets a list of permissions applied to the group in the specified context.
	 *
	 * @return Permissions
	 */
	String[] getPermissions();

	/**
	 * Gets a list of permissions applied to the group in both the specified context and in the global context.
	 *
	 * @return Permissions
	 */
	String[] getAllPermissions();

	/**
	 * Gets a list of permissions applied to the group in both the specified context and in the global context, as well as the permissions from inheritance.
	 *
 	 * @return Permissions
	 */
	String[] getEffectivePermissions();

	/**
	 * Gets a list of users, both online and offline, of users in this group in both the global and specified contexts.
	 * @return
	 */
	String[] getUsers();

	/**
	 * Adds a permission to the group in the specified context.
	 * @param permission Permission to add
	 */
	void addPermission(String permission);

	/**
	 * Removes a permission to the group in the world context
	 * Note: The permission will be added as a negation should the group not have the permission
	 * Removing node.* while a group has a node.anything will remove the matching permissions (TODO)
	 * Removing node.* while a group has no matching nodes will add a negation node of node.*
	 * @param permission Permission to remove
	 */
	void removePermission(String permission);
}
