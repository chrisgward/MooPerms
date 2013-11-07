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
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.mooperms.MooPerms;
import org.mooperms.api.IGroup;
import org.mooperms.api.IUser;
import org.mooperms.api.IWorld;

@RequiredArgsConstructor
public class World implements IWorld {

	private final MooPerms instance;
	@Getter private final String name;

	public IUser getUser(String name) {
		return null;  //To change body of implemented methods use File | Settings | File Templates.
	}

	public IGroup getGroup(String name) {
		return null;  //To change body of implemented methods use File | Settings | File Templates.
	}
}
