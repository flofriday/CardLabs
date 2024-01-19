"use client";

import Pagination from "../../components/Pagination";
import LeftPageHeader from "../../components/leftPageHeader";
import Robot, { RobotType } from "../../components/robot";
import { useState, useEffect } from "react";
import GameEntry from "./gameEntry";
import { getGames } from "@/app/services/GameService";
import { notFound } from "next/navigation";
import { Game } from "@/app/types/game";

export default function GameOverview({
  params,
}: {
  params: { slug: string };
}): JSX.Element {
  const [gameEntries, setGameEntries] = useState<Game[]>([]);
  const [pageNumber, setPageNumber] = useState<number>(0);
  const [totalPages, setTotalPages] = useState<number>(1);

  useEffect(() => {
    if (isNaN(Number(params.slug))) {
      return notFound();
    }
    getGames(Number(params.slug), 6, pageNumber)
      .then((page) => {
        setGameEntries(page.content);
        setTotalPages(page.totalPages);
        console.log(page.content);
      })
      .catch(() => {});
  }, [pageNumber, params.slug]);

  if (isNaN(Number(params.slug))) {
    return notFound();
  }

  const botId = Number(params.slug);

  const handlePageChange = (page: number): void => {
    setPageNumber(page);

    getGames(botId, 6, pageNumber)
      .then((page) => {
        setGameEntries(page.content);
        setTotalPages(page.totalPages);
      })
      .catch(() => {});
  };

  return (
    <div className="flex flex-col h-full">
      <LeftPageHeader title="Match Overview" />

      <div className="flex flex-1 justify-center items-center pt-20">
        <div className="w-1/4 p-12">
          <Robot type={RobotType.TROPHY} />
        </div>

        <div className="w-1/2 px-12 pt-16">
          <div className="flex flex-col items-center justify-center space-y-10">
            {gameEntries.map((entry, index) => (
              <GameEntry game={entry} key={index} botId={botId}></GameEntry>
            ))}
            <Pagination
              initalPage={pageNumber}
              totalNumberOfPages={totalPages}
              onPageChange={handlePageChange}
            />
          </div>
        </div>
        <div className="w-1/4 p-12"></div>
      </div>
    </div>
  );
}
