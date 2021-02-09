package io.github.vampirestudios.raa_materials.api.namegeneration;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public class MarkovChain {
	private static final char[] VOCALS = new char[] { 'a', 'e', 'i', 'o', 'u' };
	private static final List<Integer> POSITIONS = new ArrayList<Integer>();
	private List<MarkovElement> elements = new ArrayList<MarkovElement>();
	private Map<String, List<String>> ends = new HashMap<String, List<String>>();
	private Set<String> words = new HashSet<String>();

	public MarkovChain() {}

	public MarkovChain(InputStream text) {
		String line;
		try {
			InputStreamReader input = new InputStreamReader(text);
			BufferedReader reader = new BufferedReader(input);
			while ((line = reader.readLine()) != null) {
				addWord(line);
			}
			reader.close();
			input.close();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void addWord(String word) {
		word = word.toLowerCase().replace(" ", "");
		words.add(word);

		POSITIONS.clear();
		for (int i = 0; i < word.length(); i++) {
			if (isVocal(word.charAt(i))) {
				POSITIONS.add(i);
			}
		}

		int prev = 0;
		String previous = null;
		for (int i = 0; i < POSITIONS.size() - 1; i++) {
			int next = POSITIONS.get(i);
			int next2 = POSITIONS.get(i + 1);
			if (next2 - next > 2 || next2 - next == 1) {
				next += 1;
			}
			next++;
			String sub = word.substring(prev, next);
			if (!sub.isEmpty()) {
				createElement(previous, sub);
				previous = sub;
			}
			prev = next;
		}

		String last = word.substring(prev);
		createElement(previous, last);
		if (prev > 0) {
			addEnd(previous, last);
		}
	}

	private void addEnd(String prev, String end) {
		List<String> list = ends.get(prev);
		if (list == null) {
			list = new ArrayList<String>();
			list.add(end);
			ends.put(prev, list);
			return;
		}
		if (!list.contains(end))
			list.add(end);
	}

	private boolean isVocal(char c) {
		for (char c2 : VOCALS) {
			if (c == c2)
				return true;
		}
		return false;
	}

	public Set<String> makeSet(int count, int lengthMin, int lengthMax, Random random) {
		Set<String> result = new HashSet<String>();
		int count2 = 0;
		for (int i = 0; i < count * 10 && count2 < count; i++) {
			String word = makeWord(lengthMin, lengthMax, random);
			if (!result.contains(word)) {
				result.add(word);
				count2++;
			}
		}
		return result;
	}

	public String makeWord(int lengthMin, int lengthMax, Random random) {
		for (int i = 0; i < 1000; i++) {
			String word = makeWord(lengthMax, random);
			if (word.length() >= lengthMin && word.length() <= lengthMax && !words.contains(word)) {
				word = word.substring(0, 1).toUpperCase() + word.substring(1);
				return word;
			}
		}
		return "";
	}

	public String makeWord(int length, Random random) {
		if (elements.isEmpty()) return "";
		MarkovElement elem = elements.get(random.nextInt(elements.size()));
		StringBuilder result = new StringBuilder(elem.str);
		String last = "";
		for (int i = 0; i < length; i++) {
			elem = elem.getNext(random);
			if (elem == null) {
				return result.toString();
			}
			last = elem.str;
			result.append(elem.str);
		}
		List<String> list = ends.get(last);
		return result + (list == null ? "" : list.get(random.nextInt(list.size())));
	}

	private void createElement(String previous, String current) {
		MarkovElement next = new MarkovElement(current);
		int index = elements.indexOf(next);
		if (index != -1) {
			next = elements.get(index);
		}
		else {
			elements.add(next);
		}

		if (previous != null) {
			MarkovElement prev = new MarkovElement(previous);
			index = elements.indexOf(prev);
			if (index != -1) {
				prev = elements.get(index);
			}
			else {
				elements.add(prev);
			}
			prev.addElement(next);
		}
	}

	@Override
	public String toString() {
		StringBuilder result = new StringBuilder("chain: [");
		int count = elements.size();
		int n = 0;
		for (MarkovElement elem : elements) {
			n++;
			result.append("\n  \"").append(elem.str).append(" tp:").append(elem.totalProb).append("\": {");
			int count2 = elem.next.size();
			int n2 = 0;
			for (MarkovElement e : elem.next) {
				n2++;
				result.append(n2 == 1 ? "\"" : " \"").append(e.str).append(" p:").append(e.prob.get(elem)).append("\"");
				if (n2 < count2) {
					result.append(",");
				}
			}
			result.append("}");
			if (n < count) {
				result.append(",");
			}
		}
		result.append(elements.isEmpty() ? "]" : "\n]");
		return result.toString();
	}

	private static class MarkovElement {
		List<MarkovElement> next = new ArrayList<>();
		Map<MarkovElement, Integer> prob = new HashMap<>();
		int totalProb;
		String str;

		public MarkovElement(String part) {
			this.str = part;
		}

		MarkovElement getNext(Random random) {
			if (next.isEmpty()) return null;
			int rnd = random.nextInt(totalProb);
			for (MarkovElement n : next) {
				int p = n.prob.get(this);
				if (p <= rnd)
					return n;
			}
			return next.get(0);
		}

		void addElement(MarkovElement elem) {
			int index = next.indexOf(elem);
			if (index == -1) {
				elem.prob.put(this, 1);
				next.add(elem);
				totalProb = 1;
			}
			else {
				elem = next.get(index);
				elem.prob.put(this, elem.prob.get(this) + 1);
				totalProb = 0;
				for (MarkovElement e : next) {
					totalProb += e.prob.get(this);
				}
			}
		}

		@Override
		public int hashCode() {
			byte[] bytes = str.getBytes();
			int hash = 0;
			int offset = 0;
			for (byte b : bytes) {
				hash |= (int) b << offset;
				offset += 8;
			}
			return hash;
		}

		@Override
		public boolean equals(Object obj) {
			MarkovElement element = (MarkovElement) obj;
			return element != null && (element == this || element.str.equals(this.str));
		}
	}
}