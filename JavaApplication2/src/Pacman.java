import java.awt.Color;
import java.awt.Graphics;

public class Pacman {
	final int OX, OY;
	int dx, dy, x, y;
	int nextDir = -1;
	int row, col;
	int speed;
	int score = 0;
	int lives = 3;

	public Pacman(int X, int Y, int SPEED) {
		this.x = X;
		this.y = Y;
		this.speed = SPEED;

		this.OX = X;
		this.OY = Y;
	}

	void turnPacman(int dir) {
		switch (dir) {
		case 0: // Left
			this.y = this.row * 20;

			this.dy = 0;
			this.dx = -1 * this.speed;
			break;

		case 1: // Right
			this.y = this.row * 20;

			this.dy = 0;
			this.dx = this.speed;
			break;

		case 2: // Up
			this.x = this.col * 20;

			this.dy = -1 * this.speed;
			this.dx = 0;
			break;

		case 3: // Down
			this.x = this.col * 20;

			this.dy = this.speed;
			this.dx = 0;
			break;

		}
	}

	void movePacman(Board board) {
		// Move the pacman
		this.x += dx;
		this.y += dy;

		// Check if prerequested move is valid and make move if possible
		if (this.isValid(this.nextDir, board)) {
			this.turnPacman(this.nextDir);
			nextDir = -1;
		}
		// Set the row and column for use with the 2d array
		this.row = y / 20;
		this.col = x / 20;

		// Don't move it into a wall
		try {
			if (dy > 0) {
				if (board.boardMap[row + 1][col] == 1) { // Check if about to hit wall
					this.stop(); // Stop the pacman
				}
			}
			if (dy < 0) {
				if (board.boardMap[row][col] == 1) {
					this.y = (row + 1) * 20;
					this.stop();
				}
			}
			if (dx > 0) {
				if (board.boardMap[row][col + 1] == 1) {
					this.stop();
				}
			}
			if (dx < 0) {
				if (board.boardMap[row][col] == 1) {
					this.x = (col + 1) * 20;
					this.stop();
				}
			}
		} catch (Exception e) {

		}

		// Jump from side to side
		if (x > 520 && row == 12 && dx > 0) {
			x = 0;
		} else if (x < 0 && row == 12 && dx < 0) {
			x = 520;
		}

		// Eat the balls
		if (board.boardMap[row][col] == 5) {
			this.score += 5;
			board.boardMap[row][col] = 0;
			if (board.checkWin()) {
				Main.win();
			}
		}
		// Eat the big balls
		if (board.boardMap[row][col] == 6) {
			for (int i = 0; i < Main.ghosts.length; i++) {
				Main.ghosts[i].setCanBeEaten(true);
				board.boardMap[row][col] = 0;
			}
		}

		// Check ghost hit
		for (int i = 0; i < Main.ghosts.length; i++) {
			if (Main.ghosts[i].row == this.row && Main.ghosts[i].col == this.col) {
				if (!Main.ghosts[i].canBeEaten) {
					// If the ghost can eat you
					Main.sleep(500);
					this.reset();
					this.lives--;
					for (int k = 0; k < Main.ghosts.length; k++) {
						Main.ghosts[k].reset();
					}
					if (this.lives <= 0) {
						Main.lose();
					} else {
						Main.sleep(2000);
					}
					Main.paused = true;
					this.reset();
					Main.paused = false;
				} else {
					// If the you can eat the ghosts
					Main.ghosts[i].reset();
					this.score += 75;
				}
				break;
			}
		}
	}

	boolean isValid(int dir, Board board) {
		// 0:Left, 1:Right 2:Up 3:Down

		// Check if the next spot in that direction is open
		try {
			if (dir == -1) {
				return false;
			}

			if (dir == 3) {
				if (board.boardMap[row + 1][col] == 1 || board.boardMap[row + 1][col] == 2) {
					return false;
				}
			}
			if (dir == 2) {
				if (board.boardMap[row - 1][col] == 1 || board.boardMap[row - 1][col] == 2) {
					return false;
				}
			}
			if (dir == 1) {
				if (board.boardMap[row][col + 1] == 1 || board.boardMap[row][col + 1] == 2) {
					return false;
				}
			}
			if (dir == 0) {
				if (board.boardMap[row][col - 1] == 1 || board.boardMap[row][col - 1] == 2) {
					return false;
				}
			}
		} catch (Exception e) {
		}

		return true;
	}

	void drawPacman(Graphics g) {
		// Draw the pacman to the screen
		g.setColor(Color.YELLOW);

		g.fillOval(this.x + 1, this.y + 1, 16, 16);

		// If the frame count / 5 is even, draw the triangle so its mouth looks open
		if (Main.frames / 5 % 2 == 0) {
			g.setColor(Color.black);
			if (this.dx > 0) {
				g.fillPolygon(new int[] { this.x + 9, this.x + 17, this.x + 17 },
						new int[] { this.y + 9, this.y + 5, this.y + 13 }, 3);
			} else if (this.dx < 0) {
				g.fillPolygon(new int[] { this.x + 9, this.x + 1, this.x + 1 },
						new int[] { this.y + 9, this.y + 5, this.y + 14 }, 3);
			} else if (this.dy > 0) {
				g.fillPolygon(new int[] { this.x + 9, this.x + 5, this.x + 14 },
						new int[] { this.y + 9, this.y + 17, this.y + 17 }, 3);
			} else if (this.dy < 0) {
				g.fillPolygon(new int[] { this.x + 9, this.x + 5, this.x + 13 },
						new int[] { this.y + 9, this.y + 1, this.y + 1 }, 3);
			}
		}
	}

	void stop() {
		// Stop the pacman
		dx = 0;
		dy = 0;
	}

	void reset() {
		// Reset values to the originals
		this.x = this.OX;
		this.y = this.OY;
	}
}
