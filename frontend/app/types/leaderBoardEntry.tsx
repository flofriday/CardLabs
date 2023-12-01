interface leaderBoardEntry {
  place: number;
  score: number;
  botName: string;
  userName: string;
  length: () => void;
}

export type { leaderBoardEntry };
