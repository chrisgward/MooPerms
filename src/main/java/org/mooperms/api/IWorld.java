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

public interface IWorld {
	/**
	 * Gets the name of the world context.
	 * @return Name
	 */
	String getName();

	/**
	 * Gets a user object within the world context
	 * @param name Name of the user
	 * @return User object
	 */
	public IUser getUser(String name);

	/**
	 * Gets a group object within the world context
	 * @param name Name of the group
	 * @return Group object
	 */
	public IGroup getGroup(String name);
}
