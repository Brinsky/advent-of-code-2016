package advent;

import java.io.IOException;
import java.lang.Integer;
import java.lang.IllegalArgumentException;

public class Day05 {

	public static void main(String[] args) throws IOException {
		String doorId = FileUtility.fileToString("input/05.txt");

		// Part one
		// Somewhat slow, probably due to my implementation of MD5
		FileUtility.printAndOutput(buildConsecutivePassword(doorId), "output/05A.txt");

		// Part two
		FileUtility.printAndOutput(buildJumbledPassword(doorId), "output/05B.txt");
	}

	private static String buildJumbledPassword(String doorId) {
		boolean[] written = new boolean[8];
		int writtenCount = 0;
		char[] password = new char[8];
		long i = 0;

		while (writtenCount < 8) {
			String hash = md5(doorId + i);

			if (hash.startsWith("00000")) {
				int pwIndex = getIndex(hash.charAt(5));
				if (pwIndex >= 0 && !written[pwIndex]) {
					password[pwIndex] = hash.charAt(6);
					written[pwIndex] = true;
					writtenCount++;
				}
			}

			i++;
		}

		return new String(password);
	}

	/** Returns numerical representation for 0-7; returns -1 otherwise. */
	private static int getIndex(char hex) {
		if (hex >= '0' && hex <= '7') {
			return hex - '0';
		} else {
			return -1;
		}
	}


	private static String buildConsecutivePassword(String doorId) {
		StringBuilder password = new StringBuilder();
		long i = 0;

		while (password.length() < 8) {
			String hash = md5(doorId + i);

			if (hash.startsWith("00000")) {
				password.append(hash.charAt(5));
			}

			i++;
		}

		return password.toString();
	}

	private static String md5(String message) {
		int[] s = new int[] {
			7, 12, 17, 22, 7, 12, 17, 22, 7, 12, 17, 22, 7, 12, 17, 22,
			5,  9, 14, 20, 5,  9, 14, 20, 5,  9, 14, 20, 5,  9, 14, 20,
			4, 11, 16, 23, 4, 11, 16, 23, 4, 11, 16, 23, 4, 11, 16, 23,
			6, 10, 15, 21, 6, 10, 15, 21, 6, 10, 15, 21, 6, 10, 15, 21
		};

		int[] k = new int[] {
			0xd76aa478, 0xe8c7b756, 0x242070db, 0xc1bdceee,
			0xf57c0faf, 0x4787c62a, 0xa8304613, 0xfd469501,
			0x698098d8, 0x8b44f7af, 0xffff5bb1, 0x895cd7be,
			0x6b901122, 0xfd987193, 0xa679438e, 0x49b40821,
			0xf61e2562, 0xc040b340, 0x265e5a51, 0xe9b6c7aa,
			0xd62f105d, 0x02441453, 0xd8a1e681, 0xe7d3fbc8,
			0x21e1cde6, 0xc33707d6, 0xf4d50d87, 0x455a14ed,
			0xa9e3e905, 0xfcefa3f8, 0x676f02d9, 0x8d2a4c8a,
			0xfffa3942, 0x8771f681, 0x6d9d6122, 0xfde5380c,
			0xa4beea44, 0x4bdecfa9, 0xf6bb4b60, 0xbebfbc70,
			0x289b7ec6, 0xeaa127fa, 0xd4ef3085, 0x04881d05,
			0xd9d4d039, 0xe6db99e5, 0x1fa27cf8, 0xc4ac5665,
			0xf4292244, 0x432aff97, 0xab9423a7, 0xfc93a039,
			0x655b59c3, 0x8f0ccc92, 0xffeff47d, 0x85845dd1,
			0x6fa87e4f, 0xfe2ce6e0, 0xa3014314, 0x4e0811a1,
			0xf7537e82, 0xbd3af235, 0x2ad7d2bb, 0xeb86d391
		};

		int a0 = 0x67452301;
		int b0 = 0xefcdab89;
		int c0 = 0x98badcfe;
		int d0 = 0x10325476;

		int[] chunks = md5Chunks(message);

		for (int start = 0; start < chunks.length; start += 16) {
			int a = a0;
			int b = b0;
			int c = c0;
			int d = d0;

			for (int i = 0; i < 64; i++) {
				int f, g;

				if (i < 16) {
					f = (b & c) | (~b & d);
					g = i;
				} else if (i >= 16 && i < 32) {
					f = (d & b) | (~d & c);
					g = (i * 5 + 1) % 16;
				} else if (i >= 32 && i < 48) {
					f = b ^ c ^ d;
					g = (i * 3 + 5) % 16;
				} else { // if (i >= 48)
					f = c ^ (b | ~d);
					g = (i * 7) % 16;
				}

				f += a + k[i] + toLittleEndian(chunks[start + g]);
				a = d;
				d = c;
				c = b;
				b += Integer.rotateLeft(f, s[i]);
			}

			a0 += a;
			b0 += b;
			c0 += c;
			d0 += d;
		}

		return String.format("%08x%08x%08x%08x", toLittleEndian(a0), toLittleEndian(b0), toLittleEndian(c0), toLittleEndian(d0));
	}

	private static int[] md5Chunks(String message) {
		StringBuilder builder = new StringBuilder(message);

		// Add a single 1 bit, then pad with 0's to 512 - 64 (modulo 512)
		builder.append((char) 0x80);
		while (builder.length() % 64 != 56) {
			builder.append((char) 0x00);
		}

		char[] chars = builder.toString().toCharArray();
		int[] chunks = new int[(chars.length / 4) + 2];

		for (int i = 0; i < chars.length / 4; i++) {
			chunks[i] = intFromChars(chars, i * 4);
		}

		// Store the length of the original string in the last 64 bits
		// For some reason, this entire 64-bit chunk must be in little endian
		// We only expect messages with length <= 2^31
		chunks[chunks.length - 2] = toLittleEndian(message.length() * 8);
		chunks[chunks.length - 1] = 0;

		return chunks;
	}

	private static int intFromChars(char[] chars, int start) {
		int value = 0;

		value |= (chars[start] & 0xFF) << 24;
		value |= (chars[start + 1] & 0xFF) << 16;
		value |= (chars[start + 2] & 0xFF) << 8;
		value |= chars[start + 3] & 0xFF;

		return value;
	}

	private static int toLittleEndian(int value) {
		int littleEndian = 0;
		littleEndian |= (value << 24) & 0xFF000000;
		littleEndian |= (value << 8) & 0x00FF0000;
		littleEndian |= (value >>> 8) & 0x0000FF00;
		littleEndian |= (value >>> 24) & 0x000000FF;

		return littleEndian;
	}
}
