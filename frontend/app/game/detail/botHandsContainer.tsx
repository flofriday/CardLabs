"use client";

import Card, { toCardType } from "@/app/components/card";
import { Action } from "@/app/types/action";
import { BackendCard } from "@/app/types/backendCard";
import { Hand } from "@/app/types/hand";
import { useEffect, useState } from "react";

interface Props {
  hand: Hand;
  active: boolean;
  actions: Action[];
  name: string;
}

function isHighlighted(
  actions: Action[],
  index: number,
  cards: BackendCard[]
): boolean {
  for (let i = 0; i < actions.length; i++) {
    const action = actions[i];
    if (action.type === "PLAY_CARD") {
      const idx = cards.findIndex(
        (x) =>
          x.color === action.card.color &&
          x.type === action.card.type &&
          x.number === action.card.number
      );
      if (idx === index) {
        return true;
      }
    }
  }
  return false;
}

export default function BotHandsContainer({
  hand,
  active,
  actions,
  name,
}: Props): JSX.Element {
  const [showNextSet, setShowNextSet] = useState(false);
  const [isActive, setActive] = useState(active);
  const cardsToShow = showNextSet
    ? hand.cards.slice(10, 20)
    : hand.cards.slice(0, 10);

  useEffect(() => {
    setActive(active);
  }, [active]);

  return (
    <div className="flex flex-col text-4xl py-2 items-start">
      <div className="flex items-center space-x-2">
        <p
          className={`flex-shrink-0 w-40 truncate pl-2 ${
            isActive ? "font-bold [text-shadow:_#FEF9EC_1px_0_10px]" : ""
          }`}
          title={name}
        >
          {name}:
        </p>
        {cardsToShow.map((card, index) => (
          <div key={index}>
            <Card
              value={toCardType(card.type, card.number)}
              color={card.color}
              className={`h-16 w-fit px-2 ${
                isHighlighted(actions, index, cardsToShow)
                  ? "border-accent bg-accent border-4 rounded-md shadow-md"
                  : ""
              }`}
            />
          </div>
        ))}
        {hand.cards.length > 10 && (
          <div className="flex items-center ml-2">
            <button
              className="btn bg-primary p-6 py-2 font-bold rounded-lg shadow-md text-4xl  hover:bg-primary_highlight"
              onClick={() => {
                setShowNextSet(!showNextSet);
              }}
            >
              {showNextSet ? "Previous Cards" : "Next Cards"}
            </button>
          </div>
        )}
      </div>
    </div>
  );
}
