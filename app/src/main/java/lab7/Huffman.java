package lab7;

// Kaiyo Nakashima
// 2025/06/09
// This program makes a tree of letters.
// It can turn text into code and turn code back to text.

import java.util.*;

public class Huffman {
    private static class Node {
        char ch;
        int freq;
        Node left;
        Node right;

        Node(char c, int f) {
            this.ch = c;
            this.freq = f;
            this.left = null;
            this.right = null;
        }

        Node(Node l, Node r) {
            this.ch = '\0';
            this.freq = l.freq + r.freq;
            this.left = l;
            this.right = r;
        }

        boolean isLeaf() {
            return (left == null && right == null);
        }
    }

    private Node root;
    private Map<Character, String> codeMap = new HashMap<>();

    public void buildTree(String text) {
        int[] freqTable = new int[256];
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            freqTable[c]++;
        }

        List<Node> nodeList = new ArrayList<>();
        for (int i = 0; i < freqTable.length; i++) {
            if (freqTable[i] > 0) {
                nodeList.add(new Node((char) i, freqTable[i]));
            }
        }
        if (nodeList.size() == 1) {
            nodeList.add(new Node('\0', 0));
        }
        while (nodeList.size() > 1) {
            int min1 = 0, min2 = 1;
            if (nodeList.get(min2).freq < nodeList.get(min1).freq) {
                int tmp = min1;
                min1 = min2;
                min2 = tmp;
            }
            for (int i = 2; i < nodeList.size(); i++) {
                if (nodeList.get(i).freq < nodeList.get(min1).freq) {
                    min2 = min1;
                    min1 = i;
                } else if (nodeList.get(i).freq < nodeList.get(min2).freq) {
                    min2 = i;
                }
            }
            Node n1 = nodeList.get(min1);
            Node n2 = nodeList.get(min2);
            Node parent = new Node(n1, n2);
            if (min1 > min2) {
                nodeList.remove(min1);
                nodeList.remove(min2);
            } else {
                nodeList.remove(min2);
                nodeList.remove(min1);
            }
            nodeList.add(parent);
        }
        root = nodeList.get(0);
        buildCodesRec(root, "");
    }

    private void buildCodesRec(Node node, String prefix) {
        if (node.isLeaf()) {
            if (prefix.length() == 0) {
                codeMap.put(node.ch, "0");
            } else {
                codeMap.put(node.ch, prefix);
            }
        } else {
            buildCodesRec(node.left, prefix + "0");
            buildCodesRec(node.right, prefix + "1");
        }
    }

    public String encode(String text) {
        if (codeMap.isEmpty()) {
            buildTree(text);
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            String bits = codeMap.get(c);
            sb.append(bits);
        }
        return sb.toString();
    }

    public String decode(String bits) {
        StringBuilder result = new StringBuilder();
        Node current = root;
        for (int i = 0; i < bits.length(); i++) {
            char b = bits.charAt(i);
            if (b == '0') {
                current = current.left;
            } else {
                current = current.right;
            }
            if (current.isLeaf()) {
                result.append(current.ch);
                current = root;
            }
        }
        return result.toString();
    }

    public static void main(String[] args) {
        String text;
        if (args.length > 0) {
            text = args[0];
        } else {
            System.out.print("Input text: ");
            text = new Scanner(System.in).nextLine();
        }

        Huffman h = new Huffman();
        String encoded = h.encode(text);
        System.out.println("Codes: " + h.codeMap);
        System.out.println("Encoded: " + encoded);
        System.out.println("Decoded: " + h.decode(encoded));
    }
}