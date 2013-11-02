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

package com.chrisgward.mooperms.converters;

import com.chrisgward.mooperms.configuration.config.Config;
import com.chrisgward.mooperms.configuration.groups.Groups;
import com.chrisgward.mooperms.configuration.users.Users;
import lombok.Getter;

public abstract class AbstractConverter {
	public abstract void doConversion();

	@Getter protected Config config;
	@Getter protected Groups groups;
	@Getter protected Users users;
}
