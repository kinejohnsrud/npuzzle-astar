package npuzzlesolver;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class Puzzle {
	String[][] puzzleBoard;
	Node rootNode;
	
	public static void main(String[] args) {
		Puzzle run = new Puzzle();
		try {
			run.initialPuzzleBoard();		//gets the puzzleBoard from file
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	public Node initialPuzzleBoard() throws IOException{
		BufferedReader in = new BufferedReader(new FileReader("/Users/Kine/Documents/workspace/MP-1/src/npuzzlesolver/testPuzzle.txt"));
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
		return rootNode;
		
	}
	
	public void solvePuzzle(){

		
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
			board[node.posXY+1][node.posXX+1] = node.puzzleBoard[node.posXY][node.posXX];
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
	
	
	
	private void printPuzzleBoard(){
		for (int i = 0; i < puzzleBoard.length; i++) {
			System.out.println(Arrays.toString(puzzleBoard[i]));
		}
	}
}

