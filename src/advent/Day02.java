package advent;

import java.io.IOException;

public class Day02 {
	
	private static final char[] DIGITS =
			new char[] {'1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D'};
	
	public static void main(String[] args) throws IOException {
		String[] lines = FileUtility.fileToString("input/02.txt").split("\n");
		
		// Part one
		FileUtility.printAndOutput(getCode(lines, squareKeypad(3), 1, 1), "output/02A.txt");
		
		// Part two
		FileUtility.printAndOutput(getCode(lines, diamondKeypad(5), 2, 0), "output/02B.txt");
	}

	private static int getColOffset(char direction) {
		switch (direction) {
		case 'L': return -1;
		case 'R': return 1;
		default: return 0;
		}
	}
	
	private static int getRowOffset(char direction) {
		switch (direction) {
		case 'U': return -1;
		case 'D': return 1;
		default: return 0;
		}
	}
	
	private static char[][] squareKeypad(int size) {
		char[][] keypad = new char[size][size];

		for (int row = 0; row < size; row++) {
			for (int col = 0; col < size; col++) {
				keypad[row][col] = DIGITS[row * size + col];
			}
		}
		
		return keypad;
	}
	
	private static char[][] diamondKeypad(int size) {
		char[][] keypad = new char[size][size];
		int center = size / 2;
		
		int index = 0;
		for (int row = 0; row < size; row++) {
			for (int col = 0; col < size; col++) {
				if (Math.abs(center - row) + Math.abs(center - col) <= size / 2) {
					keypad[row][col] = DIGITS[index++];
				} else {
					keypad[row][col] = ' ';
				}
			}
		}
		
		return keypad;
	}
	
	private static boolean inBounds(int row, int col, char[][] keypad) {
		return row >= 0 && row < keypad.length
				&& col >= 0 && col < keypad[row].length
				&& keypad[row][col] != ' ';
	}
	
	private static String getCode(String[] lines, char[][] keypad,
			int startRow, int startCol) {
		StringBuilder builder = new StringBuilder();
		int row = startRow;
		int col = startCol;
		
		
		for (String line : lines) {
			for (int i = 0; i < line.length(); i++) {
				char current = line.charAt(i);
				
				int rowOffset = getRowOffset(current);
				int colOffset = getColOffset(current);
				
				if (inBounds(row + rowOffset, col + colOffset, keypad)) {
					row += rowOffset;
					col += colOffset;
				}
			}
			
			builder.append(keypad[row][col]);
		}
		
		return builder.toString();
	}
}
