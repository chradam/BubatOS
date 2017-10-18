package mborzymowski.shell;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Array;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.text.DefaultCaret;


public class Shell{

	/* AREA DLA KONSOLI */
	protected JTextArea area;
	
	/* SCROLL PANEL */
	protected JScrollPane scrl;
	
	/* CMD FIELD */
	protected TextField cmd;
	
	/* GUI KONSOLI */
	protected JFrame frame;
	
	/* AKTUALNY KATALOG */
	protected String drv = "C";
	protected String[] path = new String[1000];
	protected int pcounter = 0;
	
	/* OSTATNIA KOMENDA */
	protected String lastcmd;
	
	
	/* KONSTRUKTOR */
	public Shell()
	{
		
	}
	
	/* UTWORZENIE SHELL'A */
	public void startShell()
	{		
		//utw�rz frame
		this.frame = new JFrame();
		
		//text area razem z auto scrollem na sam d� 
		this.area = new JTextArea();
		DefaultCaret caret = (DefaultCaret)this.area.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		
		//utworzenie text field dla cmd
		this.cmd = new TextField();
		this.cmd.setBackground(Color.BLACK);
		this.cmd.setForeground(Color.WHITE);
		
		//utworzenie panelu scrollowania dla konsoli
		this.scrl = new JScrollPane(this.area);
		
		//ustawienie wielkosci wyjscia konsoli
		this.scrl.setPreferredSize(new Dimension(780,530));
		
		//dodanie do GUI
		this.frame.add(scrl);
		
		//dodanie do GUI cmd box
		this.frame.add(cmd, BorderLayout.SOUTH);
		
		//ustawienia GUI
		this.frame.pack();
        this.frame.setVisible( true );
        this.frame.setSize(800,600);
        this.frame.setTitle("BubatOS");
        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.frame.setLayout(new BorderLayout());
        
        //dodatkowe ustawienia dla p�l
        this.area.setEditable(false);
        this.area.setRows(30);
        this.area.setBackground(Color.BLACK);
        this.area.setForeground(Color.WHITE);
        
        //powitanie
        this.printWelcome();
        
        //zrobienie keybinda
        this.cmd.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				//przepisanie komendy
				String komenda = e.getActionCommand();
				//System.out.println(komenda);
				
				//wyczyszczenie pola komend
				cmd.setText(" ");//nwm czemu ale najpierw trzeba wpisac spacje zeby wyczyscic
				cmd.setText("");
				
				//zapisanie komendy
				if(komenda.equals(""))
				{
					lastcmd = "_";
				}
				else
				{
					lastcmd = komenda;
				}
				
				lastcmd = lastcmd.toLowerCase();
				
				//przekazanie komendy do funkcji
				execute(komenda.toLowerCase());
			}
			
		});
	}
	
	/* POWITANIE */
	public void printWelcome()
	{
		area.setFont(new Font("Lucida Console", Font.CENTER_BASELINE, 72));
		echo("", false);
		echo("", false);
		echo("", false);
		echo("     BubatOS", false);
		
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		area.setText("");
		area.setFont(new Font("Lucida Console", Font.CENTER_BASELINE, 16));
		echo("");
		
		//focus na cmd 
		this.cmd.requestFocus();
	}
	
	/* FUNKCJA ECHO DOMY�LNA Z KATALOGIEM*/
	public void echo(String text)
	{
		this.echo(text, true);
	}
	
	/*FUNKCJA ECHO BEZ KATALOGU*/
	public void echo(String text, boolean withdir)
	{
		//jezeli with dir to wypisze z katalogiem
		if(withdir)
		{
			//jezeli wpisano juz jakas komende
			if(this.lastcmd != null)
			{
				//pobieramy logi
				String lasttxt = this.area.getText();
				
				//przygotowanie area.getText() na regex
				Pattern p1 = Pattern.compile("\\\\");
				Matcher m1 = p1.matcher(this.lastcmd);
				
				this.lastcmd = m1.replaceAll("\\\\\\\\");
				
				
				//regex check
				Pattern p = Pattern.compile(">\n", Pattern.CASE_INSENSITIVE);
				Matcher m = p.matcher(lasttxt);

				//replace tak by dodalo ostatnio uzyta komende
				String output = m.replaceAll(">" + this.lastcmd +"\n");
				
				//wyczysc ostatnie komendy
				this.lastcmd = "";
				
				//wypisz co trzeba
				this.area.setText(output + this.makeStrDir()+ ">" + text + "\n");
			}
			else
			{
				this.area.setText(this.area.getText() + this.makeStrDir() + ">" + text + "\n");
			}
			//this.area.setText(this.area.getText() + this.currentDir + ">" + text + "\n");
		}
		else
		{
			this.area.setText(this.area.getText() + text + "\n");
		}
	}
	
	/* TWORZENIE STRINGU DO WYSWIETLENIA */
	public String makeStrDir()
	{
		if(this.pcounter == 0)
		{
			return this.drv + "\\:";
		}
		else
		{
			String totalpath = "";
			
			for(int x=0;x<this.pcounter;x++)
			{
				if(x<this.pcounter-1)
				{
					totalpath += this.path[x] + "\\";
				}
				else
				{
					totalpath += this.path[x];
				}
				
			}
			
			return this.drv + "\\:" + totalpath;
		}
	}
	
	/* BINDY NA KOMENDY */
	public void execute(String command)
	{
		if(command.matches("^cd.*"))
		{
			//usuniecie "cd" zeby miec same katalogi
			Pattern p2 = Pattern.compile("cd[ ]*");
			Matcher m2 = p2.matcher(command);
			
			command = m2.replaceAll("");
			
			String[] katalogi = command.split("\\\\");
			
			
			
			System.out.println(command);
			this.path[this.pcounter] = "xd";
			this.pcounter++;
		}
		else if(command.matches("^help$"))
		{
			this.echo("HELP", false);
		}
		else if(command.matches("[ ]*"))
		{
			//do nothing
		}
		else
		{
			this.echo("Nie znaleziono komendy", false);
			this.echo("Dostepne komendy po wpisaniu help", false);
		}
		
		//sciezka na koniec
		this.echo("");
	}
}
