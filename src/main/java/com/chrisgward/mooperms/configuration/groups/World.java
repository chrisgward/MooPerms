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

package com.chrisgward.mooperms.configuration.groups;

import lombok.Getter;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class World {
	@Getter Map<String, Object> info = new LinkedHashMap<>();
	public void setInfo(Map<String, Object> info) {
		this.info = new LinkedHashMap<>(info);
	}

	@Getter List<String> permissions = new LinkedList<>();
	public void setPermissions(List<String> permissions) {
		this.permissions = new LinkedList<>(permissions);
	}

	@Getter List<String> inheritance = new LinkedList<>();
	public void setInheritance(List<String> inheritance) {
		this.inheritance = new LinkedList<>(inheritance);
	}
}
