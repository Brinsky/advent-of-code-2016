package advent;

import java.io.IOException;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.Arrays;
import java.util.Set;
import java.util.HashSet;

public class Day07 {

	private static final Pattern IP_PATTERN = Pattern.compile("([a-z]+)|\\[([a-z]+)\\]");

	public static void main(String[] args) throws IOException {
		String[] addresses = FileUtility.fileToString("input/07.txt").split("\n");

		// Part one
		FileUtility.printAndOutput(
				Arrays.stream(addresses).filter(i -> supportsTls(i)).count(),
				"output/07A.txt");

		// Part two
		FileUtility.printAndOutput(
				Arrays.stream(addresses).filter(i -> supportsSsl(i)).count(),
				"output/07A.txt");
	}

	private static boolean supportsSsl(String address) {
		Set<CharPair> supernetPairs = new HashSet<CharPair>();	
		Set<CharPair> hypernetPairs = new HashSet<CharPair>();	

		Matcher matcher = IP_PATTERN.matcher(address);

		while (matcher.find()) {
			// Outside brackets
			if (matcher.group(1) != null) {
				addAbaPairs(matcher.group(), supernetPairs);
			// Inside brackets
			} else { // if (matcher.group(2) != null)
				addBabPairs(matcher.group(), hypernetPairs);
			}
		}

		// Look for overlap between ABA pairs and BAB pairs
		for (CharPair pair : supernetPairs) {
			if (hypernetPairs.contains(pair)) {
				return true;
			}
		}

		return false;
	}

	private static void addAbaPairs(String sequence, Set<CharPair> pairs) {
		for (int i = 0; i <= sequence.length() - 3; i++) {
			if (sequence.charAt(i) != sequence.charAt(i + 1)
					&& sequence.charAt(i) == sequence.charAt(i + 2)) {
				pairs.add(new CharPair(sequence.charAt(i), sequence.charAt(i + 1)));
			}
		}
	}

	private static void addBabPairs(String sequence, Set<CharPair> pairs) {
		for (int i = 0; i <= sequence.length() - 3; i++) {
			if (sequence.charAt(i) != sequence.charAt(i + 1)
					&& sequence.charAt(i) == sequence.charAt(i + 2)) {
				pairs.add(new CharPair(sequence.charAt(i + 1), sequence.charAt(i)));
			}
		}
	}

	private static class CharPair {
		public final char a;
		public final char b;

		public CharPair(char a, char b) {
			this.a = a;
			this.b = b;
		}

		@Override
		public boolean equals(Object o) {
			if (!(o instanceof CharPair)) {
				return false;
			}

			CharPair p = (CharPair) o;
			return a == p.a && b == p.b;
		}

		@Override
		public int hashCode() {
			return (a + 13) * 7 + b;
		}
	}

	private static boolean supportsTls(String address) {
		Matcher matcher = IP_PATTERN.matcher(address);

		boolean supportsTls = false;
		while (matcher.find()) {
			if (hasAbba(matcher.group())) {
				// Outside brackets
				if (matcher.group(1) != null) {
					supportsTls = true;
				// Inside brackets
				} else { // if (matcher.group(2) != null)
					return false;
				}
			}
		}

		return supportsTls;
	}

	private static boolean hasAbba(String sequence) {
		if (sequence.length() < 3) {
			return false;
		}

		for (int i = 0; i <= sequence.length() - 4; i++) {
			if (sequence.charAt(i) != sequence.charAt(i + 1)
					&& sequence.charAt(i) == sequence.charAt(i + 3)
					&& sequence.charAt(i + 1) == sequence.charAt(i + 2)) {
				return true;
			}
		}

		return false;
	}
}
