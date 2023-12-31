package main.java.backend;

import java.util.ArrayList;
import java.util.List;

public class Dictionary {
    private static class Trie_node {
        private Word dictornary_data;
        private Trie_node[] child = new Trie_node[40];

        Trie_node() {
            dictornary_data = null;
            for (int i = 0; i < 40; i++) child[i] = null;
        }

        Word getWord() {
            return dictornary_data;
        }

        void setWord(Word data) {
            this.dictornary_data = data;
        }

        Trie_node getChild(int i) {
            return child[i];
        }

        void setChild(int i, Trie_node Child) {
            this.child[i] = Child;
        }

        int childNumber() {
            int count = 0;
            for (int i = 0; i < 40; i++) if (this.child[i] != null) count++;
            return count;
        }
    }

    private final Trie_node root;
    private int dictornary_size;

    public Dictionary() {
        root = new Trie_node();
        dictornary_size = 0;
    }

    /**
     * trả về số lượng từ mà từ điển này lưu trữ
     *
     * @return size cuả từ điển
     */
    public int size() {
        return this.dictornary_size;
    }

    /**
     *  hàm để xác định thứ tự child mặc định của ký tự ch là bao nhiêu trong class Trie_node
     *
     * @param ch ký tự cần xác định
     * @return vị trí tiêu chuẩn của ch trong trie_node
     */
    private int specify(char ch) {
        if ('a' <= ch && ch <= 'z') return 10 + ch - 'a';
        else if ('0' <= ch && ch <= '9') return ch - '0';
        else if (ch == ' ') return 39;
        else if (ch == '\'') return 37;
        else if (ch == '-') return 36;
        else return 38;
    }
    /**
     * hàm thêm 1 từ mới vào từ điển
     *
     */
    public void add(Word new_Word) {
        if(new_Word.getWord_target() == null || new_Word.getWord_target().equals("")) return;
        String target = new_Word.getWord_target();
        Trie_node current_node = root;
        for (int i = 0; i < target.length(); i++) {
            int asInteger = specify(target.charAt(i));
            if (current_node.getChild(asInteger) != null) {
                current_node = current_node.getChild(asInteger);
            } else {
                Trie_node newNode = new Trie_node();
                current_node.setChild(asInteger, newNode);
                current_node = newNode;
            }
        }
        if (current_node.getWord() == null) dictornary_size++;
        current_node.setWord(new_Word);
    }

    /**
     * dfs từ gốc của trie để xóa dần 1 từ vựng target (node của trie không bao giờ bị xóa)
     * index là chỉ số hiện tại kết hợp với String target đại diện cho node trie mình đang ở
     *
     */
    private void erase_from_node(Trie_node node, String target, int index) {
        if (node == null) return;
        if (index >= target.length()) {
            if (node.getWord() != null) dictornary_size--;
            node.setWord(null);
            return;
        }
        int asInteger = specify(target.charAt(index));
        if (node.getChild(asInteger) == null) return;
        erase_from_node(node.getChild(asInteger), target, index + 1);
        if (node.getChild(asInteger).childNumber() == 0 && node.getChild(asInteger).getWord() == null) {
            node.setChild(asInteger, null);
        }
    }

    /**
     * xóa 1 từ vựng từ trie
     *
     */
    public void erase(String word) {
        erase_from_node(root, word, 0);
    }

    /**
     * hàm trả về node trie đại diện ccho string target
     */
    private Trie_node find(String target) {
        Trie_node result = root;
        if (target == null) return root;
        for (int i = 0; i < target.length(); i++) {
            int asInteger = specify(target.charAt(i));
            if (result.getChild(asInteger) == null) return null;
            else result = result.getChild(asInteger);
        }
        return result;
    }

    /**
     * truy cập vào từ điển và trả về giải nghĩa từ khóa target nào đó (dưới dạng string)
     */
    public String explain(String target) {
        Trie_node node = find(target);
        if (node == null || node.getWord() == null || target == null) return null;
        return node.getWord().getWord_explain();
    }

    public boolean inside(String target) {
        return explain(target) != null;
    }

    /**
     * hàm này sửa giá trị của 1 từ vựng trong từ điển
     *
     * @param wrong_word    là từ có nghĩa sai mà mình cần phải sửa
     * @param right_meaning là ý nghĩa đúng của từ vựng mình cần sửa
     */
    public void fix(Word wrong_word, String right_meaning) {
        Trie_node node = find(wrong_word.getWord_target());
        if(node != null && node.getWord() != null) node.getWord().setWord_explain(right_meaning);
    }

    /**
     * hàm dfs thêm các từ vựng có prefix cụ thể vào list  theo thứ tự từ điển
     *
     * @param current là node trie mình đang ở
     * @param parent  là cha của node trie đó
     * @param list    là list kết quả mình cần thêm các từ vựng vào
     */
    private void get_list(Trie_node current, Trie_node parent, List<Word> list) {
        if (current == null) return;
        if (current.getWord() != null) {
            list.add(current.getWord());
        }
        for (int i = 0; i < 40; i++) get_list(current.getChild(i), current, list);
    }

    /**
     * hàm gọi từ bên ngoài để trả về các từ có prefix là prefix , sử dụng hàm get_list ở bên trên
     *
     * @return list bao gồm toàn bộ từ trong từ điển với prefix là prefix
     */
    public List<Word> get_word_with_prefix(String prefix) {
        Trie_node target = find(prefix);
        List<Word> ans = new ArrayList<>();
        get_list(target, target, ans);
        return ans;
    }

    public static void main(String[] args) {

    }
}
