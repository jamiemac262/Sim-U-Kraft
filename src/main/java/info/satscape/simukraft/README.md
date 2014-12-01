Sim-U-Kraft 0.12.1 for Minecraft 1.6.4 source code

Sim-U-Kraft was developed by Scott Hather (aka Satscape) between October 2011 and March 2014
I'm not a professional programmer (I don't do it as my day job) and developing this mod has been a fun learning experience, there are still several bugs in this code, a lot due to the fact that I didn't know how to do proper Multi-player code at the time, so a lot of client/server stuff is mixed up, or just plain wrong! :-) I did start to add Packet Handling to start to make it work properly on SMP, the Windmill sends a packet from server to client to let it know what colour the sails are.

Anyway, I no longer have time to work on this code, and as there are quite a few people who like playing this mod (~50,000) I'm making the code "Open Source", so anyone can revive it for future Minecraft versions or use it as a basis for their own mods. I did start to re-write it from scratch, as I think that's the best way to go, but didn't get very far, so if you want to have a go at updating the code or re-writing based on the mod, feel free. If you could mention somewhere that your mod is "Based on Sim-U-Kraft by Scott Hather", I'd appreciate it, thanks.

I've commented the code in various places to help you understand various bits. There was a feature where it checked to see if there's a new version and download new buildings submitted by players from my web server, I've taken this out.

As you'll see from the code (hopefully), There's no chunk-loading, instead the EntityFolks are simply for show and spawn/despawn when required, FolkData gets ticked periodically from the TickHandler and does the actual work, wandering, performing jobs etc. Then there's Job class with all its sub-classes for each time of job, these get ticked and perform the actual work. The GUIs will all need re-doing in some way, as they're client-side only with no packets being sent to server with changes (hire/fire etc). SO... you've got your work cut out for you, I'd personally do a re-write, but this is open-source now, so whatever you think is right.

I'm leaving the Facebook page up, https://www.facebook.com/Simukraft (1200 Likes/fans) that's where I tended to post news, updates and provide support, so if you do revive Sim-U-Kraft or make a mod based on it, please post on there and I'll re-post it for all to see, possibly make you an admin too.
Also, I'm leaving up the video tutorials and let's play videos on http://www.youtube.com/satscapeminecraft for the time being, lots of people still playing MC 1.6.4 + Sim-U-Kraft (and lots of other mods)

Many thanks to the Minecraft community for support and encouragement while making this mod, when I released the first version in 2012, I really didn't expect so many people to play it! Your input has been helpful in making it a better mod.

All the best with your modding, looking forward to see what you can make!

Scott Hather
