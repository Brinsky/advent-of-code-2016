package advent;

import java.io.IOException;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class Day09 {

	private static final Pattern MARKER = Pattern.compile("\\((\\d+)x(\\d+)\\)");

	public static void main(String[] args) throws IOException {
		String compressedFile = FileUtility.fileToString("input/09.txt").replaceAll("\\s+", "");
		String uncompressedFile = decompressFile(compressedFile);

		// Part one
		FileUtility.printAndOutput(uncompressedFile.length(), "output/09A.txt");

		// Part two
		FileUtility.printAndOutput(recursivelyDecompress(compressedFile), "output/09B.txt");
	}

	private static String decompressFile(String compressedFile) {
		int current = 0;
		Matcher matcher = MARKER.matcher(compressedFile);
		StringBuilder uncompressed = new StringBuilder();

		while (matcher.find()) {
			if (current < matcher.start()) {
				uncompressed.append(compressedFile.substring(current, matcher.start()));
			} else if (current > matcher.start()) {
				// Don't process markers that were already included in other markers
				continue;	
			}

			int length = Integer.parseInt(matcher.group(1));
			int repeats = Integer.parseInt(matcher.group(2));

			String marked = compressedFile.substring(matcher.end(), matcher.end() + length);
			for (int i = 0; i < repeats; i++) {
				uncompressed.append(marked);
			}

			current = matcher.end() + length;
		}

		return uncompressed.toString();
	}

	private static long recursivelyDecompress(String compressedFile) {
		return recursivelyDecompress(compressedFile, 0, compressedFile.length());
	}

	private static long recursivelyDecompress(String compressedFile, int start, int end) {
		Matcher matcher = MARKER.matcher(compressedFile);
		matcher.region(start, end);
		
		int currentPosition = start;
		long subsequenceLength = 0;
		while (matcher.find()) {
			// Count any regular text between marked regions
			subsequenceLength += matcher.start() - currentPosition;

			int length = Integer.parseInt(matcher.group(1));
			int repeats = Integer.parseInt(matcher.group(2));

			subsequenceLength += repeats * recursivelyDecompress(compressedFile, matcher.end(), matcher.end() + length);

			currentPosition = matcher.end() + length;
			matcher.region(currentPosition, end);
		}

		// Count any remaining regular text in the length
		return subsequenceLength + (end - currentPosition);
	}
}
