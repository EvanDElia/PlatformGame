import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.Image;
import java.net.MalformedURLException;
import java.net.URL;


/**
    The Player extends the Sprite class to add states
    (STATE_NORMAL or STATE_JUMPING) and gravity.
*/
public abstract class Player extends Mob {

	public static boolean moving;
	private static final float JUMP_SPEED = -.85f;
	public int doubleJumps;
	
	private AudioClip jumpNoise;

    private boolean onGround;

    public Player(Animation anim) {
        super(anim);
        
        try {
			jumpNoise = Applet.newAudioClip(new URL("file:/Users/Evan/workspace/csc329Game/assets/sounds/Jump.wav"));
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
    }
    
    public abstract float getGravity();
    public abstract float getSpeed();
    
    public boolean moving(){
    	return moving;
    }
    
    public void addJump(){
    	doubleJumps++;
    }
    
    public int getJumps(){
    	return doubleJumps;
    }

    /**
        Updates the player's positon and animation. Also, sets the
        Player's state to NORMAL if a jumping Player landed on
        the floor.
    */
    public void update(long elapsedTime) {

        // move player
        super.update(elapsedTime);
        
        //check for movement
        if (getVelocityX() != 0) moving = true;
        else moving = false;
    }
    
    public void collideHorizontal() {
        setVelocityX(0);
    }


    public void collideVertical() {
        // check if collided with ground
        if (getVelocityY() > 0) {
            onGround = true;
        }
        setVelocityY(0);
    }


    public void setY(float y) {
        // check if falling
        if (Math.round(y) > Math.round(getY())) {
            onGround = false;
        }
        super.setY(y);
    }


    public void wakeUp() {
        // do nothing
    }


    /**
        Makes the player jump if the player is on the ground or
        if forceJump is true.
    */
    public void jump() {
        if (onGround) {
            onGround = false;
            setVelocityY(JUMP_SPEED);
            jumpNoise.play();
        }
        else if (doubleJumps > 0){
        	setVelocityY(JUMP_SPEED);
        	doubleJumps--;
        }
    }
    
    public float getMaxSpeed(){
    	return 0.75f;
    }

	public boolean isOnGround() {
		return onGround;
	}

	public void reset() {
		setVelocityX(0);
		setVelocityY(0);
		setState(0);
		doubleJumps = 0;
	}
}
