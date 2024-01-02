import { RoundInfo } from "../types/RoundInfo";
import { LogLine } from "../types/LogLine";
import { BotRoundInfo } from "../types/BotRoundInfo";
import { toast } from "react-toastify";
import Card, { CardColor, CardValue } from "../components/card";

const logExamples: LogLine[] = [
  { message: "This is a normal log message." },
  { message: "Another log entry." },
  { message: "Debugging information.", isDebug: true },
  { message: "More normal logs." },
  { message: "Debug log with additional information.", isDebug: true },
  { message: "More normal logs." },
  { message: "This is a normal log message." },
  { message: "Another log entry." },
  { message: "Debugging information.", isDebug: true },
  { message: "More normal logs." },
  { message: "Debug log with additional information.", isDebug: true },
  { message: "More normal logs." },
  { message: "This is a normal log message." },
  { message: "Another log entry." },
  { message: "Debugging information.", isDebug: true },
  { message: "More normal logs." },
  { message: "Debug log with additional information.", isDebug: true },
  { message: "More normal logs." },
];

const drawpile = [
  { cardColor: CardColor.PURPLE, cardValue: CardValue.FIVE },
  { cardColor: CardColor.PURPLE, cardValue: CardValue.FIVE },
  { cardColor: CardColor.PURPLE, cardValue: CardValue.FIVE },
  { cardColor: CardColor.PURPLE, cardValue: CardValue.FIVE },
  { cardColor: CardColor.PURPLE, cardValue: CardValue.FIVE },
];

const round1 = [
  {
    botName: "Bot1",
    botHand: [
      {
        cardColor: CardColor.PURPLE,
        cardValue: CardValue.FIVE,
      },
      {
        cardColor: CardColor.ORANGE,
        cardValue: CardValue.FIVE,
        selected: true,
      },
      { cardColor: CardColor.PURPLE, cardValue: CardValue.FIVE },
      { cardColor: CardColor.PURPLE, cardValue: CardValue.FIVE },
      { cardColor: CardColor.PURPLE, cardValue: CardValue.FIVE },
    ],
    active: true,
  },
  {
    botName: "Bot2LongLongNameIshBlublBul",
    botHand: [
      { cardColor: CardColor.GREEN, cardValue: CardValue.FIVE },
      { cardColor: CardColor.GREEN, cardValue: CardValue.FIVE },
      { cardColor: CardColor.GREEN, cardValue: CardValue.FIVE },
      { cardColor: CardColor.GREEN, cardValue: CardValue.FIVE },
      { cardColor: CardColor.GREEN, cardValue: CardValue.FIVE },
      { cardColor: CardColor.GREEN, cardValue: CardValue.FIVE },
      { cardColor: CardColor.GREEN, cardValue: CardValue.FIVE },
    ],
  },
  {
    botName: "Bot3",
    botHand: [
      { cardColor: CardColor.CYAN, cardValue: CardValue.FIVE },
      { cardColor: CardColor.CYAN, cardValue: CardValue.FIVE },
      { cardColor: CardColor.CYAN, cardValue: CardValue.FIVE },
      { cardColor: CardColor.CYAN, cardValue: CardValue.FIVE },
      { cardColor: CardColor.CYAN, cardValue: CardValue.FIVE },
      { cardColor: CardColor.CYAN, cardValue: CardValue.FIVE },
      { cardColor: CardColor.CYAN, cardValue: CardValue.FIVE },
    ],
  },
  {
    botName: "Bot4",
    botHand: [
      { cardColor: CardColor.ORANGE, cardValue: CardValue.FIVE },
      { cardColor: CardColor.ORANGE, cardValue: CardValue.FIVE },
      { cardColor: CardColor.ORANGE, cardValue: CardValue.FIVE },
      { cardColor: CardColor.ORANGE, cardValue: CardValue.FIVE },
      { cardColor: CardColor.ORANGE, cardValue: CardValue.FIVE },
      { cardColor: CardColor.ORANGE, cardValue: CardValue.FIVE },
      { cardColor: CardColor.ORANGE, cardValue: CardValue.FIVE },
      { cardColor: CardColor.ORANGE, cardValue: CardValue.FIVE },
      { cardColor: CardColor.ORANGE, cardValue: CardValue.FIVE },
      { cardColor: CardColor.ORANGE, cardValue: CardValue.FIVE },
      { cardColor: CardColor.ORANGE, cardValue: CardValue.FIVE },
      { cardColor: CardColor.ORANGE, cardValue: CardValue.FIVE },
      { cardColor: CardColor.ORANGE, cardValue: CardValue.FIVE },
      { cardColor: CardColor.ORANGE, cardValue: CardValue.FIVE },
      { cardColor: CardColor.ORANGE, cardValue: CardValue.FIVE },
      { cardColor: CardColor.ORANGE, cardValue: CardValue.FIVE },
      { cardColor: CardColor.ORANGE, cardValue: CardValue.FIVE },
      { cardColor: CardColor.ORANGE, cardValue: CardValue.FIVE },
      { cardColor: CardColor.ORANGE, cardValue: CardValue.FIVE },
    ],
  },
  {
    botName: "Bot5",
    botHand: [
      { cardColor: CardColor.GRAY, cardValue: CardValue.FIVE },
      { cardColor: CardColor.GRAY, cardValue: CardValue.FIVE },
      { cardColor: CardColor.GRAY, cardValue: CardValue.FIVE },
      { cardColor: CardColor.GRAY, cardValue: CardValue.FIVE },
      { cardColor: CardColor.GRAY, cardValue: CardValue.FIVE },
      { cardColor: CardColor.GRAY, cardValue: CardValue.FIVE },
      { cardColor: CardColor.GRAY, cardValue: CardValue.FIVE },
    ],
  },
  {
    botName: "Bot6",
    botHand: [
      { cardColor: CardColor.PURPLE, cardValue: CardValue.FIVE },
      { cardColor: CardColor.PURPLE, cardValue: CardValue.FIVE },
      { cardColor: CardColor.PURPLE, cardValue: CardValue.FIVE },
      { cardColor: CardColor.PURPLE, cardValue: CardValue.FIVE },
      { cardColor: CardColor.PURPLE, cardValue: CardValue.FIVE },
    ],
  },
];

const round2 = [
  {
    botName: "Bot1",
    botHand: [
      {
        cardColor: CardColor.PURPLE,
        cardValue: CardValue.FIVE,
      },
      {
        cardColor: CardColor.PURPLE,
        cardValue: CardValue.FIVE,
      },
      {
        cardColor: CardColor.PURPLE,
        cardValue: CardValue.FIVE,
        selected: true,
      },
    ],
    active: true,
  },
  {
    botName: "Bot2LongLongNameIshBlublBul",
    botHand: [{ cardColor: CardColor.GREEN, cardValue: CardValue.FIVE }],
  },
  {
    botName: "Bot3",
    botHand: [{ cardColor: CardColor.CYAN, cardValue: CardValue.FIVE }],
  },
  {
    botName: "Bot4",
    botHand: [
      { cardColor: CardColor.ORANGE, cardValue: CardValue.FIVE },
      { cardColor: CardColor.ORANGE, cardValue: CardValue.FIVE },
      { cardColor: CardColor.ORANGE, cardValue: CardValue.FIVE },
      { cardColor: CardColor.ORANGE, cardValue: CardValue.FIVE },
      { cardColor: CardColor.ORANGE, cardValue: CardValue.FIVE },
      { cardColor: CardColor.ORANGE, cardValue: CardValue.FIVE },
      { cardColor: CardColor.ORANGE, cardValue: CardValue.FIVE },
      { cardColor: CardColor.ORANGE, cardValue: CardValue.FIVE },
      { cardColor: CardColor.ORANGE, cardValue: CardValue.FIVE },
      { cardColor: CardColor.ORANGE, cardValue: CardValue.FIVE },
      { cardColor: CardColor.ORANGE, cardValue: CardValue.FIVE },
      { cardColor: CardColor.ORANGE, cardValue: CardValue.FIVE },
      { cardColor: CardColor.ORANGE, cardValue: CardValue.FIVE },
      { cardColor: CardColor.ORANGE, cardValue: CardValue.FIVE },
      { cardColor: CardColor.ORANGE, cardValue: CardValue.FIVE },
      { cardColor: CardColor.ORANGE, cardValue: CardValue.FIVE },
      { cardColor: CardColor.ORANGE, cardValue: CardValue.FIVE },
      { cardColor: CardColor.ORANGE, cardValue: CardValue.FIVE },
      { cardColor: CardColor.ORANGE, cardValue: CardValue.FIVE },
      { cardColor: CardColor.ORANGE, cardValue: CardValue.FIVE },
      { cardColor: CardColor.ORANGE, cardValue: CardValue.FIVE },
      { cardColor: CardColor.ORANGE, cardValue: CardValue.FIVE },
      { cardColor: CardColor.ORANGE, cardValue: CardValue.FIVE },
      { cardColor: CardColor.ORANGE, cardValue: CardValue.FIVE },
      { cardColor: CardColor.ORANGE, cardValue: CardValue.FIVE },
      { cardColor: CardColor.ORANGE, cardValue: CardValue.FIVE },
      { cardColor: CardColor.ORANGE, cardValue: CardValue.FIVE },
      { cardColor: CardColor.ORANGE, cardValue: CardValue.FIVE },
      { cardColor: CardColor.ORANGE, cardValue: CardValue.FIVE },
      { cardColor: CardColor.ORANGE, cardValue: CardValue.FIVE },
      { cardColor: CardColor.ORANGE, cardValue: CardValue.FIVE },
      { cardColor: CardColor.ORANGE, cardValue: CardValue.FIVE },
      { cardColor: CardColor.ORANGE, cardValue: CardValue.FIVE },
      { cardColor: CardColor.ORANGE, cardValue: CardValue.FIVE },
      { cardColor: CardColor.ORANGE, cardValue: CardValue.FIVE },
      { cardColor: CardColor.ORANGE, cardValue: CardValue.FIVE },
      { cardColor: CardColor.ORANGE, cardValue: CardValue.FIVE },
      { cardColor: CardColor.ORANGE, cardValue: CardValue.FIVE },
    ],
  },
  {
    botName: "Bot5",
    botHand: [
      { cardColor: CardColor.GRAY, cardValue: CardValue.FIVE },
      { cardColor: CardColor.GRAY, cardValue: CardValue.FIVE },
      { cardColor: CardColor.GRAY, cardValue: CardValue.FIVE },
      { cardColor: CardColor.GRAY, cardValue: CardValue.FIVE },
    ],
  },
  {
    botName: "Bot6",
    botHand: [
      { cardColor: CardColor.PURPLE, cardValue: CardValue.FIVE },
      { cardColor: CardColor.PURPLE, cardValue: CardValue.FIVE },
    ],
  },
];

const roundInfos: RoundInfo[] = [
  {
    botInfos: round1,
    drawPile: drawpile,
    topCard: { cardColor: CardColor.PURPLE, cardValue: CardValue.FIVE },
  },
  {
    botInfos: round2,
    drawPile: drawpile,
    topCard: { cardColor: CardColor.GRAY, cardValue: CardValue.DRAW_TWO },
  },
];

export async function getRoundInfosForGame(
  gameID: number
): Promise<RoundInfo[]> {
  // TODO replace this with calls to the backend
  //return roundInfos;

  try {
    const response = await fetch(`api/match/${gameID}/all`, {
      mode: "cors",
      method: "GET",
      headers: {
        Accept: "application/json",
      },
    });

    if (response.status !== 200) {
      throw new Error("Failed to fetch match log");
    }

    const data = await response.json();
    return data;
  } catch (error: any) {
    console.error("Error fetching match log:", error.message);
    throw error;
  }
}

export async function getLogLinesForGame(gameID: number): Promise<LogLine[]> {
  // TODO replace this with calls to the backend
  //return logExamples;

  //@GetMapping("/match/{gameId}/log")

  try {
    const response = await fetch(`api/match/${gameID}/log`, {
      mode: "cors",
      method: "GET",
      headers: {
        Accept: "application/json",
      },
    });

    if (response.status !== 200) {
      throw new Error("Failed to fetch match log");
    }

    const data = await response.json();
    return data;
  } catch (error: any) {
    console.error("Error fetching match log:", error.message);
    throw error;
  }
}
