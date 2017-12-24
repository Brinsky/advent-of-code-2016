package advent;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day04 {

	private static final Pattern NUMBER = Pattern.compile("\\d+");
	private static final String TARGET_NAME = "northpole object storage";
	
	public static void main(String[] args) throws IOException {
		Room[] rooms = parse(FileUtility.fileToString("input/04.txt"));
		
		// Part one
		FileUtility.printAndOutput(realRoomsIdSum(rooms), "output/04A.txt");
		
		// Part two
		FileUtility.printAndOutput(
				findTargetRoomId(rooms, TARGET_NAME), "output/04B.txt");
	}

	private static class Room {
		public final String name;
		public final int id;
		public final String checksum;
		
		public Room(String name, int id, String checksum) {
			this.name = name;
			this.id = id;
			this.checksum = checksum;
		}
	}
	
	private static Room[] parse(String input) {
		String[] lines = input.split("\n+");
		Room[] rooms = new Room[lines.length];
		
		for (int i = 0; i < lines.length; i++) {
			Matcher matcher = NUMBER.matcher(lines[i]);
			matcher.find();
			
			String name = lines[i].substring(0, matcher.start() - 1);
			int id = Integer.parseInt(matcher.group());
			String checksum = lines[i].substring(matcher.end() + 1, lines[i].length() - 1);
			
			rooms[i] = new Room(name, id, checksum);
		}
		
		return rooms;
	}
	
	private static class CharInfo implements Comparable<CharInfo> {
		public final char c;
		public final int count;
		
		public CharInfo(char c, int count) {
			this.c = c;
			this.count = count;
		}

		@Override
		public int compareTo(CharInfo other) {
			if (count != other.count) {
				return -Integer.compare(count, other.count);
			}
			
			return Character.compare(c, other.c);
		}
	}
	
	private static String checksum(String name) {
		char[] nameChars = name.replaceAll("-", "").toCharArray();
		Arrays.sort(nameChars);
		
		List<CharInfo> charCounts = new ArrayList<CharInfo>();
		char current = nameChars[0];
		int count = 1;
		for (int i = 1; i < nameChars.length; i++) {
			if (nameChars[i] == current) {
				count++;
			} else {
				charCounts.add(new CharInfo(current, count));
				current = nameChars[i];
				count = 1;
			}
		}
		charCounts.add(new CharInfo(current, count));
		Collections.sort(charCounts);
		
		StringBuilder checksum = new StringBuilder();
		for (int i = 0; i < 5; i++) {
			checksum.append(charCounts.get(i).c);
		}
		
		return checksum.toString();
	}
	
	private static boolean isReal(Room room) {
		return room.checksum.equals(checksum(room.name));
	}
	
	private static int realRoomsIdSum(Room[] rooms) {
		int sum = 0;
		
		for (Room room : rooms) {
			if (isReal(room)) {
				sum += room.id;
			}
		}
		
		return sum;
	}
	
	private static int charToValue(char c) {
		return (int) (c - 'a');
	}
	
	private static char valueToChar(int value) {
		return (char) (value + 'a');
	}
	
	private static char decrypt(char c, int shift) {
		if (c == '-') {
			return ' ';
		}
		
		return valueToChar((charToValue(c) + shift) % 26);
	}
	
	private static String decrypt(String s, int shift) {
		char[] chars = new char[s.length()];
		
		for (int i = 0; i < s.length(); i++) {
			chars[i] = decrypt(s.charAt(i), shift);
		}
		
		return new String(chars);
	}
	
	private static int findTargetRoomId(Room[] rooms, String targetName) {
		for (Room room : rooms) {
			if (isReal(room) && decrypt(room.name, room.id).equals(targetName)) {
				return room.id;
			}
		}
		
		return -1;
	}
}
