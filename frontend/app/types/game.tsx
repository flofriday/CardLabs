interface Game {
  id: number;
  startTime: Date;
  endTime: Date;
  winningBotId: number;
  disqualifiedBotId: number;
  turns: any;
  gameState: string;
}

export type { Game };
