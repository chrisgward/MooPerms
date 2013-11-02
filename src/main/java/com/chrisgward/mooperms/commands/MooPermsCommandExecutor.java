package com.chrisgward.mooperms.commands;

import com.chrisgward.mooperms.MooPerms;
import lombok.RequiredArgsConstructor;
import org.bukkit.command.*;

import java.util.HashMap;
import java.util.Map;

import static com.chrisgward.mooperms.I18n._;

@RequiredArgsConstructor
public class MooPermsCommandExecutor implements CommandExecutor {
	private final MooPerms instance;

	Map<String, AbstractCommand> executorMap = new HashMap<>();

	@Override
	public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {

		if(commandSender instanceof ConsoleCommandSender || commandSender.isOp() || commandSender.hasPermission("mooperms." + command.getName())) {
			AbstractCommand executor;
			if(executorMap.containsKey(command.getName())) {
				executor = executorMap.get(command.getName());
			} else {
				try {
					executor = (AbstractCommand)Class.forName("com.chrisgward.mooperms.commands.Command" + command.getName()).newInstance();
					executor.moo = instance;
					executorMap.put(command.getName(), executor);
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			}

			try {
				return executor.onCommand(commandSender, command, s, args);
			} catch (Exception e) {
				commandSender.sendMessage(_("commandError", e.getMessage()));
				instance.showError(e, true);
				return true;
			}
		} else {
			commandSender.sendMessage(_("noCommandPermission"));
			return true;
		}
	}
}
