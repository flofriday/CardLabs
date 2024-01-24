"use client";

import { getLogLinesForGame } from "@/app/services/GameService";
import { LogLine } from "@/app/types/LogLine";
import { useEffect, useState } from "react";

interface Props {
  gameId: number | undefined;
}

async function getLogLines(gameId: number): Promise<LogLine[]> {
  let logLines: LogLine[] = [];
  let counter = 0;
  do {
    logLines = await getLogLinesForGame(gameId);
    if (logLines.length !== 0) {
      break;
    }
    await new Promise((resolve) => setTimeout(resolve, 1000));
    counter += 1;
  } while (logLines.length === 0 && counter < 20);

  return logLines;
}

export default function LoggingElement({ gameId }: Props): JSX.Element {
  const [logLines, setLogLines] = useState<LogLine[]>([]);

  useEffect(() => {
    if (gameId !== undefined) {
      getLogLines(gameId)
        .then((lines) => {
          setLogLines(lines);
        })
        .catch(() => {});
    } else {
      setLogLines([]);
    }
  }, [gameId]);

  return (
    <div className="h-full w-4/12 bg-secondary border-8 border-text rounded-lg flex flex-col">
      <h1 className="text-4xl m-4 font-bold">Log</h1>
      {logLines.length === 0 ? (
        <div className=" w-full flex justify-center mt-32 text-xl text-center">
          <h2>
            Nothing here yet
            <br />
            consider playing a test game
          </h2>
        </div>
      ) : (
        <div className="overflow-auto w-full">
          {logLines.map((log, index) => (
            <pre
              key={index}
              className={`mb-2 ${
                log.type !== "system" ? "text-yellow" : ""
              } p-2 rounded-md`}
            >
              {log.type !== "system" && "[DBG] "}
              {log.message.trim()}
            </pre>
          ))}
        </div>
      )}
    </div>
  );
}
