import java.util.ArrayList;

public class PlaylistMP3 extends Thread{
	
	private ArrayList<String> songs1 = new ArrayList<String>();
	private CustomPlayerMP3 player = new CustomPlayerMP3();
	
	public PlaylistMP3(boolean metal){
		if (metal){
    		addSong("assets/music/Temptingtime.mp3");
    		addSong("assets/music/Thoroughly.mp3");
    		addSong("assets/music/Odessa.mp3");
    		addSong("assets/music/Weightless.mp3");
    		addSong("assets/music/Donotgo.mp3");
    		addSong("assets/music/Trojans.mp3");
    		addSong("assets/music/Handshake.mp3");
    		addSong("assets/music/Pointtopoint.mp3");
    	}
    	else{
    		addSong("assets/music/Fakebit.mp3");
    		addSong("assets/music/Blast.mp3");
    		addSong("assets/music/Gameboylove.mp3");
    		addSong("assets/music/Godbless.mp3");
    		addSong("assets/music/Iwantyou.mp3");
    		addSong("assets/music/Meow.mp3");
    		addSong("assets/music/Akira.mp3");
    		addSong("assets/music/Everythingexplodes.mp3");
    	}
    }
	
	public void addSong(String name){
		songs1.add(name);
	}
	
	public void run(){
		for (int i = (int) (Math.random() * songs1.size()); i < songs1.size(); i++){
			player.setPath(songs1.get(i));
			player.play(-1);
			if (i == songs1.size() - 1 && player.canResume()) i = 0;
			while (player.getTotal() > 0 && !player.isDead()){ 				//just check that song is still playing
				if (i == songs1.size() - 1 && player.canResume()) i = 0;
				if (player.isDead()) break;
				//System.out.println(player.isDead()); //fuck this shit
			}
			if (player.isDead()) break;
		} 
	}
	
	public void stopPlaying(){
		player.pause();
	}
	
	public void continuePlaying(){
		player.resume();
	}
	
	public void killIt(){
		player.stop();
	}

}
