package sebastienstdenis.scheduleBuilder;

class UWAPIException extends Exception {

	private static final long serialVersionUID = 1L;

	UWAPIException() {
		super();
	}
	
	UWAPIException(String msg) {
		super(msg);
	}
	
	public String getMessage() {
		return String.format("UW API Error: %s", super.getMessage());
	}
}
