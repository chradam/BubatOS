package mborzymowski.shell;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.text.DefaultCaret;

import com.sun.xml.internal.ws.util.StringUtils;

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
	protected String currentDir = "C:\\";
	
	/* OSTATNIA KOMENDA */
	protected String lastcmd;
	
	
	/* KONSTRUKTOR */
	public Shell()
	{
		
	}
	
	/* UTWORZENIE SHELL'A */
	public void startShell()
	{
		//utwórz frame
		this.frame = new JFrame();
		
		//text area razem z auto scrollem na sam dó³ 
		this.area = new JTextArea();
		DefaultCaret caret = (DefaultCaret)this.area.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		
		//utworzenie text field dla cmd
		this.cmd = new TextField();
		
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
        
        //dodatkowe ustawienia dla pól
        this.area.setEditable(false);
        this.area.setRows(30);
        
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
				lastcmd = komenda;
				
				//przekazanie komendy do funkcji
				execute(komenda);
			}
			
		});
	}
	
	/* FUNKCJA ECHO DOMYŒLNA Z KATALOGIEM*/
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
				
				//regex check
				Pattern p = Pattern.compile(".*>\n", Pattern.CASE_INSENSITIVE);
				Matcher m = p.matcher(lasttxt);

				//replace tak by dodalo ostatnio uzyta komende
				String output = m.replaceAll(this.currentDir + ">" + this.lastcmd +"\n");
				
				//wyczysc ostatnie komendy
				this.lastcmd = "";
				
				//wypisz co trzeba
				this.area.setText(output + this.currentDir + ">" + text + "\n");
			}
			else
			{
				this.area.setText(this.area.getText() + this.currentDir + ">" + text + "\n");
			}
			//this.area.setText(this.area.getText() + this.currentDir + ">" + text + "\n");
		}
		else
		{
			this.area.setText(this.area.getText() + text + "\n");
		}
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
	
	/* BINDY NA KOMENDY */
	public void execute(String command)
	{
		if(command.matches("^cd.*"))
		{
			System.out.println("mam cd");
		}
		else if(command.matches("^help$"))
		{
			this.echo("HELP", false);
		}
		else
		{
			this.echo("Nie znaleziono komendy", false);
			this.echo("Dostepne komendy pod komenda help", false);
		}
		
		//œcie¿ka na koniec
		this.echo("");
	}
}
