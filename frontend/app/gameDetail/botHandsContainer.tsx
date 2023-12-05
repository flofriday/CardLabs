"use client";

import { useState } from "react";
import Card from "../components/card";
import { cardType } from "../types/cardType";
import { BotRoundInfo } from "../types/BotRoundInfo";

interface Props {
  botRoundInfo: BotRoundInfo;
}

export default function BotHandsContainer({
  botRoundInfo,
}: Props): JSX.Element {
  const [showNextSet, setShowNextSet] = useState(false);
  const cardsToShow = showNextSet
    ? botRoundInfo.botHand.slice(10, 20)
    : botRoundInfo.botHand.slice(0, 10);

  return (
    <div className="flex flex-col text-4xl py-2 items-start">
      <div className="flex items-center space-x-2">
        <p
          className={`flex-shrink-0 w-40 truncate pl-2 ${
            botRoundInfo.active
              ? "font-bold [text-shadow:_#FEF9EC_1px_0_10px]"
              : ""
          }`}
          title={botRoundInfo.botName}
        >
          {botRoundInfo.botName}:
        </p>
        {cardsToShow.map((card, index) => (
          <div key={index}>
            <Card
              value={card.cardValue}
              color={card.cardColor}
              className={`h-16 w-fit px-2 ${
                card.selected
                  ? "border-accent bg-accent border-4 rounded-md shadow-md"
                  : ""
              }`}
            />
          </div>
        ))}
        {botRoundInfo.botHand.length > 10 && (
          <div className="flex items-center ml-2">
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
