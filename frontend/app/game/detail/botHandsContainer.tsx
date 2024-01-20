"use client";

import Card, { toCardType } from "@/app/components/card";
import { Hand } from "@/app/types/hand";
import { useState } from "react";

interface Props {
  hand: Hand;
}

export default function BotHandsContainer({ hand }: Props): JSX.Element {
  const [showNextSet, setShowNextSet] = useState(false);
  const cardsToShow = showNextSet
    ? hand.cards.slice(10, 20)
    : hand.cards.slice(0, 10);

  return (
    <div className="flex flex-col text-4xl py-2 items-start">
      <div className="flex items-center space-x-2">
        <p
          className={`flex-shrink-0 w-40 truncate pl-2 ${
            hand.botId === 1
              ? "font-bold [text-shadow:_#FEF9EC_1px_0_10px]"
              : ""
          }`}
          title={hand.botId + ""}
        >
          {hand.botId + ""}:
        </p>
        {cardsToShow.map((card, index) => (
          <div key={index}>
            <Card
              value={toCardType(card.type, card.number)}
              color={card.color}
              className={`h-16 w-fit px-2 ${
                card.selected
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
