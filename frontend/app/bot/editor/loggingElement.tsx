"use client";

import { getLogLinesForGame } from "@/app/services/GameService";
import { LogLine } from "@/app/types/LogLine";
import { useEffect, useState } from "react";

interface Props {
  gameId: number | undefined;
}

export default function LoggingElement({ gameId }: Props): JSX.Element {
  const [logLines, setLogLines] = useState<LogLine[]>([]);

  useEffect(() => {
    if (gameId !== undefined) {
      getLogLinesForGame(gameId)
        .then((lines) => {
          setLogLines(lines);
          console.log(lines);
        })
        .catch(() => {});
    } else {
      setLogLines([]);
    }
  }, [gameId]);

  return (
    <div className="h-full w-4/12 bg-secondary border-8 border-text rounded-lg">
      <h1 className="text-4xl ml-4 mt-4 font-bold">Log</h1>
      {logLines.length === 0 ? (
        <div className=" w-full flex justify-center mt-32 text-xl text-center">
          <h2>
            Nothing here yet
            <br />
            consider playing a test game
          </h2>
        </div>
      ) : (
        logLines.map((log, index) => (
          <div
            key={index}
            className={`mb-2 ${
              log.type !== "system" ? "text-yellow" : ""
            } p-2 rounded-md`}
          >
            {log.type !== "system" && "[DBG] "} {log.message}
          </div>
        ))
      )}
    </div>
  );
}
