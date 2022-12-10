package db61b;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import static db61b.Utils.*;

class Trie {
	public static String encrypt_sha_1(String input) {
		try {
			MessageDigest sha1 = MessageDigest.getInstance("SHA-1");
			byte[] md = sha1.digest(input.getBytes());
			BigInteger origin = new BigInteger(1, md);
			String hash_text = origin.toString(16);
			while (hash_text.length() < 32)
				hash_text = "0" + hash_text;
			return hash_text;
		} catch (NoSuchAlgorithmException e) {
			throw error("Please ensure using the correct utf-8");
		}
	}

	public static int character_to_number(Character c) {
		if (c >= '0' && c <= '9')
			return c - '0';
		else if (c>='a' && c<='f')
			return c - 'a' + 10;
		else
			return -1;
	}

	private class Node {
		public Node() {
			sum = 0;
			next = new Node[16];
			ans = null;
			only = -1;
		}

		public String ans;
		public int sum;
		public int only;
		public Node[] next;
	}

	public Trie() {
		_root = new Node();
	}

	public void Insert(String s) {
		Node now = _root;
		if(s.length()!=32){
			throw error("Invalid SHA-1 code");
		}
		else{
			for (int i = 0; i < s.length(); i++) {
				now.sum++;
				Character c = s.charAt(i);
				int next_number = character_to_number(c);
				if (now.next[next_number] == null) {
					now.next[next_number] = new Node();
				}
				if (now.sum == 1) {
					now.only = next_number;
				}
				else{
					now.only = -1;
				}
				now = now.next[next_number];
			}
			now.ans = s;
		}
	}

	public String Find(String s) {
		Node now = _root;
		if(s.length()>32) {
			throw error("Invalid SHA-1 code.");
		}
		for (int i = 0; i < s.length(); i++) {
			Character c = s.charAt(i);
			int next_number = character_to_number(c);
			if (next_number == -1) {
				throw error("Invalid SHA-1 code.");
			}
			if (now.next[next_number] == null) {
				return "Version Not Found"; // no such prefix
			}
			now = now.next[next_number];
		}
		if (now.sum > 1) {
			return "More than one version shares the same name"; //more than one string
		}
		while (now.sum == 1) {
			now = now.next[now.only];
		}
		return now.ans;
	}

	private Node _root;
}
