package org.mooperms.converters.permissionsex;

import org.mooperms.MooPerms;
import org.mooperms.converters.AbstractConverter;

import java.io.File;

public class PermissionsExConverter extends AbstractConverter {
	private File configFile;

	public PermissionsExConverter() {
		super("PermissionsEx");
	}

	@Override
	public void doConversion() {

	}

	@Override
	protected String getName() {
		return "PermissionsEx";
	}
}
