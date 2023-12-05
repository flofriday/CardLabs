"use client";

import { useState } from "react";
import Card from "../components/card";
import { cardType } from "../types/cardType";

interface Props {
  botHand: cardType[];
  botName: string;
}

export default function BotHandsContainer({
  botHand,
  botName,
}: Props): JSX.Element {
  const [showNextSet, setShowNextSet] = useState(false);
  const cardsToShow = showNextSet
    ? botHand.slice(10, 20)
    : botHand.slice(0, 10);

  return (
    <div className="flex flex-col text-4xl py-2 items-start">
      <div className="flex items-center space-x-2">
        <p className="flex-shrink-0 w-40 truncate" title={botName}>
          {botName}:
        </p>
        {cardsToShow.map((card, index) => (
          <div key={index}>
            <Card
              value={card.cardValue}
              color={card.cardColor}
              className="h-16 w-fit px-2"
            />
          </div>
        ))}
        {botHand.length > 10 && (
          <div className="flex items-center mt-2 ml-2">
            <button
              className="btn bg-primary p-6 py-2 font-bold rounded-lg shadow-md text-4xl  hover:bg-primary_highlight"
              onClick={() => setShowNextSet(!showNextSet)}
            >
              {showNextSet ? "Previous Cards" : "Next Cards"}
            </button>
          </div>
        )}
      </div>
    </div>
  );
}
