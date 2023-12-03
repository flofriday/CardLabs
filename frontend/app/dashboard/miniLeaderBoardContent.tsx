import { leaderBoardEntry } from "../types/leaderBoardEntry";
import { LeaderBoardType } from "../types/LeaderBoardType";
import { RegionType } from "../types/RegionType";

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
      <div className="flex flex-col space-y-4">
        {entries.map((entry, index) => (
          <div key={index} className="grid grid-cols-5">
            <p className="font-bold col-span-2 truncate pr-2">
              {entry.place}. {entry.botName}
            </p>
            <div className="col-span-2 pr-4">
              <div className="bg-background h-4 w-full h-full ml-2flex items-center justify-center p-1">
                <div
                  className="bg-primary_highlight h-full"
                  style={{ width: `${entry.score}%` }}
                />
              </div>
            </div>
            <p className="ml-2 col-span-1 pl-1">Score: {entry.score}</p>
          </div>
        ))}
      </div>
    </div>
  );
}
