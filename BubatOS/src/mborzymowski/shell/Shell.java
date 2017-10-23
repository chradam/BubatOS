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
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.text.DefaultCaret;

import bubatos.Core;


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
	
	/* HISTORIA KOMEND */
	protected List<String> commands = new ArrayList<String>();
	protected int cmdindex;
	
	
	/* KONSTRUKTOR */
	public Shell()
	{
		this.cmdindex = 0;
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
					commands.add(komenda);
				}
				
				lastcmd = lastcmd.toLowerCase();
				
				//przekazanie komendy do funkcji
				execute(komenda.toLowerCase());
				
				cmdindex=0;
			}
		});
        this.cmd.addKeyListener(new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) {
				
				
			}

			@Override
			public void keyPressed(KeyEvent e) {
				// TODO Auto-generated method stub
				if(e.getKeyCode() == KeyEvent.VK_UP)
				{
					//cmd.setText(commands.get(commands.size()-(cmdindex)));
					if(commands.size()>cmdindex)
					{
						cmdindex++;
						cmd.setText(commands.get(commands.size()-(cmdindex)));
					}
				}
				if(e.getKeyCode() == KeyEvent.VK_DOWN)
				{
					cmdindex--;
					if(cmdindex < 0)
					{
						cmd.setText("");
						cmdindex=0;
					}
					else
					{
						//cmdindex--;
						cmd.setText(commands.get(commands.size()-(cmdindex+1)));
						//System.out.println("sas");
					}
				}
				System.out.println("Pokzalem index: "+cmdindex);
			}

			@Override
			public void keyReleased(KeyEvent e) {
				// TODO Auto-generated method stub
				
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
		area.setFont(new Font("Lucida Console", Font.CENTER_BASELINE, 13));
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
			return this.drv + ":\\";
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
			
			return this.drv + ":\\" + totalpath;
		}
	}
	
	/* BINDY NA KOMENDY */
	public void execute(String command, boolean isScript)
	{	
		if(command.matches("^cd[ ].*") | command.matches("^cd$"))
		{
			if(command.matches("^cd[ ]*"))
			{
				//do nothing
			}
			else
			{
				//TODO poprawic jak bedzie klasa zarzadzania plikami
				
				//usuniecie "cd" zeby miec same katalogi
				Pattern p2 = Pattern.compile("cd[ ]");
				Matcher m2 = p2.matcher(command);
				
				command = m2.replaceAll("");
				
				String[] katalogi = command.split("\\\\");
				
				
				
				System.out.println(command);
				this.path[this.pcounter] = "xd";
				this.pcounter++;
			}
		}
		else if(command.matches("^run[ ].*") | command.matches("^run$"))
		{
			if(command.matches("^run[ ]*"))
			{
				this.echo("Nie podano sciezki skryptu", false);
			}
			else
			{
				//usuniecie "run" zeby miec sama sciezke
				Pattern p2 = Pattern.compile("run[ ]");
				Matcher m2 = p2.matcher(command);
				
				command = m2.replaceAll("");
				
				this.runScript(command);
			}			
		}
		else if(command.matches("^help$"))
		{
			this.echo("Dostepne komendy: ", false);
			this.echo("    - cd: przejscie do danego katalogu", false);
			this.echo("    - run skrypt: uruchomienie skryptu", false);
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
		
		if(!isScript)
		{
			//sciezka na koniec
			this.echo("");
		}
	}
	
	public void execute(String command)
	{
		this.execute(command, false);
	}
	
	public boolean wpisano(String str)
	{
		if(str.matches("^"+str+"[ ].*") | str.matches("^"+str+"$"))
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	
	
	
	
	private void runScript(String path)
	{
		String[] skrypt = Core.readFile(path);
		
		if(skrypt != null)
		{
			for(String komenda : skrypt)
			{
				this.execute(komenda, true);
			}
		}
		else
		{
			this.echo("Blad odczytu pliku", false);
		}
	}
}
