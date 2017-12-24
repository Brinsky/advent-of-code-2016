package advent;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class Day01 {
	
	// {North, East, South, West}
	private static final int[] xHeadings = new int[] {0, 1, 0, -1};
	private static final int[] yHeadings = new int[] {1, 0, -1, 0};
	
	/** Modulus that works for negative numbers */
	private static int modulus(int a, int b) {
		return ((a % b) + b) % b;
	}
	
	public static void main(String[] args) throws IOException {
		// Each instruction is of the form { turnOffset, distanceOffset }
		int[][] instructions = parse(FileUtility.fileToString("input/01.txt"));

		// Part one
		FileUtility.printAndOutput(blocksTravelled(instructions), "output/01A.txt");

		// Part one
		FileUtility.printAndOutput(firstRevisited(instructions), "output/01B.txt");
	}

	private static int[][] parse(String input) {
		String[] lines = input.split(",\\s*");
		int[][] instructions = new int[lines.length][2];
		
		for (int i = 0; i < lines.length; i++) {
			instructions[i][0] = (lines[i].charAt(0) == 'L') ? -1 : 1;
			instructions[i][1] = Integer.parseInt(lines[i].substring(1));
		}
		
		return instructions;
	}
	
	private static int blocksTravelled(int[][] instructions) {
		int x = 0;
		int y = 0;
		int heading = 0; // Initial heading is North
		
		for (int[] instruction : instructions) {
			// Perform the "turn"
			heading = modulus(heading + instruction[0], 4);
			
			// Travel in the new direction
			x += instruction[1] * xHeadings[heading];
			y += instruction[1] * yHeadings[heading];
		}
		
		return blocksAway(x, y);
	}

	private static class Position {
		public final int x;
		public final int y;
		
		public Position(int x, int y) {
			this.x = x;
			this.y = y;
		}
		
		@Override
		public boolean equals(Object o) {
			if (!(o instanceof Position)) {
				return false;
			}
			
			Position other = (Position) o;
			return x == other.x && y == other.y;
		}
		
		@Override
		public int hashCode() {
			return Math.abs(x) + Math.abs(y) * 1000;
		}
	}
	
	private static int firstRevisited(int[][] instructions) {
		Set<Position> visited = new HashSet<Position>();
		int x = 0;
		int y = 0;
		int heading = 0; // Initial heading is North
		
		for (int[] instruction : instructions) {
			// Perform the "turn"
			heading = modulus(heading + instruction[0], 4);
			
			Position current = new Position(x, y);
			int blocks = instruction[1];

			// Travel in the new direction
			while (blocks > 0) {
				if (visited.contains(current)) {
					return blocksAway(x, y);
				} else {
					visited.add(current);
				}
				
				x += xHeadings[heading];
				y += yHeadings[heading];
				blocks--;
				current = new Position(x, y);
			}
		}
		
		return -1;
	}
	
	private static int blocksAway(int x, int y) {
		return Math.abs(x) + Math.abs(y);
	}
}
