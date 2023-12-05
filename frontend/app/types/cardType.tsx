import { CardColor, CardValue_ } from "../components/card";

interface cardType {
  cardColor: CardColor;
  cardValue: CardValue_;
  selected?: boolean;
}

export type { cardType };
