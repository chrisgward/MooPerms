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

package org.mooperms;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.Locale;
import java.util.Properties;

@SuppressWarnings({"DuplicateStringLiteralInspection", "HardCodedStringLiteral", "StringConcatenation"})
public class I18n {

	private static Properties file;
	private static Properties dfault;
	private static Properties locale;

	private static String currentLocale = Locale.getDefault().getISO3Language();

	public static void setLocale(String locale) {
		currentLocale = locale;
		load();
	}

	public static void load() {
		file = dfault = locale = null;
		try {
			file = new Properties();
			file.load(new FileInputStream(new File(MooPerms.instance.getDataFolder(), "messages.properties")));
		} catch (Exception e) {
			MooPerms.instance.debug("Could not load messages.properties file from disk");
			file = null;
		}

		try {
			locale = new Properties();
			InputStream inputStream = I18n.class.getResourceAsStream("/messages_" + currentLocale + ".properties");
			if (inputStream == null) {
				locale = null;
			} else {
				locale.load(inputStream);
			}
		} catch (Exception e) {
			MooPerms.instance.debug("Could not load messages_" + currentLocale + ".properties from resource");
			locale = null;
		}

		try {
			dfault = new Properties();
			InputStream inputStream = I18n.class.getResourceAsStream("/messages.properties");
			dfault.load(inputStream);
		} catch (Exception e) {
			dfault = null;
			throw new RuntimeException("Could not load I18n!", e);
		}
	}

	public static String _(String key, String... args) {
		String response = null;

		if (file != null) {
			response = file.getProperty(key);
		}
		if (response == null && locale != null) {
			response = locale.getProperty(key);
		}
		if (response == null && dfault != null) {
			response = dfault.getProperty(key);
		}
		if (response == null) {
			return i18nerr(key, args);
		}

		MessageFormat format = new MessageFormat(response);
		return format.format(args);
	}

	private static String i18nerr(String key, String[] args) {
		StringBuilder sb = new StringBuilder();
		sb.append("I18NERR: ").append(key).append(" [");
		for (int i = 0; i < args.length; i++) {
			sb.append("\"").append(args[i].replace("'", "\\'")).append("\"");
			if (i == args.length - 1) {
				sb.append(", ");
			}
		}
		sb.append("]");

		return sb.toString();
	}

}
