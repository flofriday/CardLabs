import { cardType } from "./cardType";

interface BotRoundInfo {
  botHand: cardType[];
  botName: string;
  active?: boolean;
}

export type { BotRoundInfo };
