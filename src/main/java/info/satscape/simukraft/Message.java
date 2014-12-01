package info.satscape.simukraft;

import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;

public class Message implements IMessage {
	    
	    private String text;

	    public Message() { }

	    public Message(String text) {
	        this.text = text;
	    }


	    @Override
	    public void toBytes(ByteBuf buf) {
	        ByteBufUtils.writeUTF8String(buf, text);
	    }

	    public static class Handler implements IMessageHandler<Message, IMessage> {
	        
	        @Override
	        public IMessage onMessage(Message message, MessageContext ctx) {
	            System.out.println(String.format("Received %s from %s", message.text, ctx.getServerHandler().playerEntity.getDisplayName()));
	            return null; // no response in this case
	        }
	    }

		@Override
		public void fromBytes(ByteBuf buf) {
			// TODO Auto-generated method stub
			
		}
	}

	// Sending packets:
	//ModSimukraft.network.sendToServer(new MyMessage("foobar"));
	//ModSimukraft.network.sendTo(new SomeMessage(), somePlayer);
