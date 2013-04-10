import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


public class Solution
{
	//Metoda ce verifica daca o celula este o fundatura;
	public static boolean deadEnd(int rowMyTron, int colMyTron, char[][] board)
	{
		return (touchWall(rowMyTron, colMyTron, board) == 4) ? true : false;
	}
	
	//Metoda ce numara peretii din jurul unei celule;
	public static int touchWall(int rowMyTron, int colMyTron, char[][] board)
	{
		int numWall = 0;
		
		if (youShallNotPass(board[rowMyTron-1][colMyTron])) numWall++;
		if (youShallNotPass(board[rowMyTron][colMyTron-1])) numWall++;
		if (youShallNotPass(board[rowMyTron][colMyTron+1])) numWall++;		
		if (youShallNotPass(board[rowMyTron+1][colMyTron])) numWall++;
		
		return numWall;
	}
	
	//Metoda ce masoara celule libere in directia: stanga;
	public static int getDistLeft(int rowMyTron, int colMyTron, char[][] board)
	{
		int dist = 0;
		while (colMyTron-- != 0)
			if (board[rowMyTron][colMyTron] == '-') dist++;
			else break;
		return dist;
	}

	//Metoda ce masoara celule libere in directia: dreapta;	
	public static int getDistRight(int rowMyTron, int colMyTron, char[][] board)
	{
		int dist = 0;
		while (colMyTron++ != board[0].length)
			if (board[rowMyTron][colMyTron] == '-') dist++;
			else break;
		return dist;
	}
	
	//Metoda ce masoara celule libere in directia: sus;
	public static int getDistUp(int rowMyTron, int colMyTron, char[][] board)
	{
		int dist = 0;
		while (rowMyTron-- != 0)
			if (board[rowMyTron][colMyTron] == '-') dist++;
			else break;
		return dist;
	}
	
	//Metoda ce masoara celule libere in directia: jos;
	public static int getDistDown(int rowMyTron, int colMyTron, char[][] board)
	{
		int dist = 0;
		while (rowMyTron++ != 0)
			if (board[rowMyTron][colMyTron] == '-') dist++;
			else break;
		return dist;
	}
	
	//Metoda ce verifica daca din celula prezenta pot face o singura mutare;
	public static boolean checkSoloMove(int[] move)
	{
		return (move[0]+move[1]+move[2]+move[3] == 1) ? true : false;
	}
	
	//Metoda ce verifica daca o celula este ocupata/zid;
	public static boolean youShallNotPass(char cell)
	{
		return (cell == '#' || cell == 'r' || cell == 'g') ? true : false;
	}
	
	//Metoda ce intoarce urmatoarea mutare valida;
	public static String nextMove(char myTron, int rowMyTron, int colMyTron, int rowEnemy, int colEnemy, char[][] board)
	{
		//char enemy = (myTron == 'r') ? 'g' : 'r';
		int[] myMove = new int[4]; /* 0: daca nu pot muta pe pozitia respectiv;  1: altfel;
											move[0]: LEFT
    										move[1]: RIGHT
    										move[2]: UP
    										move[3]: DOWN */
		
		//verific mediul inconjurator, sa vad daca scap usor:
		if (!youShallNotPass(board[rowMyTron+1][colMyTron]))	myMove[3] = 1;
		if (!youShallNotPass(board[rowMyTron-1][colMyTron]))	myMove[2] = 1;
		if (!youShallNotPass(board[rowMyTron][colMyTron+1]))	myMove[1] = 1;
		if (!youShallNotPass(board[rowMyTron][colMyTron-1]))	myMove[0] = 1;

		if (checkSoloMove(myMove)) //am o singura miscare posibila, o fac;
			return ((myMove[0] == 1) ? "LEFT" : ((myMove[1] == 1) ? "RIGHT" : ((myMove[2] == 1) ? "UP" : ((myMove[3] == 1) ? "DOWN" : "ERR")))); 
	
		
		//calculez cate spatii sunt libere, in toate directiile:
		int[] dist = new int[4];
		
		dist[3] = getDistDown(rowMyTron, colMyTron, board);
		dist[2] = getDistUp(rowMyTron, colMyTron, board);
		dist[1] = getDistRight(rowMyTron, colMyTron, board);
		dist[0] = getDistLeft(rowMyTron, colMyTron, board);
		
		// implementing hug the wall, fill scenario:
		int[] walls = new int[4];
		
		if (myMove[3] == 1) walls[3] = (deadEnd(rowMyTron+1, colMyTron, board)) ? 0 : touchWall(rowMyTron+1, colMyTron, board);
		if (myMove[2] == 1) walls[2] = (deadEnd(rowMyTron-1, colMyTron, board)) ? 0 : touchWall(rowMyTron-1, colMyTron, board);
		if (myMove[1] == 1) walls[1] = (deadEnd(rowMyTron, colMyTron+1, board)) ? 0 : touchWall(rowMyTron, colMyTron+1, board);
		if (myMove[0] == 1) walls[0] = (deadEnd(rowMyTron, colMyTron-1, board)) ? 0 : touchWall(rowMyTron, colMyTron-1, board);
		
		
		int iMax = 0, max = Integer.MIN_VALUE, wallMax = Integer.MIN_VALUE;
		
		for (int i = 0; i < 4; i++)
		{
			if (walls[i] == max)
				if (dist[i] > wallMax)
					iMax = i;
			if (walls[i] > max)
			{
				max = walls[i];
				iMax = i;
			}
		}
		
		switch (iMax)
		{
			case 0: return "LEFT";
			case 1: return "RIGHT";
			case 2: return "UP";
			case 3: return "DOWN";
			default: return "ERR";
		}
		
	}
	
	
	public static void main(String[] args)
	{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		
		try
		{
			char myTron = br.readLine().charAt(0);
			String[] position = br.readLine().split(" ");
			int rowMyTron = Integer.parseInt(position[0]), colMyTron = Integer.parseInt(position[1]),
			    rowEnemy = Integer.parseInt(position[2]), colEnemy = Integer.parseInt(position[3]);
			String[] dim = br.readLine().split(" ");
			int N = Integer.parseInt(dim[0]), M = Integer.parseInt(dim[1]);
			
			char[][] board = new char[N][M];
			
			for (int i = 0; i < N; i++)
			{
				String line = br.readLine();
				for (int j = 0; j < M; j++)
					board[i][j] = line.charAt(j);
			}
			
			if (myTron == 'r')	//pe viitor, sa punem decizia in functie de jucator direct in nextMove;
				System.out.println(nextMove(myTron, rowMyTron, colMyTron, rowEnemy, colEnemy, board));
			else
				System.out.println(nextMove(myTron, rowEnemy, colEnemy, rowMyTron, colMyTron, board));
		} catch (IOException e) {
			// Trist moment.
			System.out.println("Err: "+e.getMessage());
		} /*catch (Exception e) {	//nu decomentati deocamdata deoarece face debugingul mai greu;
			// Si mai trist.
			System.out.println("Err: "+e.getMessage());
		}*/
	}
}
