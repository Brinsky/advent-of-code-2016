package advent;

import java.io.IOException;

public class Day08 {

	private static final int HEIGHT = 6;
	private static final int WIDTH = 50;

	public static void main(String[] args) throws IOException {
		String[] lines = FileUtility.fileToString("input/08.txt").split("\n");
		boolean[][] screen = draw(lines);

		// Part one
		FileUtility.printAndOutput(countOnPixels(screen), "output/08A.txt");

		// Part two
		FileUtility.printAndOutput(screenToString(screen), "output/08B.txt");
	}

	private static String screenToString(boolean[][] screen) {
		StringBuilder screenBuilder = new StringBuilder(WIDTH * HEIGHT);

		for (int row = 0; row < HEIGHT; row++) {
			for (int col = 0; col < WIDTH; col++) {
				screenBuilder.append(screen[row][col] ? '#' : '.');
			}
			screenBuilder.append("\n");
		}

		return screenBuilder.toString();
	}

	private static int countOnPixels(boolean[][] screen) {
		int count = 0;
		for (int row = 0; row < HEIGHT; row++) {
			for (int col = 0; col < WIDTH; col++) {
				if (screen[row][col]) {
					count++;
				}
			}
		}

		return count;
	}

	private static boolean[][] draw(String[] instructions) {
		boolean[][] screen = new boolean[HEIGHT][WIDTH];

		for (int i = 0; i < instructions.length; i++) {
			String[] tokens = instructions[i].split("\\s+");
			int[] values = extractInts(instructions[i]);

			if (tokens[0].equals("rect")) {
				drawRect(screen, values[0], values[1]);
			} else if (tokens[0].equals("rotate")) {
				if (tokens[1].equals("row")) {
					rotateRow(screen, values[0], values[1]);
				} else { // if (tokens[1].equals("column"))
					rotateColumn(screen, values[0], values[1]);
				}
			}
		}

		return screen;
	}

	private static void drawRect(boolean[][] screen, int width, int height) {
		for (int row = 0; row < height; row++) {
			for (int col = 0; col < width; col++) {
				screen[row][col] = true;
			}
		}
	}

	private static void rotateRow(boolean[][] screen, int row, int steps) {
		for (int i = 0; i < steps; i++) {
			boolean last = screen[row][WIDTH - 1];

			for (int col = WIDTH - 1; col > 0; col--) {
				screen[row][col] = screen[row][col - 1];
			}

			screen[row][0] = last;
		}
	}
	
	private static void rotateColumn(boolean[][] screen, int col, int steps) {
		for (int i = 0; i < steps; i++) {
			boolean last = screen[HEIGHT - 1][col];

			for (int row = HEIGHT - 1; row > 0; row--) {
				screen[row][col] = screen[row - 1][col];
			}

			screen[0][col] = last;
		}
	}

	public static int[] extractInts(String instruction) {
		String[] valuesText = instruction.split("\\D+");
		int[] values = new int[valuesText.length - 1];

		for (int i = 0; i < values.length; i++) {
			values[i] = Integer.parseInt(valuesText[i + 1]);
		}

		return values;
	}
}
