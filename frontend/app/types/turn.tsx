import { LogLine } from "./LogLine";
import { BackendCard } from "./backendCard";
import { Hand } from "./hand";

interface Turn {
  actions: any;
  drawPile: BackendCard[];
  hands: Hand[];
  logMessages: LogLine[];
  topCard: BackendCard;
  turnId: number;
}

export type { Turn };
