package refrigelator;

import javax.swing.*;
import java.util.*;

public class UI {
    public static void inputToVector(String input, List<String> v) {
        v.clear();
        if (input.isEmpty()) {
            return;
        }
        String[] words = input.split("\\s+");
        Collections.addAll(v, words);
    }

    public static void printAllFood(Refrigerator r, JTextArea outputArea) {
        r.print(outputArea);
    }

    public static void pushFood(Refrigerator r, JTextArea outputArea) {
        String foodName = JOptionPane.showInputDialog("추가할 음식의 이름을 입력하세요:");
        if (foodName != null && !foodName.trim().isEmpty()) {
            r.push(FileManage.getFood(foodName.trim()));
            outputArea.append("음식이 추가되었습니다: " + foodName + "\n");
        }
    }

    public static void popFood(Refrigerator r, JTextArea outputArea) {
        String foodName = JOptionPane.showInputDialog("제거할 음식의 이름을 입력하세요:");
        if (foodName != null && !foodName.trim().isEmpty()) {
            try {
                r.pop(foodName.trim());
                outputArea.append("음식이 제거되었습니다: " + foodName + "\n");
                outputArea.append("추천 레시피: " + FileManage.recommendRecipe(foodName.trim()) + "\n");
            } catch (NoSuchElementException e) {
                outputArea.append(e.getMessage() + "\n");
            }
        }
    }

    public static void saveFood(JTextArea outputArea) {
        String foodName = JOptionPane.showInputDialog("저장할 음식의 이름을 입력하세요:");
        if (foodName != null && !foodName.trim().isEmpty()) {
            FileManage.setFoodUseCin(foodName.trim());
            outputArea.append("음식이 저장되었습니다: " + foodName + "\n");
        }
    }

    public static void printRecipe(JTextArea outputArea) {
        String foodName = JOptionPane.showInputDialog("레시피를 볼 음식의 이름을 입력하세요:");
        if (foodName != null && !foodName.trim().isEmpty()) {
            String recipeName = JOptionPane.showInputDialog("볼 레시피의 이름을 입력하세요:");
            if (recipeName != null && !recipeName.trim().isEmpty()) {
                outputArea.append(FileManage.getRecipe(foodName.trim(), recipeName.trim()) + "\n");
            }
        }
    }

    public static void saveRecipe(JTextArea outputArea) {
        String foodName = JOptionPane.showInputDialog("저장할 레시피의 음식 이름을 입력하세요:");
        if (foodName != null && !foodName.trim().isEmpty()) {
            String recipeName = JOptionPane.showInputDialog("저장할 레시피의 이름을 입력하세요:");
            if (recipeName != null && !recipeName.trim().isEmpty()) {
                FileManage.setRecipeUseCin(foodName.trim(), recipeName.trim());
                outputArea.append("레시피가 저장되었습니다: " + recipeName + "\n");
                JOptionPane.showMessageDialog(null, recipeName + " 레시피 저장 완료", "저장 완료", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }

    public static void recommendation(JTextArea outputArea ){
        String foodName = JOptionPane.showInputDialog("레시피를 볼 음식의 이름을 입력하세요:");
        if (foodName != null && !foodName.trim().isEmpty()) {
             outputArea.append(FileManage.recommendRecipe(foodName.trim()) + "\n");
        }
    }

    public static void print5Food(Refrigerator r, JTextArea outputArea) {
        int thre = Math.min(5, r.length());
        outputArea.append("\n최근 5개 음식 목록\n");
        for (int i = 0; i < thre; i++) {
            outputArea.append(r.pop(i).toString() + "\n");
        }
        outputArea.append("\n");
    }

    public static void programOff() {
        Time.programOff();
    }
}