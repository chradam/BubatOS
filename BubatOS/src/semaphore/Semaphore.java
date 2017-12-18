package semaphore;
//ogólnie na moje działa, tylko nie ma pełnej funkcjonalnosci bo nie ma planisty. Wątki będą zastąpione przez procesy. 
//Jak coś to pytać, prosić o dodatkowe rzeczy i opierdalać itd.
import java.util.LinkedList;

public class Semaphore {
private boolean stan;

public String name;
public LinkedList<Thread> WaitingList= new LinkedList <Thread>();
public LinkedList <Thread> ReadyList = new LinkedList<Thread>();

public Semaphore( String name) {
	this.stan=true;
	this.name=name;
}

public boolean isStan() {
	return stan;
}

public void P (Thread t) throws InterruptedException
{
	if( stan==true)
	{
		System.out.println("Semafor jest juz podniesiony. Proces przechodzi dalej");
		stan=false; // semafor podniesiony, proces opuszcza semafor i wykonuje critical section 
		System.out.println("Proces" + t.getName() +" wykonuje sie");
		t.sleep(2000);
	}
	else
	{
		System.out.println("Semafor jest aktualnie opuszczony. Proces nie moze dzialac dalej.");
		System.out.println("Proces zmienia stan na oczekujacy");
		WaitingList.add(t);
	}
}

public void V(Thread t) throws InterruptedException
{

	if(stan==false)
	{
		if(WaitingList.isEmpty()==false)
		{
			t=WaitingList.getFirst();
			System.out.println("Proces" + t.getName()+" zmienia status na gotowy");
			ReadyList.add(t);
			WaitingList.removeFirst();
			stan=true;
			P(t);
		}
		else {
			System.out.println("Brak procesow oczekujacych. Podniesienie semafora");
			stan=true;
			P(t);
		}
	}
	else {System.out.println("Semafor jest juz aktualnie podniesiony");}
}

}
