package advent;

import java.io.IOException;

public class Day06 {
	
	public static void main(String[] args) throws IOException {
		char[][] grid = parse(FileUtility.fileToString("input/06.txt"));

		// Part one
		FileUtility.printAndOutput(highestRankedChars(grid, (i, j) -> i > j, 0), "output/06A.txt");

		// Part two
		FileUtility.printAndOutput(highestRankedChars(grid, (i, j) -> i < j, Integer.MAX_VALUE), "output/06B.txt");
	}

	/** Finds the highest-ranked character based on frequency and a ranking function. */
	private static String highestRankedChars(char[][] grid, RankFunction rank, int defaultFrequency) {
		StringBuilder errorCorrected = new StringBuilder();

		for (int col = 0; col < grid[0].length; col++) {
			int[] charCounts = new int[26];

			// Compute frequency of each char in this column
			for (int row = 0; row < grid.length; row++) {
				charCounts[grid[row][col] - 'a']++;
			}

			// Find the "highest-ranked" character in this column based on frequency
			char highestRanked = 'a';
			int bestFrequency = defaultFrequency;
			for (int row = 0; row < grid.length; row++) {
				if (rank.isRankedHigher(charCounts[grid[row][col] - 'a'], bestFrequency)) {
					highestRanked = grid[row][col];
					bestFrequency = charCounts[grid[row][col] - 'a'];
				}
			}

			errorCorrected.append(highestRanked);
		}

		return errorCorrected.toString();
	}

	@FunctionalInterface
	private interface RankFunction {
		boolean isRankedHigher(int currentFreq, int otherFreq);
	}

	private static char[][] parse(String input) {
		String[] lines = input.split("\n");
		char[][] grid = new char[lines.length][];

		for (int i = 0; i < lines.length; i++) {
			grid[i] = lines[i].toCharArray();
		}

		return grid;
	}
}
