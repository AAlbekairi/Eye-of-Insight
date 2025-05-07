
---

**Solution.java**
```java
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.StringTokenizer;

public class Solution {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        Index index = new Index();
        String line;

        while ((line = br.readLine()) != null) {
            line = line.trim();
            if (line.isEmpty()) continue;
            StringTokenizer st = new StringTokenizer(line);
            if (!st.hasMoreTokens()) { System.out.println(-1); return; }
            String cmdStr = st.nextToken();
            int cmd;
            try { cmd = Integer.parseInt(cmdStr); }
            catch (NumberFormatException e) { System.out.println(-1); return; }
            if (cmd < 1 || cmd > 4) { System.out.println(-1); return; }

            if (cmd == 1) {
                if (!st.hasMoreTokens()) { System.out.println(-1); return; }
                String sTok = st.nextToken();
                if (st.hasMoreTokens()) { System.out.println(-1); return; }
                int S;
                try { S = Integer.parseInt(sTok); }
                catch (NumberFormatException e) { System.out.println(-1); return; }
                if (S < 1 || S > 100000) { System.out.println(-1); return; }

                for (int i = 0; i < S; i++) {
                    String hdr = br.readLine();
                    if (hdr == null) { System.out.println(-1); return; }
                    hdr = hdr.trim();
                    StringTokenizer st2 = new StringTokenizer(hdr);
                    if (st2.countTokens() != 2) { System.out.println(-1); return; }
                    String filename = st2.nextToken();
                    int N;
                    try { N = Integer.parseInt(st2.nextToken()); }
                    catch (NumberFormatException e) { System.out.println(-1); return; }
                    if (N < 1 || N > 100) { System.out.println(-1); return; }

                    for (int ln = 1; ln <= N; ln++) {
                        String txt = br.readLine();
                        if (txt == null) { System.out.println(-1); return; }
                        StringBuilder w = new StringBuilder();
                        for (int j = 0; j < txt.length(); j++) {
                            char c = txt.charAt(j);
                            if (Character.isLetterOrDigit(c)) {
                                w.append(Character.toLowerCase(c));
                            } else if (w.length() > 0) {
                                index.Insert(w.toString(), filename, ln);
                                w.setLength(0);
                            }
                        }
                        if (w.length() > 0) {
                            index.Insert(w.toString(), filename, ln);
                        }
                    }
                }

            } else if (cmd == 2) {
                if (!st.hasMoreTokens()) { System.out.println(-1); return; }
                String raw = st.nextToken();
                if (st.hasMoreTokens()) { System.out.println(-1); return; }
                StringBuilder sb = new StringBuilder();
                for (char c : raw.toCharArray()) {
                    if (Character.isLetterOrDigit(c)) {
                        sb.append(Character.toLowerCase(c));
                    }
                }
                String token = sb.toString();
                if (token.isEmpty()) { System.out.println(-1); return; }
                index.Search(token);

            } else if (cmd == 3) {
                if (!st.hasMoreTokens()) { System.out.println(-1); return; }
                String raw = st.nextToken();
                if (st.hasMoreTokens()) { System.out.println(-1); return; }
                StringBuilder sb = new StringBuilder();
                for (char c : raw.toCharArray()) {
                    if (Character.isLetterOrDigit(c)) {
                        sb.append(Character.toLowerCase(c));
                    }
                }
                String token = sb.toString();
                if (token.isEmpty()) { System.out.println(-1); return; }
                index.Remove(token);

            } else {
                if (st.hasMoreTokens()) { System.out.println(-1); return; }
                index.Traverse();
            }
        }
    }
}

class Index {
    public BSTNode root;
    public int size;

    public Index() {
        root = null;
        size = 0;
    }

    public void Insert(String token, String filename, int lineNumber) {
        root = insertRec(root, token, filename, lineNumber);
    }

    private BSTNode insertRec(BSTNode node, String token, String fn, int ln) {
        if (node == null) {
            BSTNode n = new BSTNode(token);
            n.list.Insert(new Node(fn, ln));
            size++;
            return n;
        }
        int cmp = token.compareTo(node.token);
        if (cmp < 0) {
            node.left = insertRec(node.left, token, fn, ln);
        } else if (cmp > 0) {
            node.right = insertRec(node.right, token, fn, ln);
        } else {
            node.frequency++;
            node.list.Insert(new Node(fn, ln));
        }
        return node;
    }

    public void Search(String token) {
        BSTNode f = searchRec(root, token);
        if (f == null) {
            System.out.println(-1);
        } else {
            System.out.println(f.frequency);
            System.out.print(f.list.toString());
        }
    }

    private BSTNode searchRec(BSTNode node, String token) {
        if (node == null) return null;
        int cmp = token.compareTo(node.token);
        if (cmp < 0) return searchRec(node.left, token);
        if (cmp > 0) return searchRec(node.right, token);
        return node;
    }

    public void Remove(String token) {
        root = removeRec(root, token);
    }

    private BSTNode removeRec(BSTNode node, String token) {
        if (node == null) return null;
        int cmp = token.compareTo(node.token);
        if (cmp < 0) {
            node.left = removeRec(node.left, token);
        } else if (cmp > 0) {
            node.right = removeRec(node.right, token);
        } else {
            size--;
            if (node.left == null) return node.right;
            if (node.right == null) return node.left;
            BSTNode succ = minNode(node.right);
            node.token = succ.token;
            node.frequency = succ.frequency;
            node.list = succ.list;
            node.right = removeRec(node.right, succ.token);
        }
        return node;
    }

    private BSTNode minNode(BSTNode node) {
        while (node.left != null) node = node.left;
        return node;
    }

    public void Traverse() {
        if (root == null) {
            System.out.println(-1);
        } else {
            System.out.println(toString());
        }
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        toStringRec(root, sb);
        if (sb.length() > 0) sb.setLength(sb.length() - 1);
        return sb.toString();
    }

    private void toStringRec(BSTNode node, StringBuilder sb) {
        if (node != null) {
            toStringRec(node.left, sb);
            sb.append(node.token).append(" ");
            toStringRec(node.right, sb);
        }
    }
}

class BSTNode {
    public String token;
    public int frequency;
    public SList list;
    public BSTNode left, right;

    public BSTNode() {
        this.token = null;
        this.frequency = 0;
        this.list = new SList();
        this.left = this.right = null;
    }

    public BSTNode(String token) {
        this.token = token;
        this.frequency = 1;
        this.list = new SList();
        this.left = this.right = null;
    }
}

class SList {
    public int size;
    public Node head;

    public SList() {
        head = null;
        size = 0;
    }

    public void Insert(Node node) {
        if (head == null) {
            head = node;
        } else {
            Node cur = head;
            while (cur.next != null) {
                cur = cur.next;
            }
            cur.next = node;
        }
        size++;
    }

    public void Remove(Node node) {
        Node dummy = new Node("", 0);
        dummy.next = head;
        Node prev = dummy;
        Node cur = head;
        while (cur != null) {
            if (cur.filename.equals(node.filename) &&
                cur.lineNumber == node.lineNumber) {
                prev.next = cur.next;
                size--;
                break;
            }
            prev = cur;
            cur = cur.next;
        }
        head = dummy.next;
    }

    public Node Search(String filename) {
        Node cur = head;
        while (cur != null) {
            if (cur.filename.equals(filename)) {
                return cur;
            }
            cur = cur.next;
        }
        return null;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        Node cur = head;
        while (cur != null) {
            sb.append(cur.filename)
              .append(" ")
              .append(cur.lineNumber)
              .append("\\n");
            cur = cur.next;
        }
        return sb.toString();
    }
}

class Node {
    public String filename;
    public int lineNumber;
    public Node next;

    public Node() {
        this.filename = "";
        this.lineNumber = 0;
        this.next = null;
    }

    public Node(String filename, int lineNumber) {
        this.filename = filename;
        this.lineNumber = lineNumber;
        this.next = null;
    }

    public String toString() {
        return filename + " " + lineNumber;
    }
}
