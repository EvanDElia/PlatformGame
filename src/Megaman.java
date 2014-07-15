
public class Megaman extends Player{

    public Megaman(Animation anim) {
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
		return 0.12f;
	}
    
    
}
