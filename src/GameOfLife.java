import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.image.*;
import java.io.*;
import java.util.Scanner;

public class GameOfLife extends JFrame implements Runnable, MouseListener, MouseMotionListener
{
	
	private BufferStrategy strategy;
	private Graphics offscreenBuffer;
	private boolean gameState[][][]= new boolean[40][40][2];
	private int gameStateFrontBuffer = 0;
	private boolean isGameRunning = false;
	private boolean initialised= false;
	private String filename = "C:\\users\\cbuckley\\Desktop\\lifegame.txt";
	private String[][] arrA = new String[40][40];
	private char[] chars = new char[40];
	 char[] myChar = new char[1600];

	public static void main(String[] args) 
	{
		GameOfLife w = new GameOfLife();
	}
	
	public GameOfLife()
	{
		Dimension screensize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		int x = screensize.width/2 - 400;
		int y = screensize.height/2 - 400;
		setBounds(x,y,800,800);
		setVisible(true);
		this.setTitle("Conways Game of Life");
		
		createBufferStrategy(2);
		strategy = getBufferStrategy();
		offscreenBuffer = strategy.getDrawGraphics();
		
		addMouseListener(this);
		addMouseMotionListener(this);
		
		for(x = 0; x < 40; x++)
		{
			for(y = 0; y < 40; y++)
			{
				gameState[x][y][0] = gameState[x][y][1] = false;
			}
		}
		
		Thread t = new Thread(this);
		t.start();
		
		initialised = true;
	}
	
	@Override
	public void run() 
	{
		while(1==1)
		{
			try{Thread.sleep(100);}
			catch(InterruptedException e){}
			
			if(isGameRunning)
				doOneEpochOfGame();
			
			this.repaint();
		}
	}

	private void doOneEpochOfGame() 
	{
		int front = gameStateFrontBuffer;
		int back = (front +1)%2;
		
		for(int x = 0; x < 40; x++)
		{
			for(int y = 0; y < 40; y++)
			{
				int liveneighbours = 0;
				for(int xx = -1; xx <= 1; xx++)
				{
					for(int yy = -1; yy <= 1; yy++)
					{
						if(xx!=0 || yy!=0)
						{
							int xxx = x+xx;
							if(xxx < 0)
								xxx=39;
							else if(xxx > 39)
								xxx = 0;
							int yyy = y + yy;
							if(yyy < 0)
								yyy=39;
							else if (yyy>39)
								yyy=0;
							if(gameState[xxx][yyy][front])
								liveneighbours++;
						}
					}
				}
				
				if(gameState[x][y][front])
				{
					if(liveneighbours < 2)
						gameState[x][y][back] =false;
					else if(liveneighbours < 4)
						gameState[x][y][back] =true;
					else
						gameState[x][y][back] =false;
				}
				else
				{
					if(liveneighbours == 3)
						gameState[x][y][back] =true;
					else
						gameState[x][y][back] =false;
				}
			}
		}
		gameStateFrontBuffer = back;
	}
	
	private void randomiseGameState()
	{
		for(int x = 0; x < 40; x++)
		{
			for(int y = 0; y < 40; y++)
			{
				gameState[x][y][gameStateFrontBuffer] = (Math.random()< 0.25);
			}
		}
	}

	@Override
	public void mouseClicked(MouseEvent arg0) 
	{
		
	}

	@Override
	public void mouseEntered(MouseEvent arg0) 
	{
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) 
	{

	}

	@Override
	public void mousePressed(MouseEvent arg0) 
	{
		if(!isGameRunning)
		{
			int x = arg0.getX();
			int y = arg0.getY();
			if(x>=15 && x<=85 && y>=40 && y <= 70)
			{
				isGameRunning = true;
				return;
			}
			
			if(x>=115 && x<=215 && y>=40 && y <= 70)
			{
				randomiseGameState();
				return;
			}
			if(x>=315 && x<=385 && y>=40 && y <= 70)
			{
				// save data
				for(int a = 0; a < 40; a++)
				{
					for(int b = 0; b < 40; b++)
					{
					if(gameState[a][b][gameStateFrontBuffer])
					{
						arrA[a][b] = "1";
					}
					else{
						arrA[a][b] = "0";
					}
				}
					
				}
				
				try{
					BufferedWriter writer = new BufferedWriter(new FileWriter(filename));
					for(int a = 0; a < 40; a++)
					{
						for(int b = 0; b < 40; b++)
						{
					writer.write(arrA[a][b]);
					}
				}
					
					writer.close();
					System.out.println("Save Complete");
				}
				catch(IOException e){
				}
				
				return;
			}
			
			if(x>=415 && x<=475 && y>=40 && y <= 70)
			{
				// load data
				
				
				Scanner s;
				try {
					s = new Scanner(new BufferedReader(new FileReader(filename)));
					 while (s.hasNext())
			            {
			               String str = s.next(); 
			                myChar = str.toCharArray();
			                int n = 0;
			                for(int a = 0; a < 40; a++)
								{
									for(int b = 0; b < 40; b++)
									{
										if(myChar[n] == '1'){
								            		gameState[a][b][gameStateFrontBuffer] = true;
										}
								            	
								            	else{
								            		gameState[a][b][gameStateFrontBuffer] = false;
								            	}
										n++;
								            }
								}
			               
			            }
					
					
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				
				
				
//				try {
//					BufferedReader reader = new BufferedReader(new FileReader(filename));
//					for(int a = 0; a < 40; a++)
//					{
//						for(int b = 0; b < 40; b++)
//						{
//							 int c;
//					            while ((c = reader.read()) != -1) {
////					            	if(c == 1){
////					            		gameState[a][b][gameStateFrontBuffer] = true;
////					            	}
////					            	else{
////					            		gameState[a][b][gameStateFrontBuffer] = false;
////					            	}
//					            	System.out.println(c);
//					            }
//					}
//				}
//					reader.close();
//					System.out.println("Load Done!");
//				} 
//				catch (IOException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
				return;
			}
		}
		
		int x = arg0.getX()/20;
		int y = arg0.getY()/20;
		
		gameState[x][y][gameStateFrontBuffer] = !gameState[x][y][gameStateFrontBuffer];
		
		this.repaint();
	}

	@Override
	public void mouseReleased(MouseEvent arg0) 
	{

	}
	
	public void paint(Graphics g)
	{
		if(!initialised)
			return;
		
		g = offscreenBuffer;
		
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, 800, 800);
		
		g.setColor(Color.WHITE);
		for(int x = 0; x < 40; x++)
		{
			for(int y = 0; y < 40; y++)
			{
				if(gameState[x][y][gameStateFrontBuffer])
					g.fillRect(x*20, y*20, 20, 20);
			}
		}
		
		if(!isGameRunning)
		{
			g.setColor(Color.BLUE);
			g.fillRect(15, 40, 70, 30);
			g.fillRect(115, 40, 100, 30);
			g.fillRect(315, 40, 70, 30);
			g.fillRect(415, 40, 70, 30);
			g.setFont(new Font("Times", Font.PLAIN, 24));
			g.setColor(Color.BLACK);
			g.drawString("Start", 22, 62);
			g.drawString("Random", 122, 62);
			g.drawString("Save", 322, 62);
			g.drawString("Load", 422, 62);
			
		}
		
		strategy.show();
	}

	@Override
	public void mouseDragged(MouseEvent arg0) {		
		int x = arg0.getX()/20;
		int y = arg0.getY()/20;
		
		gameState[x][y][gameStateFrontBuffer] = !gameState[x][y][gameStateFrontBuffer];
		
		this.repaint();
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

}