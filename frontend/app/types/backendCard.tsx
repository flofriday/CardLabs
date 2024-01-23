import { CardColor } from "../components/card";

interface BackendCard {
  type: string;
  color: CardColor;
  number?: number;
}

export type { BackendCard };
