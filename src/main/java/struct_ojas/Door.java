package struct_ojas;

public class Door {

	private boolean isOpen;
	

	public Door(){
		isOpen=false;
	}
	public void open() {
		isOpen=true;
	}

	public void close() {
		isOpen=false;
	}

	public boolean isOpened() {
		return isOpen;
	}
}