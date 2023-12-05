"use client";
import Slider from "./Slider";
import RoundLogContainer from "./roundLogContainer";
import Card, { CardColor, CardValue } from "../components/card";
import BotHandsContainer from "./botHandsContainer";
import { useEffect, useState } from "react";
import {
  getLogLinesForGame,
  getRoundInfosForGame,
} from "../services/GameService";
import { LogLine } from "../types/LogLine";
import { RoundInfo } from "../types/RoundInfo";

interface Props {
  date: string;
  place: number;
}

export default function GameDetail({ date, place }: Props): JSX.Element {
  const [logLines, setLogLines] = useState<LogLine[]>([]);
  const [roundInfos, setRoundInfos] = useState<RoundInfo[] | undefined>(
    undefined
  );

  useEffect(() => {
    getLogLinesForGame("testID")
      .then((lines) => {
        setLogLines(lines);
      })
      .catch(() => {});
    getRoundInfosForGame("testID")
      .then((roundInfos) => {
        setRoundInfos(roundInfos);
      })
      .catch(() => {});
  });

  return (
    <div className="flex flex-col h-full">
      <h1
        id="leftHeading"
        className="absolute text-5xl md:text-7xl mt-6 md:mt-12 ml-10 md:ml-16 font-medium tracking-wider inline-block w-fit"
      >
        Game XX.YY.ZZZZ - X. Place
      </h1>

      <div className="flex flex-1 w-full pt-40">
        <div className="w-2/6 px-12">
          <RoundLogContainer logLines={logLines} />
        </div>
        <div className="w-full">
          <div className="w-full px-12">
            <div className="w-full flex">
              <div className="w-4/6 flex justify-between grow space-x-5">
                <Slider totalRoundNumber={5} />

                <div className="flex items-center space-x-4">
                  <p className="text-4xl font-bold whitespace-nowrap">
                    Top Card:
                  </p>
                  <Card
                    value={CardValue.FOUR}
                    color={CardColor.PURPLE}
                    className="h-16 w-fit"
                  />
                </div>
              </div>
            </div>
            <div className="flex flex-col pt-5">
              <p className="text-4xl font-bold underline">Bot Hands:</p>
              <div>
                {roundInfos != undefined
                  ? roundInfos[0].botInfos.map((info, index) => (
                      <BotHandsContainer botRoundInfo={info} key={index} />
                    ))
                  : ""}
              </div>
            </div>

            <hr className="my-2 h-0.5 border-t-0 bg-text opacity-100 dark:opacity-50" />

            <div className="">
              <div className="flex flex-row text-4xl py-2 items-center font-bold">
                <p>Draw Pile:</p>
                {roundInfos != undefined
                  ? roundInfos[0].drawPile.map((card, index) => (
                      <div>
                        <Card
                          value={card.cardValue}
                          color={card.cardColor}
                          className="h-16 w-fit px-2"
                        />
                      </div>
                    ))
                  : ""}
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}
