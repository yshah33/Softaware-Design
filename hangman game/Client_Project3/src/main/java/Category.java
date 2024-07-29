import java.io.Serializable;
import java.util.List;
import java.util.ArrayList;

// Make sure to have same in both server and client

public class Category implements Serializable {
    private String CategoryName;                // name of the Category
    private List<String> wordList;              // list of the word in that Category

    public Category() {
        this.CategoryName = "";
        this.wordList = new ArrayList<>();
    }

    // fully parameterized parameter
    public Category(String name, List<String> wordList) {
        this.CategoryName = name;
        this.wordList = wordList;
    }

    // getter and setter of that private variables
    public String getCategoryName() {
        return CategoryName;
    }

    public List<String> getWordList() {
        return wordList;
    }


    public void setCategoryName(String name) {
        this.CategoryName = name;
    }

    public void setWordList(List<String> wordList) {
        this.wordList = wordList;
    }

}