import { Turn } from "./turn";

interface Game {
  id: number;
  startTime: Date;
  endTime: Date;
  winningBotId: number;
  disqualifiedBotId: number;
  turns: Turn[];
  gameState: string;
}

export type { Game };
