package inodes;
import java.util.Date;
public class Inode {
	//str 800 i rodzia� 11.7
	//jest rekordem
	//zawiera:
	//id_u�ytkownika*
	//id grupy u�ytkownik�w pliku
	//czas ostatniej modyfikacji i czas ostaniego dost�pu do pliku
	//typ pliku(zwyk�y, katalog)
	//miejsce  15 wskaznik�w do blok�w dyskowych z danymi pliku
		//->12 na tablica? -bloki bezpo�redine - adreswy blok�w dyskowych z danymi
			//u nas blok wynosi 32B, wi�c mo�na zaadresowa� bezpo�rednio 384B
			//ale i tak plik ma ograniczon� wielkos� 40B
		//->3 - bloki po�rednie - u nas nie wyst�puj�
	//int owners_ids; //mo�e domy��lnie root?
	//int timestamps;
	String month;
	int day;
	int hour;
	int minute;
	
	int type_of_file; //katalog czy zwyk�y plik ?????????????????
	int sizeF;
	int LinkCounter; // licznik trwa�ych dowi�za�
	//zerowy indeks wskazuje bezpo�rednio na blok dyskowy z danymi. 2 nast�pne wskazuj� na bloki indeksowe.
	int [] inode_table  = new int[2];
}
