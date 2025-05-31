import javax.swing.*;
import java.awt.*;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;
import java.util.ArrayList;
import javax.swing.border.EmptyBorder;

public class simgui {
    private JFrame frame;
    private JTextArea outputArea;
    private JTextField inputField;
    private JLabel imageLabel;

    private int initialBudget;
    private double initialGhgEmissions;
    private int initialHappiness;

    // labels
    private JLabel yearLabel;
    private JLabel statsLabel;

    // game variables
    private int year = 0;
    private int budget = 100000;
    private double ghgEmissions = 264.0;
    private double ghgperyear = 0.0;
    private int happiness = 50;
    private int budgetperyear = 0;
    private int happyperyear = 0;

    private boolean cleangrids = false;

    // last year values
    // private int lastBudget;
    // private double lastGhg;
    // private int lastHappiness;

    // questions for each year
    private List<String> questions;
    private int currentQuestion = 0;

    private JProgressBar yearProgressBar;

    private String[] yearImageUrls = {
            "https://th.bing.com/th/id/OIP.S7maYyuAvpkq07VwzdzWawHaD4?rs=1&pid=ImgDetMain",
            "https://images.hdqwalls.com/download/tesla-roadster-2020-3p-1920x1200.jpg",
            "https://taxfoundation.org/wp-content/uploads/2022/10/A-Carbon-Tax-Explained-768x430.jpg",
            "https://i.cbc.ca/1.6533017.1678326802!/fileImage/httpImage/image.jpg_gen/derivatives/original_1180/ge-hitachi-bwrx-300-small-modular-reactor.jpg?im=Resize%3D1180",
            "https://th.bing.com/th/id/OIP.8h8Ts5QaWdePYU_1zMvnAAHaEo?rs=1&pid=ImgDetMain",
            "https://th.bing.com/th/id/OIP.AgIs_4suVsYu0ES8Ji2gSwHaD0?rs=1&pid=ImgDetMain",
            "https://sustainability.yale.edu/sites/default/files/resize/images/paris_3obj_infographic%20(1)-800x447.png",
            "https://everydayrecycler.com/wp-content/uploads/2021/06/single-use-plastic-main.jpg"
    };

    public simgui() {
        setupQuestions();
        buildGUI();
        resetGame();
    }

    private void setupQuestions() {
        questions = new ArrayList<>();
        questions.add(
                "Year 1: For your first decision do you fund the construction of bigger and cleaner electricity grids to start the switch to clean electricity? (yes/no)");
        questions.add("Year 2: Do you subsidize the purchase of electric vehicles for citizens? (yes/no)");
        questions.add("Year 3: Do you implement a carbon tax on fossil fuel companies? (yes/no)");
        questions.add("Year 4: Do you invest in building more nuclear power plants? (yes/no)");
        questions.add("Year 5: Do you begin using carbon capture? (yes/no)");
        questions.add("Year 6: Do you support building more public transportation infrastructure? (yes/no)");
        questions.add("Year 7: Do you withdraw from the Paris Agreeement? (yes/no)");
        questions.add("Year 8: Do you ban single use plastics? (yes/no)");
    }

    private void buildGUI() {
        frame = new JFrame("Climate Manage Simulator");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLayout(new BorderLayout());

        // Top panel for year and stats
        JPanel topPanel = new JPanel(new BorderLayout());

        // Year label (top left)
        yearLabel = new JLabel();
        yearLabel.setFont(new Font("Arial", Font.BOLD, 35));
        yearLabel.setBorder(new EmptyBorder(20, 40, 20, 40));
        topPanel.add(yearLabel, BorderLayout.WEST);

        // Stats label (top right)
        statsLabel = new JLabel();
        statsLabel.setFont(new Font("Segoe UI Emoji", Font.BOLD, 25));
        statsLabel.setBorder(new EmptyBorder(20, 40, 20, 40));
        topPanel.add(statsLabel, BorderLayout.EAST);

        // Image display (centered)
        imageLabel = new JLabel();
        imageLabel.setHorizontalAlignment(JLabel.CENTER);
        imageLabel.setPreferredSize(new Dimension(640, 280));
        topPanel.add(imageLabel, BorderLayout.CENTER);

        frame.add(topPanel, BorderLayout.NORTH);

        // Output display
        outputArea = new JTextArea();
        outputArea.setEditable(false);
        outputArea.setLineWrap(true);
        outputArea.setWrapStyleWord(true);
        outputArea.setFont(new Font("Arial", Font.BOLD, 24));
        outputArea.setMargin(new Insets(30, 60, 30, 60));
        outputArea.setBackground(new Color(240, 248, 255)); // Light blue
        JScrollPane scrollPane = new JScrollPane(outputArea);
        frame.add(scrollPane, BorderLayout.CENTER);

        // Input field
        JPanel inputPanel = new JPanel(new BorderLayout());
        inputField = new JTextField();
        inputField.setFont(new Font("Arial", Font.PLAIN, 22));
        inputField.setMargin(new Insets(10, 20, 10, 20));
        inputField.setBackground(new Color(255, 255, 224)); // Light yellow
        JButton submitButton = new JButton("Submit");

        submitButton.addActionListener(e -> handleInput());
        inputField.addActionListener(e -> handleInput());

        inputPanel.add(inputField, BorderLayout.CENTER);
        inputPanel.add(submitButton, BorderLayout.EAST);
        frame.add(inputPanel, BorderLayout.SOUTH);

        // progress bar with 50% election marker
        // progress bar with 50% election marker
        yearProgressBar = new JProgressBar(JProgressBar.VERTICAL, 0, 8) {
            @Override
            protected void paintComponent(Graphics g) {
                // Draw background
                g.setColor(getBackground());
                g.fillRect(0, 0, getWidth(), getHeight());

                int width = getWidth();
                int height = getHeight();

                // Draw 50% marker (after year 4)
                int markerY = height * 4 / 8;
                Graphics2D g2 = (Graphics2D) g;
                g2.setStroke(new BasicStroke(10));
                g2.setColor(Color.RED);
                g2.drawLine(0, markerY, width, markerY);

                g2.setStroke(new BasicStroke(1));

                // Draw "Election" label
                g.setColor(Color.BLACK);
                g.setFont(new Font("Arial", Font.BOLD, 20));
                g.drawString("Election", 5, markerY - 5);

                // Draw progress bar (corrected calculation)
                int value = getValue();
                if (value > 0) {
                    g.setColor(new Color(34, 139, 34));
                    int barHeight = height * value / 8; // Now uses value/8
                    int y = height - barHeight;
                    g.fillRect(0, y, width, barHeight);
                }

                // Draw border
                g.setColor(Color.GRAY);
                g.drawRect(0, 0, width - 1, height - 1);

                // Draw percentage at bottom
                String percentString = getString();
                if (percentString != null) {
                    FontMetrics fm = g.getFontMetrics();
                    int stringWidth = fm.stringWidth(percentString);
                    int x = (width - stringWidth) / 2;
                    int y = height - 5;
                    g.setColor(Color.BLACK);
                    g.drawString(percentString, x, y);
                }
            }
        };
        yearProgressBar.setValue(0); // Start at 0%
        yearProgressBar.setString("0%");
        yearProgressBar.setPreferredSize(new Dimension(100, 300));
        yearProgressBar.setBackground(new Color(240, 248, 255));

        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BorderLayout());
        rightPanel.add(yearProgressBar, BorderLayout.CENTER);
        rightPanel.setBorder(BorderFactory.createEmptyBorder(40, 10, 40, 40));

        frame.add(rightPanel, BorderLayout.EAST);

        updateTopPanel();
        // fullscreen
        frame.setUndecorated(true);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);

        frame.setVisible(true);
        inputField.requestFocusInWindow();
    }

    private void updateTopPanel() {
        int percent = (int) ((year / 8.0) * 100);
        yearProgressBar.setValue(year);
        yearProgressBar.setString(percent + "%");

        yearProgressBar.setValue(year);
        yearLabel.setText("Year: " + (year + 1));
        statsLabel.setText(
                "💰Budget: $" + budget + "M   🏭GHG: " + Math.round(ghgEmissions) + "Mt   😀Happiness: "
                        + happiness + "%");

        updateYearImage(year);
    }

    // updates image
    public void updateYearImage(int year) {
        try {
            URL imageUrl = new URI(yearImageUrls[year]).toURL(); // Fix deprecated URL constructor
            ImageIcon icon = new ImageIcon(imageUrl);
            imageLabel.setIcon(icon);
        } catch (URISyntaxException | MalformedURLException e) {
            imageLabel.setIcon(null); // Clear image on error
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void showIntro() {
        printText("Welcome to Climate Manage Simulator!");
        printText(
                "You have just been elected as the new Prime Minister. It's up to you to make decisions that balance the budget, reduce greenhouse gas (GHG) emissions, and keep citizens happy. You can track the budget, emissions, and citizen happiness levels at the top right of the screen. After four years, there will be an election—so make sure the citizens are happy if you want to be re-elected.");
        askNextQuestion();
    }

    private void handleInput() {
        String input = inputField.getText().trim();
        inputField.setText("");

        if (input.isEmpty())
            return;

        printText("> " + input);

        if (input.equalsIgnoreCase("exit")) {
            resetGame();
            return;
        }

        // Validate input first
        if (!CommandParser.isValidYesNo(input)) {
            printText("Please answer 'yes' or 'no'.");
            return;
        }

        // Process decision and advance the game
        processDecision(CommandParser.parseYesNo(input));
    }

    private void processDecision(boolean answerYes) {
        int prevBudget = budget;
        double prevGHG = ghgEmissions;
        int prevHappiness = happiness;
        switch (currentQuestion) {
            case 0: // Year 1: Clean grids
                if (answerYes) {
                    budget -= 48474;
                    ghgEmissions -= 15;
                    happyperyear -= 2;
                    cleangrids = true;
                    printText("You funded cleaner grids. Emissions drop, but it was expensive.");
                } else {
                    ghgEmissions += 10;
                    happiness -= 5;
                    printText("No grid upgrades. Emissions rise, but you saved a lot of money");
                }
                break;
            case 1: // Year 2: Electric vehicles
                if (answerYes) {
                    budget -= 3000;
                    ghgEmissions -= 3;
                    happiness += 15;
                    budgetperyear -= 3000;
                    ghgperyear -= 2;
                    printText("You subsidized EVs. Emissions drop, but costs rise.");
                } else {
                    happiness -= 10;
                    printText("No EV subsidies. Emissions rise, public is less happy.");
                }
                break;
            case 2: // Year 3: Carbon tax
                if (answerYes) {
                    budget += 6700; // Tax revenue
                    ghgEmissions -= 20;
                    if (cleangrids) {
                        happiness += 5;
                        happyperyear += 1;
                        printText(
                                "Carbon tax implemented. Emissions drop, public switches to cleaner energy options because of your decision to switch to clean energy.");
                    } else
                        happiness -= 5;
                    printText("Carbon tax implemented. Emissions drop, but public unhappy.");
                } else {
                    ghgEmissions += 10;
                    happiness += 5;
                    printText("No carbon tax. Emissions rise, but people are relieved that costs stay the same.");
                }
                break;
            case 3: // Year 4: Nuclear plants
                if (answerYes) {
                    budget -= 20000;
                    ghgEmissions -= 25;
                    happiness += 5;
                    printText(
                            "Nuclear plants built. Emissions drop. Extremely Costly. Public is happy about the jobs created.");
                } else {
                    ghgEmissions += 20;
                    happiness += 5;
                    printText("No new nuclear. Emissions rise, public is happy.");
                }
                break;
            case 4: // Year 5: Implement Carbon Capture
                if (answerYes) {
                    budget -= 8000; // 8 billion cost
                    ghgEmissions -= 20;
                    happiness -= 3;
                    ghgperyear -= 2;
                    printText(
                            "Carbon capture facilities funded. Emissions drop significantly, but public concerned about high costs and safety.");
                } else {
                    happiness += 2;
                    ghgperyear += 1;
                    printText("No carbon capture investment. Costs avoided, but industrial emissions remain high.");
                }
                break;
            case 5: // Year 6: Support building more public transportation infrastructure
                if (answerYes) {
                    budget -= 3900;
                    ghgEmissions -= 3;
                    happiness += 4;
                    ghgperyear -= 3;
                    printText(
                            "Major transit infrastructure built. High public support, emissions drop as ridership increases.");
                } else {
                    happiness -= 2;
                    ghgperyear += 1;
                    printText("No new transit infrastructure. Public disappointed, car dependency continues.");
                }
                break;
            case 6: // Year 7: Withdraw from the Paris Agreement?
                if (answerYes) {
                    happiness -= 10;
                    ghgperyear += 12;
                    printText(
                            "Withdrew from Paris Agreement. International reputation damaged, climate policies weakened.");
                } else {
                    happiness += 5;
                    ghgperyear -= 1;
                    printText("Stayed in Paris Agreement. Strong international climate cooperation continues.");
                }
                break;
            case 7: // Year 8: Ban Single-Use Plastics
                if (answerYes) {
                    budget -= 70;
                    ghgEmissions -= 2;
                    happiness += 10;
                    printText("Single-use plastics banned. High public support, plastic waste drops dramatically.");
                } else {
                    happiness -= 2;
                    ghgperyear += 1;
                    printText("No plastic ban. Environmental pollution continues, public disappointed.");
                }
                break;
        }

        // --- BEGIN: Apply annual changes and print summary ---
        budget += 5000 + budgetperyear;
        ghgEmissions += ghgperyear;
        happiness += happyperyear;

        // Print yearly report
        printText("\nYearly Report:");
        printText("Budget: " + (budget - prevBudget) + "M change");
        printText("GHG: " + (ghgEmissions - prevGHG) + " Mt change");
        printText("Happiness: " + (happiness - prevHappiness) + "% change");

        // Advance game state
        year = Math.min(year + 1, 8);
        currentQuestion++;
        updateTopPanel();

        // Election check after 4 years
        if (year == 4) { // Changed from 3 to 4
            checkElection();
            if (happiness < 50)
                return;
        }
        // Continue game
        if (currentQuestion < questions.size()) {
            askNextQuestion();
        } else {
            endGame();
        }
    }

    private void checkElection() {
        printText("\n=== ELECTION YEAR ===");
        printText("Citizens are voting on whether to re-elect you...");

        if (happiness >= 50) {
            printText("You won re-election! The people trust your leadership.");
            printText("Continuing to your second term...");
        } else {
            printText("You lost the election! The people have voted you out.");
            printText("Game Over - Better luck next time!");
            printText("Type 'exit' to play again!");
            inputField.setEnabled(true);
            return;
        }

    }

    private void askNextQuestion() {
        if (currentQuestion < questions.size()) {
            printText(questions.get(currentQuestion));
        }
    }

    private void printText(String text) {
        outputArea.append(text + "\n\n");
    }

    private void resetGame() {
        // Set initial values FIRST to capture starting values
        initialBudget = 100000;
        initialGhgEmissions = 264.0;
        initialHappiness = 50;

        // Reset current values to match initial values
        year = 0;
        currentQuestion = 0;
        budget = initialBudget;
        ghgEmissions = initialGhgEmissions;
        happiness = initialHappiness;
        budgetperyear = 0;
        ghgperyear = 0.0;
        happyperyear = 0;
        cleangrids = false;

        // Reset UI
        outputArea.setText("");
        inputField.setEnabled(true);
        updateTopPanel();
        updateYearImage(year);
        showIntro();
    }

    private void endGame() {
        printText("\nGame Over!");
        printText("Final Results after 8 years:");

        // Use actual values (not rounded) for percentage calculations
        printText("Budget: $" + budget + "M");
        printText("GHG Emissions: " + Math.round(ghgEmissions) + " Mt");
        printText("Final Happiness: " + happiness + "%");

        // Calculate percent changes with zero checks
        double budgetPercent = ((budget - initialBudget) / (double) initialBudget) * 100;
        double ghgPercent = ((ghgEmissions - initialGhgEmissions) / initialGhgEmissions) * 100;
        double happyPercent = ((happiness - initialHappiness) / (double) initialHappiness) * 100;

        // Print formatted percentages
        printText(String.format("Budget change: %+.1f%%", budgetPercent));
        printText(String.format("GHG emissions change: %+.1f%%", ghgPercent));
        printText(String.format("Happiness change: %+.1f%%", happyPercent));

        inputField.setEnabled(true);
        printText("Type 'exit' to play again!");
    }

    public static void main(String[] args) {
        // Start the GUI on the Event Dispatch Thread (best practice for Swing)
        javax.swing.SwingUtilities.invokeLater(() -> new simgui());
    }
}
