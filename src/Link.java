
public class Link extends Player{

	
	public Link(Animation anim){
		super(anim);
	}

	@Override
	public float getGravity() {
		// TODO Auto-generated method stub
		return 0.0014f;
	}

	@Override
	public float getSpeed() {
		// TODO Auto-generated method stub
		return 0.09f;
	}

}
