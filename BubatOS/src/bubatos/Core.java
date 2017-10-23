package bubatos;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Core {
	/*CZYTANIE PLIKU*/
	/*
	 * zwrot w postaci tablicy Stringów
	 * pojedyñczy wpis w tablicy to pojedyñcza linijka pliku
	 */
	public static String[] readFile(String filename)
	{
		String zwrot = "";
		try
		{
			FileReader fr = new FileReader(filename);
			BufferedReader br = new BufferedReader(fr);
			String linijka;
			while((linijka = br.readLine()) != null)
			{
				//IO.echoLn(linijka);
				zwrot += (linijka + "\n");
			}
			
			br.close();
			
			String[] zwrot2 = zwrot.split("\n");
			
			return zwrot2;
		}
		catch(IOException e)
		{
			return null;
		}	
	}
	
	/*WYPISANIE WARTOSCI TABLICY STRING */
	public static void printStringTab(String[] obj)
	{
		for(Object single : obj)
		{
			System.out.println(single);
		}
	}
}
