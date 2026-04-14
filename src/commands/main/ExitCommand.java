package commands.main;

import commands.Command;

public class ExitCommand implements Command {
    @Override
    public void execute() {
        System.out.println("Изход от системата. Довиждане!");
    }
}