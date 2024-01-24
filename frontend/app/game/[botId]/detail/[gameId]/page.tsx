"use client";
import Slider from "../Slider";
import RoundLogContainer from "../roundLogContainer";
import BotHandsContainer from "../botHandsContainer";
import { Key, useEffect, useState } from "react";
import { notFound, useRouter } from "next/navigation";
import { LogLine } from "@/app/types/LogLine";
import { getLogLinesForGame, getGame } from "@/app/services/GameService";
import Card, { toCardType } from "@/app/components/card";
import { Game } from "@/app/types/game";
import { BackendCard } from "@/app/types/backendCard";
import { Hand } from "@/app/types/hand";
import { getBotName } from "@/app/services/BotService";
import { UnAuthorizedError } from "@/app/exceptions/UnAuthorizedError";
import { NotFoundError } from "@/app/exceptions/NotFoundError";
import { toast } from "react-toastify";

export default function GameDetail({
  params,
}: {
  params: { gameId: string; botId: string };
}): JSX.Element {
  const [logLines, setLogLines] = useState<LogLine[]>([]);
  const [game, setGame] = useState<Game>();
  const [round, setRound] = useState(0);
  const [botNames, setBotNames] = useState<Array<{ id: number; name: string }>>(
    []
  );

  const router = useRouter();

  useEffect(() => {
    getLogLinesForGame(Number(params.gameId), Number(params.botId))
      .then((lines) => {
        setLogLines(lines);
      })
      .catch(() => {});
    getGame(Number(params.gameId))
      .then((g) => {
        setGame(g);
        if (g.turns[0] !== undefined) {
          const botIds = g.turns[0].hands.map((x) => x.botId);
          const botsPromise = [];
          for (let i = 0; i < botIds.length; i++) {
            botsPromise.push(getBotName(botIds[i]));
          }
          Promise.all(botsPromise)
            .then((b) => {
              setBotNames(b);
            })
            .catch((ex) => {
              if (ex instanceof UnAuthorizedError) {
                router.replace("/unauthorized");
              }
              if (ex instanceof NotFoundError) {
                router.replace("/dashboard");
                toast.error("The game you tried to see does not exist!");
              }
            });
        }
      })
      .catch((ex) => {
        if (ex instanceof UnAuthorizedError) {
          router.replace("/unauthorized");
        }
        if (ex instanceof NotFoundError) {
          router.replace("/dashboard");
          toast.error("The game you tried to see does not exist!");
        }
      });
  }, [params.gameId, params.botId]);

  if (isNaN(Number(params.gameId)) || isNaN(Number(params.botId))) {
    return notFound();
  }

  return (
    <div className="flex flex-col h-full">
      <h1
        id="leftHeading"
        className="absolute text-5xl md:text-7xl mt-6 md:mt-12 ml-10 md:ml-16 font-medium tracking-wider inline-block w-fit"
      >
        {"Game: " + game?.startTime.toLocaleString()}
      </h1>

      <div className="flex flex-1 w-full pt-40">
        <div className="w-2/6 px-12">
          <RoundLogContainer logLines={logLines} />
        </div>
        <div className="w-full">
          <div className="w-full px-12">
            <div className="w-full flex">
              <div className="w-4/6 flex justify-between grow space-x-5">
                <Slider
                  totalRoundNumber={game !== undefined ? game?.turns.length : 0}
                  onChange={(r) => {
                    setRound(r - 1);
                  }}
                />

                <div className="flex items-center space-x-4">
                  <p className="text-4xl font-bold whitespace-nowrap">
                    Top Card:
                  </p>
                  {game?.turns[round].topCard.type !== undefined && (
                    <Card
                      value={toCardType(
                        game?.turns[round].topCard.type,
                        game?.turns[round].topCard.number
                      )}
                      color={game?.turns[round].topCard.color}
                      className="h-16 w-fit"
                    />
                  )}
                </div>
              </div>
            </div>
            <div className="flex flex-col pt-5">
              <p className="text-4xl font-bold underline">Bot Hands:</p>
              <div>
                {game !== undefined
                  ? game.turns[round].hands.map((hand: Hand, index: Key) => (
                      <BotHandsContainer
                        name={
                          botNames.find((x) => x.id === hand.botId)?.name + ""
                        }
                        hand={hand}
                        key={index}
                        active={hand.botId === game.turns[round].activeBotId}
                        actions={game.turns[round].actions.filter(
                          (a) => a.botId === hand.botId
                        )}
                      />
                    ))
                  : ""}
              </div>
            </div>

            <hr className="my-2 h-0.5 border-t-0 bg-text opacity-100 dark:opacity-50" />

            <div className="">
              <div className="flex flex-row text-4xl py-2 items-center font-bold">
                <p className="w-56 ml-2">Draw Pile:</p>
                {game !== undefined
                  ? game.turns[round].drawPile.map(
                      (card: BackendCard, index: Key) => (
                        <div key={index}>
                          {card.type !== undefined && (
                            <Card
                              value={toCardType(card.type, card.number)}
                              color={card.color}
                              className="h-16 w-fit px-2"
                            />
                          )}
                        </div>
                      )
                    )
                  : ""}
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}
