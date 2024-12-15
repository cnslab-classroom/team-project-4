package refrigelator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainFrame extends JFrame {
    private Refrigerator refrigerator;
    private JTextArea outputArea;

    public MainFrame() {
        refrigerator = new Refrigerator();
        outputArea = new JTextArea(20, 40);
        outputArea.setEditable(false);

        setTitle("Refrigerator Management");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(4, 2));
        JPanel buttonPanel2 = new JPanel();
        buttonPanel2.setLayout(new GridLayout(1,1));

        JButton viewAllButton = new JButton("전체 음식 보기");
        JButton addFoodButton = new JButton("음식 추가");
        JButton removeFoodButton = new JButton("음식 제거");
        JButton viewRecipeButton = new JButton("레시피 보기");
        JButton viewRecentButton = new JButton("최근 5개 음식 보기");
        JButton saveFoodButton = new JButton("음식 저장");
        JButton saveRecipeButton = new JButton("레시피 저장");
        JButton exitButton = new JButton("종료");
        JButton recommendButton = new JButton("레시피 추천");

        buttonPanel2.add(recommendButton);
        buttonPanel.add(viewAllButton);
        buttonPanel.add(addFoodButton);
        buttonPanel.add(removeFoodButton);
        buttonPanel.add(viewRecipeButton);
        buttonPanel.add(viewRecentButton);
        buttonPanel.add(saveFoodButton);
        buttonPanel.add(saveRecipeButton);
        buttonPanel.add(exitButton);

        add(new JScrollPane(outputArea), BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
        add(buttonPanel2, BorderLayout.NORTH);

        viewAllButton.addActionListener(e -> UI.printAllFood(refrigerator, outputArea));
        addFoodButton.addActionListener(e -> UI.pushFood(refrigerator, outputArea));
        removeFoodButton.addActionListener(e -> UI.popFood(refrigerator, outputArea));
        viewRecipeButton.addActionListener(e -> UI.printRecipe(outputArea));
        viewRecentButton.addActionListener(e -> UI.print5Food(refrigerator, outputArea));
        saveFoodButton.addActionListener(e -> UI.saveFood(outputArea));
        saveRecipeButton.addActionListener(e -> UI.saveRecipe(outputArea));
        exitButton.addActionListener(e -> {
            UI.programOff();
            System.exit(0);
        });
        recommendButton.addActionListener(e->UI.recommendation(outputArea));

        startBackgroundThreads();
    }

    private void startBackgroundThreads() {
        Thread clockThread = new Thread(Time::flowClock);
        Thread secondWorkThread = new Thread(() -> Time.secondWork(refrigerator));
        Thread reminderWorkThread = new Thread(() -> Time.reminderWork(refrigerator));
        Thread alertWorkThread = new Thread(() -> Time.alertWork(refrigerator));

        clockThread.start();
        secondWorkThread.start();
        reminderWorkThread.start();
        alertWorkThread.start();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainFrame frame = new MainFrame();
            frame.setVisible(true);
        });
    }
}
