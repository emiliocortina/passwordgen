package Backtracking;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class PasswordGenerator {

	private Character[] letters = { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q',
			'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z' };
	private List<Character> lettersList = Arrays.asList(letters);

	private Character[] vowels = { 'a', 'e', 'i', 'o', 'u' };
	private List<Character> vowelsList = Arrays.asList(vowels);

	private Character[] nonLetters = { '.', ',', '-', '_', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' };

	private List<Character> nonLetterlist = Arrays.asList(nonLetters);

	private int numberOfTotalCharacters;
	private int numberOfNonLettersEnds;
	private int numberOfPasswords;
	private String consonantPairPath;
	private ArrayList<String> allowedPairs;
	List<String> passwords;

	public PasswordGenerator(int numberOfTotalCharacters, int numberOfNonLettersEnds, int numberOfPasswords,
			String consonantPairPath) {
		this.numberOfTotalCharacters = numberOfTotalCharacters;
		this.numberOfNonLettersEnds = numberOfNonLettersEnds;
		this.numberOfPasswords = numberOfPasswords;
		this.consonantPairPath = consonantPairPath;
		this.passwords = new ArrayList<String>();
		try {
			parsePairs(consonantPairPath);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void parsePairs(String path) throws IOException {
		FileReader r = new FileReader(path);
		BufferedReader br = new BufferedReader(r);
		ArrayList<String> al = new ArrayList();
		while (br.ready()) {
			al.add(br.readLine());
		}
		this.allowedPairs = al;
		br.close();
	}

	public List<String> getPasswords() {
		return passwords;
	}

	// 1. Two vowels can appear one after the other only if they are different
	// 2. There cannot be three vowels or three consonants consecutively in a
	// password.
	// 3. There is a set of consonant pairs that may appear in a password.
	// 4. The last characters should be punctuation marks or numbers (non-alphabetic
	// characters).
	public void generate() {
		for (int i = 0; i < numberOfPasswords; i++)
			this.passwords.add(generateOnePassword());
	}

	private String generateOnePassword() {
		StringBuilder pass = new StringBuilder();

		int numberOfLetters = numberOfTotalCharacters - numberOfNonLettersEnds;

		int currentChars = 0;
		while (currentChars < numberOfTotalCharacters) {
			if (currentChars < numberOfLetters) {
				pass.append(lettersList.get(ThreadLocalRandom.current().nextInt(lettersList.size())));
				currentChars++;
				while (!isSolution(pass.toString())) {
					pass.deleteCharAt(pass.length() - 1);
					currentChars--;
				}
			} else {
				pass.append(nonLetterlist.get(ThreadLocalRandom.current().nextInt(nonLetterlist.size())));
				currentChars++;
			}
		}
		return pass.toString();
	}

	private boolean isSolution(String str) {
		int consecutiveVowels = 0;
		int consecutiveConsonants = 0;
		// Process
		for (int i = str.length() - 1; i > str.length() - 4; i--) {
			if (i - 1 > -1) {
				if (isAVowel(str.charAt(i))) {
					if (str.charAt(i) == (str.charAt(i - 1)))
						return false;
					consecutiveVowels++;
				} else {
					if (!isAVowel(str.charAt(i-1)) && 
							!isPairAllowed(str.charAt(i-1), str.charAt(i)))
						return false;
					consecutiveConsonants++;
				}
			}
		}
		// Evaluate
		if (consecutiveVowels >= 3 || consecutiveConsonants >= 3) {
			return false;
		}
		return true;
	}

	private boolean isPairAllowed(char consonant1, char consonant2) {
		StringBuilder pair = new StringBuilder();
		pair.append(consonant1);
		pair.append(consonant2);
		return allowedPairs.contains(pair);
	}

	private boolean isAVowel(Character ch) {
		return vowelsList.contains(ch);
	}

}
