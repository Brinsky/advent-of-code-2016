package advent;

import java.io.IOException;

public class Day03 {

	public static void main(String[] args) throws IOException {
		int[][] triangles = parse(FileUtility.fileToString("input/03.txt"));
		
		// Part one
		FileUtility.printAndOutput(countValidTriangles(triangles), "output/03A.txt");
		
		// Part two
		FileUtility.printAndOutput(
				countValidTriangles(reinterpret(triangles)), "output/03B.txt");
	}

	private static boolean validTriangle(int[] sides) {
		return (sides[0] + sides[1] > sides[2])
				&& (sides[0] + sides[2] > sides[1])
						&& (sides[1] + sides[2] > sides[0]);
	}
	
	private static int[][] parse(String input) {
		String[] lines = input.split("\n");
		int[][] triangles = new int[lines.length][3];
		
		for (int i = 0; i < lines.length; i++) {
			String[] fields = lines[i].trim().split("\\s+");
			
			for (int j = 0; j < 3; j++) {
				triangles[i][j] = Integer.parseInt(fields[j]);
			}
		}
		
		return triangles;
	}
	
	private static int[][] reinterpret(int[][] triangles) {
		int[][] newTriangles = new int[triangles.length][3];
		int perColumn = triangles.length / 3;
		
		for (int triangle = 0; triangle < triangles.length; triangle++) {
			for (int side = 0; side < 3; side++) {
				newTriangles[triangle / 3 + side * perColumn][triangle % 3] =
						triangles[triangle][side];
			}
		}
		
		return newTriangles;
	}
	
	private static int countValidTriangles(int[][] triangles) {
		int valid = 0;
		
		for (int[] triangle : triangles) {
			if (validTriangle(triangle)) {
				valid++;
			}
		}
		
		return valid;
	}
}
