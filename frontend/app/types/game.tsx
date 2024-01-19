interface Game {
  id: number;
  startTime: Date;
  endTime: number;
  winningBotId: number;
  disqualifiedBotId: number;
  turns: any;
  gameState: string;
}
