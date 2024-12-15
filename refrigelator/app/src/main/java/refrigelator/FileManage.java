package refrigelator;

import javax.swing.*;
import java.io.*;
import java.nio.file.*;
import java.util.*;

public class FileManage {
    private static final String FOOD_PATH = "data/food/";
    private static final String FOOD_EXTENSION = ".bin";
    private static final String RECIPE_PATH = "data/recipe/";
    private static final String RECIPE_EXTENSION = ".txt";

    public static void createRecipeDir(String foodName) {
        Path path = Paths.get(RECIPE_PATH + foodName);
        try {
            Files.createDirectories(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void savePathLs(List<String> p, String path) {
        p.clear();
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get(path))) {
            for (Path entry : stream) {
                p.add(entry.getFileName().toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Food getFood(String key) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FOOD_PATH + key + FOOD_EXTENSION))) {
            return (Food) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("Food file open error", e);
        }
    }

    public static void setFoodUseCin(String key) {
        String expiryStr = JOptionPane.showInputDialog(key + "의 유통기한을 입력하세요:");
        if (expiryStr != null && !expiryStr.trim().isEmpty()) {
            int expiry = Integer.parseInt(expiryStr.trim());
            Food f = new Food(key, expiry);
            setFood(f);
            createRecipeDir(key);
        }
    }

    public static void setFood(Food f) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FOOD_PATH + f.getName() + FOOD_EXTENSION))) {
            oos.writeObject(f);
            JOptionPane.showMessageDialog(null, f.getName() + FOOD_EXTENSION + " 저장 완료", "저장 완료", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            throw new RuntimeException("Food file open error", e);
        }
    }

    public static String getRecipe(String key1, String key2) {
        Path path = Paths.get(RECIPE_PATH + key1 + "/" + key2 + RECIPE_EXTENSION);
        try {
            return new String(Files.readAllBytes(path));
        } catch (IOException e) {
            throw new RuntimeException("Recipe file open error", e);
        }
    }

    public static void setRecipeUseCin(String food, String reci) {
        Path path = Paths.get(RECIPE_PATH + food + "/" + reci + RECIPE_EXTENSION);
        try (BufferedWriter writer = Files.newBufferedWriter(path)) {
            StringBuilder recipeContent = new StringBuilder();
            while (true) {
                String line = JOptionPane.showInputDialog("레시피를 입력하세요 (end 입력 시 종료):");
                if ("end".equals(line)) {
                    break;
                }
                recipeContent.append(line).append("\n");
            }
            writer.write(recipeContent.toString());
        } catch (IOException e) {
            throw new RuntimeException("Recipe file open error", e);
        }
    }

    public static String recommendRecipe(String key) {
        Path path = Paths.get(RECIPE_PATH + key);
        if (!Files.exists(path)) return "No Recipe";

        try (DirectoryStream<Path> stream = Files.newDirectoryStream(path)) {
            List<Path> recipes = new ArrayList<>();
            for (Path entry : stream) {
                recipes.add(entry);
            }
            if (recipes.isEmpty()) return "No Recipe";
            Path randomRecipe = recipes.get(new Random().nextInt(recipes.size()));
            String recipeName = randomRecipe.getFileName().toString().replace(RECIPE_EXTENSION, "");
            return recipeName+getRecipe(key, recipeName);
        } catch (IOException e) {
            throw new RuntimeException("Recipe file open error", e);
        }
    }
}
