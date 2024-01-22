import { BackendCard } from "./backendCard";

interface Action {
  botId: number;
  card: BackendCard;
  type: string;
}

export type { Action };
