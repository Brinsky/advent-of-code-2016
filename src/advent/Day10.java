package advent;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;

public class Day10 {

  private interface Recipient {
    void giveChip(int chipId);
  }

  private static class Output implements Recipient {
    private final int id;

    public int chip = -1;

    public Output(int id) {
      this.id = id;
    }

    @Override
    public void giveChip(int id) {
      chip = id;
    }
  }

  private static class Bot implements Recipient {
    private final int id;

    public Bot(int id) {
      this.id = id;
    }

    public int getId() {
      return id;
    }

    public Recipient highRecipient;
    public Recipient lowRecipient;

    private int firstChip = -1;

    public int highChip = -1;
    public int lowChip = -1;

    @Override
    public void giveChip(int chip) {
      if (firstChip < 0) {
        firstChip = chip;
      } else {
        highChip = (chip > firstChip) ? chip : firstChip;
        lowChip = (chip > firstChip) ? firstChip : chip;
      }
    }

    public boolean hasTwoChips() {
      return highChip >= 0;
    }
  }

  private static final int LOW_CHIP = 17;
  private static final int HIGH_CHIP = 61;

  public static void main(String[] args) throws IOException {
    String[] instructions = FileUtility.fileToString("input/10.txt").split("\n");

    // Initialize bots
    Map<Integer, Bot> allBots = new HashMap<>();
    Map<Integer, Output> allOutputs = new HashMap<>();
    Queue<Bot> botsWithTwoChips = new ArrayDeque<>();
    initializeBots(instructions, allBots, allOutputs, botsWithTwoChips);

    simulateInstructions(botsWithTwoChips);

    // Part one
    for (Bot bot : allBots.values()) {
      if (bot.highChip == HIGH_CHIP && bot.lowChip == LOW_CHIP) {
        FileUtility.printAndOutput(bot.getId(), "output/10a.txt");
      }
    }

    // Part two
    int product = allOutputs.get(0).chip * allOutputs.get(1).chip * allOutputs.get(2).chip;
    FileUtility.printAndOutput(product, "output/10b.txt");
  }

  private static void initializeBots(
      String[] instructions,
      Map<Integer, Bot> allBots,
      Map<Integer, Output> allOutputs,
      Queue<Bot> botsWtihTwoChips) {
    for (String instruction : instructions) {
      int[] arguments = Day08.extractInts(instruction);

      if (arguments.length == 2) { // Direct chip assignment
        int chip = arguments[0];
        int botId = arguments[1];

        Bot bot = getBot(allBots, botId);
        bot.giveChip(chip);
        if (bot.hasTwoChips()) {
          botsWtihTwoChips.add(bot);
        }
      } else { // if (args.length == 3) // Low/high target assignment
        int botId = arguments[0];
        int lowId = arguments[1];
        int highId = arguments[2];

        Bot bot = getBot(allBots, botId);

        // Determine whether recipient is bot or output based on whether instruction contains e.g.
        // "low to bot" or "low to output"
        bot.lowRecipient =
            instruction.contains("low to bot")
                ? getBot(allBots, lowId)
                : getOutput(allOutputs, lowId);
        bot.highRecipient =
            instruction.contains("high to bot")
                ? getBot(allBots, highId)
                : getOutput(allOutputs, highId);
      }
    }
  }

  private static void simulateInstructions(Queue<Bot> botsWtihTwoChips) {
    while (!botsWtihTwoChips.isEmpty()) {
      Bot bot = botsWtihTwoChips.remove();

      giveChip(botsWtihTwoChips, bot.lowRecipient, bot.lowChip);
      giveChip(botsWtihTwoChips, bot.highRecipient, bot.highChip);
    }
  }

  private static Bot getBot(Map<Integer, Bot> bots, int botId) {
    if (bots.containsKey(botId)) {
      return bots.get(botId);
    }

    Bot bot = new Bot(botId);
    bots.put(botId, bot);
    return bot;
  }

  private static Output getOutput(Map<Integer, Output> allOutputs, int id) {
    if (allOutputs.containsKey(id)) {
      return allOutputs.get(id);
    }

    Output output = new Output(id);
    allOutputs.put(id, output);
    return output;
  }

  private static void giveChip(Queue<Bot> botsWithTwoChips, Recipient recipient, int chip) {
    recipient.giveChip(chip);

    if (recipient instanceof Bot && ((Bot) recipient).hasTwoChips()) {
      Bot bot = (Bot) recipient;

      if (bot.hasTwoChips()) {
        botsWithTwoChips.add(bot);
      }
    }
  }
}
