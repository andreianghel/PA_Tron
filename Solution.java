import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**Clasa Solution;
 *
 *author DFTBA;
 */
public class Solution {
	
	//Metoda ce verifica daca o celula este o fundatura;
	public static boolean deadEnd(int rowMyTron, int colMyTron, Board board) {
		return (touchWall(rowMyTron, colMyTron, board) == 4) ? true : false;
	}
	
	//Metoda ce numara peretii din jurul unei celule;
	public static int touchWall(int rowMyTron, int colMyTron, Board board) {
		int numWall = 0;
		
		if (youShallNotPass(board.b[rowMyTron-1][colMyTron].c)) numWall++;
		if (youShallNotPass(board.b[rowMyTron][colMyTron-1].c)) numWall++;
		if (youShallNotPass(board.b[rowMyTron][colMyTron+1].c)) numWall++;		
		if (youShallNotPass(board.b[rowMyTron+1][colMyTron].c)) numWall++;
		
		return numWall;
	}
	
	//Metoda ce masoara celule libere in directia: stanga;
	public static int getDistLeft(int rowMyTron, int colMyTron, Board board) {
		int dist = 0;
		
		while (colMyTron-- != 0)
			if (board.b[rowMyTron][colMyTron].c == '-') dist++;
			else break;

		return dist;
	}

	//Metoda ce masoara celule libere in directia: dreapta;	
	public static int getDistRight(int rowMyTron, int colMyTron, Board board) {
		int dist = 0;
		
		while (colMyTron++ != board.b[0].length)
			if (board.b[rowMyTron][colMyTron].c == '-') dist++;
			else break;
		
		return dist;
	}
	
	//Metoda ce masoara celule libere in directia: sus;
	public static int getDistUp(int rowMyTron, int colMyTron, Board board) {
		int dist = 0;
		
		while (rowMyTron-- != 0)
			if (board.b[rowMyTron][colMyTron].c == '-') dist++;
			else break;
		
		return dist;
	}
	
	//Metoda ce masoara celule libere in directia: jos;
	public static int getDistDown(int rowMyTron, int colMyTron, Board board) {
		int dist = 0;
		
		while (rowMyTron++ != 0)
			if (board.b[rowMyTron][colMyTron].c == '-') dist++;
			else break;
		
		return dist;
	}
	
	//Metoda ce verifica daca din celula prezenta pot face o singura mutare;
	public static boolean checkSoloMove(double[] move) {
		return (move[0]+move[1]+move[2]+move[3] == 1) ? true : false;
	}
	
	//Metoda ce verifica daca o celula este ocupata/zid;
	public static boolean youShallNotPass(char cell) {
		return (cell == '#' || cell == 'r' || cell == 'g') ? true : false;
	}
	
	//Metoda ce intoarce urmatoarea mutare valida;
	public static String nextMove(char myTron, int rowMyTron, int colMyTron, int rowEnemy, int colEnemy, Board board) {
		double[] myMove = new double[4]; /* 0: daca nu pot muta pe pozitia respectiv;  1: altfel;
											move[0]: LEFT
    										move[1]: RIGHT
    										move[2]: UP
    										move[3]: DOWN */
		
		//verific mediul inconjurator, sa vad daca scap usor:
		if (!youShallNotPass(board.b[rowMyTron+1][colMyTron].c))	myMove[3] = 1;
		if (!youShallNotPass(board.b[rowMyTron-1][colMyTron].c))	myMove[2] = 1;
		if (!youShallNotPass(board.b[rowMyTron][colMyTron+1].c))	myMove[1] = 1;
		if (!youShallNotPass(board.b[rowMyTron][colMyTron-1].c))	myMove[0] = 1;

		if (checkSoloMove(myMove)) //am o singura miscare posibila, o fac;
			return ((myMove[0] == 1) ? "LEFT" : ((myMove[1] == 1) ? "RIGHT" :
				((myMove[2] == 1) ? "UP" : ((myMove[3] == 1) ? "DOWN" : "ERR")))); 
	
		
		//calculez cate spatii sunt libere, in toate directiile:
		int[] dist = new int[4];
		
		dist[3] = getDistDown(rowMyTron, colMyTron, board);
		dist[2] = getDistUp(rowMyTron, colMyTron, board);
		dist[1] = getDistRight(rowMyTron, colMyTron, board);
		dist[0] = getDistLeft(rowMyTron, colMyTron, board);
		
		// implementing hug the wall, fill scenario:
		int[] walls = new int[4];
		
		if (myMove[3] == 1)
			walls[3] = (deadEnd(rowMyTron+1, colMyTron, board)) ? Integer.MIN_VALUE : touchWall(rowMyTron+1, colMyTron, board);
		else 
			walls[3] = Integer.MIN_VALUE;
		if (myMove[2] == 1)
			walls[2] = (deadEnd(rowMyTron-1, colMyTron, board)) ? Integer.MIN_VALUE : touchWall(rowMyTron-1, colMyTron, board);
		else
			walls[2] = Integer.MIN_VALUE;
		if (myMove[1] == 1)
			walls[1] = (deadEnd(rowMyTron, colMyTron+1, board)) ? Integer.MIN_VALUE : touchWall(rowMyTron, colMyTron+1, board);
		else 
			walls[1] = Integer.MIN_VALUE;
		if (myMove[0] == 1)
			walls[0] = (deadEnd(rowMyTron, colMyTron-1, board)) ? Integer.MIN_VALUE : touchWall(rowMyTron, colMyTron-1, board);
		else
			walls[0] = Integer.MIN_VALUE;
		
		int iMax = -1, max = Integer.MIN_VALUE, wallMax = Integer.MIN_VALUE;
		
		for (int i = 0; i < 4; i++) {
			if (walls[i] == max)
				if (dist[i] > wallMax)
					iMax = i;
			if (walls[i] > max) {
				max = walls[i];
				iMax = i;
			}
		}
		
		switch (iMax) {
			case 0: return "LEFT";
			case 1: return "RIGHT";
			case 2: return "UP";
			case 3: return "DOWN";
			default: return "ERR";
		}
		
	}
	
	public static String nextMoveHunt(char myTron, int rowMyTron, int colMyTron, int rowEnemy, int colEnemy, Board board) {
		double myMove[] = new double[4], move[] = new double[4];
		
		if (!youShallNotPass(board.b[rowMyTron+1][colMyTron].c)) {
			myMove[3] = board.getEuclideanDistance(rowMyTron, colMyTron, rowEnemy, colEnemy);
			move[3] = 1;
		}
		else {
			myMove[3] = Double.MAX_VALUE;
			move[3] = 0;
		}
		if (!youShallNotPass(board.b[rowMyTron-1][colMyTron].c)) {
			myMove[2] = board.getEuclideanDistance(rowMyTron, colMyTron, rowEnemy, colEnemy);
			move[2] = 1;
		}
		else {
			myMove[2] = Double.MAX_VALUE;
			move[2] = 0;
		}
		if (!youShallNotPass(board.b[rowMyTron][colMyTron+1].c)) {
			myMove[1] = board.getEuclideanDistance(rowMyTron, colMyTron, rowEnemy, colEnemy);
			move[1] = 1;
		}
		else {
			myMove[1] = Double.MAX_VALUE;
			move[1] = 0;
		}
		if (!youShallNotPass(board.b[rowMyTron][colMyTron-1].c)) {
			myMove[0] = board.getEuclideanDistance(rowMyTron, colMyTron, rowEnemy, colEnemy);
			move[0] = 1;
		}
		else {
			myMove[0] = Double.MAX_VALUE;
			move[0] = 0;
		}
		
		if (checkSoloMove(move)) //am o singura miscare posibila, o fac;
			return ((move[0] == 1) ? "LEFT" : ((move[1] == 1) ? "RIGHT" :
				((move[2] == 1) ? "UP" : ((move[3] == 1) ? "DOWN" : "ERR")))); 
		
		double min = Double.MAX_VALUE;
		int iMin = -1;
		
		for (int i = 0; i < 4; i++) {
			if (myMove[i] < min) {
				min = myMove[i];
				iMin = i;
			}
		}
		
		switch (iMin) {
			case 0: return "LEFT";
			case 1: return "RIGHT";
			case 2: return "UP";
			case 3: return "DOWN";
			default: return "ERR";
		}
	}
	
	public static void makeMove(char myTron, int rowMyTron, int colMyTron, int rowEnemy, int colEnemy, Board board) {
		int rowT, colT, rowE, colE;
		
		rowT = (myTron == 'r') ? rowMyTron : rowEnemy;
		colT = (myTron == 'r') ? colMyTron : colEnemy;
		rowE = (myTron == 'r') ? rowEnemy : rowMyTron; 
		colE = (myTron == 'r') ? colEnemy : colMyTron;

		//cand sunt departe de adversar incerc sa tai din spatiu dintre boti;
		if (board.getEuclideanDistance(rowT, colT, rowE, colE) > (board.N+board.M)/3 
			&& (board.nrFreeSpaces/(board.nrFreeSpaces+board.nrOccupiedSpaces)) > 0.45)
			System.out.println(nextMoveHunt(myTron, rowT, colT, rowE, colE, board));
		else
			System.out.println(nextMove(myTron, rowT, colT, rowE, colE, board));
	}
	
	
	public static void main(String[] args) {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		
		try {
			char myTron = br.readLine().charAt(0);
			String[] position = br.readLine().split(" ");
			int rowMyTron = Integer.parseInt(position[0]), colMyTron = Integer.parseInt(position[1]),
			    rowEnemy = Integer.parseInt(position[2]), colEnemy = Integer.parseInt(position[3]);
			String[] dim = br.readLine().split(" ");
			int N = Integer.parseInt(dim[0]), M = Integer.parseInt(dim[1]);
			
			Board board = new Board(N, M);
			
			for (int i = 0; i < N; i++) {
				String line = br.readLine();
				board.b[i] = new Point[M];
				for (int j = 0; j < M; j++) {
					board.b[i][j] = new Point(line.charAt(j), i, j);
					
					if (line.charAt(j) == '-') board.nrFreeSpaces++;
					else board.nrOccupiedSpaces++;
				}
			}
			
			makeMove(myTron, rowMyTron, colMyTron, rowEnemy, colEnemy, board);
		} catch (IOException e) {
			// Trist moment.
			System.out.println("Err: "+e.getMessage());
		} /*catch (Exception e) {	//nu decomentati deocamdata deoarece face debugingul mai greu;
			// Si mai trist.
			System.out.println("Err: "+e.getMessage());
		}*/
	}
}

class Point {
	public char c;
	public int row;
	public int col;
	public boolean visited;
	
	public Point(char c, int row, int col) {
		this.c = c;
		this.row = row;
		this.col = col;
		this.visited = true;
	}
}

class Board {
	public Point[][] b;
	public int nrFreeSpaces;
	public int nrOccupiedSpaces;
	public int N;
	public int M;
	
	public Board(int N, int M) {
		this.N = N; 
		this.M = M;
		this.b = new Point[N][M];
		this.nrFreeSpaces = 0;
		this.nrOccupiedSpaces = 0;
	}
	
	public double getEuclideanDistance(int rowMyTron, int colMyTron, int rowEnemy, int colEnemy) {
		return Math.sqrt(Math.abs(Math.pow((rowEnemy-rowMyTron), 2)+Math.pow((colEnemy-colMyTron), 2)));
	}
}