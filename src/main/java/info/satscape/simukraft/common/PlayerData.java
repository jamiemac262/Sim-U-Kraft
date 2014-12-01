package info.satscape.simukraft.common;

import info.satscape.simukraft.ModSimukraft;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import net.minecraft.entity.player.EntityPlayer;

public class PlayerData {
	private EntityPlayer p;
	private File f;
	private ArrayList<EntityFolk> folk= new ArrayList();
	
	public PlayerData(EntityPlayer player) {
		p = player;
		f = new File(ModSimukraft.getSavesDataFolder() + "/players/" + p.getDisplayName());
	}
	public boolean getData(){
		if(f.exists()){
			try {
				BufferedReader bufferedreader = new BufferedReader(new FileReader(f));
            String s;
            System.out.println("Loading player Folk");
            
				for (int i = 1; (s = bufferedreader.readLine().toLowerCase()) != null; i++) {
				    //folk.add(s);
				    System.out.println("[SAI] Loaded word number " + (i) + ": " + s);
				    bufferedreader.close();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
           
		}else{
			
			f.mkdir();
			
		}
		return false;
		
		
	}
public void saveData(){}
}
