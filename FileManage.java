package refirgerator;

import java.io.*;
import java.nio.file.*;
import java.util.*;

public class FileManage {
    private static final String FOOD_PATH = "data/food/";
    private static final String FOOD_EXTENSION = ".bin";
    private static final String RECIPE_PATH = "data/recipe/";
    private static final String RECIPE_EXTENSION = ".txt";

    public static void savePathLs(List<String> p, String path) {
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get(path))) {
            for (Path entry : stream) {
                p.add(entry.toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String recommendRecipe(String key) {
        Path p = Paths.get(RECIPE_PATH + key);
        if (!Files.exists(p)) return "No Recipe";

        try (DirectoryStream<Path> stream = Files.newDirectoryStream(p)) {
            List<Path> paths = new ArrayList<>();
            stream.forEach(paths::add);
            if (paths.isEmpty()) return "No Recipe";

            int fd = new Random().nextInt(paths.size());
            Path recipePath = paths.get(fd);

            return getRecipe(key, recipePath.getFileName().toString());
        } catch (IOException e) {
            e.printStackTrace();
            return "Error";
        }
    }

    public static String getRecipe(String key1, String key2) {
        try (BufferedReader reader = new BufferedReader(new FileReader(RECIPE_PATH + key1 + "/" + key2 + RECIPE_EXTENSION))) {
            StringBuilder result = new StringBuilder(key1 + " " + key2 + " recipe\n");
            String line;
            while ((line = reader.readLine()) != null) {
                result.append(line).append("\n");
            }
            return result.toString();
        } catch (IOException e) {
            throw new RuntimeException("recipe file open error", e);
        }
    }

    public static void setRecipeUseCin() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("재료, 레시피 이름 입력\n(재료 이름) (레시피 이름)");
        String[] input = scanner.nextLine().split(" ");

        Path p = Paths.get(RECIPE_PATH + input[0]);
        try {
            Files.createDirectories(p);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (PrintWriter writer = new PrintWriter(new FileWriter(RECIPE_PATH + input[0] + "/" + input[1] + RECIPE_EXTENSION))) {
            System.out.println("레시피를 입력하세요. 'end'를 입력하면 종료됩니다.");
            while (true) {
                String line = scanner.nextLine();
                if ("end".equals(line)) {
                    break;
                }
                writer.println(line);
            }
        } catch (IOException e) {
            throw new RuntimeException("recipe file open error", e);
        }
    }

    public static Food getFood(String key) {
        try (DataInputStream dis = new DataInputStream(new FileInputStream(FOOD_PATH + key + FOOD_EXTENSION))) {
            int expiry = dis.readInt();
            int nameLength = dis.readInt();
            byte[] nameBytes = new byte[nameLength];
            dis.readFully(nameBytes);
            String name = new String(nameBytes);
            Food result = new Food(name, expiry);
            System.out.println("\n" + name + " loaded\n");
            return result;
        } catch (IOException e) {
            throw new RuntimeException("food file open error", e);
        }
    }

    public static void setFoodUseCin(String key) {
        Scanner scanner = new Scanner(System.in);
        System.out.print(key + "의 유통기한 입력: ");
        int expiry = Integer.parseInt(scanner.nextLine());
        Food f = new Food(key, expiry);
        setFood(f);
    }

    public static void setFood(Food f) {
        try (DataOutputStream dos = new DataOutputStream(new FileOutputStream(FOOD_PATH + f.getName() + FOOD_EXTENSION))) {
            dos.writeInt(f.getExpiry());
            dos.writeInt(f.getName().length());
            dos.writeBytes(f.getName());
            System.out.println("\n" + f.getName() + FOOD_EXTENSION + " saved\n");
        } catch (IOException e) {
            throw new RuntimeException("food file open error", e);
        }
    }
}