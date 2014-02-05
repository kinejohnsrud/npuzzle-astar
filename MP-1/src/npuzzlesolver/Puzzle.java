package npuzzlesolver;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Puzzle {
	String[][] puzzleBoard;
	Node rootNode;
	BufferedWriter out;
	
	public static void main(String[] args) {
		Puzzle run = new Puzzle();
		try {
			run.rootNode = run.initialPuzzleBoard(args[0]);	//gets the initial puzzleBoard from file
			run.solvePuzzle("solution.txt");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	//reads the input puzzle board and puts it in a two-dimensional array
	public Node initialPuzzleBoard(String input) throws IOException{
		BufferedReader in = new BufferedReader(new FileReader(input));
		int j = 0;
		String line = in.readLine();
		String[] lineArray = line.split(" ");
		int size = lineArray.length;
		puzzleBoard = new String[size][size];
		while(line != null){
			lineArray = line.split(" ");
			for (int i = 0; i < size; i++) {
				puzzleBoard[j][i] = lineArray[i];
			}
			line = in.readLine();
			j++;
		}
		in.close();	
		Node rootNode = new Node(puzzleBoard);	//initial puzzleboard is the root node of the search tree
		rootNode.f = rootNode.g + rootNode.h;
		return rootNode;
		
	}
	
	// Solving the puzzle with the A-start algorithm
	public void solvePuzzle(String input) throws IOException{
		this.out = new BufferedWriter(new FileWriter(input));
		long start = System.nanoTime();
		ArrayList<Node> openList = new ArrayList<Node>();		//list of possible nodes
		ArrayList<Node> closedList = new ArrayList<Node>();		//list of checked nodes
		openList.add(rootNode);
		
		while (!openList.isEmpty()){
			int minF = Integer.MAX_VALUE;
			Node currentNode = null;
			
			//Program should timeout if runtime more than 30 minutes
			if((System.nanoTime() - start)/1000000000>=1800){
				this.out.write("Program stopped after 30 minute time limit exceeded");
				this.out.close();
				System.exit(0);
			}
			
			//Find node/board with lowest F value			
			for (Node node : openList){	
				if(node.f <= minF){
					minF = node.f;
					currentNode = node;
				}
			}
			
			//if goal state is found
			if(currentNode.h <= 0){	
				long runtimeInNano = System.nanoTime() - start;		//find time used
				double runtime = ((double)runtimeInNano/1000000000);
				StringBuilder sol = new StringBuilder();
				//backward traverses the node tree to get path
				while(currentNode.parent != null){
					sol.append(currentNode.direction);
					currentNode = currentNode.parent;
				}
				sol = sol.reverse();
				//prints the wanted solution (to file)
				this.out.write(sol.toString());
				this.out.newLine();
				this.out.write((int)runtime + " seconds");
				this.out.close();
				break;
			}
			
			//if goalstate is not found, traverse the children
			openList.remove(currentNode);
			closedList.add(currentNode);
			for(Node child : generateChildrenNodes(currentNode)){
				if(closedList.contains(child)){
					break;
				}
				//if child node is note in the openList, and it is not a deeper level in the tree (??)
				if(!openList.contains(child) || (currentNode.g+1) < child.g){
					child.parent = currentNode;
					child.g = currentNode.g + 1;
					child.f = child.g + child.h;
					if(!openList.contains(child)){
						openList.add(child);
					}
				}
			}
		}
	}
	
	public String[][] deepCopy(String[][] puzzleBoard){
		String[][] copy = new String[puzzleBoard.length][puzzleBoard.length];
		for (int i = 0; i < copy.length; i++) {
			System.arraycopy(puzzleBoard[i], 0, copy[i], 0, puzzleBoard.length);
		}
		return copy;
	}
	
	public Node left(Node node){
		if(node.posXX <= 0){
			return null;
		}
		else{
			String[][] board = deepCopy(node.puzzleBoard);
			board[node.posXY][node.posXX-1] = node.puzzleBoard[node.posXY][node.posXX];
			board[node.posXY][node.posXX] = node.puzzleBoard[node.posXY][node.posXX-1];
			Node leftNode = new Node(board);
			leftNode.direction = 'l';
			return leftNode;
		}
	}
	public Node right(Node node){
		if(node.posXX >= puzzleBoard.length-1){
			return null;
		}
		else{
			String[][] board = deepCopy(node.puzzleBoard);
			board[node.posXY][node.posXX+1] = node.puzzleBoard[node.posXY][node.posXX];
			board[node.posXY][node.posXX] = node.puzzleBoard[node.posXY][node.posXX+1];
			Node rightNode = new Node(board);
			rightNode.direction = 'r';
			return rightNode;
		}
	}
	public Node up(Node node){
		if(node.posXY <= 0){
			return null;
		}
		else{
			String[][] board = deepCopy(node.puzzleBoard);
			board[node.posXY-1][node.posXX] = node.puzzleBoard[node.posXY][node.posXX];
			board[node.posXY][node.posXX] = node.puzzleBoard[node.posXY-1][node.posXX];
			Node upNode = new Node(board);
			upNode.direction = 'u';
			return upNode;
		}
	}
	public Node down(Node node){
		if(node.posXY >= puzzleBoard.length-1){
			return null;
		}
		else{
			String[][] board = deepCopy(node.puzzleBoard);
			board[node.posXY+1][node.posXX] = node.puzzleBoard[node.posXY][node.posXX];
			board[node.posXY][node.posXX] = node.puzzleBoard[node.posXY+1][node.posXX];
			Node downNode = new Node(board);
			downNode.direction = 'd';
			return downNode;
		}
	}
	
	
	public ArrayList<Node> generateChildrenNodes(Node node){
		ArrayList<Node> childrenNodes = new ArrayList<Node>();
		Node rightNode = this.right(node);
		if(rightNode != null){
			childrenNodes.add(rightNode);
			rightNode.g += 1;
		}
		Node leftNode = this.left(node);
		if(leftNode != null){
			childrenNodes.add(leftNode);
			leftNode.g += 1;
		}
		Node downNode = this.down(node);
		if(downNode != null){
			childrenNodes.add(downNode);
			downNode.g += 1;
		}
		Node upNode = this.up(node);
		if(upNode != null){
			childrenNodes.add(upNode);
			upNode.g += 1;
		}
		return childrenNodes;
	}
	
//	private void printPuzzleBoard(Node node){
//		for (int i = 0; i < node.puzzleBoard.length; i++) {
//			System.out.println(Arrays.toString(node.puzzleBoard[i]));
//		}
//	}
}

