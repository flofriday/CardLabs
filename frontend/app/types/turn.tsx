import { LogLine } from "./LogLine";
import { Action } from "./action";
import { BackendCard } from "./backendCard";
import { Hand } from "./hand";

interface Turn {
  actions: Action[];
  activeBotId: number;
  drawPile: BackendCard[];
  hands: Hand[];
  logMessages: LogLine[];
  topCard: BackendCard;
  turnId: number;
}

export type { Turn };
