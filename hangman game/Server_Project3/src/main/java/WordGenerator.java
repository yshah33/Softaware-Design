import java.util.List;
import java.util.Random;
import java.util.ArrayList;

public class WordGenerator {
    public List<Category> categories;          // list of category (Fruit, Animal, Countries)

    // getter and setter for private variable
    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    // default constructor
    public WordGenerator() {
        this.categories = initializeCategories();
    }

    private List<Category> initializeCategories() {
        List<Category> categories = new ArrayList<>();

        // Create a list of the words for each category
        List<String> animalName = new ArrayList<>();
        animalName.add("Lion");
        animalName.add("Elephant");
        animalName.add("Horse");
        animalName.add("Dog");
        animalName.add("Cat");
        animalName.add("Tiger");
        animalName.add("Bear");
        animalName.add("Fox");
        animalName.add("Rabbit");
        animalName.add("Deer");
        animalName.add("Dolphin");
        animalName.add("Frog");
        animalName.add("Giraffe");
        animalName.add("Hippo");
        animalName.add("Kangaroo");
        animalName.add("Monkey");
        animalName.add("Owl");
        animalName.add("Panda");
        animalName.add("Penguin");
        animalName.add("Sheep");
        animalName.add("Zebra");


        List<String> fruitName = new ArrayList<>();
        fruitName.add("Apple");
        fruitName.add("Banana");
        fruitName.add("Orange");
        fruitName.add("Mango");
        fruitName.add("Grapes");
        fruitName.add("Strawberry");
        fruitName.add("Pineapple");
        fruitName.add("Watermelon");
        fruitName.add("Cherry");
        fruitName.add("Peach");
        fruitName.add("Pear");
        fruitName.add("Plum");
        fruitName.add("Kiwi");
        fruitName.add("Lemon");
        fruitName.add("Lime");
        fruitName.add("Blueberry");
        fruitName.add("Raspberry");
        fruitName.add("Blackberry");
        fruitName.add("Cantaloupe");
        fruitName.add("Papaya");


        List<String> countryName = new ArrayList<>();
        countryName.add("India");
        countryName.add("Japan");
        countryName.add("Mexico");
        countryName.add("Norway");
        countryName.add("Poland");
        countryName.add("Portugal");
        countryName.add("Russia");
        countryName.add("Spain");
        countryName.add("Vietnam");
        countryName.add("Zambia");
        countryName.add("France");
        countryName.add("Finland");
        countryName.add("Ethiopia");
        countryName.add("Denmark");
        countryName.add("China");
        countryName.add("Canada");
        countryName.add("Brazil");
        countryName.add("Australia");
        countryName.add("Germany");
        countryName.add("Egypt");


        // Create instances for predefined categories with words
        Category Animal = new Category("ANIMALS", animalName);
        Category Fruit = new Category("FRUITS", fruitName);
        Category Countries = new Category("COUNTRIES", countryName);


        // Add categories to the list
        categories.add(Animal);
        categories.add(Fruit);
        categories.add(Countries);

        return categories;
    }

    // generate random word from the category
    public String getRandomWord(Category category) {
        String word = "";
        for (Category x : categories) {
            if (x.getCategoryName().equals(category.getCategoryName())) {
                List<String> wordList = x.getWordList();
                Random r = new Random();
                int randomItem = r.nextInt(wordList.size());
                word = wordList.get(randomItem);
                break;
            }
        }
        return word;
    }

}