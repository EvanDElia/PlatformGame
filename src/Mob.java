import java.awt.Image;
import java.lang.reflect.Constructor;


public abstract class Mob extends Sprite implements Cloneable{
	/**
	    Amount of time to go from STATE_DYING to STATE_DEAD.
	*/
	
	public static final int STATE_NORMAL = 0;
	public static final int STATE_DEAD = 2;
	
	private Image dead;
	
	private int state;
	
	public Mob(Animation anim){
		super(anim);
		state = STATE_NORMAL;
	}
	
    public Object clone() {
        // use reflection to create the correct subclass
        Constructor constructor = getClass().getConstructors()[0];
        try {
            return constructor.newInstance(new Object[] {
                (Animation)anim.clone()
            });
        }
        catch (Exception ex) {
            // should never happen
            ex.printStackTrace();
            return null;
        }
    }
    
    /**
     	Wakes up the creature when the Creature first appears
        on screen. Normally, the creature starts moving left.
    */
    public void wakeUp() {
        if (getState() == STATE_NORMAL && getVelocityX() == 0) {
            setVelocityX(-getMaxSpeed());
        }
    }


    private int getMaxSpeed() {
		return 0;
	}
    

	/**
        Gets the state of this Creature. The state is either
        STATE_NORMAL, STATE_DYING, or STATE_DEAD.
    */
    public int getState() {
        return state;
    }


    /**
        Sets the state of this Creature to STATE_NORMAL,
        STATE_DYING, or STATE_DEAD.
    */
    public void setState(int state) {
        if (this.state != state) {
            this.state = state;
        }
    }


    /**
        Checks if this creature is alive.
    */
    public boolean isAlive() {
        return (state == STATE_NORMAL);
    }
    
    /**
    Called before update() if the creature collided with a
    tile horizontally.
*/
public void collideHorizontal() {
    setVelocityX(-getVelocityX());
}


/**
    Called before update() if the creature collided with a
    tile vertically.
*/
public void collideVertical() {
    setVelocityY(0);
}

public void setImage(Image image, Image image2){
	this.sprite = image;
	this.dead = image2;
}

/**
Gets this Sprite's current image.
*/
public Image getImage(boolean moving) {
	if (getState() == STATE_DEAD)
		return dead;
	else if (moving)
		return anim.getImage();
	else
		return sprite;
}

}
