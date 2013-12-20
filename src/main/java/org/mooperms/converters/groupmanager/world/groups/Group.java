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

package org.mooperms.converters.groupmanager.world.groups;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

public class Group {
	@Getter @Setter private Map<String, Object> info;
	@Getter @Setter private List<String> permissions;
	@Getter @Setter private List<String> inheritance;
	private boolean dfault;
	public void setDefault(boolean dfault) {
		this.dfault = dfault;
	}

	public boolean isDefault() {
		return dfault;
	}
}
