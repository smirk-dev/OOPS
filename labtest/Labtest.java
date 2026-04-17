/*6.2. A company wants to analyze customer feedback text automatically. 
Write a Java program to: 
1. Accept a paragraph from user. 
2. Perform the following operations using Java String methods: 
o Count total words and sentences 
o Count total characters (excluding spaces)
o Identify most frequently used word 
o Replace all negative words from a given list with "***" 
o Check if feedback contains promotional keywords 
o Display reversed sentence order 
 
Constraints 
• Must use Java built-in String functions 
• Case insensitive processing required 
 
Input Example 
Enter feedback: 
The product quality is bad but delivery was fast. 
 
Expected Output 
Total Words: 8 
Most Frequent Word: product 
Filtered Feedback: The product quality is *** but delivery was fast. 
Contains Promotion Keyword: No 
Reversed Sentence: fast was delivery but *** is quality product The 
*/
package labtest;
import java.util.*;
import java.util.regex.*;
public class Labtest {
	public static void main(String[] args) {
		try (Scanner sc = new Scanner(System.in)) {
			System.out.println("Enter feedback:");
			String feedback = sc.nextLine().trim();
			if (feedback.isEmpty()) {
				System.out.println("No feedback provided.");
				return;
			}
		String[] sentenceParts = feedback.split("[.!?]+\\s*");
		int sentenceCount = 0;
		for (String s : sentenceParts) if (!s.trim().isEmpty()) sentenceCount++;
		Pattern wordPattern = Pattern.compile("\\b[\\w']+\\b");
		Matcher wm = wordPattern.matcher(feedback);
		List<String> wordsInOrder = new ArrayList<>();
		while (wm.find()) {
			wordsInOrder.add(wm.group());
		}
		int wordCount = wordsInOrder.size();
		Set<String> stopwords = new HashSet<>(Arrays.asList(
			"the","is","a","an","and","but","or","was","were","in","on","at","by","for","to","of","with","this","that","these","those","it","its","as","are","be","have","has","had", "not","from","they","their","them","he","she","his","her","you","your","yours","we","our","ours","i","me","my","mine","do","does","did","so","if","then","there","when","where","who","whom"
		));
		Map<String, Integer> freq = new LinkedHashMap<>();
		for (String w : wordsInOrder) {
			String lw = w.toLowerCase();
			freq.put(lw, freq.getOrDefault(lw, 0) + 1);
		}
		String mostFreq = null;
		int best = 0;
		for (String w : freq.keySet()) {
			if (stopwords.contains(w)) continue;
			int f = freq.get(w);
			if (f > best) { best = f; mostFreq = w; }
		}
		if (mostFreq == null && !wordsInOrder.isEmpty()) {
			mostFreq = wordsInOrder.get(0).toLowerCase();
		}
		String[] negatives = {"bad","poor","terrible","awful","disappointing","unsatisfactory","horrible", "worst","negative","dissatisfied","unhappy","subpar","lacking","deficient","flawed","inadequate", "unacceptable","regret","complain","complaint","issue","problem","frustrated","angry","upset","annoyed","displeased", "unfortunate","miserable","depressing","dreadful","abysmal","atrocious","appalling","ghastly","heinous","horrendous","nasty","offensive","repulsive","rotten","sickening","terrifying","tragic","vile", "wretched","worthless","unbearable","unpleasant","unspeakable","vicious","wicked","worse", "worst","abominable","adverse","atrocious","awful","dreadful","egregious","ghastly","heinous","horrendous","nasty","offensive","repulsive","rotten","sickening","terrifying","tragic","vile", "wretched","worthless","unbearable","unpleasant","unspeakable","vicious","wicked","worse", "worst"};
		String filtered = feedback;
		for (String neg : negatives) {
			Pattern p = Pattern.compile("\\b" + Pattern.quote(neg) + "\\b", Pattern.CASE_INSENSITIVE);
			Matcher m = p.matcher(filtered);
			filtered = m.replaceAll("***");
		}
		String[] promos = {"sale","discount","offer","buy now","free","promo", "deal", "limited time", "exclusive", "save", "clearance", "bargain", "coupon", "special", "flash sale", "bundle", "gift with purchase", "last chance", "doorbuster", "price drop", "mark down", "clearance", "best price", "lowest price", "money back", "guarantee", "risk free", "no obligation", "try it now", "act now", "don't miss", "while supplies last", "limited stock", "exclusive offer", "vip", "members only", "early access", "pre-order", "new arrival", "hot deal"};
		boolean hasPromo = false;
		for (String pk : promos) {
			Pattern p = Pattern.compile("\\b" + Pattern.quote(pk) + "\\b", Pattern.CASE_INSENSITIVE);
			if (p.matcher(feedback).find()) { hasPromo = true; break; }
		}
		String[] tokens = filtered.split("\\s+");
		List<String> cleanTokens = new ArrayList<>();
		for (String t : tokens) {
			String cleaned = t.replaceAll("^[^\\p{L}\\p{Nd}]+|[^\\p{L}\\p{Nd}]+$", "");
			if (!cleaned.isEmpty()) cleanTokens.add(cleaned);
		}
		Collections.reverse(cleanTokens);
		String reversed = String.join(" ", cleanTokens);
		System.out.println();
		System.out.println("Total Sentences: " + sentenceCount);
		System.out.println();
		System.out.println("Total Words: " + wordCount);
		System.out.println();
		System.out.println("Total Characters: " + totalCharacters(feedback));
		System.out.println();
		System.out.println("Most Frequent Word: " + (mostFreq == null ? "" : mostFreq));
		System.out.println();
		System.out.println("Filtered Feedback: " + filtered);
		System.out.println();
		System.out.println("Contains Promotion Keyword: " + (hasPromo ? "Yes" : "No"));
		System.out.println();
		System.out.println("Reversed Sentence: " + reversed);
		}

		}

	public static int totalCharacters(String s) {
		if (s == null) return 0;
		return s.length();
	}
}