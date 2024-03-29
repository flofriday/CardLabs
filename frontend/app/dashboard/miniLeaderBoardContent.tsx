import { useEffect, useState } from "react";
import { leaderBoardEntry } from "../types/leaderBoardEntry";
import { LeaderBoardType } from "../types/LeaderBoardType";
import { RegionType } from "../types/RegionType";
import { getScoreOfGlobalFirstPlace } from "../services/LeaderBoardService";

interface Props {
  leaderBoardType: LeaderBoardType;
  regionType: RegionType;
  entries: leaderBoardEntry[];
}

export default function MiniLeaderBoardContent({
  leaderBoardType,
  regionType,
  entries,
}: Props): JSX.Element {
  const [maximalScore, setMaximalScore] = useState<number | undefined>(
    undefined
  );

  useEffect(() => {
    getScoreOfGlobalFirstPlace()
      .then((s) => {
        setMaximalScore(s);
      })
      .catch(() => {});
  });

  let title = "";
  if (leaderBoardType === LeaderBoardType.MY_BOTS) {
    title = "My Bots - " + regionType;
  } else if (leaderBoardType === LeaderBoardType.ALL_BOTS) {
    title = regionType + " Leaderboard";
  } else {
    title = "Leaderboard";
  }

  return (
    <div className="pb-4 px-4">
      <h2 className="text-4xl font-bold mb-4">{title}</h2>
      {entries.length > 0 ? (
        <div className="flex flex-col space-y-4">
          {entries.map((entry, index) => (
            <div key={index} className="grid grid-cols-6">
              <p className="font-bold col-span-2 truncate pr-2">
                {entry.place + ". " + entry.botName}
              </p>
              <div className="col-span-2 pr-4">
                <div className="bg-background h-4 w-full h-full ml-2flex items-center justify-center p-1">
                  {maximalScore !== undefined && (
                    <div
                      className="bg-primary_highlight h-full"
                      style={{
                        width: `${(entry.score / maximalScore) * 100}%`,
                      }}
                    />
                  )}
                </div>
              </div>
              <p className="ml-2 col-span-2 pl-1">Score: {entry.score}</p>
            </div>
          ))}
        </div>
      ) : (
        <div className="flex flex-col space-y-4 font-bold">
          No scoreboard data available yet!
        </div>
      )}
    </div>
  );
}
