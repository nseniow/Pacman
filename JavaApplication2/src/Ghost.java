
import java.awt.Color;
import java.awt.Graphics;

public class Ghost {
	final int OX, OY, OSPEED;
	int dx, dy, x, y;
	int row, col;
	int speed;
	int timer = 0;
	Color color;
	int difficulty;
	boolean canBeEaten;
	int eatenTimer;
	boolean trapped;
	int trappedTimer;

	int[] pointsX = new int[] { x, x + 4, x + 8, x + 12, x + 12, x + 8, x + 4, x };
	int[] pointsY = new int[] { y + 4, y, y, y + 4, y + 18, y + 15, y + 15, y + 18 };

	public Ghost(int X, int Y, int SPEED, Color COLOR, int DIFFICULTY) {
		this.x = X;
		this.y = Y;
		this.speed = SPEED;
		this.color = COLOR;
		this.difficulty = DIFFICULTY;
		this.canBeEaten = false;
		this.pointsX = new int[] { x, x + 4, x + 8, x + 12, x + 12, x + 8, x + 4, x };
		this.pointsY = new int[] { y + 4, y, y, y + 4, y + 18, y + 15, y + 15, y + 18 };

		// Save the originals so it can be reverted later
		this.OX = X;
		this.OY = Y;
		this.OSPEED = SPEED;

	}

	void turnGhost(int dir) {
		// Turn the ghost
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

	void moveGhost(Board board) {
		// Move the ghost
		this.x += dx;
		this.y += dy;

		this.row = y / 20;
		this.col = x / 20;

		// Don't move it into a wall
		try {
			if (dy > 0) {
				if (board.boardMap[row + 1][col] == 1) {
					this.stop();
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

		// Make next move based on pathMap
		int num = 0;
		int direction = 0;
		for (int i = 0; i < 4; i++) {
			if (this.isValid(i, board)) {
				num++;
			}
		}
		if (num > 0 && timer < 1) {
			// Delay 8 frames before making another move again to prevent the
			// ghosts from going back and forth
			if (row >= 11 && row <= 13 && col >= 13 && col <= 15) {
				this.timer = 1;
			} else {
				this.timer = 8;
			}

			// Make list of all moves
			try {
				// Get the best possible move
				int[] numbers = { board.pathMap[row][col - 1], board.pathMap[row][col + 1], board.pathMap[row - 1][col],
						board.pathMap[row + 1][col] };

				// If the chance is less than the difficulty percent, make the correct move
				if (Math.random() * 100 < this.difficulty) {
					if (!this.canBeEaten) {
						direction = this.minIndex(numbers); //Best move
					} else {
						direction = this.maxIndex(numbers); //Worst move
					}
				} else { //Reversed when the ghost can be eaten and is running away
					if (!this.canBeEaten) {
						direction = this.maxIndex(numbers); 
					} else {
						direction = this.minIndex(numbers);
					}

				}
				this.turnGhost(direction);
			} catch (Exception e) {

			}
		}
		// Reduce time
		this.timer--;
		// If it can't be eaten anymore, set canBeEaten to false
		if (this.eatenTimer <= 0 && this.canBeEaten) {
			this.setCanBeEaten(false);
		}

		// If it can be eaten reduce the timer
		if (this.canBeEaten) {
			this.eatenTimer--;
		}

		// Update the positions
		this.pointsX = new int[] { this.x + 3, this.x + 4 + 3, this.x + 8 + 3, this.x + 12 + 3, this.x + 12 + 3, this.x + 8 + 3, this.x + 4 + 3, this.x + 3};
		this.pointsY = new int[] { this.y + 4, this.y, this.y, this.y + 4, this.y + 18, this.y + 15, this.y + 15, this.y + 18 };
	}

	//Set the Ghost as trapped
	void setTrapped(int time) {
		this.trapped = true;
		this.trappedTimer = time;
	}
	
	//Reduce the trappedTimer so that the ghost will be released soon
	void reduceTrapTimer(int time) {
		this.trappedTimer--;
		if (this.trappedTimer <= 0) {
			this.x = this.OX;
			this.y = this.OY;
			this.trapped = false;
		}
	}

	//Set so the ghost can(t) be eaten
	void setCanBeEaten(boolean bool) {
		if (bool) {
			this.canBeEaten = true;
			this.eatenTimer = 240;
			this.speed--;
		} else {
			this.canBeEaten = false;
			this.speed = this.OSPEED;
		}
	}
	
	// Return the index of the smallest number in the array
	int minIndex(int[] array) {
		int minNum = 999;
		int minInd = 0;
		for (int i = 0; i < array.length; i++) {
			if (array[i] < minNum && array[i] != -1) {
				minNum = array[i];
				minInd = i;
			}
		}

		return minInd;
	}

	// Return the index of the largest number in the array
	int maxIndex(int[] array) {
		int maxNum = 0;
		int maxInd = 0;
		for (int i = 0; i < array.length; i++) {
			if (array[i] > maxNum && array[i] != -1) {
				maxNum = array[i];
				maxInd = i;
			}
		}

		return maxInd;
	}

	boolean isValid(int dir, Board board) {
		// 0:Left, 1:Right 2:Up 3:Down

		// Check if the next spot in that direction is open
		try {
			if (dir == 3) {
				if (board.boardMap[row + 1][col] == 1) {
					return false;
				}
			}
			if (dir == 2) {
				if (board.boardMap[row - 1][col] == 1) {
					return false;
				}
			}
			if (dir == 1) {
				if (board.boardMap[row][col + 1] == 1) {
					return false;
				}
			}
			if (dir == 0) {
				if (board.boardMap[row][col - 1] == 1) {
					return false;
				}
			}
		} catch (Exception e) {
		}

		return true;
	}

	void drawGhost(Graphics g) {
		// If ghost can be eaten, draw it in blue
		if (this.canBeEaten) {
			g.setColor(Color.BLUE);
			// If there's not much time left, flash blue and white
			if (this.eatenTimer < 90) {
				if ((this.eatenTimer / 10) % 2 == 0) {
					g.setColor(Color.WHITE);
				}
			}
		} else {
			g.setColor(this.color);
		}
		// g.fillRect(this.x + 2, this.y + 2, 16, 16);

		// Draw the ghost
		g.fillPolygon(pointsX, pointsY, 8);
	}

	void stop() {
		// Set movement values to 0
		dx = 0;
		dy = 0;
	}

	void reset() {
		// Reset all values to the originals
		this.x = this.OX;
		this.y = this.OY;
		this.row = this.x * 20;
		this.col = this.y * 20;
		this.setCanBeEaten(false);
		this.setTrapped(60);
		this.pointsX = new int[] { this.x, this.x + 4, this.x + 8, this.x + 12, this.x + 12, this.x + 8, this.x + 4,
				this.x };
		this.pointsY = new int[] { this.y + 4, this.y, this.y, this.y + 4, this.y + 18, this.y + 15, this.y + 15,
				this.y + 18 };
	}
}