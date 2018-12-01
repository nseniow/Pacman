import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class Main extends JPanel implements KeyListener {
	// Make all objects static
	static Board board;
	static Pacman pacman, pacman2;
	static int frames = 0;
	static int level = 1;
	static int option = 0;
	static boolean win = false;
	static boolean lose = false;
	static boolean menu = true;
	static boolean player2 = false;
	static boolean started = false;
	static boolean paused = false;
	static Ghost[] ghosts = { new Ghost(260, 225, 3, Color.RED, 80), new Ghost(240, 245, 3, Color.CYAN, 70),
			new Ghost(280, 245, 3, Color.PINK, 65), new Ghost(260, 245, 3, Color.YELLOW, 75) };

	public static void main(String[] args) {
		//Set the ghost trapped variables before starting
		ghosts[0].setTrapped(40);
		ghosts[1].setTrapped(80);
		ghosts[2].setTrapped(120);
		ghosts[3].setTrapped(180);

		//Start the game
		Main game = new Main();

	}

	public Main() {
		// Request keyboard focus
		this.requestFocus();
		// Initialize Objects
		board = new Board();
		pacman = new Pacman(260, 300, 3);
		pacman2 = new Pacman(260, 300, 3);

		// Initialize JFrame
		JFrame frame = new JFrame("Pacman");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(546, 671);
		frame.setVisible(true);
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);
		frame.setLayout(new BorderLayout());
		frame.addKeyListener(this);
		frame.setFocusable(true);

		frame.add(this, BorderLayout.CENTER);

		// Main loop
		while (true) {
			if(!menu){	
				// Move pacman
				pacman.movePacman(board);
				if(player2){
					pacman2.movePacman(board);
				}
	
				// Ghost AI
				board.createPath(pacman.row, pacman.col, 1);
				if(player2){
					board.createPath(pacman2.row, pacman2.col, 1);
				}
				for (int i = 0; i < ghosts.length; i++) {
					if (ghosts[i].trapped) {
						ghosts[i].reduceTrapTimer(1);
					} else {
						ghosts[i].moveGhost(board);
					}
				}
			}

			sleep(33);
			repaint();
			board.resetPath();
			frames++;
		}
	}

	static void win() {
		//When you win, up the level, delay, and increase ghost difficulty for the next level
		level++;
		sleep(2500);
		for (int i = 0; i < ghosts.length; i++) {
			ghosts[i].difficulty *= 1.1;
			ghosts[i].setTrapped(i * 40);
			ghosts[i].reset();
		}
		//Reset Board and pacman
		board = new Board();
		pacman.reset();
		pacman2.reset();
		
		//If the lives is less than 3, increase by 1
		if(pacman.lives<6){
			pacman.lives++;
		}
	}

	static void lose() {
		//When you lose, Reset everything
		sleep(2500);
		ghosts[0] = new Ghost(260, 225, 3, Color.RED, 80);
		ghosts[1] = new Ghost(240, 245, 3, Color.CYAN, 70);
		ghosts[2] = new Ghost(280, 245, 3, Color.PINK, 65);
		ghosts[3] = new Ghost(260, 245, 3, Color.YELLOW, 75);

		ghosts[0].setTrapped(40);
		ghosts[1].setTrapped(80);
		ghosts[2].setTrapped(120);
		ghosts[3].setTrapped(180);
		
		level = 1;
		board = new Board();

		pacman = new Pacman(260, 300, 3);
		pacman2 = new Pacman(260, 300, 3);
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		setBackground(Color.BLUE);

		// Draw map
		board.drawBoard(g);
		// board.drawPathBoard(g);

		// Draw pacman
		pacman.drawPacman(g);
		if(player2){
			pacman2.drawPacman(g);
		}

		// Draw ghosts
		for (int i = 0; i < ghosts.length; i++) {
			ghosts[i].drawGhost(g);
		}

		drawUI(board, pacman, g);
		
		if(menu){
			drawMenu(g);
		}
		//board.drawPathBoard(g);
	}

	public void drawUI(Board board, Pacman pacman, Graphics g) {
		//Draw lives
		if(player2 || option == 1){
			g.setColor(Color.YELLOW);
			switch (pacman.lives) {
			
			case 6:
				g.fillOval(145, 581, 24, 24);
			case 5:
				g.fillOval(115, 581, 24, 24);
			case 4:
				g.fillOval(85, 581, 24, 24);
			case 3:
				g.fillOval(55, 581, 24, 24);
			case 2:
				g.fillOval(25, 581, 24, 24);
	
			}
			switch (pacman2.lives) {
			
			case 6:
				g.fillOval(145, 611, 24, 24);
			case 5:
				g.fillOval(115, 611, 24, 24);
			case 4:
				g.fillOval(85, 611, 24, 24);
			case 3:
				g.fillOval(55, 611, 24, 24);
			case 2:
				g.fillOval(25, 611, 24, 24);
	
			}
			//Draw the mouths
			g.setColor(Color.BLUE);
			g.fillPolygon(new int[] { 55 + 12, 55 + 24, 55 + 24 },
					new int[] { 581 + 12, 581 + 6, 581 + 18 }, 3);
			g.fillPolygon(new int[] { 25 + 12, 25 + 24, 25 + 24 },
					new int[] { 581 + 12, 581 + 6, 581 + 18 }, 3);
			g.fillPolygon(new int[] { 85 + 12, 85 + 24, 85 + 24 },
					new int[] { 581 + 12, 581 + 6, 581 + 18 }, 3);
			g.fillPolygon(new int[] { 115 + 12, 115 + 24, 115 + 24 },
					new int[] { 581 + 12, 581 + 6, 581 + 18 }, 3);
			g.fillPolygon(new int[] { 145 + 12, 145 + 24, 145 + 24 },
					new int[] { 581 + 12, 581 + 6, 581 + 18 }, 3);
			g.fillPolygon(new int[] { 55 + 12, 55 + 24, 55 + 24 },
					new int[] { 611 + 12, 611 + 6, 611 + 18 }, 3);
			g.fillPolygon(new int[] { 25 + 12, 25 + 24, 25 + 24 },
					new int[] { 611 + 12, 611 + 6, 611 + 18 }, 3);
			g.fillPolygon(new int[] { 85 + 12, 85 + 24, 85 + 24 },
					new int[] { 611 + 12, 611 + 6, 611 + 18 }, 3);
			g.fillPolygon(new int[] { 115 + 12, 115 + 24, 115 + 24 },
					new int[] { 611 + 12, 611 + 6, 611 + 18 }, 3);
			g.fillPolygon(new int[] { 145 + 12, 145 + 24, 145 + 24 },
					new int[] { 611 + 12, 611 + 6, 611 + 18 }, 3);
			
			
		}else{
			g.setColor(Color.YELLOW);
			switch (pacman.lives) {
			
			case 6:
				g.fillOval(145, 591, 24, 24);
			case 5:
				g.fillOval(115, 591, 24, 24);
			case 4:
				g.fillOval(85, 591, 24, 24);
			case 3:
				g.fillOval(55, 591, 24, 24);
			case 2:
				g.fillOval(25, 591, 24, 24);
	
			}
			//Draw the mouths
			g.setColor(Color.BLUE);
			g.fillPolygon(new int[] { 55 + 12, 55 + 24, 55 + 24 },
					new int[] { 591 + 12, 591 + 6, 591 + 18 }, 3);
			g.fillPolygon(new int[] { 25 + 12, 25 + 24, 25 + 24 },
					new int[] { 591 + 12, 591 + 6, 591 + 18 }, 3);
			g.fillPolygon(new int[] { 85 + 12, 85 + 24, 85 + 24 },
					new int[] { 591 + 12, 591 + 6, 591 + 18 }, 3);
			g.fillPolygon(new int[] { 115 + 12, 115 + 24, 115 + 24 },
					new int[] { 591 + 12, 591 + 6, 591 + 18 }, 3);
			g.fillPolygon(new int[] { 145 + 12, 145 + 24, 145 + 24 },
					new int[] { 591 + 12, 591 + 6, 591 + 18 }, 3);
		
		}
		
		g.setColor(Color.YELLOW);
		

		//Draw the score
		Font font = new Font("Arial Monospaced", Font.BOLD, 60);
		Font font2 = new Font("Arial Monospaced", Font.BOLD, 15);
		g.setFont(font);
		String text = Integer.toString(pacman.score);
		if(player2){
			text = Integer.toString(pacman.score + pacman2.score);
		}
		int width = g.getFontMetrics().stringWidth(text);
		g.drawString(text, 518 - width, 631);
		g.drawString(Integer.toString(level), 255, 631);
		
		g.setFont(font2);
		g.drawString("Level:", 250, 575);
		g.drawString("Score:", 468, 575);
		g.drawString("Lives:", 25, 575);

	}
	
	static void drawMenu(Graphics g){
		g.setColor(Color.WHITE);
		Font font = new Font("Arial Monospaced", Font.BOLD, 60);
		Font font1 = new Font("Arial Monospaced", Font.BOLD, 30);
		Font font2 = new Font("Arial Monospaced", Font.BOLD, 15);
		g.setFont(font);
		//g.fillRect(123, 100, 300, 100);
		//g.fillRect(123, 400, 300, 100);
		
		if(started){
			g.drawString("Hit Enter To", 100, 160);
			g.drawString("Resume", 155, 210);
			
			g.drawString("ESC", 210, 360);
			g.drawString("To Exit", 165, 410);
		}else{
			g.drawString("Hit Enter To", 100, 60);
			g.drawString("Select", 180, 110);
			g.drawString("1 Player", 145, 255);
			g.drawString("2 Player", 145, 345);
			g.drawPolygon(new int[] {120, 140, 120} ,new int[]{225 + option * 90, 235 + option * 90, 245 + option * 90}, 3);
		}
		
		
		g.setColor(Color.white);
		//WASD
		g.fillRect(100, 500, 50, 50);
		g.fillRect(45, 500, 50, 50);
		g.fillRect(155, 500, 50, 50);
		g.fillRect(100, 445, 50, 50);
		
		//ARROW KEYS
		g.fillRect(390, 500, 50, 50);
		g.fillRect(445, 500, 50, 50);
		g.fillRect(335, 500, 50, 50);
		g.fillRect(390, 445, 50, 50);
		
		//If the option is single player, show you can use both
		if(option == 0){
			g.drawString("OR", 228, 520);
		}else{
			//If its multiplayer, show that player 1 uses wasd and 2 used arrows
			g.setFont(font1);
			g.drawString("Player 1", 70, 425);
			g.drawString("Player 2", 360, 425);
		}
		
		
		g.setFont(font1);
		//Draw the WASDD
		g.setColor(Color.BLACK);
		g.drawString("W", 110, 480);
		g.drawString("A", 55, 535);
		g.drawString("S", 110, 535);
		g.drawString("D", 165, 535);
		
		//Draw the arrow keys
		g.fillPolygon(new int[]{415, 430, 430, 415, 400, 400}, new int[]{540, 525, 520, 535, 520, 525} , 6);
		g.fillPolygon(new int[]{415, 430, 430, 415, 400, 400}, new int[]{455, 470, 475, 460, 475, 470} , 6);
		g.fillPolygon(new int[]{350, 365, 370, 355, 370, 365}, new int[]{525, 540, 540, 525, 510, 510} , 6);
		g.fillPolygon(new int[]{480, 465, 460, 475, 460, 465}, new int[]{525, 540, 540, 525, 510, 510} , 6);
		
		g.setFont(font2);
		g.setColor(Color.GRAY);
		g.drawString("Nicholas Seniow", 421, 237);
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if(!paused){
			// 0:Left, 1:Right 2:Up 3:Down
			// If there are 2 players, the wasd controls 1 and arrows control the other
			if(player2){
				if (e.getKeyCode() == KeyEvent.VK_W) {
					if(!menu){	
						if (pacman.isValid(2, board)) { // Check for valid move
							pacman.turnPacman(2); // Turn the pacman
						} else {
							pacman.nextDir = 2; // Set the next direction
						}
					}
				} else if (e.getKeyCode() == KeyEvent.VK_A) {
					if(!menu){	
						if (pacman.isValid(0, board)) {
							pacman.turnPacman(0);
						} else {
							pacman.nextDir = 0;
						}
					}
				} else if (e.getKeyCode() == KeyEvent.VK_S) {
					if(!menu){
						if (pacman.isValid(3, board)) {
							pacman.turnPacman(3);
						} else {
							pacman.nextDir = 3;
						}
					}
		
				} else if (e.getKeyCode() == KeyEvent.VK_D) {
					if(!menu){	
						if (pacman.isValid(1, board)) {
							pacman.turnPacman(1);
						} else {
							pacman.nextDir = 1;
						}
					}
				}else if (e.getKeyCode() == KeyEvent.VK_UP) {
					if(!menu){	
						if (pacman2.isValid(2, board)) { // Check for valid move
							pacman2.turnPacman(2); // Turn the pacman
						} else {
							pacman2.nextDir = 2; // Set the next direction
						}
					}
				} else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
					if(!menu){	
						if (pacman2.isValid(0, board)) {
							pacman2.turnPacman(0);
						} else {
							pacman2.nextDir = 0;
						}
					}
				} else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
					if(!menu){
						if (pacman2.isValid(3, board)) {
							pacman2.turnPacman(3);
						} else {
							pacman2.nextDir = 3;
						}
					}
		
				} else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
					if(!menu){	
						if (pacman2.isValid(1, board)) {
							pacman2.turnPacman(1);
						} else {
							pacman2.nextDir = 1;
						}
					}
					
				}
			}else{
				// If only one player, both controlls control it
				if (e.getKeyCode() == KeyEvent.VK_W || e.getKeyCode() == KeyEvent.VK_UP) {
					if(!menu){	
						if (pacman.isValid(2, board)) { // Check for valid move
							pacman.turnPacman(2); // Turn the pacman
						} else {
							pacman.nextDir = 2; // Set the next direction
						}
					}else if(!started){
						option = 0;
					}
				} else if (e.getKeyCode() == KeyEvent.VK_A || e.getKeyCode() == KeyEvent.VK_LEFT) {
					if(!menu){	
						if (pacman.isValid(0, board)) {
							pacman.turnPacman(0);
						} else {
							pacman.nextDir = 0;
						}
					}
				} else if (e.getKeyCode() == KeyEvent.VK_S || e.getKeyCode() == KeyEvent.VK_DOWN) {
					if(!menu){
						if (pacman.isValid(3, board)) {
							pacman.turnPacman(3);
						} else {
							pacman.nextDir = 3;
						}
					}else if(!started){
						option = 1;
					}
	
				} else if (e.getKeyCode() == KeyEvent.VK_D || e.getKeyCode() == KeyEvent.VK_RIGHT) {
					if(!menu){	
						if (pacman.isValid(1, board)) {
							pacman.turnPacman(1);
						} else {
							pacman.nextDir = 1;
						}
					}
				}
			}
			if (e.getKeyCode() == KeyEvent.VK_ENTER) {
				if(menu){
					menu = false;
					started = true;
					if(option == 1){
						player2 = true;
					}
				}
				
			} else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
				if(menu){
					System.exit(0);
				}else{
					menu = true;
				}
			}
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}

	static void sleep(int x) {
		try {
			Thread.sleep(x);
		} catch (InterruptedException e) {
		}
	}

}