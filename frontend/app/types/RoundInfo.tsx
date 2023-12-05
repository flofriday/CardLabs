import { BotRoundInfo } from "./BotRoundInfo";
import { cardType } from "./cardType";

interface RoundInfo {
  botInfos: BotRoundInfo[];
  topCard: cardType;
  drawPile: cardType[];
}

export type { RoundInfo };
