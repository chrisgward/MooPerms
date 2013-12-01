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
import org.mooperms.MooPerms;
import org.mooperms.api.IWorld;
import org.mooperms.context.Group;
import org.mooperms.context.User;

@RequiredArgsConstructor
public class World implements IWorld {

	private final MooPerms instance;
	@Getter private final String name;

	public User getUser(String name) {
		return instance.getUser(name).getUser().getInContext(this.name);
	}

	public Group getGroup(String name) {
		Group group = instance.getGroup(name);
		if(group == null) {
			throw new NullPointerException(name);
		}
		org.mooperms.storage.Group group2 = group.getGroup();
		if(group2 == null) {
			throw new NullPointerException(name);
		}
		return group2.getInContext(this.name);
	}
}
