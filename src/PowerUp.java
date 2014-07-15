import java.lang.reflect.Constructor;

/**
    A PowerUp class is a Sprite that the player can pick up.
*/
public abstract class PowerUp extends Sprite {

    public PowerUp(Animation anim) {
        super(anim);
    }


    public Object clone() {
        // use reflection to create the correct subclass
        Constructor constructor = getClass().getConstructors()[0];
        try {
            return constructor.newInstance(
                new Object[] {(Animation)anim.clone()});
        }
        catch (Exception ex) {
            // should never happen
            ex.printStackTrace();
            return null;
        }
    }


    /**
        A Star PowerUp. Gives the player points.
    */
    public static class Jump extends PowerUp {
        public Jump(Animation anim) {
            super(anim);
        }
    }
    
    public static class Goal extends PowerUp {
        public Goal(Animation anim) {
            super(anim);
        }
    }
    
    public static class Credits extends PowerUp {
    	public Credits(Animation anim){
    		super(anim);
    	}
    }
    
    public static class Instructions extends PowerUp {
    	public Instructions(Animation anim){
    		super(anim);
    	}
    }
    
    public static class MusicChange extends PowerUp {
    	public MusicChange(Animation anim){
    		super(anim);
    	}
    }
    
    public static class CharChange extends PowerUp {
    	public CharChange(Animation anim){
    		super(anim);
    	}
    }

}
