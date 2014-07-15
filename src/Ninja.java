
public class Ninja extends Player{
	
	public Ninja(Animation anim){
		super(anim);
	}

	@Override
	public float getGravity() {
		// TODO Auto-generated method stub
		return .0014f;
	}

	@Override
	public float getSpeed() {
		// TODO Auto-generated method stub
		return 0.14f;
	}
}
