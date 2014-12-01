package info.satscape.simukraft;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;

public class SimukraftCommand implements ICommand {

	 private List aliases;
	  public SimukraftCommand()
	  {
	    this.aliases = new ArrayList();
	    this.aliases.add("sk");
	    this.aliases.add("sim");
	  }

	  @Override
	  public String getCommandName()
	  {
	    return "simukraft";
	  }

	  @Override
	  public String getCommandUsage(ICommandSender icommandsender)
	  {
	    return "Simukraft <text>";
	  }

	  @Override
	  public List getCommandAliases()
	  {
	    return this.aliases;
	  }

	  @Override
	  public void processCommand(ICommandSender icommandsender, String[] astring)
	  {
	    if(astring.length == 0)
	    {
	       icommandsender.addChatMessage(new ChatComponentText("Please enter a gamemode number"));
	      return;
	    }
	    
	    if(astring[0].equalsIgnoreCase("gamemode")){
	    	ModSimukraft.changeGamemode(Integer.parseInt(astring[1]));
	    }else if(astring[0].equalsIgnoreCase("initgame")){ModSimukraft.resetAndLoadNewWorld();}
	    
	    
	    icommandsender.addChatMessage(new ChatComponentText("Sim-U-Kraft: [" + astring[0] + "]"));
	    
	  }

	  @Override
	  public boolean canCommandSenderUseCommand(ICommandSender icommandsender)
	  {
	    return true;
	  }

	  @Override
	  public List addTabCompletionOptions(ICommandSender icommandsender,
	      String[] astring)
	  {
	    return null;
	  }

	  @Override
	  public boolean isUsernameIndex(String[] astring, int i)
	  {
	    return false;
	  }

	  @Override
	  public int compareTo(Object o)
	  {
	    return 0;
	  }
	}
