package semaphore;

public class Main {

	public static void main(String[] args) throws InterruptedException {
		Semaphore s= new Semaphore("semafor");
		Thread p1= new Thread();
		Thread p2= new Thread();
		Thread p3= new Thread();
		
		s.V(p1);
		s.P(p1);
		s.P(p2);
		s.P(p3);
		s.V(p2);
		s.V(p3);
		
	}

}
